package com.denizenscript.depenizen.bukkit.bridges;
import com.denizenscript.denizen.Denizen;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.depenizen.bukkit.commands.worldedit.WorldEditCommand;
import com.denizenscript.depenizen.bukkit.properties.worldedit.WorldEditPlayerProperties;
import com.denizenscript.denizencore.objects.properties.PropertyParser;

public class WorldEditBridge extends Bridge {

    public static WorldEditBridge instance;

    @Override
    public void init() {
        instance = this;
        Denizen.getInstance().commandRegistry.registerCommand(WorldEditCommand.class);
        PropertyParser.registerProperty(WorldEditPlayerProperties.class, PlayerTag.class);
    }
}
