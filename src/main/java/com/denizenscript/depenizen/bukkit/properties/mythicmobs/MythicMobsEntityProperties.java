package com.denizenscript.depenizen.bukkit.properties.mythicmobs;

import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.depenizen.bukkit.bridges.MythicMobsBridge;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
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

    public static final String[] handledMechs = new String[] {
    }; // None

    public MythicMobsEntityProperties(EntityTag entity) {
        this.entity = entity;
    }

    EntityTag entity;

    public static void register() {

        // <--[tag]
        // @attribute <EntityTag.is_mythicmob>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns whether the entity is a MythicMob.
        // -->
        PropertyParser.registerTag(MythicMobsEntityProperties.class, ElementTag.class, "is_mythicmob", (attribute, object) -> {
            return new ElementTag(object.isMythicMob());
        }, "is_mythic_mob");

        // <--[tag]
        // @attribute <EntityTag.mythicmob>
        // @returns MythicMobsMobTag
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns the MythicMob for this entity.
        // -->
        PropertyParser.registerTag(MythicMobsEntityProperties.class, MythicMobsMobTag.class, "mythicmob", (attribute, object) -> {
            if (object.isMythicMob()) {
                return object.getMythicMob();
            }
            return null;
        }, "mythic_mob");
    }

    public boolean isMythicMob() {
        return MythicMobsBridge.isMythicMob(entity.getBukkitEntity());
    }

    public MythicMobsMobTag getMythicMob() {
        return new MythicMobsMobTag(MythicMobsBridge.getActiveMob(entity.getBukkitEntity()));
    }
}
