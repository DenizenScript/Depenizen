package com.denizenscript.depenizen.common.socket.server;

import com.denizenscript.depenizen.common.Depenizen;
import com.denizenscript.depenizen.common.socket.DataDeserializer;
import com.denizenscript.depenizen.common.socket.DataSerializer;
import com.denizenscript.depenizen.common.socket.Packet;
import com.denizenscript.depenizen.common.socket.server.packet.*;
import com.denizenscript.depenizen.common.util.Utilities;

import javax.crypto.BadPaddingException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ClientConnection implements Runnable {

    private int clientId;
    private String clientName;
    private boolean bungeeScriptCompatible;
    private SocketServer server;
    private Socket client;
    private Thread listenThread;
    private DataOutputStream output;
    private DataInputStream input;
    private Set<String> eventSubscriptions;
    private boolean isConnected;

    public ClientConnection(int clientId, SocketServer server, Socket client) {
        this.clientId = clientId;
        this.server = server;
        this.client = client;
        this.eventSubscriptions = new HashSet<String>();
    }

    public void start() throws IOException {
        if (!isConnected) {
            output = new DataOutputStream(client.getOutputStream());
            input = new DataInputStream(client.getInputStream());
            listenThread = new Thread(this);
            listenThread.start();
        }
    }

    public void send(Packet packet) throws GeneralSecurityException, IOException {
        DataSerializer data = new DataSerializer();
        packet.serialize(data);
        byte[] encryptedData = server.getEncryption().encrypt(data.toByteArray());
        output.writeInt(encryptedData.length);
        output.write(encryptedData);
        output.flush();
    }

    public void trySend(Packet packet) {
        try {
            send(packet);
        }
        catch (IOException e) {
            server.removeClient(clientId, "Client socket closed");
        }
        catch (Exception e) {
            server.removeClient(clientId, "Failed to send data due to an exception");
            Depenizen.getImplementation().debugException(e);
        }
    }

    public void stop() {
        if (isConnected) {
            isConnected = false;
            try {
                if (output != null) {
                    output.close();
                }
                if (input != null) {
                    input.close();
                }
                if (client != null) {
                    client.close();
                }
            }
            catch (IOException e) {
                Depenizen.getImplementation().debugException(e);
            }
            for (String event : eventSubscriptions) {
                server.handleEventSubscription(this, event, false);
            }
            eventSubscriptions.clear();
            if (listenThread != null) {
                listenThread.interrupt();
                listenThread = null;
            }
        }
    }

    public int getClientId() {
        return clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public boolean isRegistered() {
        return this.clientName != null;
    }

    public void setBungeeScriptCompatible(boolean bungeeScriptCompatible) {
        this.bungeeScriptCompatible = bungeeScriptCompatible;
    }

    public boolean isBungeeScriptCompatible() {
        return bungeeScriptCompatible;
    }

    private int lastPingBit;

    @Override
    public void run() {
        if (client != null && client.isConnected()) {
            isConnected = true;
            byte[] buffer;
            try {
                output.write(server.getEncryption().getIV());
                output.flush();
                connectionLoop:
                while (isConnected) {
                    long timePassed;
                    boolean pinged = false;
                    long start = System.currentTimeMillis();
                    int receivedEncryptedLength;
                    while ((receivedEncryptedLength = input.readInt()) == 0) {
                        if (!isConnected) {
                            break connectionLoop;
                        }
                        timePassed = System.currentTimeMillis() - start;
                        if (timePassed > 30 * 1000 && !pinged) {
                            lastPingBit = Utilities.getRandomUnsignedByte();
                            send(new ServerPacketOutPing(lastPingBit));
                            pinged = true;
                        }
                        if (timePassed > 60 * 1000) {
                            server.removeClient(clientId, "Ping timed out!");
                            break connectionLoop;
                        }
                        Thread.sleep(50);
                    }
                    if (receivedEncryptedLength < 0) {
                        server.removeClient(clientId, "Connection failed");
                        break;
                    }
                    if (receivedEncryptedLength > 1024 * 1024) {
                        server.removeClient(clientId, "Incoming data > 1MB. Was it encrypted incorrectly?");
                        break;
                    }
                    buffer = new byte[receivedEncryptedLength];
                    input.read(buffer);
                    byte[] encryptedBytes = new byte[receivedEncryptedLength];
                    System.arraycopy(buffer, 0, encryptedBytes, 0, encryptedBytes.length);
                    byte[] decryptedBytes = server.getEncryption().decrypt(encryptedBytes);
                    DataDeserializer data = new DataDeserializer(decryptedBytes);
                    int packetId = data.readUnsignedByte();
                    Packet.ServerBound packetType = Packet.ServerBound.getById(packetId);
                    if (packetType == null) {
                        server.removeClient(clientId, "Received invalid packet from client: " + packetId);
                        break;
                    }
                    if (!isRegistered() && packetType != Packet.ServerBound.REGISTER) {
                        server.removeClient(clientId, "Received packet before registration: " + packetType.name());
                        break;
                    }
                    switch (packetType) {
                        case REGISTER:
                            if (isRegistered()) {
                                server.removeClient(clientId, "Received a second registration packet");
                                break connectionLoop;
                            }
                            ServerPacketInRegister register = new ServerPacketInRegister();
                            register.deserialize(data);
                            String name = register.getName();
                            if (!server.registerClient(clientId, name, register.isBungeeScriptCompatible())) {
                                break connectionLoop;
                            }
                            break;
                        case PING:
                            ServerPacketInPing ping = new ServerPacketInPing();
                            ping.deserialize(data);
                            send(new ServerPacketOutPong(ping.getBit()));
                            break;
                        case PONG:
                            ServerPacketInPong pong = new ServerPacketInPong();
                            pong.deserialize(data);
                            if (pong.getBit() != lastPingBit) {
                                server.removeClient(clientId, "Invalid ping bit: Expected " + lastPingBit + ", got " + pong.getBit());
                                break connectionLoop;
                            }
                            break;
                        case SCRIPT:
                            ServerPacketInScript script = new ServerPacketInScript();
                            script.deserialize(data);
                            ServerPacketOutScript scriptOut = new ServerPacketOutScript(script.getScriptData());
                            Map<String, ClientConnection> scriptClients = server.getRegisteredClients();
                            for (String destination : script.getDestinations()) {
                                destination = destination.toLowerCase();
                                if (scriptClients.containsKey(destination)) {
                                    ClientConnection destClient = scriptClients.get(destination);
                                    if (destClient.isBungeeScriptCompatible()) {
                                        destClient.trySend(scriptOut);
                                    }
                                }
                            }
                            break;
                        case RUN_SCRIPT:
                            ServerPacketInRunScript runScript = new ServerPacketInRunScript();
                            runScript.deserialize(data);
                            ServerPacketOutRunScript runScriptOut = new ServerPacketOutRunScript(runScript.getScriptData());
                            Map<String, ClientConnection> runScriptClients = server.getRegisteredClients();
                            for (String destination : runScript.getDestinations()) {
                                destination = destination.toLowerCase();
                                if (runScriptClients.containsKey(destination)) {
                                    runScriptClients.get(destination).trySend(runScriptOut);
                                }
                            }
                            break;
                        case TAG:
                            ServerPacketInTag tag = new ServerPacketInTag();
                            tag.deserialize(data);
                            Map<String, ClientConnection> tagClients = server.getRegisteredClients();
                            String tagDestination = tag.getDestination().toLowerCase();
                            if (tagClients.containsKey(tagDestination)) {
                                ServerPacketOutTag tagOut = new ServerPacketOutTag(clientName, tag.getTagData());
                                tagClients.get(tagDestination).send(tagOut);
                            }
                            break;
                        case PARSED_TAG:
                            ServerPacketInParsedTag parsedTag = new ServerPacketInParsedTag();
                            parsedTag.deserialize(data);
                            Map<String, ClientConnection> parsedTagClients = server.getRegisteredClients();
                            String parsedTagDestination = parsedTag.getDestination().toLowerCase();
                            if (parsedTagClients.containsKey(parsedTagDestination)) {
                                ServerPacketOutParsedTag parsedTagOut = new ServerPacketOutParsedTag(parsedTag.getResultData());
                                parsedTagClients.get(parsedTagDestination).send(parsedTagOut);
                            }
                            break;
                        case EVENT_SUBSCRIPTION:
                            ServerPacketInEventSubscription eventSubscription = new ServerPacketInEventSubscription();
                            eventSubscription.deserialize(data);
                            String event = eventSubscription.getEvent();
                            boolean subscribed = eventSubscription.isSubscribed();
                            if (subscribed) {
                                eventSubscriptions.add(event);
                            }
                            else {
                                eventSubscriptions.remove(event);
                            }
                            server.handleEventSubscription(this, event, subscribed);
                            break;
                        case EVENT_RESPONSE:
                            ServerPacketInEventResponse eventResponse = new ServerPacketInEventResponse();
                            eventResponse.deserialize(data);
                            server.handleEventResponse(this, eventResponse.getId(), eventResponse.getResponse());
                            break;
                    }
                }
            }
            catch (BadPaddingException e) {
                server.removeClient(clientId, "Password is incorrect");
            }
            catch (IOException e) {
                server.removeClient(clientId, "Client socket closed");
            }
            catch (InterruptedException e) {
                // Assume this is intentional
            }
            catch (Exception e) {
                server.removeClient(clientId, "Error receiving data from client: " + e.getMessage());
                Depenizen.getImplementation().debugException(e);
            }
        }
        listenThread = null;
    }
}
