package com.denizenscript.depenizen.bukkit.events.crackshot;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.shampaggon.crackshot.events.WeaponPrepareShootEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CrackShotPlayerStartsFiringWeaponEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // crackshot player starts firing weapon
    //
    // @Regex ^on crackshot player starts firing weapon$
    //
    // @Triggers when a player tries to start firing a CrackShot weapon.
    //
    // @Cancellable true
    //
    // @Context
    // <context.weapon> returns the name of the weapon.
    //
    // @Plugin Depenizen, CrackShot
    //
    // @Player Always
    //
    // @Group Depenizen
    //
    // -->

    public static CrackShotPlayerStartsFiringWeaponEvent instance;
    public WeaponPrepareShootEvent event;

    public CrackShotPlayerStartsFiringWeaponEvent() {
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(event.getPlayer());
    }

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("crackshot player starts firing weapon");
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("weapon")) {
            return new ElementTag(event.getWeaponTitle());
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onStartsFiringWeapon(WeaponPrepareShootEvent event) {
        this.event = event;
        fire(event);
    }
}
