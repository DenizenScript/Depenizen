package com.denizenscript.depenizen.bukkit.objects.mythicmobs;

import com.denizenscript.denizencore.objects.*;
import com.denizenscript.depenizen.bukkit.bridges.MythicMobsBridge;
import com.denizenscript.denizen.objects.dEntity;
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

public class MythicMobsMob implements ObjectTag, Adjustable {

    public static MythicMobsMob valueOf(String uuid) {
        return valueOf(uuid, null);
    }

    @Fetchable("mythicmob")
    public static MythicMobsMob valueOf(String string, TagContext context) {
        if (string == null) {
            return null;
        }
        try {
            string = string.replace("mythicmob@", "");
            UUID uuid = UUID.fromString(string);
            if (!MythicMobsBridge.isMythicMob(uuid)) {
                return null;
            }
            return new MythicMobsMob(MythicMobsBridge.getActiveMob(dEntity.getEntityForID(uuid)));
        }
        catch (Exception e) {
            return null;
        }
    }

    public static boolean matches(String string) {
        return valueOf(string) != null;
    }

    public MythicMobsMob(ActiveMob mob) {
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
        return mob.getLivingEntity();
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
        // @attribute <mythicmob@mythicmob.internal_name>
        // @returns ElementTag
        // @description
        // Returns the name MythicMobs identifies the MythicMob with.
        // @Plugin Depenizen, MythicMobs
        // -->
        if (attribute.startsWith("internal_name")) {
            return new ElementTag(mobType.getInternalName()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <mythicmob@mythicmob.display_name>
        // @returns ElementTag
        // @description
        // Returns the display name of the MythicMob.
        // @Plugin Depenizen, MythicMobs
        // -->
        else if (attribute.startsWith("display_name")) {
            return new ElementTag(mobType.getDisplayName()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <mythicmob@mythicmob.spawner_name>
        // @returns ElementTag
        // @description
        // Returns the name of the spawner (as set on creation in-game) that spawned this mob.
        // Returns null, if the mob was spawned by something other than a spawner.
        // @Plugin Depenizen, MythicMobs
        // -->
        if (attribute.startsWith("spawner_name")) {
            if (mob.getSpawner() == null) {
                return null;
            }
            return new ElementTag(mob.getSpawner().getName()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <mythicmob@mythicmob.level>
        // @returns ElementTag(Number)
        // @description
        // Returns the level of the MythicMob.
        // @Plugin Depenizen, MythicMobs
        // -->
        if (attribute.startsWith("level")) {
            return new ElementTag(mob.getLevel()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <mythicmob@mythicmob.players_killed>
        // @returns ElementTag(Number)
        // @description
        // Returns the number of players the MythicMob has killed.
        // @Plugin Depenizen, MythicMobs
        // -->
        else if (attribute.startsWith("players_killed")) {
            return new ElementTag(mob.getPlayerKills()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <mythicmob@mythicmob.damage>
        // @returns ElementTag(Decimal)
        // @description
        // Returns the damage the MythicMob deals.
        // @Plugin Depenizen, MythicMobs
        // -->
        else if (attribute.startsWith("damage")) {
            return new ElementTag(mob.getDamage()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <mythicmob@mythicmob.armor>
        // @returns ElementTag(Decimal)
        // @description
        // Returns the armor the MythicMob has.
        // @Plugin Depenizen, MythicMobs
        // -->
        else if (attribute.startsWith("armor") || attribute.startsWith("armour")) {
            return new ElementTag(mob.getArmor()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <mythicmob@mythicmob.has_target>
        // @returns ElementTag(Boolean)
        // @description
        // Returns whether the MythicMob has a target.
        // @Plugin Depenizen, MythicMobs
        // -->
        else if (attribute.startsWith("has_target")) {
            return new ElementTag(mob.hasTarget()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <mythicmob@mythicmob.target>
        // @returns dEntity
        // @description
        // Returns the MythicMob's target.
        // @Plugin Depenizen, MythicMobs
        // -->
        else if (attribute.startsWith("target") && mob.hasThreatTable()) {
            AbstractEntity target = mob.getThreatTable().getTopThreatHolder();
            if (target == null) {
                return null;
            }
            return new dEntity(target.getBukkitEntity()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <mythicmob@mythicmob.is_damaging>
        // @returns ElementTag(Boolean)
        // @description
        // Returns whether the MythicMob is using its damaging skill.
        // @Plugin Depenizen, MythicMobs
        // -->
        else if (attribute.startsWith("is_damaging")) {
            return new ElementTag(mob.isUsingDamageSkill()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <mythicmob@mythicmob.entity>
        // @returns dEntity
        // @description
        // Returns the dEntity for the MythicMob.
        // @Plugin Depenizen, MythicMobs
        // -->
        else if (attribute.startsWith("entity")) {
            return new dEntity(getLivingEntity()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <mythicmob@mythicmob.global_cooldown>
        // @returns ElementTag(Number)
        // @description
        // Returns the MythicMob's global cooldown.
        // @Plugin Depenizen, MythicMobs
        // -->
        else if (attribute.startsWith("global_cooldown")) {
            return new DurationTag(mob.getGlobalCooldown()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <mythicmob@mythicmob.type>
        // @returns ElementTag
        // @description
        // Always returns 'Mythic Mob' for MythicMob objects. All objects fetchable by the Object Fetcher will return the
        // type of object that is fulfilling this attribute.
        // @Plugin Depenizen, MythicMobs
        // -->
        else if (attribute.startsWith("type")) {
            return new ElementTag("Mythic Mob").getAttribute(attribute.fulfill(1));
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
        // @object MythicMob
        // @name global_cooldown
        // @input Element(Number)
        // @description
        // Sets global cooldown of the MythicMob.
        // @tags
        // <mythicmob@mythicmob.global_cooldown>
        // -->
        if (mechanism.matches("global_cooldown") && mechanism.requireInteger()) {
            mob.setGlobalCooldown(mechanism.getValue().asInt());
        }

        // <--[mechanism]
        // @object MythicMob
        // @name reset_target
        // @input None
        // @description
        // Reset the MythicMob's target.
        // @tags
        // <mythicmob@mythicmob.target>
        // -->
        else if (mechanism.matches("reset_target")) {
            mob.resetTarget();
        }

        // <--[mechanism]
        // @object MythicMob
        // @name target
        // @input dEntity
        // @description
        // Sets MythicMob's target.
        // @tags
        // <mythicmob@mythicmob.target>
        // -->
        else if (mechanism.matches("target") && mechanism.requireObject(dEntity.class)) {
            dEntity mTarget = dEntity.valueOf(mechanism.getValue().asString());
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
