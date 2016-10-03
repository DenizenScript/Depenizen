package com.morphanone.depenizenbukkit.extensions.terraincontrol;

import com.khorn.terraincontrol.LocalBiome;
import com.khorn.terraincontrol.TerrainControl;
import com.morphanone.depenizenbukkit.extensions.dObjectExtension;
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

    private TCLocationExtension(dLocation object) {
        location = object;
    }

    dLocation location = null;

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <l@location.tc_biome.name>
        // @returns Element
        // @description
        // Returns the TerrainControl biome name at this location, if any.
        // @plugin Depenizen, TerrainControl
        // -->
        if (attribute.startsWith("tc_biome.name")) {
            LocalBiome biome = TerrainControl.getWorld(location.getWorld().getName()).getBiome(location.getBlockX(), location.getBlockZ());
            return new Element(biome.getName()).getAttribute(attribute.fulfill(2));
        }

        return null;

    }
}
