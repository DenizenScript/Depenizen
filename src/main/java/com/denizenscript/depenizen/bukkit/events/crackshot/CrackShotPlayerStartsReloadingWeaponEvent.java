package com.denizenscript.depenizen.bukkit.events.crackshot;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.DurationTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.shampaggon.crackshot.events.WeaponReloadEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Arrays;

public class CrackShotPlayerStartsReloadingWeaponEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // crackshot player starts reloading weapon
    //
    // @Regex ^on crackshot player starts reloading weapon$
    //
    // @Triggers when a player starts reloading their CrackShot weapon.
    //
    // @Context
    // <context.weapon> returns the name of the weapon.
    // <context.reload_speed> returns the rate at which the weapon is reloaded.
    // <context.reload_time> returns the time taken to reload in ticks.
    // <context.reload_sounds> returns a ListTag of the reload sounds.
    //
    // @Determine
    // "RELOAD_SPEED: <ElementTag(Number)>" to set the reload speed.
    // The reload speed can be scaled with a decimal ranging from zero to infinity.
    // For example, 0 is instantaneous, 1 is normal and 2 will double the reload time.
    // "RELOAD_TIME:<DurationTag>" to set the time taken to reload in ticks.
    // "RELOAD_SOUNDS:<ElementTag>" to set the reload sounds. Use "NONE" to have no sound. <@link url https://github.com/Shampaggon/CrackShot/wiki/The-Complete-Guide-to-CrackShot#sounds>
    //
    // @Plugin Depenizen, CrackShot
    //
    // @Player Always
    //
    // @Group Depenizen
    //
    // -->

    public static CrackShotPlayerStartsReloadingWeaponEvent instance;
    public WeaponReloadEvent event;

    public CrackShotPlayerStartsReloadingWeaponEvent() {
    }

    @Override
    public boolean applyDetermination(ScriptPath path, ObjectTag determinationObj) {
        String determination = determinationObj.toString();
        String lower = CoreUtilities.toLowerCase(determination);
        if (lower.startsWith("reload_speed:")) {
            ElementTag newReloadSpeed = new ElementTag(lower.substring("reload_speed:".length()));
            if (!newReloadSpeed.isDouble()) {
                Debug.echoError("Determination for 'reload_speed' must be a valid number.");
                return false;
            }
            event.setReloadSpeed(newReloadSpeed.asDouble());
            return true;
        }
        else if (lower.startsWith("reload_time:")) {
            String time = lower.substring("reload_time:".length());
            if (!DurationTag.matches(time)) {
                Debug.echoError("Determination for 'reload_time' must be a valid DurationTag.");
                return false;
            }
            DurationTag newReloadtime = DurationTag.valueOf(lower.substring("reload_time:".length()), getTagContext(path));
            event.setReloadDuration(newReloadtime.getTicksAsInt());
            return true;
        }
        else if (lower.startsWith("reload_sounds:")) {
            String newReloadSounds = determination.substring("reload_sounds:".length());
            if (CoreUtilities.equalsIgnoreCase(newReloadSounds, "none")) {
                newReloadSounds = "";
            }
            event.setSounds(newReloadSounds);
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
        return path.eventLower.startsWith("crackshot player starts reloading weapon");
    }

    @Override
    public ObjectTag getContext(String name) {
        switch (name) {
            case "weapon":
                return new ElementTag(event.getWeaponTitle());
            case "reload_speed":
                return new ElementTag(event.getReloadSpeed());
            case "reload_time":
                return new ElementTag(event.getReloadDuration());
            case "reload_sounds":
                return new ListTag(Arrays.asList(event.getSounds().split(",")));
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onStartsReloading(WeaponReloadEvent event) {
        this.event = event;
        fire(event);
    }
}
