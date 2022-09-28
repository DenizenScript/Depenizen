package com.denizenscript.depenizen.bukkit.clientizen.events;

import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.depenizen.bukkit.clientizen.DataDeserializer;
import com.denizenscript.depenizen.bukkit.clientizen.DataSerializer;

import java.util.*;

public class PlayerOpenCloseScreenClientizenEvent extends ClientizenScriptEvent {

    public static final String[] SCREEN_TYPES = {
            "inventory", "creative", "pause", "options", "advancements"
    };

    public static Set<String> listenToOpenScreens = new HashSet<>();
    public static Set<String> listenToCloseScreens = new HashSet<>();

    public String inventoryType;
    public boolean opened;
    public boolean switched;

    public PlayerOpenCloseScreenClientizenEvent() {
        registerCouldMatcher("clientizen player opens|closes (inventory|creative|pause|options|advancements) screen");
        id = "PlayerOpenCloseScreen";
    }

    @Override
    public boolean matches(ScriptPath path) {
        String screenMatcher = path.eventArgLowerAt(3);
        if (!screenMatcher.equals("screen") && !runGenericCheck(screenMatcher, inventoryType)) {
            return false;
        }
        if (opened && !path.eventArgLowerAt(2).equals("opens")) {
            return false;
        }
        return super.matches(path);
    }

    @Override
    public ObjectTag getContext(String name) {
        switch (name) {
            case "screen_type": return new ElementTag(inventoryType);
            case "switched": return new ElementTag(switched);
        }
        return super.getContext(name);
    }

    @Override
    public void init() {
        for (ScriptPath path : eventPaths) {
            String screenMatcher = path.eventArgLowerAt(3);
            if (path.eventArgLowerAt(2).equals("opens")) {
                listenToOpenScreens.addAll(getScreenTypes(screenMatcher));
            }
            else {
                listenToCloseScreens.addAll(getScreenTypes(screenMatcher));
            }
        }
        super.init();
    }

    public List<String> getScreenTypes(String type) {
        return type.equals("screen")
                ? Arrays.asList(SCREEN_TYPES)
                : Collections.singletonList(type);
    }

    @Override
    public void destroy() {
        listenToOpenScreens.clear();
        listenToCloseScreens.clear();
        super.destroy();
    }

    @Override
    public void fire(DataDeserializer data) {
        inventoryType = data.readString();
        opened = data.readBoolean();
        switched = data.readBoolean();
        fire();
    }

    @Override
    public void write(DataSerializer serializer) {
        serializer.writeStringList(listenToOpenScreens);
        serializer.writeStringList(listenToCloseScreens);
    }
}
