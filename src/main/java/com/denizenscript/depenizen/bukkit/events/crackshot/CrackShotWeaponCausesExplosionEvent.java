package com.denizenscript.depenizen.bukkit.events.crackshot;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.objects.PlayerTag;
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
    // @Cancellable false
    //
    // @Context
    // <context.location> returns the LocationTag for where the explosion occurred.
    // <context.weapon> returns the weapon name that caused the explosion.
    // <context.split> returns whether the explosion was a cluster bomb splitting.
    // <context.airstrike> returns whether the explosion was an airstrike call.
    // <player> returns the player that caused the explosion
    //
    // @Plugin Depenizen, CrackShot
    //
    // @Player Always
    //
    // -->

    public static CrackShotWeaponCausesExplosionEvent instance;
    public WeaponExplodeEvent event;
    public PlayerTag player;

    public CrackShotWeaponCausesExplosionEvent() {
        instance = this;
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(new PlayerTag(event.getPlayer()), null);
    }

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("crackshot weapon causes explosion");
    }

    @Override
    public String getName() {
        return "CrackShotWeaponCausesExplosionEvent";
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("location")) {
            return new LocationTag(event.getLocation());
        }
        else if (name.equals("weapon")) {
            return new ElementTag(event.getWeaponTitle());
        }
        else if (name.equals("split")) {
            return new ElementTag(event.isSplit());
        }
        else if (name.equals("airstrike")) {
            return new ElementTag(event.isAirstrike());
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onCrackShotWeaponCausesExplosionEvent(WeaponExplodeEvent event) {
        this.event = event;
        fire(event);
    }
}
