package net.gnomeffinway.depenizen.support.plugins;

import net.aufdemrand.denizen.objects.dItem;
import net.aufdemrand.denizen.objects.dPlayer;
import net.gnomeffinway.depenizen.events.essentials.PlayerAFKStatusScriptEvent;
import net.gnomeffinway.depenizen.events.essentials.PlayerGodModeStatusScriptEvent;
import net.gnomeffinway.depenizen.events.essentials.PlayerJailStatusScriptEvent;
import net.gnomeffinway.depenizen.events.essentials.PlayerMuteStatusScriptEvent;
import net.gnomeffinway.depenizen.extensions.essentials.EssentialsItemExtension;
import net.gnomeffinway.depenizen.extensions.essentials.EssentialsPlayerExtension;
import net.gnomeffinway.depenizen.support.Support;

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
