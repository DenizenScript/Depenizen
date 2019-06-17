package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.commands.noteblockapi.NBSCommand;
import com.denizenscript.depenizen.bukkit.properties.noteblockapi.NoteBlockAPIPlayerProperties;
import com.denizenscript.depenizen.bukkit.Bridge;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.properties.PropertyParser;

public class NoteBlockAPIBridge extends Bridge {

    @Override
    public void init() {

        PropertyParser.registerProperty(NoteBlockAPIPlayerProperties.class, dPlayer.class);
        new NBSCommand().activate().as("NBS").withOptions("See Documentation.", 1);
    }
}
