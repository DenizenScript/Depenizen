package com.denizenscript.depenizen.bukkit.events.bungee;

import com.denizenscript.denizen.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.dPlayer;
import com.denizenscript.denizencore.objects.Element;
import com.denizenscript.denizencore.objects.dObject;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.denizencore.scripts.containers.ScriptContainer;
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
    // -->

    public BungeePlayerServerSwitchScriptEvent() {
        instance = this;
    }

    public static BungeePlayerServerSwitchScriptEvent instance;

    public String name;

    public UUID uuid;

    public String newServer;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        return s.startsWith("bungee player switches to");
    }

    @Override
    public boolean matches(ScriptPath path) {
        String serverName = path.eventArgLowerAt(4);
        if (!serverName.equals("server") && !serverName.equalsIgnoreCase(newServer)) {
            return false;
        }
        return true;
    }

    @Override
    public String getName() {
        return "BungeePlayerServerSwitch";
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        OfflinePlayer player = null;
        try {
            player = Bukkit.getOfflinePlayer(uuid);
        }
        catch (IllegalArgumentException ex) {
            // Ignore.
        }
        return new BukkitScriptEntryData(player == null ? null : new dPlayer(player), null);
    }

    @Override
    public dObject getContext(String name) {
        if (name.equals("name")) {
            return new Element(name);
        }
        else if (name.equals("uuid")) {
            return new Element(uuid.toString());
        }
        else if (name.equals("server")) {
            return new Element(newServer);
        }
        return super.getContext(name);
    }
}
