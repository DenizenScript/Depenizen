package com.denizenscript.depenizen.bukkit.events.crackshot;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.shampaggon.crackshot.events.WeaponFireRateEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CrackShotPlayerFiresAutomaticWeaponEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // crackshot player fires automatic weapon
    //
    // @Regex ^crackshot player fires automatic weapon$
    //
    // @Triggers while a player is firing a fully automatic CrackShot weapon.
    //
    // @Cancellable false
    //
    // @Context
    // <context.weapon> returns the name of the weapon.
    // <context.fire_rate> returns the weapon fire rate.
    //
    // @Determine
    // "FIRE_RATE:" + ElementTag(Number) to set the fire rate.
    //
    // @Plugin Depenizen, CrackShot
    //
    // @Player Always
    //
    // -->

    public static CrackShotPlayerFiresAutomaticWeaponEvent instance;
    public WeaponFireRateEvent event;

    public CrackShotPlayerFiresAutomaticWeaponEvent() {
        instance = this;
    }

    @Override
    public boolean applyDetermination(ScriptPath path, ObjectTag determinationObj) {
        String determination = determinationObj.toString();
        String lower = CoreUtilities.toLowerCase(determination);
        if (lower.startsWith("fire_rate:")) {
            ElementTag newFireRate = new ElementTag(lower.substring("fire_rate:".length()));
            if (!newFireRate.isInt()) {
                Debug.echoError("Determination for 'fire_rate' must be a valid integer.");
                return false;
            }
            event.setFireRate(newFireRate.asInt());
            return true;
        }
        return super.applyDetermination(path, determinationObj);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(new PlayerTag(event.getPlayer()), null);
    }

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("crackshot player fires automatic weapon");
    }

    @Override
    public String getName() {
        return "CrackShotPlayerFiresAutomaticWeaponEvent";
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("weapon")) {
            return new ElementTag(event.getWeaponTitle());
        }
        else if (name.equals("fire_rate")) {
            return new ElementTag(event.getFireRate());
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onFiresAutomaticWeapon(WeaponFireRateEvent event) {
        this.event = event;
        fire(event);
    }
}
