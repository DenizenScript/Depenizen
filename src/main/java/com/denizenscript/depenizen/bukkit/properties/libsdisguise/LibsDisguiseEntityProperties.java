package com.denizenscript.depenizen.bukkit.properties.libsdisguise;

import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.depenizen.bukkit.objects.libsdisguises.LibsDisguise;
import me.libraryaddict.disguise.DisguiseAPI;
import com.denizenscript.denizen.objects.dEntity;
import com.denizenscript.denizencore.objects.Element;
import com.denizenscript.denizencore.objects.dObject;
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
    public static boolean describes(dObject object) {
        return object instanceof dEntity;
    }

    public static LibsDisguiseEntityProperties getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new LibsDisguiseEntityProperties((dEntity) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "is_disguised", "disguise"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    public LibsDisguiseEntityProperties(dEntity entity) {
        this.entity = entity;
    }

    dEntity entity;

    @Override
    public String getAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        // <--[tag]
        // @attribute <e@entity.is_disguised>
        // @returns Element(Boolean)
        // @description
        // Returns whether the entity is in a disguise.
        // @Plugin Depenizen, LibsDisguises
        // -->
        if (attribute.startsWith("is_disguised") || attribute.startsWith("is_disguise")) {
            return new Element(DisguiseAPI.isDisguised(entity.getBukkitEntity())).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <e@entity.disguise>
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
