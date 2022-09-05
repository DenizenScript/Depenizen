package com.denizenscript.depenizen.bukkit.events.bungee;

import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;

public class BungeeServerConnectScriptEvent extends BukkitScriptEvent {

    // <--[event]
    // @Events
    // bungee server connects
    //
    // @Regex ^on bungee server connects$
    //
    // @Triggers when another server on the Bungee network becomes connected.
    //
    // @Context
    // <context.server> returns the name of the newly connected server.
    //
    // @Plugin Depenizen, DepenizenBungee, BungeeCord
    //
    // @Group Depenizen
    //
    // -->

    public BungeeServerConnectScriptEvent() {
        instance = this;
    }

    public static BungeeServerConnectScriptEvent instance;

    public String serverName;

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("bungee server connects");
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(null, null);
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("server")) {
            return new ElementTag(serverName);
        }
        return super.getContext(name);
    }
}
