package com.denizenscript.depenizen.common.socket.server;

import com.denizenscript.depenizen.common.Depenizen;
import com.denizenscript.depenizen.common.socket.DataDeserializer;
import com.denizenscript.depenizen.common.socket.Packet;
import com.denizenscript.depenizen.common.socket.server.packet.ServerPingPacketIn;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientConnection implements Runnable {

    private int clientId;
    private SocketServer server;
    private Socket client;
    private Thread listenThread;
    private DataOutputStream output;
    private DataInputStream input;
    private boolean isConnected;

    public ClientConnection(int clientId, SocketServer server, Socket client) {
        this.clientId = clientId;
        this.server = server;
        this.client = client;
    }

    public void start() throws IOException {
        if (!isConnected) {
            output = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));
            input = new DataInputStream(new BufferedInputStream(client.getInputStream()));
            listenThread = new Thread(this);
            listenThread.start();
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
            catch (Exception e) {
                Depenizen.getImplementation().debugException(e);
            }
        }
    }

    private int lastPingBit;

    @Override
    public void run() {
        if (client != null && client.isConnected()) {
            isConnected = true;
            byte[] buffer;
            try {
                while (isConnected) {
                    int receivedEncryptedLength = this.input.readInt();
                    if (receivedEncryptedLength == -1) {
                        server.removeClient(clientId, "Connection failed");
                        return;
                    }
                    if (receivedEncryptedLength > 1024 * 1024) {
                        server.removeClient(clientId, "Incoming data > 1MB. Was it encrypted incorrectly?");
                        return;
                    }
                    buffer = new byte[receivedEncryptedLength];
                    input.read(buffer);
                    byte[] encryptedBytes = new byte[receivedEncryptedLength];
                    System.arraycopy(buffer, 0, encryptedBytes, 0, encryptedBytes.length);
                    byte[] decryptedBytes = server.getEncryption().decrypt(encryptedBytes);
                    DataDeserializer data = new DataDeserializer(decryptedBytes);
                    int packetId = data.readUnsignedByte();
                    Packet.ClientBound packetType = Packet.ClientBound.getById(packetId);
                    if (packetType == null) {
                        server.removeClient(clientId, "Received invalid packet from server: " + packetId);
                        return;
                    }
                    switch (packetType) {
                        case PING:
                            ServerPingPacketIn pingPacketIn = new ServerPingPacketIn();
                            pingPacketIn.deserialize(data);
                            if (pingPacketIn.getBit() != lastPingBit) {
                                server.removeClient(clientId, "Invalid ping bit: Expected " + lastPingBit + ", got " + pingPacketIn.getBit());
                                return;
                            }
                            break;
                    }
                }
                listenThread = null;
            }
            catch (IllegalStateException e) {
                server.removeClient(clientId, "Password is incorrect");
            }
            catch (IOException e) {
                server.removeClient(clientId, "Client socket closed");
            }
            catch (Exception e) {
                server.removeClient(clientId, "Error receiving data from client: " + e.getMessage());
                Depenizen.getImplementation().debugException(e);
            }
        }
    }
}
