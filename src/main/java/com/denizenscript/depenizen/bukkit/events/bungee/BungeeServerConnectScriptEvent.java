package com.denizenscript.depenizen.bukkit.events.bungee;

import com.denizenscript.denizen.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizencore.objects.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.denizencore.scripts.containers.ScriptContainer;

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
    public ObjectTag getContext(String name) {
        if (name.equals("server")) {
            return new ElementTag(serverName);
        }
        return super.getContext(name);
    }
}
