package net.gnomeffinway.depenizen.support.plugins;

import net.aufdemrand.denizen.objects.dPlayer;
import net.gnomeffinway.depenizen.events.EssentialsEvents;
import net.gnomeffinway.depenizen.support.Support;
import net.gnomeffinway.depenizen.extensions.essentials.EssentialsPlayerTags;

public class EssentialsSupport extends Support {

    public EssentialsSupport() {
        registerEvents(EssentialsEvents.class);
        registerProperty(EssentialsPlayerTags.class, dPlayer.class);
    }

}
