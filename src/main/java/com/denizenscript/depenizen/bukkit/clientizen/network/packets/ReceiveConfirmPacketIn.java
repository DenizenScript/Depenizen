package com.denizenscript.depenizen.bukkit.clientizen.network.packets;

import com.denizenscript.depenizen.bukkit.clientizen.ClientizenBridge;
import com.denizenscript.depenizen.bukkit.clientizen.network.ClientizenPacketIn;
import io.netty.buffer.ByteBuf;
import org.bukkit.entity.Player;

public class ReceiveConfirmPacketIn extends ClientizenPacketIn {

    @Override
    public void process(Player sender, ByteBuf data) {
        ClientizenBridge.acceptNewPlayer(sender);
    }

    @Override
    public String getName() {
        return "receive_confirmation";
    }
}
