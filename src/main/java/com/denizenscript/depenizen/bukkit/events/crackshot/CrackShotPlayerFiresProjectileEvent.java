package com.denizenscript.depenizen.bukkit.events.crackshot;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.shampaggon.crackshot.events.WeaponPreShootEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Arrays;

public class CrackShotPlayerFiresProjectileEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // crackshot player fires projectile
    //
    // @Regex ^on crackshot player fires projectile$
    //
    // @Triggers just before a projectile is launched from a CrackShot weapon.
    //
    // @Cancellable true
    //
    // @Context
    // <context.weapon> returns the name of the weapon about to fire.
    // <context.bullet_spread> returns the spread of the projectiles being fired.
    // <context.left_click> returns whether the fire was a left click.
    // <context.shot_sounds> returns a list of the shot sounds.
    //
    // @Determine
    // "BULLET_SPREAD:<ElementTag(Number)>" to set the bullet spread.
    // "SHOT_SOUNDS:<ElementTag>" to set the shot sounds. Use "NONE" to have no sound. <@link url https://github.com/Shampaggon/CrackShot/wiki/The-Complete-Guide-to-CrackShot#sounds>
    //
    // @Plugin Depenizen, CrackShot
    //
    // @Player Always
    //
    // @Group Depenizen
    //
    // -->

    public static CrackShotPlayerFiresProjectileEvent instance;
    public WeaponPreShootEvent event;

    public CrackShotPlayerFiresProjectileEvent() {
    }

    @Override
    public boolean applyDetermination(ScriptPath path, ObjectTag determinationObj) {
        String determination = determinationObj.toString();
        String lower = CoreUtilities.toLowerCase(determination);
        if (lower.startsWith("bullet_spread:")) {
            ElementTag newBulletSpread = new ElementTag(lower.substring("bullet_spread:".length()));
            if (!newBulletSpread.isDouble()) {
                Debug.echoError("Determination for 'bullet_spread' must be a valid number.");
                return false;
            }
            event.setBulletSpread(newBulletSpread.asDouble());
            return true;
        }
        else if (lower.startsWith("shot_sounds:")) {
            String newReloadSounds = determination.substring("shot_sounds:".length());
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
        return path.eventLower.startsWith("crackshot player fires projectile");
    }

    @Override
    public ObjectTag getContext(String name) {
        switch (name) {
            case "weapon":
                return new ElementTag(event.getWeaponTitle());
            case "bullet_spread":
                return new ElementTag(event.getBulletSpread());
            case "shot_sounds":
                return new ListTag(Arrays.asList(event.getSounds().split(",")));
            case "left_click":
                return new ElementTag(event.isLeftClick());
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onFiresProjectile(WeaponPreShootEvent event) {
        this.event = event;
        fire(event);
    }
}
