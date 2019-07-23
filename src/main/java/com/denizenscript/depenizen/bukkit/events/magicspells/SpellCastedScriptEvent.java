package com.denizenscript.depenizen.bukkit.events.magicspells;

import com.nisovin.magicspells.events.SpellCastedEvent;
import com.denizenscript.denizen.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.denizencore.scripts.containers.ScriptContainer;
import com.denizenscript.denizencore.utilities.CoreUtilities;
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
    public PlayerTag player;
    private float power;
    private float cooldown;
    private ElementTag spell;

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
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(player, null);
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("power")) {
            return new ElementTag(power);
        }
        else if (name.equals("cooldown")) {
            return new ElementTag(cooldown);
        }
        else if (name.equals("spell_name")) {
            return spell;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onPlayerCastsSpell(SpellCastedEvent event) {
        player = PlayerTag.mirrorBukkitPlayer(event.getCaster());
        power = event.getPower();
        cooldown = event.getCooldown();
        spell = new ElementTag(event.getSpell().getName());
        this.event = event;
        fire(event);
    }
}
