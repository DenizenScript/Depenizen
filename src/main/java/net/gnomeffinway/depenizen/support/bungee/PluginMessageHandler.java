package net.gnomeffinway.depenizen.support.bungee;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.gnomeffinway.depenizen.Settings;

import java.util.ArrayList;
import java.util.List;

public class PluginMessageHandler {

    private static final List<byte[]> queuedMessages = new ArrayList<byte[]>();

    public static void handle(byte[] message) {
        ByteArrayDataInput data = ByteStreams.newDataInput(message);
        String subChannel = data.readUTF();

    }

    public static void sendRegister() {
        ByteArrayDataOutput data = ByteStreams.newDataOutput();
        data.writeUTF(Settings.socketName());
    }
}
