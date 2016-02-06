package net.gnomeffinway.depenizen.objects.MythicMobs;

import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizencore.objects.*;
import net.aufdemrand.denizencore.tags.Attribute;
import net.aufdemrand.denizencore.tags.TagContext;
import net.aufdemrand.denizencore.utilities.debugging.dB;
import net.elseland.xikage.MythicLib.Adapters.AbstractEntity;
import net.elseland.xikage.MythicLib.Adapters.Bukkit.BukkitEntity;
import net.elseland.xikage.MythicMobs.API.IMobsAPI;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;
import net.elseland.xikage.MythicMobs.MythicMobs;
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
        string = string.replace("e@", "").replace("mythicmob@", "");
        UUID uuid;
        try {
            uuid = UUID.fromString(string);
        }
        catch (Exception e) {
            return null;
        }
        IMobsAPI api = MythicMobs.inst().getAPI().getMobAPI();
        if (!api.isMythicMob(uuid)) {
            return null;
        }
        return new MythicMobsMob(api.getMythicMobInstance(dEntity.getEntityForID(uuid)));
    }

    public static boolean matches(String string) {
        return valueOf(string) != null;
    }

    public MythicMobsMob(ActiveMob mob) {
        if (mob != null) {
            this.mob = mob;
        }
        else {
            dB.echoError("ActiveMob referenced is null!");
        }
    }

    public ActiveMob getMob() {
        return mob;
    }

    public LivingEntity getLivingEntity() {
        return mob.getLivingEntity();
    }

    public Entity getEntity() {
        return mob.getEntity().getBukkitEntity();
    }

    String prefix;
    ActiveMob mob;

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
        return null;
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
        return prefix + "='<A>" + identify() + "<G>' ";
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
        // @attribute <mythicmob@mythicmob.level>
        // @returns Element(Number)
        // @description
        // Returns the level of the MythicMob.
        // @plugin Depenizen, MythicMobs
        // -->
        if (attribute.startsWith("level")) {
            return new Element(mob.getLevel()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <mythicmob@mythicmob.players_killed>
        // @returns Element(Number)
        // @description
        // Returns the number of players the MythicMob has killed.
        // NOTE: Untested and cannot guarantee accuracy of results.
        // @plugin Depenizen, MythicMobs
        // -->
        else if (attribute.startsWith("players_killed")) {
            return new Element(mob.getPlayerKills()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <mythicmob@mythicmob.damage>
        // @returns Element(Decimal)
        // @description
        // Returns the damage the MythicMob deals.
        // @plugin Depenizen, MythicMobs
        // -->
        else if (attribute.startsWith("damage")) {
            return new Element(mob.getDamage()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <mythicmob@mythicmob.armor>
        // @returns Element(Decimal)
        // @description
        // Returns the armor the MythicMob has.
        // @plugin Depenizen, MythicMobs
        // -->
        else if (attribute.startsWith("armor") || attribute.startsWith("armour")) {
            return new Element(mob.getArmor()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <mythicmob@mythicmob.has_target>
        // @returns Element(Boolean)
        // @description
        // Returns whether the MythicMob has a target.
        // @plugin Depenizen, MythicMobs
        // -->
        else if (attribute.startsWith("has_target")) {
            return new Element(mob.hasTarget()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <mythicmob@mythicmob.target>
        // @returns dEntity
        // @description
        // Returns the MythicMob's target.
        // @plugin Depenizen, MythicMobs
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
        // @plugin Depenizen, MythicMobs
        // -->
        else if (attribute.startsWith("is_damaging")) {
            return new Element(mob.isUsingDamageSkill()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <mythicmob@mythicmob.entity>
        // @returns dEntity
        // @description
        // Returns the dEntity for the MythicMob.
        // @plugin Depenizen, MythicMobs
        // -->
        else if (attribute.startsWith("entity")) {
            return new dEntity(getLivingEntity()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <mythicmob@mythicmob.global_cooldown>
        // @returns Element(Number)
        // @description
        // Returns the MythicMob's global cooldown.
        // @plugin Depenizen, MythicMobs
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
        // @plugin Depenizen, MythicMobs
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

    }

    @Override
    public void applyProperty(Mechanism mechanism) {
        dB.echoError("Cannot apply properties to a MythicMob!");
    }

}
