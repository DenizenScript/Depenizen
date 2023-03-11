package com.denizenscript.depenizen.bukkit.events.crackshot;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.shampaggon.crackshot.events.WeaponReloadCompleteEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CrackShotPlayerFinishesReloadingWeaponEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // crackshot player finishes reloading weapon
    //
    // @Regex ^on crackshot player finishes reloading weapon$
    //
    // @Triggers when a player finishes reloading their CrackShot weapon.
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

    public static CrackShotPlayerFinishesReloadingWeaponEvent instance;
    public WeaponReloadCompleteEvent event;

    public CrackShotPlayerFinishesReloadingWeaponEvent() {
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(event.getPlayer());
    }

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("crackshot player finishes reloading weapon");
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("weapon")) {
            return new ElementTag(event.getWeaponTitle());
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onFinishesReloading(WeaponReloadCompleteEvent event) {
        this.event = event;
        fire(event);
    }
}
