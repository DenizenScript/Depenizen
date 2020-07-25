package com.denizenscript.depenizen.bukkit.objects.mythicmobs;

import com.denizenscript.denizencore.objects.*;
import com.denizenscript.denizencore.objects.core.DurationTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
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

import java.util.UUID;

public class MythicMobsMobTag implements ObjectTag, Adjustable {

    // <--[language]
    // @name MythicMobsMobTag Objects
    // @group Depenizen Object Types
    // @plugin Depenizen, MythicMobs
    // @description
    // A MythicMobsMobTag represents a Mythic mob entity in the world.
    //
    // These use the object notation "mythicmob@".
    // The identity format for mythicmobs is <uuid>
    // For example, 'mythicmob@1234-1234-1234'.
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

    @Override
    public String getAttribute(Attribute attribute) {

        if (attribute == null) {
            return null;
        }

        // <--[tag]
        // @attribute <MythicMobsMobTag.internal_name>
        // @returns ElementTag
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns the name MythicMobs identifies the MythicMob with.
        // -->
        if (attribute.startsWith("internal_name")) {
            return new ElementTag(mobType.getInternalName()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <MythicMobsMobTag.display_name>
        // @returns ElementTag
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns the display name of the MythicMob.
        // -->
        else if (attribute.startsWith("display_name")) {
            return new ElementTag(mobType.getDisplayName().get()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <MythicMobsMobTag.spawner_name>
        // @returns ElementTag
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns the name of the spawner (as set on creation in-game) that spawned this mob.
        // Returns null, if the mob was spawned by something other than a spawner.
        // -->
        if (attribute.startsWith("spawner_name")) {
            if (mob.getSpawner() == null) {
                return null;
            }
            return new ElementTag(mob.getSpawner().getName()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <MythicMobsMobTag.level>
        // @returns ElementTag(Number)
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns the level of the MythicMob.
        // -->
        if (attribute.startsWith("level")) {
            return new ElementTag(mob.getLevel()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <MythicMobsMobTag.players_killed>
        // @returns ElementTag(Number)
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns the number of players the MythicMob has killed.
        // -->
        else if (attribute.startsWith("players_killed")) {
            return new ElementTag(mob.getPlayerKills()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <MythicMobsMobTag.damage>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns the damage the MythicMob deals.
        // -->
        else if (attribute.startsWith("damage")) {
            return new ElementTag(mob.getDamage()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <MythicMobsMobTag.armor>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns the armor the MythicMob has.
        // -->
        else if (attribute.startsWith("armor") || attribute.startsWith("armour")) {
            return new ElementTag(mob.getArmor()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <MythicMobsMobTag.has_target>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns whether the MythicMob has a target.
        // -->
        else if (attribute.startsWith("has_target")) {
            return new ElementTag(mob.hasTarget()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <MythicMobsMobTag.target>
        // @returns EntityTag
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns the MythicMob's target.
        // -->
        else if (attribute.startsWith("target") && mob.hasThreatTable()) {
            AbstractEntity target = mob.getThreatTable().getTopThreatHolder();
            if (target == null) {
                return null;
            }
            return new EntityTag(target.getBukkitEntity()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <MythicMobsMobTag.is_damaging>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns whether the MythicMob is using its damaging skill.
        // -->
        else if (attribute.startsWith("is_damaging")) {
            return new ElementTag(mob.isUsingDamageSkill()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <MythicMobsMobTag.entity>
        // @returns EntityTag
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns the EntityTag for the MythicMob.
        // -->
        else if (attribute.startsWith("entity")) {
            return new EntityTag(getLivingEntity()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <MythicMobsMobTag.global_cooldown>
        // @returns ElementTag(Number)
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns the MythicMob's global cooldown.
        // -->
        else if (attribute.startsWith("global_cooldown")) {
            return new DurationTag(mob.getGlobalCooldown()).getAttribute(attribute.fulfill(1));
        }

        String returned = CoreUtilities.autoPropertyTag(this, attribute);
        if (returned != null) {
            return returned;
        }

        return new ElementTag(identify()).getAttribute(attribute);
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
