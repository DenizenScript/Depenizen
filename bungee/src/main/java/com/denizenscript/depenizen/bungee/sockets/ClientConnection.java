package com.denizenscript.depenizen.bungee.sockets;

import com.denizenscript.depenizen.bungee.Depenizen;
import com.denizenscript.depenizen.bungee.packets.*;
import com.denizenscript.depenizen.bungee.EventManager;
import com.denizenscript.depenizen.bungee.dB;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientConnection implements Runnable {

    private SocketServer server;
    private Socket client;
    private ScheduledTask task;
    private boolean isRunning;
    private DataOutputStream output;
    private DataInputStream input;
    private int clientId;
    private String clientName;
    private List<String> eventSubscriptions;

    public ClientConnection(int clientId, SocketServer server, Socket client) {
        this.clientId = clientId;
        this.server = server;
        this.client = client;
        this.eventSubscriptions = new ArrayList<String>();
        this.start();
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

    public void send(Packet packet) {
        try {
            DataSerializer data = new DataSerializer();

            packet.serialize(data);

            byte[] encryptedData = encryptOrDecrypt(this.server.getPassword(), data.toByteArray());
            this.output.writeInt(encryptedData.length);
            this.output.write(encryptedData);
            this.output.flush();
        } catch (Exception e) {
            this.server.removeClient(this.clientId, "Error sending data to client: " + e.getMessage());
            dB.echoError(e);
        }
    }

    public void open() {
        try {
            this.output = new DataOutputStream(new BufferedOutputStream(this.client.getOutputStream()));
            this.input = new DataInputStream(new BufferedInputStream(this.client.getInputStream()));
        } catch (Exception e) {
            dB.echoError(e);
        }
    }

    public void close() {
        try {
            if (this.output != null) this.output.close();
            if (this.input != null) this.input.close();
            if (this.client != null) this.client.close();
        } catch (Exception e) {
            // Stay silent, nothing really important could happen here
        }
    }

    public void start() {
        this.open();

        if (task == null) {
            this.isRunning = true;
            this.task = ProxyServer.getInstance().getScheduler().runAsync(Depenizen.getCurrentInstance(), this);
        }
    }

    public void stop() {
        this.close();

        if (task != null) {
            this.isRunning = false;
            this.task.cancel();
            task = null;
            for (String event : eventSubscriptions) {
                EventManager.unsubscribe(event, this);
            }
        }
    }

    @Override
    public void run() {
        try {
            byte[] buffer;
            while (this.isRunning) {
                int receivedEncryptedLength = this.input.readInt();
                if (receivedEncryptedLength == -1) {
                    this.server.removeClient(this.clientId, "Connection failed");
                    break;
                }

                if (receivedEncryptedLength > 1048576)
                    throw new IOException("Incoming data > 1MB. Was it encrypted incorrectly?");

                buffer = new byte[receivedEncryptedLength];

                this.input.read(buffer);
                byte[] encryptedBytes = new byte[receivedEncryptedLength];
                System.arraycopy(buffer, 0, encryptedBytes, 0, encryptedBytes.length);
                byte[] decryptedBytes = encryptOrDecrypt(this.server.getPassword(), encryptedBytes);

                DataDeserializer data = new DataDeserializer(decryptedBytes);

                int packetType = data.readInt();

                if (packetType == 0x00) {
                    ServerPacketInRegister packet = new ServerPacketInRegister();
                    packet.deserialize(data);
                    String name = packet.getName();
                    if (!this.server.registerClient(clientId, name)) {
                        send(new ServerPacketOutAcceptRegister(ServerPacketOutAcceptRegister.Action.DENIED, null));
                        this.server.removeClient(this.clientId, "Tried to register with a name in use: " + name);
                        break;
                    }
                }
                else if (!isRegistered()) {
                    this.server.removeClient(this.clientId, "Received packet before registration: " + packetType);
                    break;
                }
                // 0x01 (Server) is outbound only
                else if (packetType == 0x02) {
                    ServerPacketInScript packet = new ServerPacketInScript();
                    packet.deserialize(data);
                    List<String> destinations = packet.getDestinations();
                    ServerPacketOutScript outPacket = new ServerPacketOutScript(packet.getScriptData(),
                            packet.getDefinitionsData());
                    for (String destination : destinations) {
                        if (server.isRegistered(destination)) {
                            server.getClient(destination).send(outPacket);
                        }
                        else {
                            dB.echoError(this.clientName + " tried forwarding a script packet to a server that is not" +
                                    " registered: " + destination);
                        }
                    }
                }
                else if (packetType == 0x03) {
                    ServerPacketInEventResponse packet = new ServerPacketInEventResponse();
                    packet.deserialize(data);
                    EventManager.respond(packet.getEventId(), packet.getDeterminations());
                }
                else if (packetType == 0x04) {
                    ServerPacketInEventSubscribe packet = new ServerPacketInEventSubscribe();
                    packet.deserialize(data);
                    String event = packet.getEvent();
                    if (packet.isSubscribed()) {
                        EventManager.subscribe(event, this);
                        eventSubscriptions.add(event);
                    }
                    else {
                        EventManager.unsubscribe(event, this);
                        eventSubscriptions.remove(event);
                    }
                }
                else if (packetType == 0x05) {
                    ServerPacketInTag packet = new ServerPacketInTag();
                    packet.deserialize(data);
                    ServerPacketOutTag toSend = new ServerPacketOutTag(packet.getBox(), getClientName());
                    this.server.getClient(packet.getServer()).send(toSend);
                }
                else if (packetType == 0x06) {
                    ServerPacketInTagParsed packet = new ServerPacketInTagParsed();
                    packet.deserialize(data);
                    ServerPacketOutTagParsed toSend = new ServerPacketOutTagParsed(packet.getBox());
                    this.server.getClient(packet.getReturnToSender()).send(toSend);
                }
                else {
                    this.server.removeClient(this.clientId, "Received invalid packet: " + packetType);
                    break;
                }
            }
        } catch (IllegalStateException e) {
            dB.echoError(e);
            this.server.removeClient(this.clientId, "Password is incorrect");
        } catch (IOException e) {
            this.server.removeClient(this.clientId, "Client socket closed");
        } catch (Exception e) {
            this.server.removeClient(this.clientId, "Error receiving data from client: " + e.getMessage());
            dB.echoError(e);
        }
    }

    private static byte[] encryptOrDecrypt(String password, byte[] data) throws Exception {
        byte[] result = new byte[data.length];
        byte[] passwordBytes = password.getBytes("UTF-8");
        for (int i = 0; i < data.length; i++)
            result[i] = ((byte)(data[i] ^ passwordBytes[(i % passwordBytes.length)]));
        return result;
    }
}
