package net.gnomeffinway.depenizen.objects.bungee;

import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.Fetchable;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;
import net.aufdemrand.denizencore.tags.TagContext;
import net.aufdemrand.denizencore.utilities.CoreUtilities;

import java.util.HashMap;
import java.util.Map;

public class dServer implements dObject {

    private static final Map<String, dServer> onlineServers = new HashMap<String, dServer>();

    public static void addOnlineServer(String name) {
        onlineServers.put(CoreUtilities.toLowerCase(name), new dServer(name));
    }

    public static void removeOnlineServer(String name) {
        name = CoreUtilities.toLowerCase(name);
        if (onlineServers.containsKey(name))
            onlineServers.remove(name);
    }

    public static dServer getServerFromName(String name) {
        name = CoreUtilities.toLowerCase(name);
        if (onlineServers.containsKey(name)) {
            return onlineServers.get(name);
        }
        return null;
    }

    public static Map<String, dServer> getOnlineServers() {
        return onlineServers;
    }

    /////////////////////
    //   OBJECT FETCHER
    /////////////////

    public static dServer valueOf(String string) {
        return valueOf(string, null);
    }

    @Fetchable("server")
    public static dServer valueOf(String string, TagContext context) {
        if (string == null) return null;

        dServer server = getServerFromName(CoreUtilities.toLowerCase(string).replace("server@", ""));

        if (server == null) {
            dB.echoError("Specified server '" + string + "' does not exist or is not online.");
        }

        string = CoreUtilities.toLowerCase(string).replace("server@", "");
        if (onlineServers.containsKey(string)) {
            return onlineServers.get(string);
        }
        if (context == null || context.debug) {
            dB.echoError("Specified server '" + string + "' does not exist or is not online.");
        }

        return server;
    }

    public static boolean matches(String arg) {
        if (onlineServers.containsKey(CoreUtilities.toLowerCase(arg).replace("server@", "")))
            return true;

        return false;
    }

    /////////////////////
    //   STATIC CONSTRUCTORS
    /////////////////

    String name;

    private dServer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /////////////////////
    //   dObject Methods
    /////////////////

    private String prefix = "Server";

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public dServer setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public String debug() {
        return (prefix + "='<A>" + identify() + "<G>' ");
    }

    @Override
    public boolean isUnique() {
        return true;
    }

    @Override
    public String getObjectType() {
        return "Server";
    }

    @Override
    public String identify() {
        return "server@" + name;
    }

    @Override
    public String identifySimple() {
        return identify();
    }

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <server@server.name>
        // @returns Element
        // @plugin Depenizen, BungeeCord
        // @description
        // The name of the server as set by its Depenizen's config.yml file.
        // -->
        if (attribute.startsWith("name")) {
            return new Element(name).getAttribute(attribute.fulfill(1));
        }

        return new Element(identify()).getAttribute(attribute);
    }
}
