package com.denizenscript.depenizen.bukkit.support.plugins;

import com.denizenscript.depenizen.bukkit.commands.libsdisguises.DisguiseCommands;
import com.denizenscript.depenizen.bukkit.events.libsdisguises.EntityDisguisesScriptEvent;
import com.denizenscript.depenizen.bukkit.events.libsdisguises.EntityUndisguisesScriptEvent;
import com.denizenscript.depenizen.bukkit.extensions.libsdisguise.LibsDisguiseEntityExtension;
import com.denizenscript.depenizen.bukkit.objects.libsdisguises.LibsDisguise;
import com.denizenscript.depenizen.bukkit.support.Support;
import net.aufdemrand.denizen.objects.dEntity;

public class LibsDisguisesSupport extends Support {

    public LibsDisguisesSupport() {
        registerScriptEvents(new EntityDisguisesScriptEvent());
        registerScriptEvents(new EntityUndisguisesScriptEvent());
        registerObjects(LibsDisguise.class);
        registerProperty(LibsDisguiseEntityExtension.class, dEntity.class);
        new DisguiseCommands().activate().as("DISGUISE").withOptions("See Documentation.", 1);
    }
}
