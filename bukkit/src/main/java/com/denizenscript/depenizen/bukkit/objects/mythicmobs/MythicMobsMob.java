package com.denizenscript.depenizen.bukkit.objects.mythicmobs;

import com.denizenscript.depenizen.bukkit.support.plugins.MythicMobsSupport;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizencore.objects.*;
import net.aufdemrand.denizencore.objects.properties.Property;
import net.aufdemrand.denizencore.objects.properties.PropertyParser;
import net.aufdemrand.denizencore.tags.Attribute;
import net.aufdemrand.denizencore.tags.TagContext;
import net.aufdemrand.denizencore.utilities.debugging.dB;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitEntity;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.UUID;

public class MythicMobsMob implements dObject, Adjustable {

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
            if (!MythicMobsSupport.isMythicMob(uuid)) {
                return null;
            }
            return new MythicMobsMob(MythicMobsSupport.getActiveMob(dEntity.getEntityForID(uuid)));
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
            dB.echoError("ActiveMob referenced is null!");
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
    public dObject setPrefix(String prefix) {
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
    public String getAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        // <--[tag]
        // @attribute <mythicmob@mythicmob.internal_name>
        // @returns Element
        // @description
        // Returns the name MythicMobs identifies the MythicMob with.
        // @Plugin DepenizenBukkit, MythicMobs
        // -->
        if (attribute.startsWith("internal_name")) {
            return new Element(mobType.getInternalName()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <mythicmob@mythicmob.display_name>
        // @returns Element
        // @description
        // Returns the display name of the MythicMob.
        // @Plugin DepenizenBukkit, MythicMobs
        // -->
        else if (attribute.startsWith("display_name")) {
            return new Element(mobType.getDisplayName()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <mythicmob@mythicmob.level>
        // @returns Element(Number)
        // @description
        // Returns the level of the MythicMob.
        // @Plugin DepenizenBukkit, MythicMobs
        // -->
        if (attribute.startsWith("level")) {
            return new Element(mob.getLevel()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <mythicmob@mythicmob.players_killed>
        // @returns Element(Number)
        // @description
        // Returns the number of players the MythicMob has killed.
        // @Plugin DepenizenBukkit, MythicMobs
        // -->
        else if (attribute.startsWith("players_killed")) {
            return new Element(mob.getPlayerKills()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <mythicmob@mythicmob.damage>
        // @returns Element(Decimal)
        // @description
        // Returns the damage the MythicMob deals.
        // @Plugin DepenizenBukkit, MythicMobs
        // -->
        else if (attribute.startsWith("damage")) {
            return new Element(mob.getDamage()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <mythicmob@mythicmob.armor>
        // @returns Element(Decimal)
        // @description
        // Returns the armor the MythicMob has.
        // @Plugin DepenizenBukkit, MythicMobs
        // -->
        else if (attribute.startsWith("armor") || attribute.startsWith("armour")) {
            return new Element(mob.getArmor()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <mythicmob@mythicmob.has_target>
        // @returns Element(Boolean)
        // @description
        // Returns whether the MythicMob has a target.
        // @Plugin DepenizenBukkit, MythicMobs
        // -->
        else if (attribute.startsWith("has_target")) {
            return new Element(mob.hasTarget()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <mythicmob@mythicmob.target>
        // @returns dEntity
        // @description
        // Returns the MythicMob's target.
        // @Plugin DepenizenBukkit, MythicMobs
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
        // @returns Element(Boolean)
        // @description
        // Returns whether the MythicMob is using its damaging skill.
        // @Plugin DepenizenBukkit, MythicMobs
        // -->
        else if (attribute.startsWith("is_damaging")) {
            return new Element(mob.isUsingDamageSkill()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <mythicmob@mythicmob.entity>
        // @returns dEntity
        // @description
        // Returns the dEntity for the MythicMob.
        // @Plugin DepenizenBukkit, MythicMobs
        // -->
        else if (attribute.startsWith("entity")) {
            return new dEntity(getLivingEntity()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <mythicmob@mythicmob.global_cooldown>
        // @returns Element(Number)
        // @description
        // Returns the MythicMob's global cooldown.
        // @Plugin DepenizenBukkit, MythicMobs
        // -->
        else if (attribute.startsWith("global_cooldown")) {
            return new Duration(mob.getGlobalCooldown()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <mythicmob@mythicmob.type>
        // @returns Element
        // @description
        // Always returns 'Mythic Mob' for MythicMob objects. All objects fetchable by the Object Fetcher will return the
        // type of object that is fulfilling this attribute.
        // @Plugin DepenizenBukkit, MythicMobs
        // -->
        else if (attribute.startsWith("type")) {
            return new Element("Mythic Mob").getAttribute(attribute.fulfill(1));
        }

        return new Element(identify()).getAttribute(attribute);
    }

    @Override
    public void adjust(Mechanism mechanism) {
        Element value = mechanism.getValue();

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
            mob.setGlobalCooldown(value.asInt());
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
            dEntity mTarget = dEntity.valueOf(value.asString());
            if (mTarget == null || !mTarget.isValid() || mTarget.getLivingEntity() == null) {
                return;
            }
            BukkitEntity target = new BukkitEntity(mTarget.getBukkitEntity());
            mob.setTarget(target);
        }

        // Iterate through this object's properties' mechanisms
        for (Property property : PropertyParser.getProperties(this)) {
            property.adjust(mechanism);
            if (mechanism.fulfilled()) {
                break;
            }
        }

        if (!mechanism.fulfilled()) {
            mechanism.reportInvalid();
        }

    }

    @Override
    public void applyProperty(Mechanism mechanism) {
        dB.echoError("Cannot apply properties to a MythicMob!");
    }

}
