package com.denizenscript.depenizen.bukkit.events.magicspells;

import com.nisovin.magicspells.events.ManaChangeEvent;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.denizencore.scripts.containers.ScriptContainer;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ManaChangeScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // magicspells player mana change
    //
    // @Regex ^on magicspells [^\s]+ mana change$
    //
    // @Triggers when a player's mana value changes.
    //
    // @Context
    // <context.old_mana> returns the old amount of mana.
    // <context.new_mana> returns the new amount of mana.
    // <context.max_mana> returns maximum mana the player can have.
    // <context.reason> returns the reason of the change. Can either be POTION, REGEN, SPELL_COST or OTHER
    //
    // @Determine
    // ElementTag(Number) to set a new mana value.
    //
    // @Plugin Depenizen, MagicSpells
    //
    // -->

    public ManaChangeScriptEvent() {
        instance = this;
    }

    public static ManaChangeScriptEvent instance;

    public ManaChangeEvent event;
    public PlayerTag player;
    private int new_mana;
    private int old_mana;
    private int max_mana;
    private ElementTag reason;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        return lower.startsWith("magicspells player mana change");
    }

    @Override
    public boolean matches(ScriptPath path) {
        return super.matches(path);
    }

    @Override
    public String getName() {
        return "ManaChangeEvent";
    }

    @Override
    public boolean applyDetermination(ScriptPath path, ObjectTag determinationObj) {
        String determination = determinationObj.toString();
        if (determination.length() > 0 && !isDefaultDetermination(determinationObj)) {
            ElementTag mana = new ElementTag(determination);
            if (!mana.isInt()) {
                Debug.echoError("Determination for 'mana' must be a valid number.");
                return false;
            }
            new_mana = mana.asInt();
        }
        return super.applyDetermination(path, determinationObj);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(player, null);
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("new_mana")) {
            return new ElementTag(new_mana);
        }
        else if (name.equals("old_mana")) {
            return new ElementTag(old_mana);
        }
        else if (name.equals("max_mana")) {
            return new ElementTag(max_mana);
        }
        else if (name.equals("reason")) {
            return reason;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onPlayerCastsSpell(ManaChangeEvent event) {
        player = PlayerTag.mirrorBukkitPlayer(event.getPlayer());
        new_mana = event.getNewAmount();
        old_mana = event.getOldAmount();
        max_mana = event.getMaxMana();
        reason = new ElementTag(event.getReason().name());
        this.event = event;
        fire(event);
        event.setNewAmount(new_mana);
    }
}
