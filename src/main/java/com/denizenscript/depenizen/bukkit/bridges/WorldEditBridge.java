package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizen.utilities.DenizenAPI;
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
        DenizenAPI.getCurrentInstance().getCommandRegistry().registerCoreMember(WorldEditCommand.class,
                "WORLDEDIT", "worldedit [create_schematic/copy_to_clipboard/paste] (file:<file path>) (cuboid:<cuboid>) (position:<location>) (rotate:<#>) (undoable) (noair)", 2);
        PropertyParser.registerProperty(WorldEditPlayerProperties.class, PlayerTag.class);
    }
}
