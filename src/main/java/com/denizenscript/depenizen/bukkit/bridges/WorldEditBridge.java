package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizen.objects.CuboidTag;
import com.denizenscript.denizen.objects.EllipsoidTag;
import com.denizenscript.denizen.objects.PolygonTag;
import com.denizenscript.denizencore.DenizenCore;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.commands.worldedit.WorldEditCommand;
import com.denizenscript.depenizen.bukkit.properties.worldedit.WorldEditAreaContainmentExtensions;
import com.denizenscript.depenizen.bukkit.properties.worldedit.WorldEditPlayerExtensions;

public class WorldEditBridge extends Bridge {

    public static WorldEditBridge instance;

    @Override
    public void init() {
        instance = this;
        DenizenCore.commandRegistry.registerCommand(WorldEditCommand.class);
        WorldEditAreaContainmentExtensions.register(CuboidTag.class, CuboidTag.tagProcessor);
        WorldEditAreaContainmentExtensions.register(EllipsoidTag.class, EllipsoidTag.tagProcessor);
        WorldEditAreaContainmentExtensions.register(PolygonTag.class, PolygonTag.tagProcessor);
        WorldEditPlayerExtensions.register();
    }
}
