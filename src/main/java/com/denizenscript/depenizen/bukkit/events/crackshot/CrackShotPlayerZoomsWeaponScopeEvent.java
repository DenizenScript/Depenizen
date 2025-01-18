package com.denizenscript.depenizen.bukkit.events.crackshot;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.shampaggon.crackshot.events.WeaponScopeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import javax.swing.text.Element;

public class CrackShotPlayerZoomsWeaponScopeEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // crackshot player zooms weapon scope
    //
    // @Regex ^on crackshot player zooms weapon scope$
    //
    // @Triggers when a player zooms their CrackShot weapon scope.
    //
    // @Cancellable true
    //
    // @Context
    // <context.weapon> returns the name of the weapon.
    // <context.zoomed> returns whether the player zoomed in.
    //
    // @Plugin Depenizen, CrackShot
    //
    // @Player Always
    //
    // @Group Depenizen
    //
    // -->

    public static CrackShotPlayerZoomsWeaponScopeEvent instance;
    public WeaponScopeEvent event;

    public CrackShotPlayerZoomsWeaponScopeEvent() {
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(event.getPlayer());
    }

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("crackshot player zooms weapon scope");
    }

    @Override
    public ObjectTag getContext(String name) {
        return switch (name) {
            case "weapon" -> new ElementTag(event.getWeaponTitle());
            case "zoomed" -> new ElementTag(event.isZoomIn());
            default -> super.getContext(name);
        };
    }

    @EventHandler
    public void onZoomsWeapon(WeaponScopeEvent event) {
        this.event = event;
        fire(event);
    }
}
