package com.denizenscript.depenizen.bukkit.events.crackshot;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.shampaggon.crackshot.events.WeaponTriggerEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CrackShotLandmineTriggerEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // crackshot landmine triggered
    //
    // @Regex ^on crackshot landmine triggered$
    //
    // @Triggers when a CrackShot landmine is triggered by an entity walking through it.
    //
    // @Cancellable true
    //
    // @Context
    // <context.weapon> returns the weapon name of the landmine.
    // <context.victim> returns the entity that triggered the landmine.
    //
    // @Plugin Depenizen, CrackShot
    //
    // @Player Always
    //
    // @Group Depenizen
    //
    // -->

    public static CrackShotLandmineTriggerEvent instance;
    public WeaponTriggerEvent event;

    public CrackShotLandmineTriggerEvent() {
        instance = this;
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(event.getPlayer());
    }

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("crackshot landmine triggered");
    }

    @Override
    public String getName() {
        return "CrackShotLandmineTriggered";
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("victim")) {
            return new EntityTag(event.getVictim());
        }
        else if (name.equals("weapon")) {
            return new ElementTag(event.getWeaponTitle());
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onLandMineTrigger(WeaponTriggerEvent event) {
        this.event = event;
        fire(event);
    }
}
