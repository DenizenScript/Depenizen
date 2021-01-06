package com.denizenscript.depenizen.bukkit.events.crackshot;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.shampaggon.crackshot.events.WeaponDamageEntityEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CrackShotWeaponDamageEntityEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // crackshot weapon damages entity
    //
    // @Regex ^on crackshot weapon damages entity$
    //
    // @Triggers when an entity is damaged by a CrackShot weapon.
    //
    // @Cancellable true
    //
    // @Context
    // <context.damager> returns the entity that did the damage. This can be a projectile, or TNT.
    // <context.weapon> returns the name of the weapon that did the damage.
    // <context.victim> returns the entity that was damaged.
    // <context.damage> returns the amount of damage dealt.
    // <context.backstab> returns if the attack was a back-stab.
    // <context.critical> returns if the attack was a critical hit.
    // <context.headshot> returns if the attack was a head-shot.
    //
    // @Determine
    // ElementTag(Decimal) to set damage dealt.
    //
    // @Plugin Depenizen, CrackShot
    //
    // @Player Always
    //
    // @Group Depenizen
    //
    // -->

    public static CrackShotWeaponDamageEntityEvent instance;
    public WeaponDamageEntityEvent event;

    public CrackShotWeaponDamageEntityEvent() {
        instance = this;
    }

    @Override
    public boolean applyDetermination(ScriptPath path, ObjectTag determinationObj) {
        String determination = determinationObj.toString();

        if (!isDefaultDetermination(determinationObj)) {
            ElementTag newDamage = new ElementTag(determination);
            if (!newDamage.isDouble()) {
                Debug.echoError("Determination for 'damage' must be a valid number.");
                return false;
            }
            event.setDamage(newDamage.asDouble());
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
        return path.eventLower.startsWith("crackshot weapon damages entity");
    }

    @Override
    public String getName() {
        return "CrackShotWeaponDamagesEntity";
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("headshot")) {
            return new ElementTag(event.isHeadshot());
        }
        else if (name.equals("critical")) {
            return new ElementTag(event.isCritical());
        }
        else if (name.equals("backstab")) {
            return new ElementTag(event.isBackstab());
        }
        else if (name.equals("damage")) {
            return new ElementTag(event.getDamage());
        }
        else if (name.equals("damager")) {
            return getDamager();
        }
        else if (name.equals("victim")) {
            return new EntityTag(event.getVictim());
        }
        else if (name.equals("weapon")) {
            return new ElementTag(event.getWeaponTitle());
        }
        return super.getContext(name);
    }

    public ObjectTag getDamager() {
        return event.getDamager() != null ? new EntityTag(event.getDamager()) : new PlayerTag(event.getPlayer());
    }

    @EventHandler
    public void onEntityDamaged(WeaponDamageEntityEvent event) {
        this.event = event;
        fire(event);
    }
}
