package com.denizenscript.depenizen.bukkit.extensions.bungee;

import com.denizenscript.depenizen.bukkit.DepenizenPlugin;
import com.denizenscript.depenizen.bukkit.extensions.dObjectExtension;
import com.denizenscript.depenizen.bukkit.objects.bungee.dServer;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.Mechanism;
import net.aufdemrand.denizencore.objects.dObject;

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
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Connect");
            out.writeUTF(value.asType(dServer.class).getName());
            player.getPlayerEntity().sendPluginMessage(DepenizenPlugin.getCurrentInstance(), "BungeeCord", out.toByteArray());
        }
    }
}
