package com.morphanone.depenizenbungee.sockets;

import com.morphanone.depenizenbungee.Depenizen;
import com.morphanone.depenizenbungee.dB;
import com.morphanone.depenizenbungee.packets.Packet;
import com.morphanone.depenizenbungee.packets.ServerPacketOutAcceptRegister;
import com.morphanone.depenizenbungee.packets.ServerPacketOutServer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

public class SocketServer implements Runnable {

    private String password;
    private ServerSocket socket;
    private ScheduledTask task;
    private boolean isRunning;
    private ClientConnection[] clients;
    private Map<String, ClientConnection> registeredClients;

    public SocketServer(int maxClients, int port, String password) {
        this.clients = new ClientConnection[maxClients];
        this.registeredClients = new HashMap<String, ClientConnection>();
        this.password = md5(password);
        try {
            this.socket = new ServerSocket(port);
            this.socket.setSoTimeout(0);
            this.start();
            dB.log("SocketServer started on port " + port + ".");
        } catch (Exception e) {
            dB.echoError(e);
        }
    }

    public String getPassword() {
        return password;
    }

    public boolean isRegistered(String name) {
        return registeredClients.containsKey(name.toLowerCase());
    }

    public ClientConnection getClient(String name) {
        return registeredClients.get(name.toLowerCase());
    }

    public void sendToAll(Packet packet) {
        for (ClientConnection clientConnection : registeredClients.values()) {
            clientConnection.send(packet);
        }
    }

    public void sendToAllExcept(int exceptId, Packet packet) {
        for (ClientConnection clientConnection : registeredClients.values()) {
            if (clientConnection.getClientId() == exceptId)
                continue;
            clientConnection.send(packet);
        }
    }

    public void start() {
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
        }
    }

    public void close() {
        for (ClientConnection client : this.clients) {
            if (client != null)
                client.stop();
        }
        try {
            this.socket.close();
            dB.log("SocketServer stopped.");
        } catch (Exception e) {
            dB.echoError(e);
        }
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
            dB.log("Refused client '" + client.getInetAddress() + "': Maximum clients reached.");
            return;
        }

        ClientConnection clientConnection = new ClientConnection(clientId, this, client);
        this.clients[clientId] = clientConnection;
        dB.log("New client connected: " + clientId + " (" + client.getInetAddress() + ")");
    }

    public synchronized boolean registerClient(int clientId, String name) {
        String nameLwr = name.toLowerCase();
        if (registeredClients.containsKey(nameLwr))
            return false;
        ServerPacketOutServer packet = new ServerPacketOutServer(ServerPacketOutServer.Action.REGISTERED, name);
        for (ClientConnection clientConnection : registeredClients.values()) {
            clientConnection.send(packet);
        }
        ClientConnection client = clients[clientId];
        client.send(new ServerPacketOutAcceptRegister(ServerPacketOutAcceptRegister.Action.ACCEPTED, registeredClients.keySet()));
        registeredClients.put(nameLwr, clients[clientId]);
        client.setClientName(name);
        dB.log("Client " + clientId + " has registered as '" + name + "'.");
        return true;
    }

    public synchronized void removeClient(int clientId, String reason) {
        ClientConnection client = this.clients[clientId];
        client.stop();
        dB.log("Client " + (client.isRegistered() ? client.getClientName() : clientId)
                + " has disconnected from SocketServer: " + reason);
        this.clients[clientId] = null;
        if (client.isRegistered()) {
            String name = client.getClientName();
            registeredClients.remove(name.toLowerCase());
            ServerPacketOutServer packet = new ServerPacketOutServer(ServerPacketOutServer.Action.DISCONNECTED, name);
            for (ClientConnection clientConnection : registeredClients.values()) {
                clientConnection.send(packet);
            }
        }
    }

    private static String md5(String string) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(string.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes)
                sb.append(Integer.toHexString(b & 0xFF | 0x100)).substring(1, 3);
            return sb.toString();
        }
        catch (Exception e) {
            dB.echoError(e);
            return null;
        }
    }

    @Override
    public void run() {
        while (task != null && this.isRunning) {
            try {
                Socket socket = this.socket.accept();
                socket.setSoTimeout(0);
                socket.setKeepAlive(true);
                this.addClient(socket);
            } catch (Exception e) {
                dB.echoError(e);
            }
        }
    }
}
