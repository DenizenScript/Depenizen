package com.denizenscript.depenizen.bukkit.properties.mcmmo;

import com.gmail.nossr50.mcMMO;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.Mechanism;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.objects.properties.Property;
import net.aufdemrand.denizencore.tags.Attribute;

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

    public static boolean describes(dObject object) {
        return object instanceof dEntity;
    }

    public static McMMOEntityProperties getFrom(dObject object) {
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
            // @returns Element(Boolean)
            // @description
            // Returns whether the entity is tracked by McMMO as a 'spawned' mob (one from a spawner block or spawn egg).
            // @Plugin Depenizen, mcMMO
            // -->
            if (attribute.startsWith("is_spawned_mob")) {
                return new Element(entity.getBukkitEntity().hasMetadata(mcMMO.entityMetadataKey))
                        .getAttribute(attribute.fulfill(1));
            }
        }

        return null;
    }
}
