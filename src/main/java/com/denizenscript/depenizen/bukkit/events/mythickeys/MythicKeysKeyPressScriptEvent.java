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
    //
    // @Triggers When a key is pressed by a client running MythicKeys, if that key is in the MythicKeys config.
    //
    // @Context
    // <context.id> Returns the ID of the key that was pressed according to the MythicKeys config, as a namespaced key.
    //
    // @Player Always.
    //
    // @Plugin Depenizen, MythicKeys
    //
    // @Group Depenizen
    //
    // -->

    public MythicKeysKeyPressScriptEvent() {
        registerCouldMatcher("mythickeys key pressed");
        registerSwitches("id");
    }

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
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(event.getPlayer());
    }

    @Override
    public ObjectTag getContext(String name) {
        switch (name) {
            case "id": return new ElementTag(event.getId().toString());
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onKeyPress(MythicKeyPressEvent event) {
        this.event = event;
        fire(event);
    }
}
