package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.commands.noteblockapi.NBSCommand;
import com.denizenscript.depenizen.bukkit.properties.noteblockapi.NoteBlockAPIPlayerProperties;
import com.denizenscript.depenizen.bukkit.Bridge;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.utilities.DenizenAPI;
import net.aufdemrand.denizencore.objects.properties.PropertyParser;

public class NoteBlockAPIBridge extends Bridge {

    @Override
    public void init() {

        PropertyParser.registerProperty(NoteBlockAPIPlayerProperties.class, dPlayer.class);
        DenizenAPI.getCurrentInstance().getCommandRegistry().registerCoreMember(NBSCommand.class,
                "NBS", "nbs [play/stop] (file:<file path>) [targets:<entity>|...]", 1);
    }
}
