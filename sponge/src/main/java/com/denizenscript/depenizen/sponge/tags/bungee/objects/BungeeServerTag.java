package com.denizenscript.depenizen.sponge.tags.bungee.objects;

import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.TagData;
import com.denizenscript.denizen2core.tags.objects.TextTag;
import com.denizenscript.denizen2core.utilities.Action;
import com.denizenscript.denizen2core.utilities.CoreUtilities;
import com.denizenscript.denizen2core.utilities.Function2;

import java.util.HashMap;
import java.util.Map;

public class BungeeServerTag extends AbstractTagObject {

    // <--[object]
    // @Type BungeeServerTag
    // @SubType TextTag
    // @Group Depenizen2Sponge
    // @Description Represents a server connected to the BungeeCord socket.
    // -->

    private String internal;

    public BungeeServerTag(String internal) {
        this.internal = internal;
    }

    public String getInternal() {
        return internal;
    }

    public final static HashMap<String, Function2<TagData, AbstractTagObject, AbstractTagObject>> handlers = new HashMap<>();

    static {
        // <--[tag]
        // @Name BungeeServerTag.name
        // @Updated 2016/10/05
        // @Group Identification
        // @ReturnType TextTag
        // @Returns the name of the server.
        // @Example "MyServer1" .name returns "MyServer1".
        // @Plugin Depenizen2Sponge, DepenizenBungee
        // -->
        handlers.put("name", (dat, obj) -> new TextTag(((BungeeServerTag) obj).internal));
    }

    private static final Map<String, BungeeServerTag> onlineServers = new HashMap<>();

    public static void addOnlineServer(String name) {
        onlineServers.put(CoreUtilities.toLowerCase(name), new BungeeServerTag(name));
    }

    public static void removeOnlineServer(String name) {
        name = CoreUtilities.toLowerCase(name);
        if (onlineServers.containsKey(name)) {
            onlineServers.remove(name);
        }
    }

    public static BungeeServerTag getFor(Action<String> error, String text) {
        text = CoreUtilities.toLowerCase(text);
        if (onlineServers.containsKey(text)) {
            return onlineServers.get(text);
        }
        error.run("Invalid BungeeServerTag input!");
        return null;
    }

    public static BungeeServerTag getFor(Action<String> error, AbstractTagObject text) {
        return (text instanceof BungeeServerTag) ? (BungeeServerTag) text : getFor(error, text.toString());
    }

    @Override
    public HashMap<String, Function2<TagData, AbstractTagObject, AbstractTagObject>> getHandlers() {
        return handlers;
    }

    @Override
    public AbstractTagObject handleElseCase(TagData data) {
        return new TextTag(toString()).handle(data);
    }

    @Override
    public String toString() {
        return internal;
    }
}
