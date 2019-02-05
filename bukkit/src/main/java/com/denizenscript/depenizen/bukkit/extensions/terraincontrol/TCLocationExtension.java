package com.denizenscript.depenizen.bukkit.extensions.terraincontrol;

import com.denizenscript.depenizen.bukkit.extensions.dObjectExtension;
import com.khorn.terraincontrol.LocalBiome;
import com.khorn.terraincontrol.TerrainControl;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;

public class TCLocationExtension extends dObjectExtension {

    public static boolean describes(dObject object) {
        return object instanceof dLocation;
    }

    public static TCLocationExtension getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new TCLocationExtension((dLocation) object);
        }
    }

    public static final String[] handledTags = new String[]{
            "tc_biome"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    private TCLocationExtension(dLocation object) {
        location = object;
    }

    dLocation location = null;

    @Override
    public String getAttribute(Attribute attribute) {

        if (attribute.startsWith("tc_biome")) {
            attribute = attribute.fulfill(1);
            LocalBiome biome = TerrainControl.getWorld(location.getWorld().getName())
                    .getBiome(location.getBlockX(), location.getBlockZ());

            // <--[tag]
            // @attribute <l@location.tc_biome.name>
            // @returns Element
            // @description
            // Returns the TerrainControl biome name at this location, if any.
            // @Plugin DepenizenBukkit, TerrainControl
            // -->
            if (attribute.startsWith("name")) {
                return new Element(biome.getName()).getAttribute(attribute.fulfill(1));
            }

            else if (attribute.startsWith("temperature")) {
                // <--[tag]
                // @attribute <l@location.tc_biome.temperature>
                // @returns Element
                // @description
                // Returns the TerrainControl biome temperature at this location, if any.
                // @Plugin DepenizenBukkit, TerrainControl
                // -->
                return new Element(biome.getTemperatureAt(location.getBlockX(), location.getBlockY(), location.getBlockZ()))
                        .getAttribute(attribute.fulfill(1));
            }
        }

        return null;

    }
}
