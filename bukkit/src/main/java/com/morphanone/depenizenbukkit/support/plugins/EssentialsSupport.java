package com.morphanone.depenizenbukkit.support.plugins;

import com.morphanone.depenizenbukkit.events.essentials.PlayerAFKStatusScriptEvent;
import com.morphanone.depenizenbukkit.events.essentials.PlayerGodModeStatusScriptEvent;
import com.morphanone.depenizenbukkit.events.essentials.PlayerJailStatusScriptEvent;
import com.morphanone.depenizenbukkit.events.essentials.PlayerMuteStatusScriptEvent;
import com.morphanone.depenizenbukkit.extensions.essentials.EssentialsItemExtension;
import com.morphanone.depenizenbukkit.extensions.essentials.EssentialsPlayerExtension;
import net.aufdemrand.denizen.objects.dItem;
import net.aufdemrand.denizen.objects.dPlayer;
import com.morphanone.depenizenbukkit.support.Support;

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
