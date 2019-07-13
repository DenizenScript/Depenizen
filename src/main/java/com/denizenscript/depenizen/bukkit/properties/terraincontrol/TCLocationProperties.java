package com.denizenscript.depenizen.bukkit.properties.terraincontrol;

import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.khorn.terraincontrol.LocalBiome;
import com.khorn.terraincontrol.TerrainControl;
import com.denizenscript.denizen.objects.dLocation;
import com.denizenscript.denizencore.objects.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;

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

    public static boolean describes(ObjectTag object) {
        return object instanceof dLocation;
    }

    public static TCLocationProperties getFrom(ObjectTag object) {
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
            // @returns ElementTag
            // @description
            // Returns the TerrainControl biome name at this location, if any.
            // @Plugin Depenizen, TerrainControl
            // -->
            if (attribute.startsWith("name")) {
                return new ElementTag(biome.getName()).getAttribute(attribute.fulfill(1));
            }

            else if (attribute.startsWith("temperature")) {
                // <--[tag]
                // @attribute <l@location.tc_biome.temperature>
                // @returns ElementTag
                // @description
                // Returns the TerrainControl biome temperature at this location, if any.
                // @Plugin Depenizen, TerrainControl
                // -->
                return new ElementTag(biome.getTemperatureAt(location.getBlockX(), location.getBlockY(), location.getBlockZ()))
                        .getAttribute(attribute.fulfill(1));
            }
        }

        return null;

    }
}
