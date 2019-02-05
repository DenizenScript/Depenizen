package com.denizenscript.depenizen.bukkit.extensions.bungee;

import com.denizenscript.depenizen.bukkit.extensions.dObjectExtension;
import com.denizenscript.depenizen.bukkit.objects.bungee.dServer;
import com.denizenscript.depenizen.bukkit.support.bungee.BungeeSupport;
import com.denizenscript.depenizen.common.socket.Packet;
import com.denizenscript.depenizen.common.socket.client.packet.ClientPacketOutSendPlayer;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.Mechanism;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.utilities.debugging.dB;

public class BungeePlayerExtension extends dObjectExtension {

    public static boolean describes(dObject object) {
        return object instanceof dPlayer;
    }

    public static BungeePlayerExtension getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new BungeePlayerExtension((dPlayer) object);
        }
    }

    public static final String[] handledTags = new String[] {
    }; // None

    public static final String[] handledMechs = new String[] {
            "send_to"
    };

    private BungeePlayerExtension(dPlayer player) {
        this.player = player;
    }

    dPlayer player = null;

    @Override
    public void adjust(Mechanism mechanism) {

        Element value = mechanism.getValue();

        // <--[mechanism]
        // @object dPlayer
        // @name send_to
        // @input dServer
        // @description
        // Sends a player to the specified server.
        // @tags
        // None
        // @Plugin DepenizenBukkit, DepenizenBungee
        // -->
        if (mechanism.matches("send_to")
                && mechanism.requireObject(dServer.class)) {
            if (BungeeSupport.isSocketRegistered()) {
                Packet packet = new ClientPacketOutSendPlayer(player.getName(), value.asType(dServer.class).getName());
                BungeeSupport.getSocketClient().trySend(packet);
            }
            else {
                dB.echoError("Server is not registered to a BungeeCord Socket.");
            }
        }
    }
}
