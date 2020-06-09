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
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class crackshotWeaponDamageEntityEvent extends BukkitScriptEvent implements Listener {

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
    // <context.damager> returns the entity that did the damage.
    // This can be a projectile, or TNT.
    // <context.weapon_name> returns the name of the weapon that did the damage.
    // <context.victim> returns the entity that was damaged.
    // <context.damage> returns the amount of damage dealt.
    // <context.backstab> returns if the attack was a back-stab.
    // <context.critical> returns if the attack was a critical hit.
    // <context.headshot> returns if the attack was a head-shot.
    //
    // @Determine
    // ElementTag(Number) to set damage dealt.
    //
    // @Plugin Depenizen, CrackShot
    //
    // @Player Always
    //
    // -->

    public static crackshotWeaponDamageEntityEvent instance;
    public        WeaponDamageEntityEvent          event;
    public        PlayerTag                        player;
    public        Entity                           damager;
    public        Entity                           victim;
    public        double                           damage;
    public        boolean                          critical;
    public        boolean                          headshot;
    public        boolean                          backstab;

    public crackshotWeaponDamageEntityEvent() {
        instance = this;
    }

    @Override
    public boolean applyDetermination(ScriptPath path, ObjectTag determinationObj) {
        String determination = determinationObj.toString();

        if (!isDefaultDetermination(determinationObj)) {
            ElementTag newDamage = new ElementTag(determination);
            if (!newDamage.isDouble()) {
                Debug.echoError("Determination for 'damage' must be a valid integer.");
                return false;
            }
            event.setDamage(newDamage.asDouble());
            return true;
        }

        return super.applyDetermination(path, determinationObj);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(player, null);
    }

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("crackshot weapon damages entity");
    }

    @Override
    public String getName() {
        return "CrackShotWeaponDamageEntity";
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("headshot")) {
            return new ElementTag(headshot);
        }
        else if (name.equals("critical")) {
            return new ElementTag(critical);
        }
        else if (name.equals("backstab")) {
            return new ElementTag(backstab);
        }
        else if (name.equals("damage")) {
            return new ElementTag(damage);
        }
        else if (name.equals("damager")) {
            return getDamager();
        }
        else if (name.equals("victim")) {
            return new EntityTag(victim);
        }
        return super.getContext(name);
    }

    public ObjectTag getDamager() {
        return damager != null ? new EntityTag(damager) : player;
    }

    @EventHandler
    public void onCrackShotPlayerTogglesWeaponAttachment(WeaponDamageEntityEvent event) {
        player = PlayerTag.mirrorBukkitPlayer(event.getPlayer());
        headshot = event.isHeadshot();
        critical = event.isCritical();
        backstab = event.isBackstab();
        damage = event.getDamage();
        damager = event.getDamager();
        victim = event.getVictim();
        this.event = event;
        fire(event);
    }
}
