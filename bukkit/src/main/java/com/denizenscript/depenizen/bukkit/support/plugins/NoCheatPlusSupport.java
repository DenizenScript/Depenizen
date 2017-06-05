package com.denizenscript.depenizen.bukkit.support.plugins;

import com.denizenscript.depenizen.bukkit.events.NCPEvents;
import com.denizenscript.depenizen.bukkit.support.Support;
import fr.neatmonster.nocheatplus.checks.CheckType;
import fr.neatmonster.nocheatplus.hooks.NCPHook;
import fr.neatmonster.nocheatplus.hooks.NCPHookManager;
import net.aufdemrand.denizen.objects.dPlayer;
import com.denizenscript.depenizen.bukkit.extensions.nocheatplus.NoCheatPlusPlayerExtension;

public class NoCheatPlusSupport extends Support {

    public NoCheatPlusSupport() {
        registerProperty(NoCheatPlusPlayerExtension.class, dPlayer.class);
        NCPHook hook = new NCPEvents();
        NCPHookManager.addHook(CheckType.ALL, hook);
    }
}
