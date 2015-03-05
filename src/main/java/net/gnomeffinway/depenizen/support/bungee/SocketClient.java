package net.gnomeffinway.depenizen.support.bungee;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizencore.scripts.queues.ScriptQueue;
import net.aufdemrand.denizencore.scripts.queues.core.InstantQueue;
import net.gnomeffinway.depenizen.Depenizen;
import net.gnomeffinway.depenizen.objects.bungee.dServer;
import net.gnomeffinway.depenizen.support.bungee.packets.*;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.MessageDigest;

public class SocketClient implements Runnable {

    private String ipAddress;
    private int port;
    private String password;
    private String registrationName;
    private int timeout;
    private Socket socket;
    private BukkitTask task;
    private boolean isConnected;
    private DataOutputStream output;
    private DataInputStream input;

    public SocketClient(String ipAddress, int port, String password, String name, int timeout) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.password = md5(password);
        this.registrationName = name;
        this.timeout = timeout;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void send(Packet packet) {
        try {
            ByteArrayDataOutput data = ByteStreams.newDataOutput();

            packet.serialize(data);

            byte[] encryptedData = encryptOrDecrypt(this.password, data.toByteArray());
            this.output.writeInt(encryptedData.length);
            this.output.write(encryptedData);
            this.output.flush();
        } catch(Exception e) {
            dB.echoError(e);
            this.close("Error sending data to server: " + e.getMessage());
        }
    }

    public void connect() {
        if (!isConnected) {
            try {
                this.socket = new Socket();
                this.socket.connect(new InetSocketAddress(this.ipAddress, this.port), timeout);
                this.output = new DataOutputStream(this.socket.getOutputStream());
                this.input = new DataInputStream(this.socket.getInputStream());
                this.isConnected = true;
                this.task = Bukkit.getServer().getScheduler().runTaskAsynchronously(Depenizen.getCurrentInstance(), this);
                this.send(new ClientPacketOutRegister(this.registrationName));
            } catch (IOException e) {
                dB.log("Error while connecting to BungeeCord Socket: " + e.getMessage());
            } catch (Exception e) {
                dB.echoError(e);
            }
        }
    }

    public void close() {
        close(null);
    }

    public void close(String reason) {
        if (isConnected) {
            if (reason != null) {
                dB.log("Disconnected from BungeeCord Socket: " + reason);
            }
            this.isConnected = false;
            this.task.cancel();
            this.task = null;
            try {
                if (this.output != null) this.output.close();
                if (this.input != null) this.input.close();
                if (this.socket != null) this.socket.close();
            } catch (Exception e) {
                dB.echoError(e);
            }
        }
    }

    @Override
    public void run() {
        if (this.socket != null && this.socket.isConnected()) {
            long timeoutExpired = System.currentTimeMillis() + this.timeout;
            try {
                byte[] buffer;
                while (this.isConnected) {
                    int receivedEncryptedLength = this.input.readInt();
                    if (receivedEncryptedLength == -1 || (this.timeout > 0 && System.currentTimeMillis() >= timeoutExpired)) {
                        this.close(receivedEncryptedLength == -1 ? "Connection failed" : "Connection timed out");
                        break;
                    }

                    buffer = new byte[receivedEncryptedLength];

                    this.input.read(buffer);
                    byte[] encryptedBytes = new byte[receivedEncryptedLength];
                    System.arraycopy(buffer, 0, encryptedBytes, 0, encryptedBytes.length);
                    byte[] decryptedBytes = encryptOrDecrypt(this.password, encryptedBytes);

                    ByteArrayDataInput data = ByteStreams.newDataInput(decryptedBytes);

                    int packetType = data.readInt();

                    if (packetType == 0x00) {
                        ClientPacketInAcceptRegister packet = new ClientPacketInAcceptRegister();
                        packet.deserialize(data);
                        if (packet.isAccepted()) {
                            dB.log("Successfully registered name with the server");
                            for (String server : packet.getServerList()) {
                                if (!server.isEmpty())
                                    dServer.addOnlineServer(server);
                            }
                        }
                        else
                            this.close("Specified name in config.yml is already registered to the server");
                    }
                    else if (packetType == 0x01) {
                        ClientPacketInServer packet = new ClientPacketInServer();
                        packet.deserialize(data);
                        if (packet.getAction() == ClientPacketInServer.Action.REGISTERED)
                            dServer.addOnlineServer(packet.getServerName());
                        else if (packet.getAction() == ClientPacketInServer.Action.DISCONNECTED)
                            dServer.removeOnlineServer(packet.getServerName());
                    }
                    else if (packetType == 0x02) {
                        ClientPacketInScript packet = new ClientPacketInScript();
                        packet.deserialize(data);
                        InstantQueue queue = new InstantQueue(ScriptQueue.getNextId("BUNGEE"));
                        queue.addEntries(packet.getScriptEntries());
                        queue.getAllDefinitions().putAll(packet.getDefinitions());
                        queue.start();
                    }
                    else {
                        this.close("Received invalid packet from server: " + packetType);
                    }
                }

            } catch (IllegalStateException e) {
                this.close("Password is incorrect");
            } catch (IOException e) {
                this.close("Server socket closed");
            } catch (Exception e) {
                this.close("Error receiving data from server: " + e.getMessage());
                dB.echoError(e);
            }
        }
    }

    private static String md5(String string) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(string.getBytes());
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

    private static byte[] encryptOrDecrypt(String password, byte[] data) {
        byte[] result = new byte[data.length];
        byte[] passwordBytes = password.getBytes();
        for (int i = 0; i < data.length; i++)
            result[i] = ((byte)(data[i] ^ passwordBytes[(i % passwordBytes.length)]));
        return result;
    }
}
