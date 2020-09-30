package com.denizenscript.depenizen.bukkit.properties.sentinel;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.objects.NPCTag;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.DurationTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.mcmonkey.sentinel.SentinelTrait;
import org.mcmonkey.sentinel.commands.SentinelCommand;
import org.mcmonkey.sentinel.targeting.SentinelTargetList;

import java.util.Map;

public class SentinelNPCProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "SentinelNPC";
    }

    public static boolean describes(ObjectTag object) {
        return object instanceof NPCTag;
    }

    public static SentinelNPCProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new SentinelNPCProperties((NPCTag) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "sentinel"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    private SentinelNPCProperties(NPCTag npc) {
        this.npc = npc;
    }

    NPCTag npc;

    public static ListTag listTargets(SentinelTargetList list) {
        String comboStr = list.toComboString().replace(SentinelCommand.colorBasic, "").replace(ChatColor.AQUA.toString(), "").trim();
        ListTag result = new ListTag();
        if (comboStr.isEmpty()) {
            return result;
        }
        for (String target : CoreUtilities.split(comboStr, (char) 0x01)) {
            result.add(target.trim());
        }
        return result;
    }

    @Override
    public String getAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        if (attribute.startsWith("sentinel")) {
            attribute = attribute.fulfill(1);

            if (!npc.getCitizen().hasTrait(SentinelTrait.class)) {
                return null;
            }
            SentinelTrait sentinel = npc.getCitizen().getTrait(SentinelTrait.class);

            // <--[tag]
            // @attribute <NPCTag.sentinel.chasing>
            // @returns EntityTag
            // @plugin Depenizen, Sentinel
            // @description
            // Returns the entity currently being chased by this Sentinel NPC (if any).
            // -->
            if (attribute.startsWith("chasing")) {
                if (sentinel.chasing == null) {
                    return null;
                }
                return new EntityTag(sentinel.chasing).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <NPCTag.sentinel.targets>
            // @returns ListTag
            // @plugin Depenizen, Sentinel
            // @description
            // Returns a list of all targets currently targeted by this Sentinel NPC.
            // -->
            if (attribute.startsWith("targets")) {
                return listTargets(sentinel.allTargets).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <NPCTag.sentinel.ignores>
            // @returns ListTag
            // @plugin Depenizen, Sentinel
            // @description
            // Returns a list of all targets currently ignored by this Sentinel NPC.
            // -->
            if (attribute.startsWith("ignores")) {
                return listTargets(sentinel.allIgnores).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <NPCTag.sentinel.avoids>
            // @returns ListTag
            // @plugin Depenizen, Sentinel
            // @description
            // Returns a list of all targets currently avoided by this Sentinel NPC.
            // -->
            if (attribute.startsWith("avoids")) {
                return listTargets(sentinel.allAvoids).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <NPCTag.sentinel.avoid_range>
            // @returns ElementTag(Decimal)
            // @plugin Depenizen, Sentinel
            // @description
            // Returns the avoid range of a Sentinel NPC.
            // -->
            if (attribute.startsWith("avoid_range")) {
                return new ElementTag(sentinel.avoidRange).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <NPCTag.sentinel.range>
            // @returns ElementTag(Decimal)
            // @plugin Depenizen, Sentinel
            // @description
            // Returns the range of a Sentinel NPC.
            // -->
            if (attribute.startsWith("range")) {
                return new ElementTag(sentinel.range).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <NPCTag.sentinel.raw_damage>
            // @returns ElementTag(Decimal)
            // @plugin Depenizen, Sentinel
            // @description
            // Returns the raw damage value (no calculating) of a Sentinel NPC.
            // -->
            if (attribute.startsWith("raw_damage")) {
                return new ElementTag(sentinel.damage).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <NPCTag.sentinel.damage>
            // @returns ElementTag(Decimal)
            // @plugin Depenizen, Sentinel
            // @description
            // Returns the current damage value (with calculations if needed) of a Sentinel NPC.
            // -->
            if (attribute.startsWith("damage")) {
                return new ElementTag(sentinel.getDamage()).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <NPCTag.sentinel.raw_armor>
            // @returns ElementTag(Decimal)
            // @plugin Depenizen, Sentinel
            // @description
            // Returns the raw armor value (no calculating) of a Sentinel NPC.
            // -->
            if (attribute.startsWith("raw_armor")) {
                return new ElementTag(sentinel.armor).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <NPCTag.sentinel.armor>
            // @returns ElementTag(Decimal)
            // @plugin Depenizen, Sentinel
            // @description
            // Returns the current armor value (with calculations if needed) of a Sentinel NPC.
            // -->
            if (attribute.startsWith("armor")) {
                return new ElementTag(sentinel.getArmor(sentinel.getLivingEntity())).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <NPCTag.sentinel.health>
            // @returns ElementTag(Decimal)
            // @plugin Depenizen, Sentinel
            // @description
            // Returns the max health of a Sentinel NPC.
            // -->
            if (attribute.startsWith("health")) {
                return new ElementTag(sentinel.health).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <NPCTag.sentinel.ranged_chase>
            // @returns ElementTag(Boolean)
            // @plugin Depenizen, Sentinel
            // @description
            // Returns whether the Sentinel NPC will chase with ranged weapons.
            // -->
            if (attribute.startsWith("ranged_chase")) {
                return new ElementTag(sentinel.rangedChase).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <NPCTag.sentinel.close_chase>
            // @returns ElementTag(Boolean)
            // @plugin Depenizen, Sentinel
            // @description
            // Returns whether the Sentinel NPC will chase with close weapons.
            // -->
            if (attribute.startsWith("close_chase")) {
                return new ElementTag(sentinel.closeChase).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <NPCTag.sentinel.invincible>
            // @returns ElementTag(Boolean)
            // @plugin Depenizen, Sentinel
            // @description
            // Returns whether the Sentinel NPC is invincible.
            // -->
            if (attribute.startsWith("invincible")) {
                return new ElementTag(sentinel.invincible).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <NPCTag.sentinel.fightback>
            // @returns ElementTag(Boolean)
            // @plugin Depenizen, Sentinel
            // @description
            // Returns whether the Sentinel NPC will fight back.
            // -->
            if (attribute.startsWith("fightback")) {
                return new ElementTag(sentinel.fightback).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <NPCTag.sentinel.runaway>
            // @returns ElementTag(Boolean)
            // @plugin Depenizen, Sentinel
            // @description
            // Returns whether the Sentinel NPC will run away.
            // -->
            if (attribute.startsWith("runaway")) {
                return new ElementTag(sentinel.runaway).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <NPCTag.sentinel.attack_rate>
            // @returns DurationTag
            // @plugin Depenizen, Sentinel
            // @description
            // Returns the attack rate of a Sentinel NPC.
            // -->
            if (attribute.startsWith("attack_rate")) {
                return new DurationTag((long) sentinel.attackRate).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <NPCTag.sentinel.attack_rate_ranged>
            // @returns DurationTag
            // @plugin Depenizen, Sentinel
            // @description
            // Returns the attack rate (at range) of a Sentinel NPC.
            // -->
            if (attribute.startsWith("attack_rate")) {
                return new DurationTag((long) sentinel.attackRateRanged).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <NPCTag.sentinel.heal_rate>
            // @returns DurationTag
            // @plugin Depenizen, Sentinel
            // @description
            // Returns the heal rate of a Sentinel NPC.
            // -->
            if (attribute.startsWith("heal_rate")) {
                return new DurationTag((long) sentinel.healRate).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <NPCTag.sentinel.guarding>
            // @returns EntityTag
            // @plugin Depenizen, Sentinel
            // @description
            // Returns the entity currently being guarded by this Sentinel NPC (if any).
            // -->
            if (attribute.startsWith("guarding")) {
                LivingEntity entity = sentinel.getGuardingEntity();
                if (entity == null) {
                    return null;
                }
                return new EntityTag(entity).getDenizenObject().getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <NPCTag.sentinel.needs_ammo>
            // @returns ElementTag(Boolean)
            // @plugin Depenizen, Sentinel
            // @description
            // Returns whether the Sentinel NPC needs ammo.
            // -->
            if (attribute.startsWith("needs_ammo")) {
                return new ElementTag(sentinel.needsAmmo).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <NPCTag.sentinel.safe_shot>
            // @returns ElementTag(Boolean)
            // @plugin Depenizen, Sentinel
            // @description
            // Returns whether the Sentinel NPC has safe-shot enabled.
            // -->
            if (attribute.startsWith("safe_shot")) {
                return new ElementTag(sentinel.safeShot).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <NPCTag.sentinel.respawn_time>
            // @returns DurationTag
            // @plugin Depenizen, Sentinel
            // @description
            // Returns how long a Sentinel NPC will wait after death before respawning.
            // -->
            if (attribute.startsWith("respawn_time")) {
                return new DurationTag(sentinel.respawnTime).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <NPCTag.sentinel.chase_range>
            // @returns ElementTag(Decimal)
            // @plugin Depenizen, Sentinel
            // @description
            // Returns the chase range of a Sentinel NPC.
            // -->
            if (attribute.startsWith("chase_range")) {
                return new ElementTag(sentinel.chaseRange).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <NPCTag.sentinel.spawn_point>
            // @returns LocationTag
            // @plugin Depenizen, Sentinel
            // @description
            // Returns the respawn point of a Sentinel NPC.
            // -->
            if (attribute.startsWith("spawn_point")) {
                if (sentinel.spawnPoint == null) {
                    return null;
                }
                return new LocationTag(sentinel.spawnPoint).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <NPCTag.sentinel.avoid_return_point>
            // @returns LocationTag
            // @plugin Depenizen, Sentinel
            // @description
            // Returns the avoid return point of a Sentinel NPC.
            // -->
            if (attribute.startsWith("avoid_return_point")) {
                if (sentinel.avoidReturnPoint == null) {
                    return null;
                }
                return new LocationTag(sentinel.avoidReturnPoint).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <NPCTag.sentinel.drops>
            // @returns ListTag(ItemTag)
            // @plugin Depenizen, Sentinel
            // @description
            // Returns a list of items that this Sentinel NPC will drop on death.
            // -->
            if (attribute.startsWith("drops")) {
                ListTag drops = new ListTag();
                for (ItemStack item : sentinel.drops) {
                    drops.addObject(new ItemTag(item.clone()));
                }
                return drops.getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <NPCTag.sentinel.drop_chances>
            // @returns ListTag
            // @plugin Depenizen, Sentinel
            // @description
            // Returns a list of drop chances for items that this Sentinel NPC will drop on death.
            // -->
            if (attribute.startsWith("drop_chances")) {
                ListTag drops = new ListTag();
                for (Double chance : sentinel.dropChances) {
                    drops.add(chance.toString());
                }
                return drops.getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <NPCTag.sentinel.enemy_drops>
            // @returns ElementTag(Boolean)
            // @plugin Depenizen, Sentinel
            // @description
            // Returns whether the Sentinel NPC has enemy-drops enabled.
            // -->
            if (attribute.startsWith("enemy_drops")) {
                return new ElementTag(sentinel.enemyDrops).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <NPCTag.sentinel.enemy_target_time>
            // @returns DurationTag
            // @plugin Depenizen, Sentinel
            // @description
            // Returns how long a Sentinel NPC will wait before abandoning an unseen target.
            // -->
            if (attribute.startsWith("enemy_target_time")) {
                return new DurationTag(sentinel.enemyTargetTime).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <NPCTag.sentinel.speed>
            // @returns ElementTag(Decimal)
            // @plugin Depenizen, Sentinel
            // @description
            // Returns the movement speed of a Sentinel NPC.
            // -->
            if (attribute.startsWith("speed")) {
                return new ElementTag(sentinel.speed).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <NPCTag.sentinel.warning>
            // @returns ElementTag
            // @plugin Depenizen, Sentinel
            // @description
            // Returns the warning message of a Sentinel NPC.
            // -->
            if (attribute.startsWith("warning")) {
                return new ElementTag(sentinel.warningText).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <NPCTag.sentinel.greeting>
            // @returns ElementTag
            // @plugin Depenizen, Sentinel
            // @description
            // Returns the greeting message of a Sentinel NPC.
            // -->
            if (attribute.startsWith("greeting")) {
                return new ElementTag(sentinel.greetingText).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <NPCTag.sentinel.greet_range>
            // @returns ElementTag(Decimal)
            // @plugin Depenizen, Sentinel
            // @description
            // Returns the greeting range of a Sentinel NPC.
            // -->
            if (attribute.startsWith("greet_range")) {
                return new ElementTag(sentinel.greetRange).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <NPCTag.sentinel.greet_rate>
            // @returns DurationTag
            // @plugin Depenizen, Sentinel
            // @description
            // Returns the greeting rate of a Sentinel NPC.
            // -->
            if (attribute.startsWith("greet_rate")) {
                return new DurationTag((long) sentinel.greetRate).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <NPCTag.sentinel.autoswitch>
            // @returns ElementTag(Boolean)
            // @plugin Depenizen, Sentinel
            // @description
            // Returns whether the Sentinel NPC will automatically switch weapons.
            // -->
            if (attribute.startsWith("autoswitch")) {
                return new ElementTag(sentinel.autoswitch).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <NPCTag.sentinel.squad>
            // @returns ElementTag
            // @plugin Depenizen, Sentinel
            // @description
            // Returns the name of this Sentinel NPC's squad (if any).
            // -->
            if (attribute.startsWith("squad")) {
                if (sentinel.squad == null) {
                    return null;
                }
                return new ElementTag(sentinel.squad).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <NPCTag.sentinel.accuracy>
            // @returns ElementTag(Decimal)
            // @plugin Depenizen, Sentinel
            // @description
            // Returns the accuracy value of a Sentinel NPC.
            // -->
            if (attribute.startsWith("accuracy")) {
                return new ElementTag(sentinel.accuracy).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <NPCTag.sentinel.realistic>
            // @returns ElementTag(Boolean)
            // @plugin Depenizen, Sentinel
            // @description
            // Returns whether the Sentinel NPC is set to realistic targeting mode.
            // -->
            if (attribute.startsWith("realistic")) {
                return new ElementTag(sentinel.realistic).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <NPCTag.sentinel.reach>
            // @returns ElementTag(Decimal)
            // @plugin Depenizen, Sentinel
            // @description
            // Returns the hand reach value of a Sentinel NPC.
            // -->
            if (attribute.startsWith("reach")) {
                return new ElementTag(sentinel.reach).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <NPCTag.sentinel.guard_distance_minimum>
            // @returns ElementTag(Decimal)
            // @plugin Depenizen, Sentinel
            // @description
            // Returns the minimum-guard-distance value of a Sentinel NPC.
            // -->
            if (attribute.startsWith("guard_distance_minimum")) {
                return new ElementTag(sentinel.guardDistanceMinimum).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <NPCTag.sentinel.guard_selection_range>
            // @returns ElementTag(Decimal)
            // @plugin Depenizen, Sentinel
            // @description
            // Returns the guard-selection-range value of a Sentinel NPC.
            // -->
            if (attribute.startsWith("guard_selection_range")) {
                return new ElementTag(sentinel.guardSelectionRange).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <NPCTag.sentinel.weapon_damage>
            // @returns ListTag
            // @plugin Depenizen, Sentinel
            // @description
            // Returns a list-mapping of custom weapon damage values of a Sentinel NPC.
            // For example, "stick/5|stone/3"
            // -->
            if (attribute.startsWith("weapon_damage")) {
                ListTag result = new ListTag();
                for (Map.Entry<String, Double> damage : sentinel.weaponDamage.entrySet()) {
                    result.add(damage.getKey() + "/" + damage.getValue());
                }
                return result.getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <NPCTag.sentinel.weapon_redirects>
            // @returns ListTag
            // @plugin Depenizen, Sentinel
            // @description
            // Returns a list-mapping of custom weapon redirect values of a Sentinel NPC.
            // For example, "stick/bow|stone/diamond_sword"
            // -->
            if (attribute.startsWith("weapon_redirects")) {
                ListTag result = new ListTag();
                for (Map.Entry<String, String> redir : sentinel.weaponRedirects.entrySet()) {
                    result.add(redir.getKey() + "/" + redir.getValue());
                }
                return result.getAttribute(attribute.fulfill(1));
            }
        }
        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {
    }
}
