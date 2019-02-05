package com.denizenscript.depenizen.bukkit.extensions.libsdisguise;

import com.denizenscript.depenizen.bukkit.extensions.dObjectExtension;
import com.denizenscript.depenizen.bukkit.objects.libsdisguises.LibsDisguise;
import me.libraryaddict.disguise.DisguiseAPI;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;

public class LibsDisguiseEntityExtension extends dObjectExtension {
    public static boolean describes(dObject object) {
        return object instanceof dEntity;
    }

    public static LibsDisguiseEntityExtension getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new LibsDisguiseEntityExtension((dEntity) object);
        }
    }

    public static final String[] handledTags = new String[]{
            "is_disguised", "disguise"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    public LibsDisguiseEntityExtension(dEntity entity) {
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
        // @Plugin DepenizenBukkit, LibsDisguises
        // -->
        if (attribute.startsWith("is_disguised") || attribute.startsWith("is_disguise")) {
            return new Element(DisguiseAPI.isDisguised(entity.getBukkitEntity())).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <e@entity.disguise>
        // @returns dlibsdisguise
        // @description
        // Returns the disguise of the entity.
        // @Plugin DepenizenBukkit, LibsDisguises
        // -->
        if (attribute.startsWith("disguise")
                && DisguiseAPI.isDisguised(entity.getBukkitEntity())) {
            return new LibsDisguise(DisguiseAPI.getDisguise(entity.getBukkitEntity())).getAttribute(attribute.fulfill(1));
        }

        return null;
    }
}
