package net.gnomeffinway.depenizen.events.bungee;

import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.ScriptEntryData;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import net.aufdemrand.denizencore.utilities.debugging.dB;
import net.gnomeffinway.depenizen.support.bungee.packets.ClientPacketOutEventResponse;

import java.util.HashMap;
import java.util.Map;

public class ProxyPingScriptEvent extends BungeeScriptEvent {

    // <--[event]
    // @Events
    // proxy server list ping
    //
    // @Triggers when the BungeeCord proxy is pinged from a client's server list.
    //
    // @Cancellable false
    //
    // @Context
    // <context.address> returns an Element of the IP address of the request.
    // <context.num_players>  returns an Element(Number) of the number of players currently on the server.
    // <context.max_players> returns an Element(Number) of the maximum players allowed on the server.
    // <context.motd> returns an Element of the server's MOTD.
    // <context.protocol> returns an Element(Number) of the protocol number being used.
    // <context.version> returns an Element of the version of the proxy.

    // @Determine
    // "NUM_PLAYERS:" + Element(Number) to change the response for the number of online players.
    // "MAX_PLAYERS:" + Element(Number) to change the response for the number of maximum players.
    // "VERSION:" + Element to change the response for the server version.
    // "MOTD:" + Element to change the MOTD.
    //
    // @Plugin Depenizen, BungeeCord
    // -->


    public ProxyPingScriptEvent() {
        instance = this;
    }

    public static ProxyPingScriptEvent instance;
    public Element address;
    public Element numPlayers;
    public Element maxPlayers;
    public Element motd;
    public Element version;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        return lower.startsWith("proxy server list ");
    }

    @Override
    public boolean matches(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        return CoreUtilities.xthArgEquals(3, lower, "ping");
    }

    @Override
    public String getName() {
        return "ProxyPing";
    }

    @Override
    public boolean applyDetermination(ScriptContainer container, String determination) {
        String lower = CoreUtilities.toLowerCase(determination);
        if (lower.startsWith("num_players:")) {
            Element num = new Element(determination.substring(12));
            if (!num.isInt()) {
                dB.echoError("Determination for 'num_players' must be a valid number.");
                return false;
            }
            numPlayers = num;
        }
        else if (lower.startsWith("max_players:")) {
            Element max = new Element(determination.substring(12));
            if (!max.isInt()) {
                dB.echoError("Determination for 'max_players' must be a valid number.");
                return false;
            }
            maxPlayers = max;
        }
        else if (lower.startsWith("version:")) {
            version = new Element(determination.substring(8));
        }
        else if (lower.startsWith("motd:")) {
            motd = new Element(determination.substring(5));
        }
        else {
            motd = new Element(determination);
        }
        return true;
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(null, null);
    }

    @Override
    public HashMap<String, dObject> getContext() {
        HashMap<String, dObject> context = super.getContext();
        context.put("address", address);
        context.put("motd", motd);
        context.put("num_players", numPlayers);
        context.put("max_players", maxPlayers);
        context.put("version", version);
        return context;
    }

    @Override
    public Map<String, String> fire(Map<String, String> context) {
        address = new Element(context.get("address"));
        numPlayers = new Element(context.get("num_players"));
        motd = new Element(context.get("motd"));
        maxPlayers = new Element(context.get("max_players"));
        version = new Element(context.get("version"));
        fire();
        Map<String, String> determinations = new HashMap<String, String>();
        determinations.put("num_players", numPlayers.asString());
        determinations.put("max_players", maxPlayers.asString());
        determinations.put("motd", motd.asString());
        determinations.put("version", version.asString());
        return determinations;
    }
}
