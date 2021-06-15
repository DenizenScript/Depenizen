package com.denizenscript.depenizen.bukkit.objects.mythicmobs;

import com.denizenscript.denizencore.objects.*;
import com.denizenscript.denizencore.objects.core.DurationTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.MapTag;
import com.denizenscript.denizencore.tags.ObjectTagProcessor;
import com.denizenscript.denizencore.tags.TagRunnable;
import com.denizenscript.depenizen.bukkit.bridges.MythicMobsBridge;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.TagContext;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitEntity;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.Map;
import java.util.UUID;

public class MythicMobsMobTag implements ObjectTag, Adjustable {

    // <--[ObjectType]
    // @name MythicMobsMobTag
    // @prefix mythicmob
    // @base ElementTag
    // @format
    // The identity format for MythicMobsMobTag is <uuid>
    // For example, 'mythicmob@1234-1234-1234'.
    //
    // @plugin Depenizen, MythicMobs
    // @description
    // A MythicMobsMobTag represents a Mythic mob entity in the world.
    //
    // -->

    public static MythicMobsMobTag valueOf(String uuid) {
        return valueOf(uuid, null);
    }

    @Fetchable("mythicmob")
    public static MythicMobsMobTag valueOf(String string, TagContext context) {
        if (string == null) {
            return null;
        }
        try {
            string = string.replace("mythicmob@", "");
            UUID uuid = UUID.fromString(string);
            if (!MythicMobsBridge.isMythicMob(uuid)) {
                return null;
            }
            return new MythicMobsMobTag(MythicMobsBridge.getActiveMob(EntityTag.getEntityForID(uuid)));
        }
        catch (Exception e) {
            return null;
        }
    }

    public static boolean matches(String string) {
        return valueOf(string) != null;
    }

    public MythicMobsMobTag(ActiveMob mob) {
        if (mob != null) {
            this.mob = mob;
            this.mobType = mob.getType();
        }
        else {
            Debug.echoError("ActiveMob referenced is null!");
        }
    }

    public ActiveMob getMob() {
        return mob;
    }

    public MythicMob getMobType() {
        return mobType;
    }

    public LivingEntity getLivingEntity() {
        return (LivingEntity) mob.getEntity().getBukkitEntity();
    }

    public Entity getEntity() {
        return mob.getEntity().getBukkitEntity();
    }

    private String prefix;
    ActiveMob mob;
    MythicMob mobType;

