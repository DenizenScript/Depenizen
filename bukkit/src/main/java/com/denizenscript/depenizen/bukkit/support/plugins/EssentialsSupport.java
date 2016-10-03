package com.denizenscript.depenizen.bukkit.support.plugins;

import com.denizenscript.depenizen.bukkit.events.essentials.PlayerAFKStatusScriptEvent;
import com.denizenscript.depenizen.bukkit.events.essentials.PlayerGodModeStatusScriptEvent;
import com.denizenscript.depenizen.bukkit.events.essentials.PlayerJailStatusScriptEvent;
import com.denizenscript.depenizen.bukkit.events.essentials.PlayerMuteStatusScriptEvent;
import com.denizenscript.depenizen.bukkit.extensions.essentials.EssentialsItemExtension;
import com.denizenscript.depenizen.bukkit.extensions.essentials.EssentialsPlayerExtension;
import com.denizenscript.depenizen.bukkit.support.Support;
import net.aufdemrand.denizen.objects.dItem;
import net.aufdemrand.denizen.objects.dPlayer;

public class EssentialsSupport extends Support {

    public EssentialsSupport() {
        registerScriptEvents(new PlayerAFKStatusScriptEvent());
        registerScriptEvents(new PlayerGodModeStatusScriptEvent());
        registerScriptEvents(new PlayerJailStatusScriptEvent());
        registerScriptEvents(new PlayerMuteStatusScriptEvent());
        registerProperty(EssentialsPlayerExtension.class, dPlayer.class);
        registerProperty(EssentialsItemExtension.class, dItem.class);
    }
}
