package com.denizenscript.depenizen.bukkit.support.plugins;

import com.denizenscript.depenizen.bukkit.support.Support;
import net.aufdemrand.denizen.objects.dPlayer;
import com.denizenscript.depenizen.bukkit.extensions.worldedit.WorldEditPlayerExtension;

public class WorldEditSupport extends Support {

    public WorldEditSupport() {
        registerProperty(WorldEditPlayerExtension.class, dPlayer.class);
        // Removed schematic command in favour of Denizen's schematic command.
    }
}
