package com.denizenscript.depenizen.bukkit.properties.mcmmo;

import com.gmail.nossr50.mcMMO;
import com.denizenscript.denizen.objects.dEntity;
import com.denizenscript.denizencore.objects.ElementTag;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.tags.Attribute;

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
        return object instanceof dEntity;
    }

    public static McMMOEntityProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new McMMOEntityProperties((dEntity) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "mcmmo"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    private McMMOEntityProperties(dEntity entity) {
        this.entity = entity;
    }

    dEntity entity = null;

    @Override
    public String getAttribute(Attribute attribute) {

        if (attribute.startsWith("mcmmo")) {

            attribute = attribute.fulfill(1);

            // <--[tag]
            // @attribute <e@entity.mcmmo.is_spawned_mob>
            // @returns ElementTag(Boolean)
            // @description
            // Returns whether the entity is tracked by McMMO as a 'spawned' mob (one from a spawner block or spawn egg).
            // @Plugin Depenizen, mcMMO
            // -->
            if (attribute.startsWith("is_spawned_mob")) {
                return new ElementTag(entity.getBukkitEntity().hasMetadata(mcMMO.entityMetadataKey))
                        .getAttribute(attribute.fulfill(1));
            }
        }

        return null;
    }
}
