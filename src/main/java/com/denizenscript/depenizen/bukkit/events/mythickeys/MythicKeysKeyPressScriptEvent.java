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
    // @Switch id:<id> to only process the event if the specific key ID was pressed.
    //
    // @Triggers when a key was pressed by a client running MythicKeys, if that key is in the MythicKeys config.
    //
    // @Context
    // <context.id> Returns the ID of the key pressed.
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
        registerSwitches("id");
    }

    public static MythicKeysKeyPressScriptEvent instance;
    public MythicKeyPressEvent event;

    @Override
    public boolean matches(ScriptPath path) {
        if (!runGenericSwitchCheck(path, "id", String.valueOf(event.getId()))) {
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
        if (name.equals("id")) {
            return new ElementTag(event.getId());
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onKeyPress(MythicKeyPressEvent event) {
        this.event = event;
        fire(event);
    }
}
