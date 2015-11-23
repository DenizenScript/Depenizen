package net.gnomeffinway.depenizen.support.bungee;

import net.aufdemrand.denizencore.scripts.queues.ScriptQueue;
import net.aufdemrand.denizencore.scripts.queues.core.InstantQueue;
import net.aufdemrand.denizencore.tags.TagManager;
import net.aufdemrand.denizencore.utilities.debugging.dB;
import net.gnomeffinway.depenizen.Depenizen;
import net.gnomeffinway.depenizen.Settings;
import net.gnomeffinway.depenizen.commands.bungee.BungeeTagCommand;
import net.gnomeffinway.depenizen.events.bungee.BungeeScriptEvent;
import net.gnomeffinway.depenizen.objects.bungee.dServer;
import net.gnomeffinway.depenizen.support.bungee.packets.*;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.security.MessageDigest;
import java.util.Map;

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
    private boolean isReconnecting;

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
            DataSerializer data = new DataSerializer();

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
                this.socket.setSoTimeout(timeout);
                this.output = new DataOutputStream(this.socket.getOutputStream());
                this.input = new DataInputStream(this.socket.getInputStream());
                this.isConnected = true;
                this.task = Bukkit.getServer().getScheduler().runTaskAsynchronously(Depenizen.getCurrentInstance(), this);
                this.send(new ClientPacketOutRegister(this.registrationName));
            } catch (Exception e) {
                dB.log("Error while connecting to BungeeCord Socket: " + e.getMessage());
                if (socket.isConnected()) {
                    close();
                }
                attemptReconnect();
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
                if (this.output != null) {
                    this.output.close();
                }
                if (this.input != null) {
                    this.input.close();
                }
                if (this.socket != null) {
                    this.socket.close();
                }
            } catch (Exception e) {
                dB.echoError(e);
            }
        }
    }

    @Override
    public void run() {
        if (this.socket != null && this.socket.isConnected()) {
            try {
                byte[] buffer;
                while (this.isConnected) {
                    int receivedEncryptedLength = this.input.readInt();
                    if (receivedEncryptedLength == -1) {
                        this.close("Connection failed");
                        break;
                    }

                    buffer = new byte[receivedEncryptedLength];

                    this.input.read(buffer);
                    byte[] encryptedBytes = new byte[receivedEncryptedLength];
                    System.arraycopy(buffer, 0, encryptedBytes, 0, encryptedBytes.length);
                    byte[] decryptedBytes = encryptOrDecrypt(this.password, encryptedBytes);

                    DataDeserializer data = new DataDeserializer(decryptedBytes);

                    int packetType = data.readInt();

                    if (packetType == 0x00) {
                        ClientPacketInAcceptRegister packet = new ClientPacketInAcceptRegister();
                        packet.deserialize(data);
                        if (packet.isAccepted()) {
                            dB.log("Successfully registered name with the server");
                            dServer.addOnlineServer(this.registrationName);
                            for (String server : packet.getServerList()) {
                                if (!server.isEmpty()) {
                                    if (dB.verbose) {
                                        dB.log("[Bungee]: Registered with " + server);
                                    }
                                    dServer.addOnlineServer(server);
                                }
                            }
                        }
                        else
                            this.close("Specified name in config.yml is already registered to the server");
                    }
                    else if (packetType == 0x01) {
                        ClientPacketInServer packet = new ClientPacketInServer();
                        packet.deserialize(data);
                        if (packet.getAction() == ClientPacketInServer.Action.REGISTERED) {
                            if (dB.verbose) {
                                dB.log("[Bungee]: Registered with " + packet.getServerName());
                            }
                            dServer.addOnlineServer(packet.getServerName());
                        }
                        else if (packet.getAction() == ClientPacketInServer.Action.DISCONNECTED) {
                            if (dB.verbose) {
                                dB.log("[Bungee]: Disconnected from " + packet.getServerName());
                            }
                            dServer.removeOnlineServer(packet.getServerName());
                        }
                    }
                    else if (packetType == 0x02) {
                        ClientPacketInScript packet = new ClientPacketInScript();
                        packet.deserialize(data);
                        InstantQueue queue = new InstantQueue(ScriptQueue.getNextId("BUNGEE_CMD"));
                        queue.addEntries(packet.getScriptEntries());
                        queue.getAllDefinitions().putAll(packet.getDefinitions());
                        queue.start();
                    }
                    else if (packetType == 0x03) {
                        ClientPacketInEvent packet = new ClientPacketInEvent();
                        packet.deserialize(data);
                        long id = packet.getEventId();
                        String name = packet.getEventName();
                        Map<String, String> context = packet.getContext();
                        Map<String, String> determinations = BungeeScriptEvent.fire(name, context);
                        if (packet.shouldSendResponse() && determinations != null) {
                            ClientPacketOutEventResponse response = new ClientPacketOutEventResponse(id, determinations);
                            send(response);
                        }
                    }
                    // 0x04 (EventSubscribe) is outbound
                    else if (packetType == 0x05) {
                        ClientPacketInTag packet = new ClientPacketInTag();
                        packet.deserialize(data);
                        DefinitionsWrapper definitions = new DefinitionsWrapper(packet.getDefinitions());
                        String parsed = TagManager.tag(packet.getTag(), new BungeeTagContext(packet.shouldDebug(), definitions));
                        send(new ClientPacketOutTagParsed(packet.getId(), parsed, packet.getFrom()));
                    }
                    else if (packetType == 0x06) {
                        ClientPacketInTagParsed packet = new ClientPacketInTagParsed();
                        packet.deserialize(data);
                        BungeeTagCommand.returnTag(packet.getId(), packet.getResult());
                    }
                    else {
                        this.close("Received invalid packet from server: " + packetType);
                    }
                }

            } catch (IllegalStateException e) {
                this.close("Password is incorrect");
            } catch (SocketTimeoutException e) {
                this.close("Connection timed out");
                attemptReconnect();
            } catch (IOException e) {
                this.close("Server socket closed");
                attemptReconnect();
            } catch (Exception e) {
                this.close("Error receiving data from server: " + e.getMessage());
                dB.echoError(e);
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

    private static byte[] encryptOrDecrypt(String password, byte[] data) throws Exception {
        byte[] result = new byte[data.length];
        byte[] passwordBytes = password.getBytes("UTF-8");
        for (int i = 0; i < data.length; i++)
            result[i] = ((byte)(data[i] ^ passwordBytes[(i % passwordBytes.length)]));
        return result;
    }

    private void attemptReconnect() {
        if (this.isReconnecting) {
            return;
        }
        final long delay = Settings.socketReconnectDelay();
        this.isReconnecting = true;
        Bukkit.getServer().getScheduler().runTaskAsynchronously(Depenizen.getCurrentInstance(),
                new Runnable() {
                    @Override
                    public void run() {
                        while (!SocketClient.this.isConnected) {
                            try {
                                Thread.sleep(delay);
                                SocketClient.this.connect();
                            } catch (InterruptedException e) {
                                dB.echoError(e);
                                return;
                            }
                        }
                        SocketClient.this.isReconnecting = false;
                    }
                });
    }
}
