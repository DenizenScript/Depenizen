package com.denizenscript.depenizen.common.socket.client;

import com.denizenscript.depenizen.common.Depenizen;
import com.denizenscript.depenizen.common.socket.DataDeserializer;
import com.denizenscript.depenizen.common.socket.DataSerializer;
import com.denizenscript.depenizen.common.socket.Packet;
import com.denizenscript.depenizen.common.socket.client.packet.ClientPingPacketIn;
import com.denizenscript.depenizen.common.socket.client.packet.ClientPingPacketOut;
import com.denizenscript.depenizen.common.util.Encryption;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;

public class SocketClient implements Runnable {

    private String ipAddress;
    private int port;
    private String registrationName;
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
        this.encryption = new Encryption(password, ENCRYPTION_SALT);
    }

    public void connect() throws IOException {
        if (!isConnected) {
            socket = new Socket(ipAddress, port);
            listenThread = new Thread(this);
            listenThread.start();
            output = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        }
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void send(Packet packet) {
        try {
            DataSerializer data = new DataSerializer();
            packet.serialize(data);
            byte[] encryptedData = encryption.encrypt(data.toByteArray());
            output.writeInt(encryptedData.length);
            output.write(encryptedData);
            output.flush();
        }
        catch (Exception e) {
            close("Error sending data to server: " + e.getMessage());
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

    private long lastPingValue;
    private long lastPingTime;

    @Override
    public void run() {
        if (socket != null && socket.isConnected()) {
            isConnected = true;
            byte[] buffer;
            try {
                while (isConnected) {
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
                    Packet.ServerBound packetType = Packet.ServerBound.getById(packetId);
                    if (packetType == null) {
                        close("Received invalid packet from server: " + packetId);
                        return;
                    }
                    switch (packetType) {
                        case PING:
                            ClientPingPacketIn pingPacketIn = new ClientPingPacketIn();
                            pingPacketIn.deserialize(data);
                            send(new ClientPingPacketOut(pingPacketIn.getBit()));
                            long current = System.currentTimeMillis();
                            lastPingValue = current - lastPingTime;
                            lastPingTime = current;
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
}
