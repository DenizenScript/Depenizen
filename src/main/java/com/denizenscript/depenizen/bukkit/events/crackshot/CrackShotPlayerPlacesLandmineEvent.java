package com.denizenscript.depenizen.bukkit.events.crackshot;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.shampaggon.crackshot.events.WeaponPlaceMineEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CrackShotPlayerPlacesLandmineEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // crackshot player places landmine
    //
    // @Regex ^on crackshot player places landmine$
    //
    // @Triggers when a player places a landmine.
    //
    // @Context
    // <context.landmine> returns the EntityTag for the landmine.
    //
    // @Plugin Depenizen, CrackShot
    //
    // @Player Always
    //
    // @Group Depenizen
    //
    // -->

    public static CrackShotPlayerPlacesLandmineEvent instance;
    public WeaponPlaceMineEvent event;

    public CrackShotPlayerPlacesLandmineEvent() {
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(event.getPlayer());
    }

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("crackshot player places landmine");
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("landmine")) {
            return new EntityTag(event.getMine());
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onPlacesLandmine(WeaponPlaceMineEvent event) {
        this.event = event;
        fire(event);
    }
}
