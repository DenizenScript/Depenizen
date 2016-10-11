package com.denizenscript.depenizen.common.socket.server;

import com.denizenscript.depenizen.common.Depenizen;
import com.denizenscript.depenizen.common.socket.server.packet.ServerPacketOutAcceptRegister;
import com.denizenscript.depenizen.common.socket.server.packet.ServerPacketOutUpdateServer;
import com.denizenscript.depenizen.common.util.Encryption;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public abstract class SocketServer implements Runnable {

    private int port;
    private ClientConnection[] clients;
    private Map<String, ClientConnection> registeredClients;
    private Encryption encryption;
    private ServerSocket socket;
    private Thread listenThread;
    private boolean isRunning;

    private static final byte[] ENCRYPTION_SALT = "m6n4sOqVxhIgIWA3yfJF".getBytes(Charset.forName("UTF-8"));

    public SocketServer(int port, int maxClients, char[] password) throws GeneralSecurityException {
        this.port = port;
        this.clients = new ClientConnection[maxClients];
        this.registeredClients = new HashMap<String, ClientConnection>();
        this.encryption = new Encryption(password, ENCRYPTION_SALT);
    }

    public void start() throws IOException {
        if (!isRunning) {
            Depenizen.getImplementation().debugMessage("Starting SocketServer on port " + port);
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
            if (listenThread != null) {
                listenThread.interrupt();
                listenThread = null;
            }
        }
    }

    public Map<String, ClientConnection> getRegisteredClients() {
        return registeredClients;
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
        Depenizen.getImplementation().debugMessage("Client attempting to join");
        int clientId = getNewClientId();
        if (clientId == -1) {
            Depenizen.getImplementation().debugMessage("Refused client '" + client.getInetAddress() + "': Maximum clients reached.");
            return;
        }
        ClientConnection clientConnection = new ClientConnection(clientId, this, client);
        try {
            clientConnection.start();
        }
        catch (IOException e) {
            Depenizen.getImplementation().debugMessage("Failed to accept client '" + client.getInetAddress() + "':");
            Depenizen.getImplementation().debugException(e);
        }
        this.clients[clientId] = clientConnection;
        Depenizen.getImplementation().debugMessage("New client connected: " + clientId + " (" + client.getInetAddress() + ")");
    }

    public boolean registerClient(int clientId, String name, boolean bungeeScriptCompatible) {
        String nameLwr = name.toLowerCase();
        ClientConnection client = clients[clientId];
        if (registeredClients.containsKey(nameLwr)) {
            client.trySend(new ServerPacketOutAcceptRegister(false, null));
            removeClient(clientId, "Tried to register with a name in use: " + name);
            return false;
        }
        client.trySend(new ServerPacketOutAcceptRegister(true, registeredClients.keySet()));
        registeredClients.put(nameLwr, client);
        client.setClientName(name);
        client.setBungeeScriptCompatible(bungeeScriptCompatible);
        Depenizen.getImplementation().debugMessage("Client " + clientId + " has registered as '" + name + "'");
        ServerPacketOutUpdateServer packet = new ServerPacketOutUpdateServer(name, true);
        for (ClientConnection clientConnection : new HashSet<ClientConnection>(registeredClients.values())) {
            clientConnection.trySend(packet);
        }
        return true;
    }

    public void removeClient(int clientId, String reason) {
        if (clients.length <= clientId || clients[clientId] == null) {
            return;
        }
        ClientConnection client = this.clients[clientId];
        client.stop();
        Depenizen.getImplementation().debugMessage("Client " + (client.isRegistered() ? client.getClientName() : clientId)
                + " has disconnected from SocketServer: " + reason);
        this.clients[clientId] = null;
        if (client.isRegistered()) {
            String name = client.getClientName();
            registeredClients.remove(name.toLowerCase());
            ServerPacketOutUpdateServer packet = new ServerPacketOutUpdateServer(name, false);
            for (ClientConnection clientConnection : new HashSet<ClientConnection>(registeredClients.values())) {
                clientConnection.trySend(packet);
            }
        }
    }

    @Override
    public void run() {
        if (socket != null && socket.isBound()) {
            isRunning = true;
            while (isRunning) {
                try {
                    Depenizen.getImplementation().debugMessage("Waiting for next client...");
                    addClient(socket.accept());
                }
                catch (IOException e) {
                    Depenizen.getImplementation().debugException(e);
                }
            }
        }
        listenThread = null;
    }

    protected abstract void handleEventSubscription(ClientConnection client, String event, boolean subscribed);

    protected abstract void handleEventResponse(ClientConnection client, long id, Map<String, String> response);
}
