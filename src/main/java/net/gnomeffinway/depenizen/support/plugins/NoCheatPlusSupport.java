package net.gnomeffinway.depenizen.support.plugins;

import net.aufdemrand.denizen.objects.dPlayer;
import net.gnomeffinway.depenizen.extensions.NoCheatPlus.NoCheatPlusPlayerExtension;
import net.gnomeffinway.depenizen.support.Support;

public class NoCheatPlusSupport extends Support {

    public NoCheatPlusSupport() {
        registerProperty(NoCheatPlusPlayerExtension.class, dPlayer.class);
    }
}
