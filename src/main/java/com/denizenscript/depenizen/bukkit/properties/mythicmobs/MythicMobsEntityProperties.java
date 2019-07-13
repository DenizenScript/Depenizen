package com.denizenscript.depenizen.bukkit.properties.mythicmobs;

import com.denizenscript.depenizen.bukkit.bridges.MythicMobsBridge;
import com.denizenscript.denizen.objects.dEntity;
import com.denizenscript.denizencore.objects.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.depenizen.bukkit.objects.mythicmobs.MythicMobsMob;

public class MythicMobsEntityProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "MythicMobsEntity";
    }

    @Override
    public void adjust(Mechanism mechanism) {
        // None
    }

    public static boolean describes(ObjectTag object) {
        return object instanceof dEntity;
    }

    public static MythicMobsEntityProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new MythicMobsEntityProperties((dEntity) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "is_mythicmob", "mythicmob"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    public MythicMobsEntityProperties(dEntity entity) {
        this.entity = entity;
    }

    dEntity entity;

    @Override
    public String getAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        // <--[tag]
        // @attribute <e@entity.is_mythicmob>
        // @returns ElementTag(Boolean)
        // @description
        // Returns whether the entity is a MythicMob.
        // @Plugin Depenizen, MythicMobs
        // -->
        if (attribute.startsWith("is_mythic_mob") || attribute.startsWith("is_mythicmob")) {
            return new ElementTag(MythicMobsBridge.isMythicMob(entity.getBukkitEntity())).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <e@entity.mythicmob>
        // @returns MythicMobsMob
        // @description
        // Returns the MythicMob for this entity.
        // @Plugin Depenizen, MythicMobs
        // -->
        else if ((attribute.startsWith("mythicmob") || attribute.startsWith("mythic_mob"))
                && MythicMobsBridge.isMythicMob(entity.getBukkitEntity())) {
            return new MythicMobsMob(MythicMobsBridge.getActiveMob(entity.getBukkitEntity()))
                    .getAttribute(attribute.fulfill(1));
        }

        return null;
    }
}
