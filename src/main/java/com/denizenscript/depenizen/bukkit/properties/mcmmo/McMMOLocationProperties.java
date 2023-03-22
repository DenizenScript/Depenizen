package com.denizenscript.depenizen.bukkit.properties.mcmmo;

import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.tags.Attribute;
import com.gmail.nossr50.mcMMO;

public class McMMOLocationProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "McMMOLocation";
    }

    @Override
    public void adjust(Mechanism mechanism) {
        // None
    }

    public static boolean describes(ObjectTag object) {
        return object instanceof LocationTag;
    }

    public static McMMOLocationProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new McMMOLocationProperties((LocationTag) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "mcmmo"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    public McMMOLocationProperties(LocationTag location) {
        this.location = location;
    }

    LocationTag location;

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {

        if (attribute.startsWith("mcmmo")) {

            attribute = attribute.fulfill(1);

            // <--[tag]
            // @attribute <LocationTag.mcmmo.is_placed>
            // @returns ElementTag(Boolean)
            // @plugin Depenizen, mcMMO
            // @description
            // Returns whether the location is tracked by McMMO as a player-placed block (might only apply to certain block types).
            // -->
            if (attribute.startsWith("is_placed")) {
                return new ElementTag(mcMMO.getPlaceStore().isTrue(location.getBlock()))
                        .getObjectAttribute(attribute.fulfill(1));
            }
        }

        return null;
    }
}
