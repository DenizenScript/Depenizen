package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.commands.noteblockapi.NBSCommand;
import com.denizenscript.depenizen.bukkit.extensions.noteblockapi.NoteBlockAPIPlayerExtension;
import com.denizenscript.depenizen.bukkit.support.Support;
import net.aufdemrand.denizen.objects.dPlayer;

public class NoteBlockAPISupport extends Support {

    public NoteBlockAPISupport() {

        registerProperty(NoteBlockAPIPlayerExtension.class, dPlayer.class);
        new NBSCommand().activate().as("NBS").withOptions("See Documentation.", 1);
    }
}
