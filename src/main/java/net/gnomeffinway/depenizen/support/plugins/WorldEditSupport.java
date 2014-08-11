package net.gnomeffinway.depenizen.support.plugins;

import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.utilities.DenizenAPI;
import net.gnomeffinway.depenizen.Depenizen;
import net.gnomeffinway.depenizen.commands.worldedit.SchematicCommand;
import net.gnomeffinway.depenizen.support.Support;
import net.gnomeffinway.depenizen.tags.worldedit.WorldEditPlayerTags;

public class WorldEditSupport extends Support {

    public WorldEditSupport() {
        registerProperty(WorldEditPlayerTags.class, dPlayer.class);
        new SchematicCommand().activate().as("SCHEMATIC").withOptions("schematic [create/load/unload/rotate/paste/save] [name:<name>] (angle:<#>) (<location>) (<cuboid>) (noair)", 2);
    }

}
