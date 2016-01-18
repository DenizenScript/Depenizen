package net.gnomeffinway.depenizen.support.plugins;

import net.aufdemrand.denizen.objects.dItem;
import net.aufdemrand.denizen.objects.dPlayer;
import net.gnomeffinway.depenizen.events.EssentialsEvents;
import net.gnomeffinway.depenizen.extensions.essentials.EssentialsItemExtension;
import net.gnomeffinway.depenizen.support.Support;
import net.gnomeffinway.depenizen.extensions.essentials.EssentialsPlayerExtension;

public class EssentialsSupport extends Support {

    public EssentialsSupport() {
        registerEvents(EssentialsEvents.class);
        registerProperty(EssentialsPlayerExtension.class, dPlayer.class);
        registerProperty(EssentialsItemExtension.class, dItem.class);
    }
}
