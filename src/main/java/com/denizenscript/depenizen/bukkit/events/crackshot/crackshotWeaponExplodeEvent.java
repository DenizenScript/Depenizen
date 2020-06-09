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

public class crackshotWeaponExplodeEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // crackshot weapon explodes
    //
    // @Regex ^on crackshot weapon explodes$
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

    public static crackshotWeaponExplodeEvent instance;
    public        WeaponExplodeEvent          event;
    public        PlayerTag                   player;
    public        ElementTag                  weapon;
    public        ElementTag                  split;
    public        LocationTag                 location;
    public        ElementTag                  airstrike;

    public crackshotWeaponExplodeEvent() {
        instance = this;
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(player, null);
    }

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("crackshot weapon explodes");
    }

    @Override
    public String getName() {
        return "CrackShotWeaponDamageEntity";
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("location")) {
            return location;
        }
        else if (name.equals("weapon")) {
            return weapon;
        }
        else if (name.equals("split")) {
            return split;
        }
        else if (name.equals("airstrike")) {
            return airstrike;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onCrackShotPlayerTogglesWeaponAttachment(WeaponExplodeEvent event) {
        player = PlayerTag.mirrorBukkitPlayer(event.getPlayer());
        location = new LocationTag(event.getLocation());
        weapon = new ElementTag(event.getWeaponTitle());
        split = new ElementTag(event.isSplit());
        airstrike = new ElementTag(event.isAirstrike());
        this.event = event;
        fire(event);
    }
}
