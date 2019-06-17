package com.denizenscript.depenizen.common.socket.server.packet;

import com.denizenscript.depenizen.common.socket.DataDeserializer;
import com.denizenscript.depenizen.common.socket.Packet;

public class ServerPacketInRegister extends Packet {

    private String name;
    private boolean bungeeScriptCompatible;

    @Override
    public void deserialize(DataDeserializer deserializer) {
        name = deserializer.readString();
        bungeeScriptCompatible = deserializer.readBoolean();
    }

    public String getName() {
        return name;
    }

    public boolean isBungeeScriptCompatible() {
        return bungeeScriptCompatible;
    }
}
