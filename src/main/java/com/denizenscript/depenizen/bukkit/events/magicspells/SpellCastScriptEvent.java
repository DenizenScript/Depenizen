package com.denizenscript.depenizen.bukkit.events.magicspells;

import com.nisovin.magicspells.events.SpellCastEvent;
import com.nisovin.magicspells.util.SpellReagents;
import com.denizenscript.denizen.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.dItem;
import com.denizenscript.denizen.objects.dPlayer;
import com.denizenscript.denizencore.objects.Element;
import com.denizenscript.denizencore.objects.dList;
import com.denizenscript.denizencore.objects.dObject;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.denizencore.scripts.containers.ScriptContainer;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.denizenscript.denizencore.utilities.debugging.dB;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpellCastScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // magicspells player casts spell
    // magicspells player casts <spell>
    //
    // @Regex ^on magicspells casts [^\s]+$
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
    // <context.spell_reagent_TYPE> returns an Element(Number) of the reagent cost for the given type. Valid types are: mana, health, hunger, experience, levels, durability, money
    // <context.spell_reagent_variables> returns a dList in the form variable/cost|...
    // <context.spell_reagent_items> returns a dList of dItems of reagent cost.
    //
    // @Determine
    // "POWER:" + Element(Number) to change the power of the spell.
    // "CAST_TIME:" + Element(Decimal) to change the cast time.
    // "COOLDOWN:" + Element(Number) to change the cooldown.
    // "REAGENT:<TYPE>:" + Element(Number) to change the reagent cost of the given type. Valid types are: mana, health, hunger, experience, levels, durability, money
    // "REAGANT:VARIABLE:<NAME>:" + Element(Decimal) to change the reagant cost for the given variable name.
    // "REAGENT:ITEMS:" + dList(dItem) to change the reagent item cost.
    // "CLEAR_REAGENTS" to clear away all reagent costs.
    //
    // @Plugin Depenizen, MagicSpells
    //
    // -->

    public SpellCastScriptEvent() {
        instance = this;
    }

    public static SpellCastScriptEvent instance;

    public SpellCastEvent event;
    public dPlayer player;
    private Element spell;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        return lower.startsWith("magicspells player casts");
    }

    @Override
    public boolean matches(ScriptPath path) {
        String spellName = path.eventArgLowerAt(3);
        if (spellName.equals("spell") || spellName.equalsIgnoreCase(spell.asString())) {
            return true;
        }
        return false;
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
                Element num = new Element(determination.substring("power:".length()));
                if (!num.isFloat()) {
                    dB.echoError("Determination for 'power' must be a valid decimal number.");
                    return false;
                }
                event.setPower(num.asFloat());
                return true;
            }
            else if (lower.startsWith("cast_time:")) {
                Element max = new Element(determination.substring("cast_time:".length()));
                if (!max.isInt()) {
                    dB.echoError("Determination for 'cast_time' must be a valid number.");
                    return false;
                }
                event.setCastTime(max.asInt());
                return true;
            }
            else if (lower.startsWith("cooldown:")) {
                Element num = new Element(determination.substring("cooldown:".length()));
                if (!num.isFloat()) {
                    dB.echoError("Determination for 'cooldown' must be a valid decimal number.");
                    return false;
                }
                event.setCooldown(num.asFloat());
                return true;
            }
            else if (lower.equals("clear_reagents")) {
                event.setReagents(new SpellReagents());
                return true;
            }
            else if (lower.startsWith("reagent:")) {
                String type = determination.substring("reagent:".length());
                String typeLower = CoreUtilities.toLowerCase(type);
                SpellReagents reagents = event.getReagents();
                if (reagents == null) {
                    reagents = new SpellReagents();
                }
                if (typeLower.startsWith("mana:")) {
                    reagents.setMana(new Element(type.substring("mana:".length())).asInt());
                }
                else if (typeLower.startsWith("health:")) {
                    reagents.setHealth(new Element(type.substring("health:".length())).asInt());
                }
                else if (typeLower.startsWith("hunger:")) {
                    reagents.setHunger(new Element(type.substring("hunger:".length())).asInt());
                }
                else if (typeLower.startsWith("experience:")) {
                    reagents.setExperience(new Element(type.substring("experience:".length())).asInt());
                }
                else if (typeLower.startsWith("levels:")) {
                    reagents.setLevels(new Element(type.substring("levels:".length())).asInt());
                }
                else if (typeLower.startsWith("durability:")) {
                    reagents.setDurability(new Element(type.substring("durability:".length())).asInt());
                }
                else if (typeLower.startsWith("money:")) {
                    reagents.setMoney(new Element(type.substring("money:".length())).asFloat());
                }
                else if (typeLower.startsWith("variable:")) {
                    String variable = type.substring("variable:".length());
                    int ind = variable.indexOf(':');
                    if (reagents.getVariables() == null) {
                        reagents.setVariables(new HashMap<>());
                    }
                    reagents.getVariables().put(variable.substring(0, ind), new Element(variable.substring(ind + 1)).asDouble());
                }
                else if (typeLower.startsWith("items:")) {
                    List<ItemStack> itemsToSet = new ArrayList<>();
                    for (dItem item : new dList(type.substring("items:".length())).filter(dItem.class)) {
                        itemsToSet.add(item.getItemStack());
                    }
                    reagents.setItems(itemsToSet);
                }
                event.setReagents(reagents);
                return true;
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
            return new Element(event.getPower());
        }
        else if (name.equals("cast_time")) {
            return new Element(event.getCastTime());
        }
        else if (name.equals("cooldown")) {
            return new Element(event.getCooldown());
        }
        else if (name.equals("spell_name")) {
            return spell;
        }
        else if (name.startsWith("spell_reagent_")) {
            SpellReagents reagents = event.getReagents();
            if (reagents != null) {
                if (name.equals("spell_reagant_mana")) {
                    return new Element(reagents.getMana());
                }
                else if (name.equals("spell_reagant_health")) {
                    return new Element(reagents.getHealth());
                }
                else if (name.equals("spell_reagant_hunger")) {
                    return new Element(reagents.getHunger());
                }
                else if (name.equals("spell_reagant_experience")) {
                    return new Element(reagents.getExperience());
                }
                else if (name.equals("spell_reagant_levels")) {
                    return new Element(reagents.getLevels());
                }
                else if (name.equals("spell_reagant_durability")) {
                    return new Element(reagents.getDurability());
                }
                else if (name.equals("spell_reagant_money")) {
                    return new Element(reagents.getMoney());
                }
                else if (name.equals("spell_reagant_variables")) {
                    dList list = new dList();
                    if (reagents.getVariables() != null) {
                        for (Map.Entry<String, Double> entry : reagents.getVariables().entrySet()) {
                            list.add(entry.getKey() + "/" + entry.getValue());
                        }
                    }
                    return list;
                }
                else if (name.equals("spell_reagant_items")) {
                    dList list = new dList();
                    if (reagents.getItems() != null) {
                        for (ItemStack item : reagents.getItems()) {
                            list.addObject(new dItem(item));
                        }
                    }
                    return list;
                }
            }
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onPlayerCastsSpell(SpellCastEvent event) {
        player = dPlayer.mirrorBukkitPlayer(event.getCaster());
        spell = new Element(event.getSpell().getName());
        this.event = event;
        fire(event);
    }
}
