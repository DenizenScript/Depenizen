package com.denizenscript.depenizen.bukkit.properties.mcmmo;

import com.gmail.nossr50.mcMMO;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.tags.Attribute;
import com.gmail.nossr50.util.compat.layers.persistentdata.MobMetaFlagType;

public class McMMOEntityProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "McMMOEntity";
    }

    @Override
    public void adjust(Mechanism mechanism) {
        // None
    }

    public static boolean describes(ObjectTag object) {
        return object instanceof EntityTag;
    }

    public static McMMOEntityProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new McMMOEntityProperties((EntityTag) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "mcmmo"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    public McMMOEntityProperties(EntityTag entity) {
        this.entity = entity;
    }

    EntityTag entity;

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {

        if (attribute.startsWith("mcmmo")) {

            attribute = attribute.fulfill(1);

            // <--[tag]
            // @attribute <EntityTag.mcmmo.is_spawned_mob>
            // @returns ElementTag(Boolean)
            // @plugin Depenizen, mcMMO
            // @description
            // Returns whether the entity is tracked by McMMO as a 'spawned' mob (one from a spawner block or spawn egg).
            // -->
            if (attribute.startsWith("is_spawned_mob")) {
                return new ElementTag(mcMMO.getCompatibilityManager().getPersistentDataLayer().hasMobFlag(MobMetaFlagType.MOB_SPAWNER_MOB, entity.getLivingEntity()))
                        .getObjectAttribute(attribute.fulfill(1));
            }
        }

        return null;
    }
}
