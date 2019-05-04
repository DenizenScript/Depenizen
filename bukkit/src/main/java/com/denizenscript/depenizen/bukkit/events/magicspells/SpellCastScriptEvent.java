package com.denizenscript.depenizen.bukkit.events.magicspells;

import com.nisovin.magicspells.events.SpellCastEvent;
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


public class SpellCastScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // magicspells player casts spell
    //
    // @Regex ^on magicspells [^\s]+ spell$
    //
    // @Triggers when a player starts to casts a spell.
    //
    // @Cancellable true
    //
    // @Context
    // <context.spell_name> returns the name of the spell.
    // <context.power> returns an Element(Decimal) of the power of the spell.
    // <context.cast_time> returns an Element(Number) of the cast time of the spell.
    // <context.cooldown> returns an Element(Decimal) of the cooldown of the spell.
    //
    // @Determine
    // "POWER:" + Element(Number) to change the power of the spell.
    // "CAST_TIME:" + Element(Decimal) to change the cast time.
    // "COOLDOWN:" + Element(Number) to change the cooldown.
    //
    // @Plugin DepenizenBukkit, MagicSpells
    //
    // -->

    public SpellCastScriptEvent() {
        instance = this;
    }

    public static SpellCastScriptEvent instance;

    public SpellCastEvent event;
    public dPlayer player;
    private float power;
    private float cooldown;
    private int castTime;
    private Element spell;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        return lower.startsWith("magicspells player casts spell");
    }

    @Override
    public boolean matches(ScriptPath path) {
        String lower = path.eventArgLowerAt(3);
        return lower.startsWith("magicspells player casts spell");
    }

    @Override
    public String getName() {
        return "SpellCastEvent";
    }

    @Override
    public boolean applyDetermination(ScriptContainer container, String determination) {
        if (determination.length() > 0 && !isDefaultDetermination(determination)) {
            String lower = CoreUtilities.toLowerCase(determination);
            if (lower.startsWith("power:")) {
                Element num = new Element(determination.substring(6));
                if (!num.isFloat()) {
                    dB.echoError("Determination for 'power' must be a valid decimal number.");
                    return false;
                }
                power = num.asFloat();
            } else if (lower.startsWith("cast_time:")) {
                Element max = new Element(determination.substring(10));
                if (!max.isInt()) {
                    dB.echoError("Determination for 'cast_time' must be a valid number.");
                    return false;
                }
                castTime = max.asInt();
            } else if (lower.startsWith("cooldown:")) {
                Element num = new Element(determination.substring(9));
                if (!num.isFloat()) {
                    dB.echoError("Determination for 'cooldown' must be a valid decimal number.");
                    return false;
                }
                cooldown = num.asFloat();
            }
        }
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
        else if (name.equals("cast_time")) {
            return new Element(castTime);
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
    public void onPlayerCastsSpell(SpellCastEvent event) {
        player = dPlayer.mirrorBukkitPlayer(event.getCaster());
        power = event.getPower();
        castTime = event.getCastTime();
        cooldown = event.getCooldown();
        spell = new Element(event.getSpell().getName());
        cancelled = event.isCancelled();
        this.event = event;
        fire(event);
        event.setCancelled(cancelled);
        event.setPower(power);
        event.setCastTime(castTime);
        event.setCooldown(cooldown);
    }
}
