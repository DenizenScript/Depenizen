package net.gnomeffinway.depenizen.support.plugins;

import net.aufdemrand.denizen.objects.dLocation;
import net.gnomeffinway.depenizen.commands.worldguard.RegionCommand;
import net.gnomeffinway.depenizen.objects.worldguard.WorldGuardRegion;
import net.gnomeffinway.depenizen.support.Support;
import net.gnomeffinway.depenizen.extensions.worldguard.WorldGuardLocationTags;

public class WorldGuardSupport extends Support {

    public WorldGuardSupport() {
        registerObjects(WorldGuardRegion.class);
        registerProperty(WorldGuardLocationTags.class, dLocation.class);
        new RegionCommand().activate().as("REGION").withOptions("see documentation", 2);
    }

}
