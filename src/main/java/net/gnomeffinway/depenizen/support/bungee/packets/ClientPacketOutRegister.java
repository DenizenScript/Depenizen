package net.gnomeffinway.depenizen.support.bungee.packets;

import com.google.common.io.ByteArrayDataOutput;

public class ClientPacketOutRegister extends Packet {

    private byte[] name;

    public ClientPacketOutRegister(String name) {
        this.name = name.getBytes();
    }

    @Override
    public void serialize(ByteArrayDataOutput output) {
        output.writeInt(0x00);
        output.writeInt(name.length);
        output.write(name);
    }
}
