package com.denizenscript.depenizen.bukkit.events.bungee;

import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.ScriptEntryData;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;

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
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        return s.startsWith("bungee server disconnects");
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
    public dObject getContext(String name) {
        if (name.equals("server")) {
            return new Element(serverName);
        }
        return super.getContext(name);
    }
}
