package net.gnomeffinway.depenizen.support.plugins;

import net.aufdemrand.denizen.objects.dPlayer;
import net.gnomeffinway.depenizen.commands.worldedit.SchematicCommand;
import net.gnomeffinway.depenizen.support.Support;
import net.gnomeffinway.depenizen.extensions.worldedit.WorldEditPlayerExtension;

public class WorldEditSupport extends Support {

    public WorldEditSupport() {
        registerProperty(WorldEditPlayerExtension.class, dPlayer.class);
        new SchematicCommand().activate().as("SCHEMATIC").withOptions("schematic [create/load/unload/rotate/paste/save] [name:<name>] (angle:<#>) (<location>) (<cuboid>) (noair)", 2);
    }
}
