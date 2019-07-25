package com.denizenscript.depenizen.bukkit.events.bungee;

import com.denizenscript.denizen.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;

public class BungeeServerDisconnectScriptEvent extends BukkitScriptEvent {

    // <--[event]
    // @Events
    // bungee server disconnects
    //
    // @Regex ^on bungee server disconnects$
    //
    // @Triggers when another server on the Bungee network becomes disconnected.
    //
    // @Context
    // <context.server> returns the name of the previously connected server.
    //
    // -->

    public BungeeServerDisconnectScriptEvent() {
        instance = this;
    }

    public static BungeeServerDisconnectScriptEvent instance;

    public String serverName;

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("bungee server disconnects");
    }

    @Override
    public boolean matches(ScriptPath path) {
        return true;
    }

    @Override
    public String getName() {
        return "BungeeServerDisconnects";
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
