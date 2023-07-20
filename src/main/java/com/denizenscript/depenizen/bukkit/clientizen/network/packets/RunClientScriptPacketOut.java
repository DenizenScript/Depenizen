package com.denizenscript.depenizen.bukkit.clientizen.network.packets;

import com.denizenscript.depenizen.bukkit.clientizen.network.ClientizenPacketOut;
import io.netty.buffer.ByteBuf;

import java.util.Map;

public class RunClientScriptPacketOut extends ClientizenPacketOut {

    public RunClientScriptPacketOut(String script, String path, Map<String, String> definitions) {
        this.script = script;
        this.path = path;
        this.definitions = definitions;
    }

    String script;
    String path;
    Map<String, String> definitions;

    @Override
    public void writeTo(ByteBuf buf) {
        writeString(buf, script);
        writeNullable(buf, path, this::writeString);
        writeStringMap(buf, definitions);
    }

    @Override
    public String getName() {
        return "run_script";
    }
}
