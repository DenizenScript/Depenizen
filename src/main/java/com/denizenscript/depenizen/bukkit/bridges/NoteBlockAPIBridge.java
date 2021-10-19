package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizencore.DenizenCore;
import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.depenizen.bukkit.commands.noteblockapi.NBSCommand;
import com.denizenscript.depenizen.bukkit.events.noteblockapi.NoteBlockAPISongEndsScriptEvent;
import com.denizenscript.depenizen.bukkit.properties.noteblockapi.NoteBlockAPIPlayerProperties;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.properties.PropertyParser;

public class NoteBlockAPIBridge extends Bridge {

    @Override
    public void init() {

        PropertyParser.registerProperty(NoteBlockAPIPlayerProperties.class, PlayerTag.class);
        DenizenCore.commandRegistry.registerCommand(NBSCommand.class);
        ScriptEvent.registerScriptEvent(new NoteBlockAPISongEndsScriptEvent());
    }
}
