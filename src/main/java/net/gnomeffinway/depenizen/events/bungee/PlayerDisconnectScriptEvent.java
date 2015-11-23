package net.gnomeffinway.depenizen.events.bungee;

import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.ScriptEntryData;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import net.gnomeffinway.depenizen.objects.bungee.dServer;

import java.util.HashMap;
import java.util.Map;

public class PlayerDisconnectScriptEvent extends BungeeScriptEvent {

    // <--[event]
    // @Events
    // bungee player leaves network
    //
    // @Triggers when a player leaves the BungeeCord network.
    //
    // @Cancellable false
    //
    // @Context
    // <context.uuid> returns an Element of the player's UUID.
    // <context.name> returns an Element of the player's current name.

    // @Determine
    // None
    //
    // @Plugin Depenizen, BungeeCord
    // -->


    public PlayerDisconnectScriptEvent() {
        instance = this;
    }

    public static PlayerDisconnectScriptEvent instance;
    public Element uuid;
    public Element name;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        return lower.startsWith("bungee player leaves ");
    }

    @Override
    public boolean matches(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        return lower.endsWith("network");
    }

    @Override
    public String getName() {
        return "PlayerDisconnect";
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(null, null);
    }

    @Override
    public HashMap<String, dObject> getContext() {
        HashMap<String, dObject> context = super.getContext();
        context.put("uuid", uuid);
        context.put("name", name);
        return context;
    }

    @Override
    public Map<String, String> fire(Map<String, String> context) {
        uuid = new Element(context.get("uuid"));
        name = new Element(context.get("name"));
        fire();
        return null;
    }
}
