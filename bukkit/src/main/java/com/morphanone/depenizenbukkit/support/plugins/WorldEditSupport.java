package com.morphanone.depenizenbukkit.support.plugins;

import net.aufdemrand.denizen.objects.dPlayer;
import com.morphanone.depenizenbukkit.extensions.worldedit.WorldEditPlayerExtension;
import com.morphanone.depenizenbukkit.support.Support;

public class WorldEditSupport extends Support {

    public WorldEditSupport() {
        registerProperty(WorldEditPlayerExtension.class, dPlayer.class);
        // Removed schematic command in favour of Denizen's schematic command.
    }
}
