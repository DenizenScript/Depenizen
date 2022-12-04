package com.denizenscript.depenizen.bukkit.events.magicspells;

import com.denizenscript.denizen.objects.EntityTag;
import com.nisovin.magicspells.events.SpellCastEvent;
import com.nisovin.magicspells.util.SpellReagents;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.denizenscript.denizencore.utilities.debugging.Debug;
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
    // magicspells <entity> casts <'spell'>
    //
    // @Triggers when an entity starts to casts a spell.
    //
    // @Cancellable true
    //
    // @Context
    // <context.spell_name> returns the name of the spell.
    // <context.caster> returns the entity that casted the spell.
    // <context.power> returns an ElementTag(Decimal) of the power of the spell.
    // <context.cast_time> returns an ElementTag(Number) of the cast time of the spell.
    // <context.cooldown> returns an ElementTag(Decimal) of the cooldown of the spell.
    // <context.spell_reagent_TYPE> returns an ElementTag(Number) of the reagent cost for the given type. Valid types are: mana, health, hunger, experience, levels, durability, money
    // <context.spell_reagent_variables> returns a ListTag in the form variable/cost|...
    // <context.spell_reagent_items> returns a ListTag of ItemTags of reagent cost.
    //
    // @Determine
    // "POWER:" + ElementTag(Number) to change the power of the spell.
    // "CAST_TIME:" + ElementTag(Decimal) to change the cast time.
    // "COOLDOWN:" + ElementTag(Number) to change the cooldown.
    // "REAGENT:<TYPE>:" + ElementTag(Number) to change the reagent cost of the given type. Valid types are: mana, health, hunger, experience, levels, durability, money
    // "REAGANT:VARIABLE:<NAME>:" + ElementTag(Decimal) to change the reagant cost for the given variable name.
    // "REAGENT:ITEMS:" + ListTag(ItemTag) to change the reagent item cost.
    // "CLEAR_REAGENTS" to clear away all reagent costs.
    //
    // @Plugin Depenizen, MagicSpells
    //
    // @Player When the caster is a player.
    //
    // @Group Depenizen
    //
    // -->

    public SpellCastScriptEvent() {
        registerCouldMatcher("magicspells <entity> casts <'spell'>");
    }


    public SpellCastEvent event;
    public EntityTag caster;
    private ElementTag spell;

    @Override
    public boolean matches(ScriptPath path) {
        String spellName = path.eventArgLowerAt(3);
        if (!spellName.equals("spell") && !spellName.equalsIgnoreCase(spell.asString())) {
            return false;
        }
        if (!path.tryArgObject(1, caster)) {
            return false;
        }
        return super.matches(path);
    }

    @Override
    public boolean applyDetermination(ScriptPath path, ObjectTag determinationObj) {
        String determination = determinationObj.toString();
        if (determination.length() > 0) {
            String lower = CoreUtilities.toLowerCase(determination);
            if (lower.startsWith("power:")) {
                ElementTag num = new ElementTag(determination.substring("power:".length()));
                if (!num.isFloat()) {
                    Debug.echoError("Determination for 'power' must be a valid decimal number.");
                    return false;
                }
                event.setPower(num.asFloat());
                return true;
            }
            else if (lower.startsWith("cast_time:")) {
                ElementTag max = new ElementTag(determination.substring("cast_time:".length()));
                if (!max.isInt()) {
                    Debug.echoError("Determination for 'cast_time' must be a valid number.");
                    return false;
                }
                event.setCastTime(max.asInt());
                return true;
            }
            else if (lower.startsWith("cooldown:")) {
                ElementTag num = new ElementTag(determination.substring("cooldown:".length()));
                if (!num.isFloat()) {
                    Debug.echoError("Determination for 'cooldown' must be a valid decimal number.");
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
                    reagents.setMana(new ElementTag(type.substring("mana:".length())).asInt());
                }
                else if (typeLower.startsWith("health:")) {
                    reagents.setHealth(new ElementTag(type.substring("health:".length())).asInt());
                }
                else if (typeLower.startsWith("hunger:")) {
                    reagents.setHunger(new ElementTag(type.substring("hunger:".length())).asInt());
                }
                else if (typeLower.startsWith("experience:")) {
                    reagents.setExperience(new ElementTag(type.substring("experience:".length())).asInt());
                }
                else if (typeLower.startsWith("levels:")) {
                    reagents.setLevels(new ElementTag(type.substring("levels:".length())).asInt());
                }
                else if (typeLower.startsWith("durability:")) {
                    reagents.setDurability(new ElementTag(type.substring("durability:".length())).asInt());
                }
                else if (typeLower.startsWith("money:")) {
                    reagents.setMoney(new ElementTag(type.substring("money:".length())).asFloat());
                }
                else if (typeLower.startsWith("variable:")) {
                    String variable = type.substring("variable:".length());
                    int ind = variable.indexOf(':');
                    if (reagents.getVariables() == null) {
                        reagents.setVariables(new HashMap<>());
                    }
                    reagents.getVariables().put(variable.substring(0, ind), new ElementTag(variable.substring(ind + 1)).asDouble());
                }
                else if (typeLower.startsWith("items:")) {
                    List<ItemStack> itemsToSet = new ArrayList<>();
                    for (ItemTag item : ListTag.valueOf(type.substring("items:".length()), CoreUtilities.basicContext).filter(ItemTag.class, path.container, true)) {
                        itemsToSet.add(item.getItemStack());
                    }
                    reagents.setItems(itemsToSet);
                }
                event.setReagents(reagents);
                return true;
            }
        }
        return super.applyDetermination(path, determinationObj);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(caster);
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("power")) {
            return new ElementTag(event.getPower());
        }
        else if (name.equals("caster")) {
            return caster.getDenizenObject();
        }
        else if (name.equals("cast_time")) {
            return new ElementTag(event.getCastTime());
        }
        else if (name.equals("cooldown")) {
            return new ElementTag(event.getCooldown());
        }
        else if (name.equals("spell_name")) {
            return spell;
        }
        else if (name.startsWith("spell_reagent_")) {
            SpellReagents reagents = event.getReagents();
            if (reagents != null) {
                switch (name) {
                    case "spell_reagant_mana":
                        return new ElementTag(reagents.getMana());
                    case "spell_reagant_health":
                        return new ElementTag(reagents.getHealth());
                    case "spell_reagant_hunger":
                        return new ElementTag(reagents.getHunger());
                    case "spell_reagant_experience":
                        return new ElementTag(reagents.getExperience());
                    case "spell_reagant_levels":
                        return new ElementTag(reagents.getLevels());
                    case "spell_reagant_durability":
                        return new ElementTag(reagents.getDurability());
                    case "spell_reagant_money":
                        return new ElementTag(reagents.getMoney());
                    case "spell_reagant_variables": {
                        ListTag list = new ListTag();
                        if (reagents.getVariables() != null) {
                            for (Map.Entry<String, Double> entry : reagents.getVariables().entrySet()) {
                                list.add(entry.getKey() + "/" + entry.getValue());
                            }
                        }
                        return list;
                    }
                    case "spell_reagant_items": {
                        ListTag list = new ListTag();
                        if (reagents.getItems() != null) {
                            for (ItemStack item : reagents.getItems()) {
                                list.addObject(new ItemTag(item));
                            }
                        }
                        return list;
                    }
                }
            }
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onPlayerCastsSpell(SpellCastEvent event) {
        caster = new EntityTag(event.getCaster());
        spell = new ElementTag(event.getSpell().getName());
        this.event = event;
        fire(event);
    }
}
