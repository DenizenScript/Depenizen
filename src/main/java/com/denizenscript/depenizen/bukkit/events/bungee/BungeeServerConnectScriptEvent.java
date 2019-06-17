package com.denizenscript.depenizen.bukkit.events.bungee;

import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.ScriptEntryData;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;

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
    // -->

    public BungeeServerConnectScriptEvent() {
        instance = this;
    }

    public static BungeeServerConnectScriptEvent instance;

    public String serverName;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        return s.startsWith("bungee server connects");
    }

    @Override
    public boolean matches(ScriptPath path) {
        return true;
    }

    @Override
    public String getName() {
        return "BungeeServerConnects";
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
