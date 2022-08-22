package com.denizenscript.depenizen.bukkit.events.bungee;

import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class BungeePlayerServerSwitchScriptEvent extends BukkitScriptEvent {

    // <--[event]
    // @Events
    // bungee player switches to server
    //
    // @Regex ^on bungee player switches to \w+$
    //
    // @Triggers when a player switches to a new server on the Bungee network.
    //
    // @Context
    // <context.name> returns the switching player's name.
    // <context.uuid> returns the switching player's UUID.
    // <context.server> returns the name of the server being switched to.
    //
    // @Plugin Depenizen, DepenizenBungee, BungeeCord
    //
    // @Player when the player has been on this specific server before.
    //
    // @Group Depenizen
    //
    // -->

    public BungeePlayerServerSwitchScriptEvent() {
        instance = this;
    }

    public static BungeePlayerServerSwitchScriptEvent instance;

    public String name;

    public UUID uuid;

    public String newServer;

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("bungee player switches to");
    }

    @Override
    public boolean matches(ScriptPath path) {
        String serverName = path.eventArgLowerAt(4);
        if (!serverName.equals("server") && !serverName.equalsIgnoreCase(newServer)) {
            return false;
        }
        return super.matches(path);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        OfflinePlayer player = null;
        try {
            player = Bukkit.getOfflinePlayer(uuid);
            if (!player.isOnline() && !player.hasPlayedBefore()) {
                player = null;
            }
        }
        catch (IllegalArgumentException ex) {
            // Ignore.
        }
        return new BukkitScriptEntryData(player == null ? null : new PlayerTag(player), null);
    }

    @Override
    public ObjectTag getContext(String name) {
        switch (name) {
            case "name":
                return new ElementTag(this.name);
            case "uuid":
                return new ElementTag(uuid.toString());
            case "server":
                return new ElementTag(newServer);
        }
        return super.getContext(name);
    }
}
