package com.denizenscript.depenizen.bukkit.properties.mythicmobs;

import com.denizenscript.depenizen.bukkit.bridges.MythicMobsBridge;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;
import net.aufdemrand.denizencore.objects.properties.Property;
import net.aufdemrand.denizencore.objects.Mechanism;
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

    public static boolean describes(dObject object) {
        return object instanceof dEntity;
    }

    public static MythicMobsEntityProperties getFrom(dObject object) {
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
        // @returns Element(Boolean)
        // @description
        // Returns whether the entity is a MythicMob.
        // @Plugin Depenizen, MythicMobs
        // -->
        if (attribute.startsWith("is_mythic_mob") || attribute.startsWith("is_mythicmob")) {
            return new Element(MythicMobsBridge.isMythicMob(entity.getBukkitEntity())).getAttribute(attribute.fulfill(1));
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
