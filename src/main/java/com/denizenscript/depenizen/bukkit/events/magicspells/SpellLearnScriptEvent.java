package com.denizenscript.depenizen.bukkit.events.magicspells;

import com.nisovin.magicspells.events.SpellLearnEvent;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SpellLearnScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // magicspells player learns spell
    //
    // @Regex ^on magicspells [^\s]+ learns spell$
    //
    // @Triggers when a player is about to learn a spell.
    //
    // @Cancellable true
    //
    // @Context
    // <context.spell_name> returns the name of the spell.
    // <context.source> returns the source. Can either be SPELLBOOK, TEACH, TOME or OTHER
    //
    // @Plugin Depenizen, MagicSpells
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->

    public SpellLearnScriptEvent() {
    }


    public SpellLearnEvent event;
    public PlayerTag player;
    private ElementTag source;
    private ElementTag spell;

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("magicspells player learns spell");
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(player, null);
    }

    @Override
    public ObjectTag getContext(String name) {
        return switch (name) {
            case "source" -> source;
            case "spell_name" -> spell;
            default -> super.getContext(name);
        };
    }

    @EventHandler
    public void onPlayerCastsSpell(SpellLearnEvent event) {
        player = PlayerTag.mirrorBukkitPlayer(event.getLearner());
        spell = new ElementTag(event.getSpell().getName());
        source = new ElementTag(event.getSource());
        this.event = event;
        fire(event);
    }
}
