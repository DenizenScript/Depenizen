package com.denizenscript.depenizen.bukkit.bungee;

import com.denizenscript.depenizen.bukkit.networking.PacketIn;

public abstract class BungeePacketIn extends PacketIn {

    @Override
    public void fail(String reason) {
        BungeeBridge.instance.handler.fail(reason);
    }
}
