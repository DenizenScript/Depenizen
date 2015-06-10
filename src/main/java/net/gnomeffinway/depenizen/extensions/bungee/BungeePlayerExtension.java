package net.gnomeffinway.depenizen.extensions.bungee;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.Mechanism;
import net.aufdemrand.denizencore.objects.dObject;
import net.gnomeffinway.depenizen.Depenizen;
import net.gnomeffinway.depenizen.extensions.dObjectExtension;
import net.gnomeffinway.depenizen.objects.bungee.dServer;

public class BungeePlayerExtension extends dObjectExtension {

    public static boolean describes(dObject pl) {
        return pl instanceof dPlayer;
    }

    public static BungeePlayerExtension getFrom(dObject pl) {
        if (!describes(pl)) return null;
        else return new BungeePlayerExtension((dPlayer) pl);
    }

    private BungeePlayerExtension(dPlayer pl) { player = pl; }

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
