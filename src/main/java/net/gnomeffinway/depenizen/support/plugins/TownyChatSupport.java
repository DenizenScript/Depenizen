package net.gnomeffinway.depenizen.support.plugins;

import net.aufdemrand.denizen.objects.dPlayer;
import net.gnomeffinway.depenizen.extensions.towny.TownyChatPlayerExtension;
import net.gnomeffinway.depenizen.support.Support;

public class TownyChatSupport extends Support {

    public TownyChatSupport() {
        registerProperty(TownyChatPlayerExtension.class, dPlayer.class);
    }

}
