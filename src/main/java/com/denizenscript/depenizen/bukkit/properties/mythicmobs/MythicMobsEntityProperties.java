package com.denizenscript.depenizen.bukkit.properties.mythicmobs;

import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.depenizen.bukkit.bridges.MythicMobsBridge;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.depenizen.bukkit.objects.mythicmobs.MythicMobsMobTag;
import io.lumine.mythic.api.skills.placeholders.PlaceholderString;
import io.lumine.mythic.bukkit.BukkitAdapter;

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

    public static void registerTags() {

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

        // <--[tag]
        // @attribute <EntityTag.parse_mythic[<placeholder_text>]>
        // @returns ElementTag
        // @plugin Depenizen MythicMobs
        // @description
        // Parses Mythic placeholders and returns the result. The target scope is set as the entity. (For example, <target.name> returns the entity's name.) Keep in mind that <> will have to be escaped using tags. This is usually over-engineering, consider using other Denizen tags instead.
        // -->
        PropertyParser.registerTag(MythicMobsEntityProperties.class, ElementTag.class, "parse_mythic", (attribute, object) -> {
            if (!attribute.hasParam()) {
                Debug.echoError("Placeholder text is required!");
                return null;
            }
            return new ElementTag(PlaceholderString.of(attribute.getParam()).get(BukkitAdapter.adapt(object.entity.getBukkitEntity())));
        }, "parse_mythicmob");
    }

    public boolean isMythicMob() {
        return MythicMobsBridge.isMythicMob(entity.getBukkitEntity());
    }

    public MythicMobsMobTag getMythicMob() {
        return new MythicMobsMobTag(MythicMobsBridge.getActiveMob(entity.getBukkitEntity()));
    }
}
