package com.denizenscript.depenizen.bukkit.properties.terraincontrol;

import net.aufdemrand.denizencore.objects.properties.Property;
import net.aufdemrand.denizencore.objects.Mechanism;
import com.khorn.terraincontrol.LocalBiome;
import com.khorn.terraincontrol.TerrainControl;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;

public class TCLocationProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "TCLocation";
    }

    @Override
    public void adjust(Mechanism mechanism) {
        // None
    }

    public static boolean describes(dObject object) {
        return object instanceof dLocation;
    }

    public static TCLocationProperties getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new TCLocationProperties((dLocation) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "tc_biome"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    private TCLocationProperties(dLocation object) {
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
            // @Plugin Depenizen, TerrainControl
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
                // @Plugin Depenizen, TerrainControl
                // -->
                return new Element(biome.getTemperatureAt(location.getBlockX(), location.getBlockY(), location.getBlockZ()))
                        .getAttribute(attribute.fulfill(1));
            }
        }

        return null;

    }
}
