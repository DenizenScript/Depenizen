package com.denizenscript.depenizen.bukkit.support.plugins;

import com.denizenscript.depenizen.bukkit.commands.worldguard.RegionCommand;
import com.denizenscript.depenizen.bukkit.extensions.worldguard.WorldGuardCuboidExtension;
import com.denizenscript.depenizen.bukkit.extensions.worldguard.WorldGuardWorldExtension;
import com.denizenscript.depenizen.bukkit.support.Support;
import net.aufdemrand.denizen.objects.dCuboid;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.objects.dWorld;
import net.aufdemrand.denizencore.tags.TagContext;
import net.aufdemrand.denizencore.tags.Attribute;
import com.denizenscript.depenizen.bukkit.extensions.worldguard.WorldGuardLocationExtension;
import com.denizenscript.depenizen.bukkit.extensions.worldguard.WorldGuardPlayerExtension;
import com.denizenscript.depenizen.bukkit.objects.worldguard.WorldGuardRegion;

public class WorldGuardSupport extends Support {

    public WorldGuardSupport() {
        registerObjects(WorldGuardRegion.class);
        registerProperty(WorldGuardLocationExtension.class, dLocation.class);
        registerProperty(WorldGuardPlayerExtension.class, dPlayer.class);
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
