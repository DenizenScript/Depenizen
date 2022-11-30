package com.denizenscript.depenizen.bukkit.events.arikeys;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import eu.asangarin.arikeys.api.AriKeyPressEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AriKeysKeyPressScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // arikeys key pressed
    //
    // @Location true
    //
    // @Switch id:<id> to only process the event if the key ID matches the given text matcher.
    //
    // @Triggers When a key is pressed by a client running AriKeys, if that key is in the AriKeys config.
    //
    // @Context
    // <context.id> Returns the ID of the key that was pressed according to the AriKeys config, as a namespaced key.
    //
    // @Player Always.
    //
    // @Plugin Depenizen, AriKeys
    //
    // @Group Depenizen
    //
    // -->

    public AriKeysKeyPressScriptEvent() {
        registerCouldMatcher("arikeys key pressed");
        registerSwitches("id");
    }

    public AriKeyPressEvent event;

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
    public void onKeyPress(AriKeyPressEvent event) {
        this.event = event;
        fire(event);
    }
}
