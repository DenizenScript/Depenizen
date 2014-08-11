package net.gnomeffinway.depenizen.support.plugins;

import net.aufdemrand.denizen.objects.dLocation;
import net.gnomeffinway.depenizen.commands.worldguard.RegionCommand;
import net.gnomeffinway.depenizen.support.Support;
import net.gnomeffinway.depenizen.tags.worldguard.WorldGuardLocationTags;

public class WorldGuardSupport extends Support {

    public WorldGuardSupport() {
        registerProperty(WorldGuardLocationTags.class, dLocation.class);
        new RegionCommand().activate().as("REGION").withOptions("see documentation", 2);
    }

}
