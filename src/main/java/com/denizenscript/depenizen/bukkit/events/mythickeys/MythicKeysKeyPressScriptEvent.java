package com.denizenscript.depenizen.bukkit.events.mythickeys;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import eu.asangarin.mythickeys.api.MythicKeyPressEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MythicKeysKeyPressScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // mythickeys key pressed
    //
    // @Location true
    //
    // @Switch id:<id> to only process the event if the key ID matches the given text matcher.
    // @Switch held:<true/false> to only process the event for the given 'held' state. If 'false', only fires once per press. If 'true', fires continually after being pressed except the first tick. If left off, fires continually until released.
    //
    // @Triggers every tick in which a key is being held by a client running MythicKeys, if that key is in the MythicKeys config.
    //
    // @Context
    // <context.id> Returns the ID of the key tha twas pressed according to the MythicKeys config, as a namespaced key.
    // <context.held> returns true if the key is being held, false if this is the first tick the button has been pressed for.
    //
    // @Player Always.
    //
    // @Plugin Depenizen, MythicKeys
    //
    // @Group Depenizen
    //
    // -->

    public MythicKeysKeyPressScriptEvent() {
        instance = this;
        registerCouldMatcher("mythickeys key pressed");
        registerSwitches("id", "held");
    }

    public static MythicKeysKeyPressScriptEvent instance;
    public MythicKeyPressEvent event;

    @Override
    public boolean matches(ScriptPath path) {
        if (!runGenericSwitchCheck(path, "id", String.valueOf(event.getId()))) {
            return false;
        }
        if (!runGenericSwitchCheck(path, "held", String.valueOf(event.isHeld()))) {
            return false;
        }
        if (!runInCheck(path, event.getPlayer().getLocation())) {
            return false;
        }
        return super.matches(path);
    }

    @Override
    public String getName() {
        return "MythicKeysKeyPressed";
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(event.getPlayer());
    }

    @Override
    public ObjectTag getContext(String name) {
        switch (name) {
            case "id": return new ElementTag(event.getId().toString());
            case "held": return new ElementTag(event.isHeld());
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onKeyPress(MythicKeyPressEvent event) {
        this.event = event;
        fire(event);
    }
}
