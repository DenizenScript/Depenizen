package com.denizenscript.depenizen.bukkit.properties.askyblock;

import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.wasteofplastic.askyblock.ASkyBlockAPI;
import net.aufdemrand.denizen.objects.dWorld;
import com.denizenscript.denizencore.objects.Element;
import com.denizenscript.denizencore.objects.dObject;
import com.denizenscript.denizencore.tags.Attribute;

public class ASkyBlockWorldProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "ASkyBlockWorld";
    }

    @Override
    public void adjust(Mechanism mechanism) {
        // None
    }

    public static boolean describes(dObject object) {
        return object instanceof dWorld;
    }

    public static ASkyBlockWorldProperties getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new ASkyBlockWorldProperties((dWorld) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "is_skyblock_world"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    public ASkyBlockWorldProperties(dWorld world) {
        this.world = world;
    }

    dWorld world;
    ASkyBlockAPI api = ASkyBlockAPI.getInstance();

    @Override
    public String getAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        // <--[tag]
        // @attribute <w@world.is_skyblock_world>
        // @returns Element(Boolean)
        // @description
        // Returns whether the world is used by A SkyBlock.
        // @Plugin Depenizen, A SkyBlock
        // -->
        if (attribute.startsWith("is_skyblock_world")) {
            return new Element(api.getIslandWorld() == world.getWorld() ||
                    api.getNetherWorld() == world.getWorld()).getAttribute(attribute.fulfill(1));
        }

        return null;
    }
}
