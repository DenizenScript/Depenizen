package net.gnomeffinway.depenizen.support.plugins;

import net.aufdemrand.denizen.objects.dCuboid;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.objects.dWorld;
import net.aufdemrand.denizencore.tags.Attribute;
import net.gnomeffinway.depenizen.commands.worldguard.RegionCommand;
import net.gnomeffinway.depenizen.extensions.worldguard.WorldGuardCuboidExtension;
import net.gnomeffinway.depenizen.extensions.worldguard.WorldGuardLocationExtension;
import net.gnomeffinway.depenizen.extensions.worldguard.WorldGuardWorldExtension;
import net.gnomeffinway.depenizen.objects.worldguard.WorldGuardRegion;
import net.gnomeffinway.depenizen.support.Support;

public class WorldGuardSupport extends Support {

    public WorldGuardSupport() {
        registerObjects(WorldGuardRegion.class);
        registerProperty(WorldGuardLocationExtension.class, dLocation.class);
        registerProperty(WorldGuardCuboidExtension.class, dCuboid.class);
        registerProperty(WorldGuardWorldExtension.class, dWorld.class);
        new RegionCommand().activate().as("REGION").withOptions("see documentation", 2);
        registerAdditionalTags("region");
    }

    @Override
    public String additionalTags(Attribute attribute) {
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
