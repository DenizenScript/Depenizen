package com.denizenscript.depenizen.bukkit.properties.mythicmobs;

import com.denizenscript.depenizen.bukkit.bridges.MythicMobsBridge;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.depenizen.bukkit.objects.mythicmobs.MythicMobsMobTag;

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
        return object instanceof EntityTag;
    }

    public static MythicMobsEntityProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new MythicMobsEntityProperties((EntityTag) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "is_mythicmob", "mythicmob"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    public MythicMobsEntityProperties(EntityTag entity) {
        this.entity = entity;
    }

    EntityTag entity;

    @Override
    public String getAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        // <--[tag]
        // @attribute <EntityTag.is_mythicmob>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns whether the entity is a MythicMob.
        // -->
        if (attribute.startsWith("is_mythic_mob") || attribute.startsWith("is_mythicmob")) {
            return new ElementTag(MythicMobsBridge.isMythicMob(entity.getBukkitEntity())).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <EntityTag.mythicmob>
        // @returns MythicMobsMobTag
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns the MythicMob for this entity.
        // -->
        else if ((attribute.startsWith("mythicmob") || attribute.startsWith("mythic_mob"))
                && MythicMobsBridge.isMythicMob(entity.getBukkitEntity())) {
            return new MythicMobsMobTag(MythicMobsBridge.getActiveMob(entity.getBukkitEntity()))
                    .getAttribute(attribute.fulfill(1));
        }

        return null;
    }
}
