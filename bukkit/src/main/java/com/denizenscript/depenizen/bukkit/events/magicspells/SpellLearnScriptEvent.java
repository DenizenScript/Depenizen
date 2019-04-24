package com.denizenscript.depenizen.bukkit.events.magicspells;

import com.nisovin.magicspells.events.SpellLearnEvent;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.utilities.DenizenAPI;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.ScriptEntryData;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import org.bukkit.Bukkit;
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
    // @Plugin DepenizenBukkit, MagicSpells
    //
    // -->

    public SpellLearnScriptEvent() {
        instance = this;
    }

    public static SpellLearnScriptEvent instance;

    public SpellLearnEvent event;
    public dPlayer player;
    private Element source;
    private Element spell;

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
    public dObject getContext(String name) {
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
        spell = new Element(event.getSpell().getName());
        source = new Element(event.getSource().name());
        cancelled = event.isCancelled();
        this.event = event;
        fire(event);
        event.setCancelled(cancelled);
    }
}
