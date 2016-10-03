package com.denizenscript.depenizen.common.socket.server;

import com.denizenscript.depenizen.common.Depenizen;
import com.denizenscript.depenizen.common.util.Encryption;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;

public class SocketServer implements Runnable {

    private int port;
    private ClientConnection[] clients;
    private Encryption encryption;
    private ServerSocket socket;
    private Thread listenThread;
    private boolean isRunning;

    private static final byte[] ENCRYPTION_SALT = "m6n4sOqVxhIgIWA3yfJF".getBytes(Charset.forName("UTF-8"));

    public SocketServer(int port, int maxClients, char[] password) throws GeneralSecurityException {
        this.port = port;
        this.clients = new ClientConnection[maxClients];
        this.encryption = new Encryption(password, ENCRYPTION_SALT);
    }

    public void start() throws IOException {
        if (!isRunning) {
            socket = new ServerSocket(port);
            listenThread = new Thread(this);
            listenThread.start();
        }
    }

    public void stop() {
        if (isRunning) {
            isRunning = false;
            for (ClientConnection client : clients) {
                if (client != null) {
                    client.stop();
                }
            }
            try {
                socket.close();
                Depenizen.getImplementation().debugMessage("SocketServer stopped.");
            }
            catch (Exception e) {
                Depenizen.getImplementation().debugException(e);
            }
        }
    }

    public Encryption getEncryption() {
        return encryption;
    }

    private int getNewClientId() {
        for (int id = 0; id < clients.length; id++) {
            if (clients[id] == null)
                return id;
        }
        return -1;
    }

    private void addClient(Socket client) {
        int clientId = getNewClientId();
        if (clientId == -1) {
            Depenizen.getImplementation().debugMessage("Refused client '" + client.getInetAddress() + "': Maximum clients reached.");
            return;
        }
        ClientConnection clientConnection = new ClientConnection(clientId, this, client);
        this.clients[clientId] = clientConnection;
        Depenizen.getImplementation().debugMessage("New client connected: " + clientId + " (" + client.getInetAddress() + ")");
    }

    public void removeClient(int clientId, String reason) {
        ClientConnection client = this.clients[clientId];
        client.stop();
        Depenizen.getImplementation().debugMessage("Client " + (/*client.isRegistered() ? client.getClientName() : */clientId)
                + " has disconnected from SocketServer: " + reason);
        this.clients[clientId] = null;
        /*
        if (client.isRegistered()) {
            String name = client.getClientName();
            registeredClients.remove(name.toLowerCase());
            ServerPacketOutServer packet = new ServerPacketOutServer(ServerPacketOutServer.Action.DISCONNECTED, name);
            for (ClientConnection clientConnection : registeredClients.values()) {
                clientConnection.send(packet);
            }
        }
        */
    }

    @Override
    public void run() {
        if (socket != null && socket.isBound()) {
            isRunning = true;
            while (isRunning) {
                try {
                    addClient(socket.accept());
                }
                catch (IOException e) {
                    Depenizen.getImplementation().debugException(e);
                }
            }
        }
    }
}
