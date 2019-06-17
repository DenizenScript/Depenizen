package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.commands.libsdisguises.DisguiseCommand;
import com.denizenscript.depenizen.bukkit.events.libsdisguises.EntityDisguisesScriptEvent;
import com.denizenscript.depenizen.bukkit.events.libsdisguises.EntityUndisguisesScriptEvent;
import com.denizenscript.depenizen.bukkit.extensions.libsdisguise.LibsDisguiseEntityExtension;
import com.denizenscript.depenizen.bukkit.objects.libsdisguises.LibsDisguise;
import com.denizenscript.depenizen.bukkit.Bridge;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizencore.events.ScriptEvent;
import net.aufdemrand.denizencore.objects.ObjectFetcher;
import net.aufdemrand.denizencore.objects.properties.PropertyParser;

public class LibsDisguisesBridge extends Bridge {

    @Override
    public void init() {
        ScriptEvent.registerScriptEvent(new EntityDisguisesScriptEvent());
        ScriptEvent.registerScriptEvent(new EntityUndisguisesScriptEvent());
        ObjectFetcher.registerWithObjectFetcher(LibsDisguise.class);
        PropertyParser.registerProperty(LibsDisguiseEntityExtension.class, dEntity.class);
        new DisguiseCommand().activate().as("DISGUISE").withOptions("See Documentation.", 1);
    }
}
