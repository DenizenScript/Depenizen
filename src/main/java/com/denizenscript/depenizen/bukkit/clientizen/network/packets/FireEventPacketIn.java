package com.denizenscript.depenizen.bukkit.clientizen.network.packets;

import com.denizenscript.depenizen.bukkit.clientizen.ClientizenEventScriptEvent;
import com.denizenscript.depenizen.bukkit.clientizen.network.ClientizenPacketIn;
import io.netty.buffer.ByteBuf;
import org.bukkit.entity.Player;

import java.util.Map;

public class FireEventPacketIn extends ClientizenPacketIn {

    @Override
    public void process(Player sender, ByteBuf data) {
        if (ClientizenEventScriptEvent.instance.enabled) {
            String id = readString(data, "Event ID");
            Map<String, String> contexts = readStringMap(data);
            if (id == null || contexts == null) {
                return;
            }
            ClientizenEventScriptEvent.instance.fire(sender, id, contexts);
        }
    }

    @Override
    public String getName() {
        return "fire_event";
    }
}