    @Override
    public ObjectTag setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {
        return tagProcessor.getObjectAttribute(this, attribute);
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public String debug() {
        return prefix + "='<A>" + identify() + "<G>' ";
    }

    @Override
    public boolean isUnique() {
        return true;
    }

    @Override
    public String getObjectType() {
        return "MythicMob";
    }

    @Override
    public String identify() {
        return "mythicmob@" + mob.getUniqueId();
    }

    @Override
    public String identifySimple() {
        return identify();
    }

    @Override
    public String toString() {
        return identify();
    }

    public static void registerTag(String name, TagRunnable.ObjectInterface<MythicMobsMobTag> runnable, String... variants) {
        tagProcessor.registerTag(name, runnable, variants);
    }
    public static ObjectTagProcessor<MythicMobsMobTag> tagProcessor = new ObjectTagProcessor<>();

    public static void registerTags() {

        // <--[tag]
        // @attribute <MythicMobsMobTag.internal_name>
        // @returns ElementTag
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns the name MythicMobs identifies the MythicMob with.
        // -->
        registerTag("internal_name", (attribute, object) -> {
            return new ElementTag(object.getMobType().getInternalName());
        });

        // <--[tag]
        // @attribute <MythicMobsMobTag.display_name>
        // @returns ElementTag
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns the display name of the MythicMob.
        // -->
        registerTag("display_name", (attribute, object) -> {
            return new ElementTag(object.getMob().getDisplayName());
        });

        // <--[tag]
        // @attribute <MythicMobsMobTag.spawner_name>
        // @returns ElementTag
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns the name of the spawner (as set on creation in-game) that spawned this mob.
        // Returns null, if the mob was spawned by something other than a spawner.
        // -->
        registerTag("spawner_name", (attribute, object) -> {
            if (object.getMob().getSpawner() == null) {
                return null;
            }
            return new ElementTag(object.getMob().getSpawner().getName());
        });

        // <--[tag]
        // @attribute <MythicMobsMobTag.spawner>
        // @returns MythicSpawnerTag
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns the MythicSpawnerTag that spawned this mob.
        // Returns null, if the mob was spawned by something other than a spawner.
        // -->
        registerTag("spawner", (attribute, object) -> {
            if (object.getMob().getSpawner() == null) {
                return null;
            }
            return new MythicSpawnerTag(object.getMob().getSpawner());
        });

        // <--[tag]
        // @attribute <MythicMobsMobTag.level>
        // @returns ElementTag(Number)
        // @plugin Depenizen, MythicMobs
        // @mechanism MythicMobsMobTag.level
        // @description
        // Returns the level of the MythicMob.
        // -->
        registerTag("level", (attribute, object) -> {
            return new ElementTag(object.getMob().getLevel());
        });

        // <--[tag]
        // @attribute <MythicMobsMobTag.players_killed>
        // @returns ElementTag(Number)
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns the number of players the MythicMob has killed.
        // -->
        registerTag("players_killed", (attribute, object) -> {
            return new ElementTag(object.getMob().getPlayerKills());
        });

        // <--[tag]
        // @attribute <MythicMobsMobTag.damage>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns the damage the MythicMob deals.
        // -->
        registerTag("damage", (attribute, object) -> {
            return new ElementTag(object.getMob().getDamage());
        });

        // <--[tag]
        // @attribute <MythicMobsMobTag.armor>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns the armor the MythicMob has.
        // -->
        registerTag("armor", (attribute, object) -> {
            return new ElementTag(object.getMob().getArmor());
        });

        // <--[tag]
        // @attribute <MythicMobsMobTag.has_target>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns whether the MythicMob has a target.
        // -->
        registerTag("has_target", (attribute, object) -> {
            return new ElementTag(object.getMob().hasTarget());
        });

        // <--[tag]
        // @attribute <MythicMobsMobTag.target>
        // @returns EntityTag
        // @mechanism MythicMobsMobTag.target
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns the MythicMob's target.
        // -->
        registerTag("target", (attribute, object) -> {
            AbstractEntity target = object.getMob().getThreatTable().getTopThreatHolder();
            if (target == null) {
                return null;
            }
            return new EntityTag(target.getBukkitEntity());
        });

        // <--[tag]
        // @attribute <MythicMobsMobTag.has_threat_table>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns whether the MythicMob has a threat table.
        // -->
        registerTag("has_threat_table", (attribute, object) -> {
            return new ElementTag(object.getMob().hasThreatTable());
        });

        // <--[tag]
        // @attribute <MythicMobsMobTag.threat_table>
        // @returns MapTag
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns the MythicMob's threat table, can contain multiple types of entities.
        // Map is in the formatting of "UUID/Threat|UUID/Threat"
        // -->
        registerTag("threat_table", (attribute, object) -> {
            if (!object.getMob().hasThreatTable()) {
                return null;
            }
            Map<AbstractEntity, Double> table = object.getMob().getThreatTable().asMap();
            MapTag map = new MapTag();
            for (AbstractEntity entity : table.keySet()) {
                map.putObject(entity.getUniqueId().toString(), new ElementTag(table.get(entity)));
            }
            return map;
        });

        // <--[tag]
        // @attribute <MythicMobsMobTag.threat_table_players>
        // @returns MapTag
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns the MythicMob's threat table, only containing players.
        // Map is in the formatting of "UUID/Threat|UUID/Threat"
        // -->
        registerTag("threat_table_players", (attribute, object) -> {
            if (!object.getMob().hasThreatTable()) {
                return null;
            }
            Map<AbstractEntity, Double> table = object.getMob().getThreatTable().asMap();
            MapTag map = new MapTag();
            for (AbstractEntity entity : table.keySet()) {
                if (entity.isPlayer()) {
                    map.putObject(entity.getUniqueId().toString(), new ElementTag(table.get(entity)));
                }
            }
            return map;
        });

        // <--[tag]
        // @attribute <MythicMobsMobTag.is_damaging>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns whether the MythicMob is using its damaging skill.
        // -->
        registerTag("is_damaging", (attribute, object) -> {
            return new ElementTag(object.getMob().isUsingDamageSkill());
        });

        // <--[tag]
        // @attribute <MythicMobsMobTag.stance>
        // @returns ElementTag
        // @plugin Depenizen, MythicMobs
        // @mechanism MythicMobsMobTag.stance
        // @description
        // Returns the current stance of the MythicMob.
        // -->
        registerTag("stance", (attribute, object) -> {
            return new ElementTag(object.getMob().getStance());
        });

        // <--[tag]
        // @attribute <MythicMobsMobTag.entity>
        // @returns EntityTag
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns the EntityTag for the MythicMob.
        // -->
        registerTag("entity", (attribute, object) -> {
            return new EntityTag(object.getMob().getEntity().getBukkitEntity());
        });

        // <--[tag]
        // @attribute <MythicMobsMobTag.global_cooldown>
        // @returns ElementTag(Number)
        // @mechanism MythicMobsMobTag.global_cooldown
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns the MythicMob's global cooldown.
        // -->
        registerTag("global_cooldown", (attribute, object) -> {
            return new DurationTag(object.getMob().getGlobalCooldown());
        });
    }

    @Override
    public void adjust(Mechanism mechanism) {

        // <--[mechanism]
        // @object MythicMobsMobTag
        // @name global_cooldown
        // @input ElementTag(Number)
        // @description
        // Sets global cooldown of the MythicMob.
        // @tags
        // <MythicMobsMobTag.global_cooldown>
        // -->
        if (mechanism.matches("global_cooldown") && mechanism.requireInteger()) {
            mob.setGlobalCooldown(mechanism.getValue().asInt());
        }

        // <--[mechanism]
        // @object MythicMobsMobTag
        // @name stance
        // @input ElementTag
        // @description
        // Set the stance of the MythicMob.
        // @tags
        // <MythicMobsMobTag.stance>
        // -->
        if (mechanism.matches("stance")) {
            mob.setStance(mechanism.getValue().asString());
        }

        // <--[mechanism]
        // @object MythicMobsMobTag
        // @name reset_target
        // @input None
        // @description
        // Reset the MythicMob's target.
        // @tags
        // <MythicMobsMobTag.target>
        // -->
        else if (mechanism.matches("reset_target")) {
            mob.resetTarget();
        }

        // <--[mechanism]
        // @object MythicMobsMobTag
        // @name level
        // @input ElementTag(Number)
        // @description
        // Set the MythicMob's level.
        // @tags
        // <MythicMobsMobTag.level>
        // -->
        else if (mechanism.matches("level") && mechanism.requireInteger()) {
            mob.setLevel(mechanism.getValue().asInt());
        }

        // <--[mechanism]
        // @object MythicMobsMobTag
        // @name target
        // @input EntityTag
        // @description
        // Sets MythicMob's target.
        // @tags
        // <MythicMobsMobTag.target>
        // -->
        else if (mechanism.matches("target") && mechanism.requireObject(EntityTag.class)) {
            EntityTag mTarget = mechanism.valueAsType(EntityTag.class);
            if (mTarget == null || !mTarget.isValid() || mTarget.getLivingEntity() == null) {
                return;
            }
            BukkitEntity target = new BukkitEntity(mTarget.getBukkitEntity());
            mob.setTarget(target);
        }
        CoreUtilities.autoPropertyMechanism(this, mechanism);
        if (!mechanism.fulfilled()) {
            mechanism.reportInvalid();
        }
    }

    @Override
    public void applyProperty(Mechanism mechanism) {
        Debug.echoError("Cannot apply properties to a MythicMob!");
    }
}
