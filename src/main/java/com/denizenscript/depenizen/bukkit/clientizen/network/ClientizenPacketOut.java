package com.denizenscript.depenizen.bukkit.clientizen.network;

import com.denizenscript.depenizen.bukkit.clientizen.network.NetworkManager;
import com.denizenscript.depenizen.bukkit.networking.PacketOut;

public abstract class ClientizenPacketOut extends PacketOut {

    public final String channel = NetworkManager.channel(getName());

    public abstract String getName();
}
