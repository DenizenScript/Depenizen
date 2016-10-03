package com.morphanone.depenizenbukkit.support.plugins;

import com.morphanone.depenizenbukkit.extensions.quests.QuestsPlayerExtension;
import net.aufdemrand.denizen.objects.dPlayer;
import com.morphanone.depenizenbukkit.support.Support;

public class QuestsSupport extends Support {

    public QuestsSupport() {
        registerProperty(QuestsPlayerExtension.class, dPlayer.class);
    }

}
