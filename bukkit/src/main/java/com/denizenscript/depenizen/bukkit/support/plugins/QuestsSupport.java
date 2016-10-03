package com.denizenscript.depenizen.bukkit.support.plugins;

import com.denizenscript.depenizen.bukkit.extensions.quests.QuestsPlayerExtension;
import com.denizenscript.depenizen.bukkit.support.Support;
import net.aufdemrand.denizen.objects.dPlayer;

public class QuestsSupport extends Support {

    public QuestsSupport() {
        registerProperty(QuestsPlayerExtension.class, dPlayer.class);
    }

}
