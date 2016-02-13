package net.gnomeffinway.depenizen.support.plugins;

import net.aufdemrand.denizen.objects.dPlayer;
import net.gnomeffinway.depenizen.extensions.quests.QuestsPlayerExtension;
import net.gnomeffinway.depenizen.support.Support;

public class QuestsSupport extends Support {

    public QuestsSupport() {
        registerProperty(QuestsPlayerExtension.class, dPlayer.class);
    }

}
