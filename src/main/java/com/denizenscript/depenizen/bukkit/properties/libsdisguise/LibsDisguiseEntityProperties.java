package com.denizenscript.depenizen.bukkit.properties.libsdisguise;

import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.depenizen.bukkit.objects.libsdisguises.LibsDisguise;
import me.libraryaddict.disguise.DisguiseAPI;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;

public class LibsDisguiseEntityProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "LibsDisguiseEntity";
    }

    @Override
    public void adjust(Mechanism mechanism) {
        // None
    }
    public static boolean describes(ObjectTag object) {
        return object instanceof EntityTag;
    }

    public static LibsDisguiseEntityProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new LibsDisguiseEntityProperties((EntityTag) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "is_disguised", "disguise"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    public LibsDisguiseEntityProperties(EntityTag entity) {
        this.entity = entity;
    }

    EntityTag entity;

    @Override
    public String getAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        // <--[tag]
        // @attribute <EntityTag.is_disguised>
        // @returns ElementTag(Boolean)
        // @description
        // Returns whether the entity is in a disguise.
        // @Plugin Depenizen, LibsDisguises
        // -->
        if (attribute.startsWith("is_disguised") || attribute.startsWith("is_disguise")) {
            return new ElementTag(DisguiseAPI.isDisguised(entity.getBukkitEntity())).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <EntityTag.disguise>
        // @returns dlibsdisguise
        // @description
        // Returns the disguise of the entity.
        // @Plugin Depenizen, LibsDisguises
        // -->
        if (attribute.startsWith("disguise")
                && DisguiseAPI.isDisguised(entity.getBukkitEntity())) {
            return new LibsDisguise(DisguiseAPI.getDisguise(entity.getBukkitEntity())).getAttribute(attribute.fulfill(1));
        }

        return null;
    }
}
