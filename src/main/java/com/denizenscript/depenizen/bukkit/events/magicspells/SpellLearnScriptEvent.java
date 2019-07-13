package com.denizenscript.depenizen.bukkit.events.magicspells;

import com.nisovin.magicspells.events.SpellLearnEvent;
import com.denizenscript.denizen.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.dPlayer;
import com.denizenscript.denizencore.objects.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.denizencore.scripts.containers.ScriptContainer;
import com.denizenscript.denizencore.utilities.CoreUtilities;
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
    // -->

    public SpellLearnScriptEvent() {
        instance = this;
    }

    public static SpellLearnScriptEvent instance;

    public SpellLearnEvent event;
    public dPlayer player;
    private ElementTag source;
    private ElementTag spell;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        return lower.startsWith("magicspells player learns spell");
    }

    @Override
    public boolean matches(ScriptPath path) {
        String lower = path.eventArgLowerAt(3);
        return lower.startsWith("magicspells player learns spell");
    }

    @Override
    public String getName() {
        return "SpellLearnEvent";
    }

    @Override
    public boolean applyDetermination(ScriptContainer container, String determination) {
        return super.applyDetermination(container, determination);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(player, null);
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("source")) {
            return source;
        }
        else if (name.equals("spell_name")) {
            return spell;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onPlayerCastsSpell(SpellLearnEvent event) {
        player = dPlayer.mirrorBukkitPlayer(event.getLearner());
        spell = new ElementTag(event.getSpell().getName());
        source = new ElementTag(event.getSource().name());
        this.event = event;
        fire(event);
    }
}
