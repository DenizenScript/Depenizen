package com.morphanone.depenizenbukkit.support.plugins;

import com.morphanone.depenizenbukkit.commands.heroes.HeroesXPCommand;
import com.morphanone.depenizenbukkit.events.HeroesEvents;
import com.morphanone.depenizenbukkit.extensions.heroes.HeroesPlayerNPCExtension;
import com.morphanone.depenizenbukkit.objects.heroes.HeroesClass;
import net.aufdemrand.denizen.objects.dNPC;
import net.aufdemrand.denizen.objects.dPlayer;
import com.morphanone.depenizenbukkit.objects.heroes.HeroesHero;
import com.morphanone.depenizenbukkit.support.Support;

public class HeroesSupport extends Support {

    public HeroesSupport() {
        registerEvents(HeroesEvents.class);
        registerObjects(HeroesClass.class, HeroesHero.class);
        registerProperty(HeroesPlayerNPCExtension.class, dNPC.class, dPlayer.class);
        new HeroesXPCommand().activate().as("heroesxp")
                .withOptions("[add/remove/set] <heroesclass> quantity:<#.#>", 3);
    }

}
