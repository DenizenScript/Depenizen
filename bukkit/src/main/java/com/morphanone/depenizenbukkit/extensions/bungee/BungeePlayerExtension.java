package com.morphanone.depenizenbukkit.extensions.bungee;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.morphanone.depenizenbukkit.Depenizen;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.Mechanism;
import net.aufdemrand.denizencore.objects.dObject;
import com.morphanone.depenizenbukkit.extensions.dObjectExtension;
import com.morphanone.depenizenbukkit.objects.bungee.dServer;

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
        // @plugin Depenizen, BungeeCord
        // -->
        if (mechanism.matches("send_to")
                && mechanism.requireObject(dServer.class)) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Connect");
            out.writeUTF(value.asType(dServer.class).getName());
            player.getPlayerEntity().sendPluginMessage(Depenizen.getCurrentInstance(), "BungeeCord", out.toByteArray());
        }
    }
}
