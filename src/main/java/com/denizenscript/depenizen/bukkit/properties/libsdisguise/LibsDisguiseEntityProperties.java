package com.denizenscript.depenizen.bukkit.properties.libsdisguise;

import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.depenizen.bukkit.objects.libsdisguises.LibsDisguiseTag;
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
            "libsdisguise_is_disguised", "libsdisguise_disguise"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    public LibsDisguiseEntityProperties(EntityTag entity) {
        this.entity = entity;
    }

    EntityTag entity;

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        // <--[tag]
        // @attribute <EntityTag.libsdisguise_is_disguised>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, LibsDisguises
        // @description
        // Returns whether the entity is in a disguise.
        // -->
        if (attribute.startsWith("libsdisguise_is_disguised")) {
            return new ElementTag(DisguiseAPI.isDisguised(entity.getBukkitEntity())).getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <EntityTag.libsdisguise_disguise>
        // @returns LibsDisguiseTag
        // @plugin Depenizen, LibsDisguises
        // @description
        // Returns the disguise of the entity.
        // -->
        if (attribute.startsWith("libsdisguise_disguise")
                && DisguiseAPI.isDisguised(entity.getBukkitEntity())) {
            return new LibsDisguiseTag(DisguiseAPI.getDisguise(entity.getBukkitEntity())).getObjectAttribute(attribute.fulfill(1));
        }

        return null;
    }
}
