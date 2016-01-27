package net.gnomeffinway.depenizen.support.plugins;

import net.aufdemrand.denizen.objects.dPlayer;
import net.gnomeffinway.depenizen.extensions.worldedit.WorldEditPlayerExtension;
import net.gnomeffinway.depenizen.support.Support;

public class WorldEditSupport extends Support {

    public WorldEditSupport() {
        registerProperty(WorldEditPlayerExtension.class, dPlayer.class);
        // Removed schematic command in favour of Denizen's schematic command.
    }
}
