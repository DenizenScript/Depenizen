package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.commands.libsdisguises.DisguiseCommand;
import com.denizenscript.depenizen.bukkit.events.libsdisguises.EntityDisguisesScriptEvent;
import com.denizenscript.depenizen.bukkit.events.libsdisguises.EntityUndisguisesScriptEvent;
import com.denizenscript.depenizen.bukkit.properties.libsdisguise.LibsDisguiseEntityProperties;
import com.denizenscript.depenizen.bukkit.objects.libsdisguises.LibsDisguiseTag;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.utilities.DenizenAPI;
import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.ObjectFetcher;
import com.denizenscript.denizencore.objects.properties.PropertyParser;

public class LibsDisguisesBridge extends Bridge {

    @Override
    public void init() {
        ScriptEvent.registerScriptEvent(new EntityDisguisesScriptEvent());
        ScriptEvent.registerScriptEvent(new EntityUndisguisesScriptEvent());
        ObjectFetcher.registerWithObjectFetcher(LibsDisguiseTag.class);
        PropertyParser.registerProperty(LibsDisguiseEntityProperties.class, EntityTag.class);
        DenizenAPI.getCurrentInstance().getCommandRegistry().registerCommand(DisguiseCommand.class);
    }
}
