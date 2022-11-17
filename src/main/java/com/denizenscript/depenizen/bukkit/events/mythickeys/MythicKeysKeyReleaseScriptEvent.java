package com.denizenscript.depenizen.bukkit.events.mythickeys;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.nms.NMSHandler;
import com.denizenscript.denizen.nms.NMSVersion;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import eu.asangarin.mythickeys.api.MythicKeyReleaseEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MythicKeysKeyReleaseScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // mythickeys key released
    //
    // @Location true
    //
    // @Switch id:<id> to only process the event if the key ID matches the given text matcher.
    //
    // @Triggers When a key stops being pressed by a client running MythicKeys, if that key is in the MythicKeys config.
    //
    // @Context
    // <context.id> Returns the ID of the key that was released according to the MythicKeys config, as a namespaced key.
    //
    // @Player Always.
    //
    // @Plugin Depenizen, MythicKeys
    //
    // @Group Depenizen
    //
    // @Warning For 1.19+ servers use AriKeysPlugin.
    //
    // -->

    public MythicKeysKeyReleaseScriptEvent() {
        registerCouldMatcher("mythickeys key released");
        registerSwitches("id");
    }

    public MythicKeyReleaseEvent event;

    @Override
    public boolean couldMatch(ScriptPath path) {
        if (NMSHandler.getVersion().isAtLeast(NMSVersion.v1_19)) {
            MythicKeysKeyPressScriptEvent.outdatedMythicKeys.warn();
        }
        return super.couldMatch(path);
    }

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
    public void onKeyRelease(MythicKeyReleaseEvent event) {
        this.event = event;
        fire(event);
    }
}
