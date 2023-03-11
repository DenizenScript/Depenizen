package com.denizenscript.depenizen.bukkit.events.crackshot;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.shampaggon.crackshot.events.WeaponExplodeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CrackShotWeaponCausesExplosionEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // crackshot weapon causes explosion
    //
    // @Regex ^on crackshot weapon causes explosion$
    //
    // @Triggers when a CrackShot weapon explodes, splits or airstrikes.
    //
    // @Context
    // <context.location> returns the LocationTag for where the explosion occurred.
    // <context.weapon> returns the weapon name that caused the explosion.
    // <context.split> returns whether the explosion was a cluster bomb splitting.
    // <context.airstrike> returns whether the explosion was an airstrike call.
    //
    // @Plugin Depenizen, CrackShot
    //
    // @Player Always
    //
    // @Group Depenizen
    //
    // -->

    public static CrackShotWeaponCausesExplosionEvent instance;
    public WeaponExplodeEvent event;

    public CrackShotWeaponCausesExplosionEvent() {
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(event.getPlayer());
    }

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("crackshot weapon causes explosion");
    }

    @Override
    public ObjectTag getContext(String name) {
        switch (name) {
            case "location":
                return new LocationTag(event.getLocation());
            case "weapon":
                return new ElementTag(event.getWeaponTitle());
            case "split":
                return new ElementTag(event.isSplit());
            case "airstrike":
                return new ElementTag(event.isAirstrike());
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onExplosion(WeaponExplodeEvent event) {
        this.event = event;
        fire(event);
    }
}
