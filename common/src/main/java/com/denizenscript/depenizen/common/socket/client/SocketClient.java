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

public abstract class SocketClient implements Runnable {

    private String ipAddress;
    private int port;
    private String registrationName;
    private boolean registered;
    private char[] password;
    private Encryption encryption;
    private Socket socket;
    private Thread listenThread;
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
            listenThread = new Thread(this);
            listenThread.start();
            output = new DataOutputStream(socket.getOutputStream());
            input = new DataInputStream(socket.getInputStream());
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
            close("Server socket closed");
        }
        catch (Exception e) {
            close("Failed to send data due to an exception");
            Depenizen.getImplementation().debugException(e);
        }
    }

    public void close(String reason) {
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
            }
            catch (Exception e) {
                Depenizen.getImplementation().debugException(e);
            }
        }
    }

    private int lastPingBit;

    @Override
    public void run() {
        if (socket != null && socket.isConnected()) {
            isConnected = true;
            byte[] buffer;
            try {
                while (input.available() <= 0) {
                    Thread.sleep(1);
                }
                byte[] iv = new byte[input.readInt()];
                input.read(iv);
                this.encryption = new Encryption(password, ENCRYPTION_SALT, iv);
                send(new ClientPacketOutRegister(registrationName));
                connectionLoop:
                while (isConnected) {
                    long timePassed;
                    boolean pinged = false;
                    long start = System.currentTimeMillis();
                    while (input.available() <= 0) {
                        if (!isConnected) {
                            break connectionLoop;
                        }
                        timePassed = System.currentTimeMillis() - start;
                        if (timePassed > 30 * 1000 && !pinged) {
                            lastPingBit = Utilities.getRandomUnsignedByte();
                            send(new ClientPacketOutPing(lastPingBit));
                            Depenizen.getImplementation().debugMessage("Sent ping: " + lastPingBit);
                            pinged = true;
                        }
                        if (timePassed > 60 * 1000) {
                            close("Ping timed out!");
                            break connectionLoop;
                        }
                        Thread.sleep(50);
                    }
                    int receivedEncryptedLength = this.input.readInt();
                    if (receivedEncryptedLength == -1) {
                        close("Connection failed");
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
                        close("Received invalid packet from server: " + packetId);
                        break;
                    }
                    switch (packetType) {
                        case ACCEPT_REGISTER:
                            if (registered) {
                                close("Server tried to accept registration twice");
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
                                close("Specified name in config.yml is already registered to the server");
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
                                close("Invalid ping bit: Expected " + lastPingBit + ", got " + pong.getBit());
                                break connectionLoop;
                            }
                            break;
                        case UPDATE_SERVER:
                            ClientPacketInUpdateServer updateServer = new ClientPacketInUpdateServer();
                            updateServer.deserialize(data);
                            handleUpdateServer(updateServer.getName(), updateServer.isRegistered());
                            break;
                    }
                }
                listenThread = null;
            }
            catch (IllegalStateException e) {
                close("Password is incorrect");
            }
            catch (SocketTimeoutException e) {
                close("Connection timed out");
                //attemptReconnect();
            }
            catch (IOException e) {
                close("Server socket closed");
                //attemptReconnect();
            }
            catch (Exception e) {
                close("Error receiving data from server: " + e.getMessage());
                Depenizen.getImplementation().debugException(e);
            }
        }
    }

    public abstract void handleAcceptRegister(String registrationName, List<String> existingServers);

    public abstract void handleUpdateServer(String serverName, boolean registered);
}
