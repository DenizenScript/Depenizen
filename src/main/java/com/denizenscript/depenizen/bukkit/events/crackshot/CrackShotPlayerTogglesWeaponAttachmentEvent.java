package com.denizenscript.depenizen.bukkit.events.crackshot;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.DurationTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.shampaggon.crackshot.events.WeaponAttachmentToggleEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CrackShotPlayerTogglesWeaponAttachmentEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // crackshot player toggles weapon attachment
    //
    // @Regex ^on crackshot player toggles weapon attachment$
    //
    // @Triggers when a player toggles an attachment on a CrackShot weapon.
    //
    // @Cancellable true
    //
    // @Context
    // <context.weapon> returns the name of the weapon.
    // <context.delay> returns the delay between toggles.
    //
    // @Determine
    // "TOGGLE_DELAY:<ElementTag(Number)>" to set the time between toggles.
    //
    // @Plugin Depenizen, CrackShot
    //
    // @Player Always
    //
    // @Group Depenizen
    //
    // -->

    public static CrackShotPlayerTogglesWeaponAttachmentEvent instance;
    public WeaponAttachmentToggleEvent event;

    public CrackShotPlayerTogglesWeaponAttachmentEvent() {
    }

    @Override
    public boolean applyDetermination(ScriptPath path, ObjectTag determinationObj) {
        String determination = determinationObj.toString();
        String lower = CoreUtilities.toLowerCase(determination);
        if (lower.startsWith("toggle_delay:")) {
            ElementTag newBulletSpread = new ElementTag(lower.substring("toggle_delay:".length()));
            if (!newBulletSpread.isInt()) {
                Debug.echoError("Determination for 'toggle_delay' must be a valid integer.");
                return false;
            }
            event.setToggleDelay(newBulletSpread.asInt());
            return true;
        }
        return super.applyDetermination(path, determinationObj);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(event.getPlayer());
    }

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("crackshot player toggles weapon attachment");
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("delay")) {
            return new DurationTag(event.getToggleDelay());
        }
        else if (name.equals("weapon")) {
            return new ElementTag(event.getWeaponTitle());
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onTogglesAttachment(WeaponAttachmentToggleEvent event) {
        this.event = event;
        fire(event);
    }
}
