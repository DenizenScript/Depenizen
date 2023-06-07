package com.denizenscript.depenizen.bukkit.bungee;

import com.denizenscript.depenizen.bukkit.networking.PacketOut;

public abstract class BungeePacketOut extends PacketOut {

    public boolean canBeFirstPacket = false;

    public abstract int getPacketId();

}
