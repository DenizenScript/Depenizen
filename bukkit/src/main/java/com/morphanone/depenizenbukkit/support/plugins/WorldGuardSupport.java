package com.morphanone.depenizenbukkit.support.plugins;

import com.morphanone.depenizenbukkit.commands.worldguard.RegionCommand;
import com.morphanone.depenizenbukkit.extensions.worldguard.WorldGuardCuboidExtension;
import net.aufdemrand.denizen.objects.dCuboid;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.objects.dWorld;
import net.aufdemrand.denizencore.tags.TagContext;
import net.aufdemrand.denizencore.tags.Attribute;
import com.morphanone.depenizenbukkit.extensions.worldguard.WorldGuardLocationExtension;
import com.morphanone.depenizenbukkit.extensions.worldguard.WorldGuardWorldExtension;
import com.morphanone.depenizenbukkit.objects.worldguard.WorldGuardRegion;
import com.morphanone.depenizenbukkit.support.Support;

public class WorldGuardSupport extends Support {

    public WorldGuardSupport() {
        registerObjects(WorldGuardRegion.class);
        registerProperty(WorldGuardLocationExtension.class, dLocation.class);
        registerProperty(WorldGuardCuboidExtension.class, dCuboid.class);
        registerProperty(WorldGuardWorldExtension.class, dWorld.class);
        new RegionCommand().activate().as("REGION").withOptions("See Documentation.", 2);
        registerAdditionalTags("region");
    }

    @Override
    public String additionalTags(Attribute attribute, TagContext tagContext) {
        if (attribute == null) {
            return null;
        }
        if (attribute.startsWith("region") && attribute.hasContext(1)) {
            WorldGuardRegion region = WorldGuardRegion.valueOf(attribute.getContext(1));
            if (region == null) {
                return null;
            }
            return region.getAttribute(attribute.fulfill(1));
        }
        return null;
    }

}
