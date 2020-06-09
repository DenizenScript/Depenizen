package com.denizenscript.depenizen.bukkit.events.crackshot;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.shampaggon.crackshot.events.WeaponAttachmentToggleEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class crackshotWeaponAttachmentToggleEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // crackshot player toggles weapon attachment
    //
    // @Regex ^on crackshot player toggles weapon attachment$
    //
    // @Triggers when a player toggles an attachment on a CrackShot weapon.
    //
    // @Cancellable true
    //
    // @Context
    // <context.weapon> returns the ItemTag for the weapon.
    // <context.delay> returns the delay between toggles.
    //
    // @Determine
    // ElementTag(Number) to set the time between toggles.
    //
    // @Plugin Depenizen, CrackShot
    //
    // @Player Always
    //
    // -->

    public static crackshotWeaponAttachmentToggleEvent instance;
    public  WeaponAttachmentToggleEvent event;
    public  PlayerTag player;
    private int       delay;
    private ItemTag   weapon;
    public crackshotWeaponAttachmentToggleEvent() {
        instance = this;
    }

    @Override
    public boolean applyDetermination(ScriptPath path, ObjectTag determinationObj) {
        String determination = determinationObj.toString();
        if (!isDefaultDetermination(determinationObj)) {
            ElementTag newDelay = new ElementTag(determination);
            if (!newDelay.isInt()) {
                Debug.echoError("Determination for 'delay' must be a valid integer.");
                return false;
            }
            event.setToggleDelay(newDelay.asInt());
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
        return path.eventLower.startsWith("crackshot player toggles weapon attachment");
    }

    @Override
    public String getName() {
        return "CrackShotWeaponAttachmentToggle";
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("delay")) {
            return new ElementTag(delay);
        }
        else if (name.equals("weapon")) {
            return weapon;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onCrackShotPlayerTogglesWeaponAttachment(WeaponAttachmentToggleEvent event) {
        player = PlayerTag.mirrorBukkitPlayer(event.getPlayer());
        weapon = new ItemTag(event.getItemStack());
        delay = event.getToggleDelay();
        this.event = event;
        fire(event);
    }
}
