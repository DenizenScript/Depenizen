package com.denizenscript.depenizen.bukkit.events.magicspells;

import com.nisovin.magicspells.events.SpellCastedEvent;
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

public class SpellCastedScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // magicspells player completes spell
    //
    // @Regex ^on magicspells [^\s]+ spell$
    //
    // @Triggers when the spell is done and everything has been handled.
    //
    // @Cancellable false
    //
    // @Context
    // <context.spell_name> returns the name of the spell.
    // <context.power> returns an Element(Decimal) of the power of the spell.
    // <context.cooldown> returns an Element(Decimal) of the cooldown of the spell.
    //
    // @Plugin Depenizen, MagicSpells
    //
    // -->

    public SpellCastedScriptEvent() {
        instance = this;
    }

    public static SpellCastedScriptEvent instance;

    public SpellCastedEvent event;
    public dPlayer player;
    private float power;
    private float cooldown;
    private Element spell;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        return lower.startsWith("magicspells player completes spell");
    }

    @Override
    public boolean matches(ScriptPath path) {
        String lower = path.eventArgLowerAt(3);
        return lower.startsWith("magicspells player completes spell");
    }

    @Override
    public String getName() {
        return "SpellCastedEvent";
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
        if (name.equals("power")) {
            return new Element(power);
        }
        else if (name.equals("cooldown")) {
            return new Element(cooldown);
        }
        else if (name.equals("spell_name")) {
            return spell;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onPlayerCastsSpell(SpellCastedEvent event) {
        player = dPlayer.mirrorBukkitPlayer(event.getCaster());
        power = event.getPower();
        cooldown = event.getCooldown();
        spell = new Element(event.getSpell().getName());
        this.event = event;
        fire(event);
    }
}
