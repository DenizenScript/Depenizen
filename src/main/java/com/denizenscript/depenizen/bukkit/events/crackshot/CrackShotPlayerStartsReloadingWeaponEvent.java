package com.denizenscript.depenizen.bukkit.events.crackshot;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
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
    // @Cancellable false
    //
    // @Context
    // <context.weapon_name> returns the name of the weapon.
    // <context.reload_speed> returns the rate at which the weapon is reloaded.
    // <context.reload_time> returns the time taken to reload in ticks.
    // <context.reload_sounds> returns a ListTag(ElementTag) of the reload sounds.
    //
    // @Determine
    // "RELOAD_SPEED: " + ElementTag(Number) to set the reload speed.
    // The reload speed can be scaled with a decimal ranging from zero to infinity.
    // For example, 0 is instantaneous, 1 is normal and 2 will double the reload time.
    // "RELOAD_DURATION:" + ElementTag(Number) to set the time taken to reload in ticks.
    // "RELOAD_SOUNDS:" + ElementTag to set the reload sounds. Use "NONE" to have no sound. <@link https://github.com/Shampaggon/CrackShot/wiki/The-Complete-Guide-to-CrackShot#sounds>
    //
    // @Plugin Depenizen, CrackShot
    //
    // @Player Always
    //
    // -->

    public static CrackShotPlayerStartsReloadingWeaponEvent instance;
    public WeaponReloadEvent event;
    public PlayerTag player;

    public CrackShotPlayerStartsReloadingWeaponEvent() {
        instance = this;
    }

    @Override
    public boolean applyDetermination(ScriptPath path, ObjectTag determinationObj) {
        if (!isDefaultDetermination(determinationObj)) {
            String determination = determinationObj.toString();
            String lower = CoreUtilities.toLowerCase(determination);
            if (lower.startsWith("reload_speed:")) {
                ElementTag newBulletSpread = new ElementTag(lower.substring("reload_speed:".length()));
                if (!newBulletSpread.isDouble()) {
                    Debug.echoError("Determination for 'reload_speed' must be a valid number.");
                    return false;
                }
                event.setReloadSpeed(newBulletSpread.asDouble());
                return true;
            }
            else if (lower.startsWith("reload_time:")) {
                ElementTag newBulletSpread = new ElementTag(lower.substring("reload_time:".length()));
                if (!newBulletSpread.isInt()) {
                    Debug.echoError("Determination for 'reload_time' must be a valid number.");
                    return false;
                }
                event.setReloadDuration(newBulletSpread.asInt());
                return true;
            }
            else if (lower.startsWith("reload_sounds:")) {
                String newReloadSounds = determination.substring("reload_sounds:".length());
                if (CoreUtilities.toLowerCase(newReloadSounds).equals("none")) {
                    newReloadSounds = "";
                }
                event.setSounds(newReloadSounds);
                return true;
            }
        }
        return super.applyDetermination(path, determinationObj);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(new PlayerTag(event.getPlayer()), null);
    }

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("crackshot player starts reloading weapon");
    }

    @Override
    public String getName() {
        return "CrackShotPlayerStartsReloadingWeaponEvent";
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("weapon_name")) {
            return new ElementTag(event.getWeaponTitle());
        }
        else if (name.equals("reload_speed")) {
            return new ElementTag(event.getReloadSpeed());
        }
        else if (name.equals("reload_time")) {
            return new ElementTag(event.getReloadDuration());
        }
        else if (name.equals("reload_sounds")) {
            return new ListTag(Arrays.asList(event.getSounds().split(",")));
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onCrackShotPlayerStartsReloadingWeaponEvent(WeaponReloadEvent event) {
        this.event = event;
        fire(event);
    }
}