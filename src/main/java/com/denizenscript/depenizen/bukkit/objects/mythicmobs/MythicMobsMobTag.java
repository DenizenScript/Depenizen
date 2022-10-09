package com.denizenscript.depenizen.bukkit.objects.mythicmobs;

import com.denizenscript.denizen.objects.EntityFormObject;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.objects.Adjustable;
import com.denizenscript.denizencore.objects.Fetchable;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.DurationTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.core.MapTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.ObjectTagProcessor;
import com.denizenscript.denizencore.tags.TagContext;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.denizencore.utilities.text.StringHolder;
import com.denizenscript.depenizen.bukkit.bridges.MythicMobsBridge;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.mobs.GenericCaster;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.api.skills.SkillCaster;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.adapters.BukkitEntity;
import io.lumine.mythic.bukkit.utils.serialize.Optl;
import io.lumine.mythic.core.mobs.ActiveMob;
import io.lumine.mythic.core.skills.auras.Aura;
import io.lumine.mythic.core.skills.variables.Variable;
import io.lumine.mythic.core.skills.variables.VariableType;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
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
    public boolean isUnique() {
        return true;
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

    public static ObjectTagProcessor<MythicMobsMobTag> tagProcessor = new ObjectTagProcessor<>();

    public static void registerTags() {

        // <--[tag]
        // @attribute <MythicMobsMobTag.internal_name>
        // @returns ElementTag
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns the name MythicMobs identifies the MythicMob with.
        // -->
        tagProcessor.registerTag(ElementTag.class, "internal_name", (attribute, object) -> {
            return new ElementTag(object.getMobType().getInternalName());
        });

        // <--[tag]
        // @attribute <MythicMobsMobTag.display_name>
        // @returns ElementTag
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns the display name of the MythicMob.
        // -->
        tagProcessor.registerTag(ElementTag.class, "display_name", (attribute, object) -> {
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
        tagProcessor.registerTag(ElementTag.class, "spawner_name", (attribute, object) -> {
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
        tagProcessor.registerTag(MythicSpawnerTag.class, "spawner", (attribute, object) -> {
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
        tagProcessor.registerTag(ElementTag.class, "level", (attribute, object) -> {
            return new ElementTag(object.getMob().getLevel());
        });

        // <--[tag]
        // @attribute <MythicMobsMobTag.players_killed>
        // @returns ElementTag(Number)
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns the number of players the MythicMob has killed.
        // -->
        tagProcessor.registerTag(ElementTag.class, "players_killed", (attribute, object) -> {
            return new ElementTag(object.getMob().getPlayerKills());
        });

        // <--[tag]
        // @attribute <MythicMobsMobTag.damage>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns the damage the MythicMob deals.
        // -->
        tagProcessor.registerTag(ElementTag.class, "damage", (attribute, object) -> {
            return new ElementTag(object.getMob().getDamage());
        });

        // <--[tag]
        // @attribute <MythicMobsMobTag.armor>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns the armor the MythicMob has.
        // -->
        tagProcessor.registerTag(ElementTag.class, "armor", (attribute, object) -> {
            return new ElementTag(object.getMob().getArmor());
        });

        // <--[tag]
        // @attribute <MythicMobsMobTag.has_target>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns whether the MythicMob has a target.
        // -->
        tagProcessor.registerTag(ElementTag.class, "has_target", (attribute, object) -> {
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
        tagProcessor.registerTag(EntityFormObject.class, "target", (attribute, object) -> {
            AbstractEntity target = object.getMob().getThreatTable().getTopThreatHolder();
            if (target == null) {
                return null;
            }
            return new EntityTag(target.getBukkitEntity()).getDenizenObject();
        });

        // <--[tag]
        // @attribute <MythicMobsMobTag.has_threat_table>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns whether the MythicMob has a threat table.
        // -->
        tagProcessor.registerTag(ElementTag.class, "has_threat_table", (attribute, object) -> {
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
        tagProcessor.registerTag(MapTag.class, "threat_table", (attribute, object) -> {
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
        tagProcessor.registerTag(MapTag.class, "threat_table_players", (attribute, object) -> {
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
        tagProcessor.registerTag(ElementTag.class, "is_damaging", (attribute, object) -> {
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
        tagProcessor.registerTag(ElementTag.class, "stance", (attribute, object) -> {
            return new ElementTag(object.getMob().getStance());
        });

        // <--[tag]
        // @attribute <MythicMobsMobTag.entity>
        // @returns EntityTag
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns the EntityTag for the MythicMob.
        // -->
        tagProcessor.registerTag(EntityFormObject.class, "entity", (attribute, object) -> {
            return new EntityTag(object.getMob().getEntity().getBukkitEntity()).getDenizenObject();
        });

        // <--[tag]
        // @attribute <MythicMobsMobTag.global_cooldown>
        // @returns DurationTag
        // @mechanism MythicMobsMobTag.global_cooldown
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns the MythicMob's global cooldown.
        // -->
        tagProcessor.registerTag(DurationTag.class, "global_cooldown", (attribute, object) -> {
            return new DurationTag(object.getMob().getGlobalCooldown());
        });

        // <--[tag]
        // @attribute <MythicMobsMobTag.mythic_variable[<name>]>
        // @returns ElementTag
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns the value of a MythicMobs variable for this MythicMob.
        // -->
        tagProcessor.registerTag(ElementTag.class, ElementTag.class, "mythic_variable", (attribute, object, secondVal) -> {
            return new ElementTag(MythicMobsBridge.getMythicVariable(object.getEntity(), secondVal.asString()), true);
        });

        // <--[tag]
        // @attribute <MythicMobsMobTag.mythic_variable_map>
        // @returns MapTag
        // @plugin Depenizen, MythicMobs
        // @mechanism MythicMobsMobTag.mythic_variable_map
        // @description
        // Returns a map of the MythicMob's variables.
        // -->
        tagProcessor.registerTag(MapTag.class, "mythic_variable_map", (attribute, object) -> {
            MapTag result = new MapTag();
            for (Map.Entry<String, Variable> entry : MythicMobsBridge.getMythicVariableMap(object.getEntity()).entrySet()) {
                result.putObject(entry.getKey(), new ElementTag(entry.getValue().toString(), true));
            }
            return result;
        });

        // <--[tag]
        // @attribute <MythicMobsMobTag.ai_goal_selectors>
        // @returns ListTag
        // @plugin Depenizen MythicMbos
        // @description
        // Returns a ListTag of the MythicMob's AIGoalSelectors.
        // -->
        tagProcessor.registerTag(ListTag.class, "ai_goal_selectors", (attribute, object) -> {
            ListTag list = new ListTag();
            for (String goal : object.getMobType().getAIGoalSelectors()) {
                list.addObject(new ElementTag(goal, true));
            }
            return list;
        });

        // <--[tag]
        // @attribute <MythicMobsMobTag.ai_target_selectors>
        // @returns ListTag
        // @plugin Depenizen MythicMbos
        // @description
        // Returns a ListTag of the MythicMob's AITargetSelectors.
        // -->
        tagProcessor.registerTag(ListTag.class, "ai_target_selectors", (attribute, object) -> {
            ListTag list = new ListTag();
            for (String target : object.getMobType().getAITargetSelectors()) {
                list.addObject(new ElementTag(target, true));
            }
            return list;
        });

        // <--[tag]
        // @attribute <MythicMobsMobTag.damage_modifiers>
        // @returns MapTag
        // @plugin Depenizen MythicMobs
        // @description
        // Returns a MapTag of the MythicMob's damage modifiers.
        // -->
        tagProcessor.registerTag(MapTag.class, "damage_modifiers", (attribute, object) -> {
            MapTag result = new MapTag();
            for (Map.Entry<String, Double> entry : MythicMobsBridge.getDamageModifiers(object.getMobType()).entrySet()) {
                result.putObject(entry.getKey(), new ElementTag(entry.getValue().toString()));
            }
            return result;
        });

        // <--[tag]
        // @attribute <MythicMobsMobTag.faction>
        // @returns ElementTag
        // @plugin Depenizen MythicMobs
        // @description
        // Returns the mob's faction.
        // -->
        tagProcessor.registerTag(ElementTag.class, "faction", (attribute, object) -> {
            return new ElementTag(MythicMobsBridge.getFaction(object.getMob()), true);
        });

        // <--[tag]
        // @attribute <MythicMobsMobTag.children>
        // @returns ListTag(EntityTag)
        // @plugin Depenizen MythicMobs
        // @description
        // Returns the mob's children.
        // @mechanism MythicMobsMobTag.add_child
        // -->
        tagProcessor.registerTag(ListTag.class, "children", (attribute, object) -> {
            ListTag result = new ListTag();
            for (AbstractEntity entity : object.getMob().getChildren()) {
                result.addObject(new EntityTag(entity.getBukkitEntity()));
            }
            return result;
        });

        // <--[tag]
        // @attribute <MythicMobsMobTag.parent>
        // @returns EntityTag
        // @plugin Depenizen MythicMobs
        // @description
        // Returns the mob's parent.
        // @mechanism MythicMobsMobTag.parent
        // -->
        tagProcessor.registerTag(EntityTag.class, "parent", (attribute, object) -> {
            SkillCaster parent = object.getMob().getParent();
            return parent != null ? new EntityTag(parent.getEntity().getBukkitEntity()) : null;
        });

        // <--[tag]
        // @attribute <MythicMobsMobTag.mob_path>
        // @returns ElementTag
        // @plugin Depenizen MythicMobs
        // @description
        // Returns the path to the file that stores this MythicMob's configuration. Equivalent to <@link tag mythicmobs.mob_path>.
        // -->
        tagProcessor.registerTag(ElementTag.class, "mob_path", (attribute, object) -> {
            return new ElementTag(object.getMob().getType().getConfig().getFile().getPath(), true);
        });

        // <--[tag]
        // @attribute <MythicMobsMobTag.tracked_location>
        // @returns LocationTag
        // @plugin Depenizen MythicMobs
        // @description
        // Returns the MythicMob's tracked location. (The location that the @TrackedLocation targeter in Mythic skills will return)
        // @mechanism MythicMobsMobTag.tracked_location
        // -->
        tagProcessor.registerTag(LocationTag.class, "tracked_location", (attribute, object) -> {
            return object.getMob().getTrackedLocation() != null ? new LocationTag(MythicMobsBridge.locationFromAbstractLocation(object.getMob().getTrackedLocation())) : null;
        });

        // <--[tag]
        // @attribute <MythicMobsMobTag.owner>
        // @returns EntityTag
        // @plugin Depenizen MythicMobs
        // @description
        // Returns the MythicMob's owner, if present.
        // @mechanism MythicMobsMobTag.owner
        // -->
        tagProcessor.registerTag(EntityTag.class, "owner", (attribute, object) -> {
            Optl<UUID> owner = object.getMob().getOwner();
            if (!owner.isPresent()) {
                return null;
            }
            Entity ownerEntity = EntityTag.getEntityForID(owner.get());
            if (ownerEntity == null) {
                return null;
            }
            return new EntityTag(ownerEntity);
        });

        // <--[tag]
        // @attribute <MythicMobsMobTag.has_aura[<name>]>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen MythicMobs
        // @description
        // Returns whether the MythicMob has an aura whose name is the same as the parameter.
        // -->
        tagProcessor.registerTag(ElementTag.class, ElementTag.class, "has_aura", (attribute, object, auraName) -> {
            return new ElementTag(object.getMob().getAuraRegistry().hasAura(auraName.asString()));
        });

        // <--[tag]
        // @attribute <MythicMobsMobTag.aura_stacks[<name>]>
        // @return ElementTag(Number)
        // @plugin Depenizen MythicMobs
        // @description
        // Returns the amount of stacks of auras with the same name as the parameter that the MythicMob has.
        // -->
        tagProcessor.registerTag(ElementTag.class, ElementTag.class, "aura_stacks", (attribute, object, auraName) -> {
            return new ElementTag(object.getMob().getAuraRegistry().getStacks(auraName.asString()));
        });

        // <--[tag]
        // @attribute <MythicMobsMobTag.auras>
        // @return ListTag
        // @plugin Depenizen MythicMobs
        // @description
        // Returns a list of auras that the MythicMob currently has.
        // -->
        tagProcessor.registerTag(ListTag.class, "auras", (attribute, object) -> {
            ListTag result = new ListTag();
            for (Map.Entry<String, Queue<Aura.AuraTracker>> name : object.getMob().getAuraRegistry().getAuras().entrySet()) {
                result.addObject(new ElementTag(name.getKey()));
            }
            return result;
        });
    }

    @Override
    public void applyProperty(Mechanism mechanism) {
        mechanism.echoError("Cannot apply properties to a MythicMob!");
    }

    @Override
    public void adjust(Mechanism mechanism) {

        // <--[mechanism]
        // @object MythicMobsMobTag
        // @name mythic_variable_map
        // @input MapTag
        // @plugin Depenizen, MythicMobs
        // @description
        // Sets the variables of a MythicMob from a map.
        // @tags
        // <MythicMobsMobTag.mythic_variable_map>
        // -->
        if (mechanism.matches("mythic_variable_map") && mechanism.requireObject(MapTag.class)) {
            MapTag map = mechanism.valueAsType(MapTag.class);
            Map<String, Variable> newMap = new HashMap<>();
            for (Map.Entry<StringHolder, ObjectTag> entry : map.map.entrySet()) {
                newMap.put(entry.getKey().str, Variable.ofType(VariableType.STRING, entry.getValue()));
            }
            MythicMobsBridge.setMythicVariableMap(mob.getEntity().getBukkitEntity(), newMap);
        }

        // <--[mechanism]
        // @object MythicMobsMobTag
        // @name faction
        // @input ElementTag
        // @plugin Depenizen, Mythicmobs
        // @description
        // Sets the mob's faction.
        // @tags
        // <MythicMobsMobTag.faction>
        // -->
        else if (mechanism.matches("faction") && mechanism.requireObject(ElementTag.class)) {
            MythicMobsBridge.setFaction(mob, mechanism.getValue().asString());
        }

        // <--[mechanism]
        // @object MythicMobsMobTag
        // @name global_cooldown
        // @input ElementTag(Number)
        // @plugin Depenizen, MythicMobs
        // @description
        // Sets global cooldown of the MythicMob.
        // @tags
        // <MythicMobsMobTag.global_cooldown>
        // -->
        else if (mechanism.matches("global_cooldown") && mechanism.requireInteger()) {
            mob.setGlobalCooldown(mechanism.getValue().asInt());
        }

        // <--[mechanism]
        // @object MythicMobsMobTag
        // @name stance
        // @input ElementTag
        // @plugin Depenizen, MythicMobs
        // @description
        // Set the stance of the MythicMob.
        // @tags
        // <MythicMobsMobTag.stance>
        // -->
        else if (mechanism.matches("stance")) {
            mob.setStance(mechanism.getValue().asString());
        }

        // <--[mechanism]
        // @object MythicMobsMobTag
        // @name reset_target
        // @input None
        // @plugin Depenizen, MythicMobs
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
        // @plugin Depenizen, MythicMobs
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
        // @plugin Depenizen, MythicMobs
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

        // <--[mechanism]
        // @object MythicMobsMobTag
        // @name add_child
        // @input EntityTag
        // @plugin Depenizen, MythicMobs
        // @description
        // Add a child to the MythicMob.
        // @tags
        // <MythicMobsMobTag.children>
        // -->
        else if (mechanism.matches("add_child") && mechanism.requireObject(EntityTag.class)) {
            EntityTag child = mechanism.valueAsType(EntityTag.class);
            mob.addChild(BukkitAdapter.adapt(child.getBukkitEntity()));
        }

        // <--[mechanism]
        // @object MythicMobsMobTag
        // @name parent
        // @input EntityTag
        // @plugin Depenizen, MythicMobs
        // @description
        // Sets the MythicMob's parent.
        // @tags
        // <MythicMobsMobTag.parent>
        // -->
        else if (mechanism.matches("parent") && mechanism.requireObject(EntityTag.class)) {
            EntityTag parent = mechanism.valueAsType(EntityTag.class);
            mob.setParent(new GenericCaster(BukkitAdapter.adapt(parent.getBukkitEntity())));
        }

        // <--[mechanism]
        // @object MythicMobsMobTag
        // @name tracked_location
        // @input LocationTag
        // @plugin Depenizen, MythicMobs
        // @description
        // Sets the MythicMob's tracked location. (The location that the @TrackedLocation targeter in Mythic skills will return)
        // @tags
        // <MythicMobsMobTag.tracked_location>
        // -->
        else if (mechanism.matches("tracked_location") && mechanism.requireObject(LocationTag.class)) {
            mob.setTrackedLocation(BukkitAdapter.adapt((Location) mechanism.valueAsType(LocationTag.class).getJavaObject()));
        }

        // <--[mechanism]
        // @object MythicMobsMobTag
        // @name owner
        // @input EntityTag
        // @plugin Depenizen, MythicMobs
        // @description
        // Sets the MythicMob's owner.
        // @tags
        // <MythicMobsMobTag.owner>
        // -->
        else if (mechanism.matches("owner") && mechanism.requireObject(EntityTag.class)) {
            mob.setOwner(mechanism.valueAsType(EntityTag.class).getUUID());
        }

        tagProcessor.processMechanism(this, mechanism);
        CoreUtilities.autoPropertyMechanism(this, mechanism);
        if (!mechanism.fulfilled()) {
            mechanism.reportInvalid();
        }
    }
}
