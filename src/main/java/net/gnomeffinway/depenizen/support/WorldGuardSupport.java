package net.gnomeffinway.depenizen.support;

import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.utilities.DenizenAPI;
import net.gnomeffinway.depenizen.Depenizen;
import net.gnomeffinway.depenizen.commands.worldguard.RegionCommand;
import net.gnomeffinway.depenizen.tags.worldguard.WorldGuardLocationTags;

public class WorldGuardSupport {

    private Depenizen depenizen;

    public WorldGuardSupport(Depenizen depenizen) {
        this.depenizen = depenizen;
    }

    public void register() {
        DenizenAPI.getCurrentInstance().getPropertyParser().registerProperty(WorldGuardLocationTags.class, dLocation.class);
        new RegionCommand().activate().as("REGION").withOptions("see documentation", 2);
    }

}
