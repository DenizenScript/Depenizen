package net.gnomeffinway.depenizen.support.bungee;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.gnomeffinway.depenizen.Depenizen;
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
    private Socket socket;
    private BukkitTask task;
    private boolean isListening;
    private DataOutputStream output;
    private DataInputStream input;

    public SocketClient(String ipAddress, int port, String password) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.password = md5(password);
    }

    public void send(String message) {
        try {
            ByteArrayDataOutput data = ByteStreams.newDataOutput();

            byte[] messageBytes = message.getBytes("UTF-8");
            data.writeInt(messageBytes.length);
            data.write(messageBytes);

            byte[] encryptedData = encryptOrDecrypt(this.password, data.toByteArray());
            this.output.writeInt(encryptedData.length);
            this.output.write(encryptedData);
            this.output.flush();
        } catch(Exception e) {
            dB.echoError(e);
            this.close("Error sending data to client: " + e.getMessage());
        }
    }

    public void connect() {
        if (!isListening) {
            try {
                this.socket = new Socket();
                this.socket.connect(new InetSocketAddress(this.ipAddress, this.port), 3000);
                this.output = new DataOutputStream(this.socket.getOutputStream());
                this.input = new DataInputStream(this.socket.getInputStream());
                this.isListening = true;
                this.task = Bukkit.getServer().getScheduler().runTaskAsynchronously(Depenizen.getCurrentInstance(), this);
            } catch (Exception e) {
                dB.echoError(e);
            }
        }
    }

    public void close() {
        close(null);
    }

    public void close(String reason) {
        if (isListening) {
            if (reason != null) {
                dB.log("Disconnected from BungeeCord Socket: " + reason);
            }
            this.isListening = false;
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
        // TODO: remove this line
        send("Hello BungeeCord!");
        if (this.socket != null && this.socket.isConnected()) {
            long timeoutExpired = System.currentTimeMillis() + 3000;
            try {
                byte[] buffer;
                String content;
                while (this.isListening) {
                    int receivedEncryptedLength = this.input.readInt();
                    if (receivedEncryptedLength == -1 || System.currentTimeMillis() >= timeoutExpired) {
                        this.close(receivedEncryptedLength == -1 ? "Connection failed" : "Connection timed out");
                        break;
                    }

                    buffer = new byte[receivedEncryptedLength];

                    this.input.read(buffer);
                    byte[] encryptedBytes = new byte[receivedEncryptedLength];
                    System.arraycopy(buffer, 0, encryptedBytes, 0, encryptedBytes.length);
                    byte[] decryptedBytes = encryptOrDecrypt(this.password, encryptedBytes);

                    ByteArrayDataInput data = ByteStreams.newDataInput(decryptedBytes);
                    int contentLength = data.readInt();
                    byte[] contentData = new byte[contentLength];
                    data.readFully(contentData);

                    content = new String(contentData);
                    this.handle(content);
                }

            } catch (IOException e) {
                this.close("Server socket closed");
            } catch (Exception e) {
                this.close("Error receiving data from server: " + e.getMessage());
                dB.echoError(e);
            }
        }
    }

    private void handle(String message) {
        // TODO: make this do stuff
        dB.log("BungeeCord says: " + message);
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
