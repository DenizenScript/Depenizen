package com.denizenscript.depenizen.bukkit.events.magicspells;

import com.nisovin.magicspells.events.ManaChangeEvent;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.utilities.DenizenAPI;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.ScriptEntryData;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import net.aufdemrand.denizencore.utilities.debugging.dB;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

// <--[event]
// @Events
// magicspells player mana change
//
// @Regex ^on magicspells [^\s]+ mana change$
//
// @Triggers when a player's mana value changes.
//
// @Cancellable false
//
// @Context
// <context.old_mana> returns the old amount of mana.
// <context.new_mana> returns the new amount of mana.
// <context.max_mana> returns maximum mana the player can have.
// <context.reason> returns the reason of the change. Can either be POTION, REGEN, SPELL_COST or OTHER
//
// @Determine
// Element(Number) to set a new mana value.
//
// @Plugin DepenizenBukkit, MagicSpells
//
// -->

public class ManaChangeScriptEvent extends BukkitScriptEvent implements Listener {

    public ManaChangeScriptEvent() {
        instance = this;
    }

    public static ManaChangeScriptEvent instance;

    public ManaChangeEvent event;
    public dPlayer player;
    private int new_mana;
    private int old_mana;
    private int max_mana;
    private Element reason;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        return lower.startsWith("magicspells player mana change");
    }

    @Override
    public boolean matches(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        return lower.startsWith("magicspells player mana change");
    }

    @Override
    public String getName() {
        return "ManaChangeEvent";
    }

    @Override
    public void init() {
        Bukkit.getServer().getPluginManager().registerEvents(this, DenizenAPI.getCurrentInstance());
    }

    @Override
    public void destroy() {
        ManaChangeEvent.getHandlerList().unregister(this);
    }

    @Override
    public boolean applyDetermination(ScriptContainer container, String determination) {
        Element mana = new Element(determination);
        if (!mana.isInt()) {
            dB.echoError("Determination for 'mana' must be a valid number.");
            return false;
        }
        new_mana = mana.asInt();
        return super.applyDetermination(container, determination);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(player, null);
    }

    @Override
    public dObject getContext(String name) {
        if (name.equals("new_mana")) {
            return new Element(new_mana);
        }
        else if (name.equals("old_mana")) {
            return new Element(old_mana);
        }
        else if (name.equals("max_mana")) {
            return new Element(max_mana);
        }
        else if (name.equals("reason")) {
            return reason;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onPlayerCastsSpell(ManaChangeEvent event) {
        player = dPlayer.mirrorBukkitPlayer(event.getPlayer());
        new_mana = event.getNewAmount();
        old_mana = event.getOldAmount();
        max_mana = event.getMaxMana();
        reason = new Element(event.getReason().name());
        this.event = event;
        fire();
        event.setNewAmount(new_mana);
    }
}
