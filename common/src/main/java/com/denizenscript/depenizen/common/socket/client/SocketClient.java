package com.denizenscript.depenizen.common.socket.client;

import com.denizenscript.depenizen.common.Depenizen;
import com.denizenscript.depenizen.common.socket.DataDeserializer;
import com.denizenscript.depenizen.common.socket.DataSerializer;
import com.denizenscript.depenizen.common.socket.Packet;
import com.denizenscript.depenizen.common.socket.client.packet.*;
import com.denizenscript.depenizen.common.util.Encryption;
import com.denizenscript.depenizen.common.util.Utilities;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class SocketClient implements Runnable {

    private String ipAddress;
    private int port;
    private String registrationName;
    private boolean registered;
    private char[] password;
    private Encryption encryption;
    private Socket socket;
    private Thread listenThread;
    private Thread reconnectThread;
    private DataOutputStream output;
    private DataInputStream input;
    private boolean isConnected;

    private static final byte[] ENCRYPTION_SALT = "m6n4sOqVxhIgIWA3yfJF".getBytes(Charset.forName("UTF-8"));

    public SocketClient(String ipAddress, int port, String name, char[] password) throws GeneralSecurityException {
        this.ipAddress = ipAddress;
        this.port = port;
        this.registrationName = name;
        this.password = password;
    }

    public void connect() throws IOException {
        if (!isConnected) {
            socket = new Socket(ipAddress, port);
            output = new DataOutputStream(socket.getOutputStream());
            input = new DataInputStream(socket.getInputStream());
            listenThread = new Thread(this);
            listenThread.start();
        }
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void send(Packet packet) throws GeneralSecurityException, IOException {
        DataSerializer data = new DataSerializer();
        packet.serialize(data);
        byte[] encryptedData = encryption.encrypt(data.toByteArray());
        output.writeInt(encryptedData.length);
        output.write(encryptedData);
        output.flush();
    }

    public void trySend(Packet packet) {
        try {
            send(packet);
        }
        catch (IOException e) {
            close("Server socket closed", true);
        }
        catch (Exception e) {
            close("Failed to send data due to an exception", true);
            Depenizen.getImplementation().debugException(e);
        }
    }

    public void close(String reason, boolean shouldReconnect) {
        if (isConnected) {
            if (reason != null) {
                Depenizen.getImplementation().debugMessage("Disconnected from socket: " + reason);
            }
            isConnected = false;
            try {
                if (output != null) {
                    output.close();
                }
                if (input != null) {
                    input.close();
                }
                if (socket != null) {
                    socket.close();
                }
                if (shouldReconnect) {
                    attemptReconnect();
                }
            }
            catch (Exception e) {
                Depenizen.getImplementation().debugException(e);
            }
        }
    }

    public void attemptReconnect() {
        if (!isConnected && reconnectThread == null) {
            Depenizen.getImplementation().debugMessage("Attempting to reconnect...");
            reconnectThread = new Thread(new ReconnectTask(this, getReconnectAttempts(), getReconnectDelay()));
            reconnectThread.start();
        }
    }

    public boolean isReconnecting() {
        return reconnectThread != null;
    }

    private int lastPingBit;

    @Override
    public void run() {
        if (socket != null && socket.isConnected()) {
            isConnected = true;
            reconnectThread = null;
            byte[] buffer;
            try {
                while (input.available() <= 0) {
                    Thread.sleep(1);
                }
                byte[] iv = new byte[input.readInt()];
                input.read(iv);
                this.encryption = new Encryption(password, ENCRYPTION_SALT, iv);
                send(new ClientPacketOutRegister(registrationName, isBungeeScriptCompatible()));
                for (String event : getSubscribedEvents()) {
                    send(new ClientPacketOutEventSubscription(event, true));
                }
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
                        if (timePassed > getPingDelay() && !pinged) {
                            lastPingBit = Utilities.getRandomUnsignedByte();
                            send(new ClientPacketOutPing(lastPingBit));
                            pinged = true;
                        }
                        if (timePassed > getPingDelay() + getPingTimeout()) {
                            close("Ping timed out!", true);
                            break connectionLoop;
                        }
                        Thread.sleep(50);
                    }
                    if (receivedEncryptedLength == -1) {
                        close("Connection failed", true);
                        break;
                    }
                    buffer = new byte[receivedEncryptedLength];
                    input.read(buffer);
                    byte[] encryptedBytes = new byte[receivedEncryptedLength];
                    System.arraycopy(buffer, 0, encryptedBytes, 0, encryptedBytes.length);
                    byte[] decryptedBytes = encryption.decrypt(encryptedBytes);
                    DataDeserializer data = new DataDeserializer(decryptedBytes);
                    int packetId = data.readUnsignedByte();
                    Packet.ClientBound packetType = Packet.ClientBound.getById(packetId);
                    if (packetType == null) {
                        close("Received invalid packet from server: " + packetId, true);
                        break;
                    }
                    switch (packetType) {
                        case ACCEPT_REGISTER:
                            if (registered) {
                                close("Server tried to accept registration twice", true);
                                break connectionLoop;
                            }
                            ClientPacketInAcceptRegister acceptRegister = new ClientPacketInAcceptRegister();
                            acceptRegister.deserialize(data);
                            if (acceptRegister.isAccepted()) {
                                Depenizen.getImplementation().debugMessage("Successfully registered as '" + registrationName + "'");
                                handleAcceptRegister(registrationName, acceptRegister.getExistingServers());
                                registered = true;
                            }
                            else {
                                close("Specified name in config.yml is already registered to the server", false);
                                break connectionLoop;
                            }
                            break;
                        case PING:
                            ClientPacketInPing ping = new ClientPacketInPing();
                            ping.deserialize(data);
                            send(new ClientPacketOutPong(ping.getBit()));
                            break;
                        case PONG:
                            ClientPacketInPong pong = new ClientPacketInPong();
                            pong.deserialize(data);
                            if (pong.getBit() != lastPingBit) {
                                close("Invalid ping bit: Expected " + lastPingBit + ", got " + pong.getBit(), true);
                                break connectionLoop;
                            }
                            break;
                        case UPDATE_SERVER:
                            ClientPacketInUpdateServer updateServer = new ClientPacketInUpdateServer();
                            updateServer.deserialize(data);
                            handleUpdateServer(updateServer.getName(), updateServer.isRegistered());
                            break;
                        case SCRIPT:
                            ClientPacketInScript script = new ClientPacketInScript();
                            script.deserialize(data);
                            handleScript(script.shouldDebug(), script.getScriptEntries(), script.getDefinitions());
                            break;
                        case RUN_SCRIPT:
                            ClientPacketInRunScript runScript = new ClientPacketInRunScript();
                            runScript.deserialize(data);
                            handleRunScript(runScript.getScriptName(), runScript.getDefinitions(), runScript.shouldDebug());
                            break;
                        case TAG:
                            ClientPacketInTag tag = new ClientPacketInTag();
                            tag.deserialize(data);
                            String tagResult = handleTag(tag.getTag(), tag.shouldDebug(), tag.getDefinitions());
                            send(new ClientPacketOutParsedTag(tag.getFrom(), tag.getId(), tagResult));
                            break;
                        case PARSED_TAG:
                            ClientPacketInParsedTag parsedTag = new ClientPacketInParsedTag();
                            parsedTag.deserialize(data);
                            handleParsedTag(parsedTag.getId(), parsedTag.getResult());
                            break;
                        case EVENT:
                            ClientPacketInEvent event = new ClientPacketInEvent();
                            event.deserialize(data);
                            Map<String, String> eventResponse = handleEvent(event.getEvent(), event.getContext());
                            if (event.shouldRespond()) {
                                send(new ClientPacketOutEventResponse(event.getId(), eventResponse));
                            }
                            break;
                    }
                }
                listenThread = null;
            }
            catch (IllegalStateException e) {
                close("Password is incorrect", false);
            }
            catch (SocketTimeoutException e) {
                close("Connection timed out", true);
            }
            catch (IOException e) {
                close("Server socket closed", true);
            }
            catch (Exception e) {
                close("Error receiving data from server: " + e.getMessage(), true);
                Depenizen.getImplementation().debugException(e);
            }
        }
    }

    protected abstract boolean isBungeeScriptCompatible();

    protected abstract long getPingDelay();

    protected abstract long getPingTimeout();

    protected abstract int getReconnectAttempts();

    protected abstract long getReconnectDelay();

    protected abstract Set<String> getSubscribedEvents();

    protected abstract void fireReconnectFailEvent();

    protected abstract void handleAcceptRegister(String registrationName, List<String> existingServers);

    protected abstract void handleUpdateServer(String serverName, boolean registered);

    protected abstract void handleScript(boolean shouldDebug, Map<String, List<String>> scriptEntries, Map<String, String> definitions);

    protected abstract void handleRunScript(String scriptName, Map<String, String> definitions, boolean shouldDebug);

    protected abstract String handleTag(String tag, boolean shouldDebug, Map<String, String> definitions);

    protected abstract void handleParsedTag(int id, String result);

    protected abstract Map<String, String> handleEvent(String event, Map<String, String> context);
}
