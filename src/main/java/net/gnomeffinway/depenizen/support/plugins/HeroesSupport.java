package net.gnomeffinway.depenizen.support.plugins;

import net.aufdemrand.denizen.objects.dNPC;
import net.aufdemrand.denizen.objects.dPlayer;
import net.gnomeffinway.depenizen.commands.heroes.HeroesXPCommand;
import net.gnomeffinway.depenizen.events.HeroesEvents;
import net.gnomeffinway.depenizen.objects.heroes.HeroesClass;
import net.gnomeffinway.depenizen.objects.heroes.HeroesHero;
import net.gnomeffinway.depenizen.support.Support;
import net.gnomeffinway.depenizen.extensions.heroes.HeroesPlayerNPCExtension;

public class HeroesSupport extends Support {

    public HeroesSupport() {
        registerEvents(HeroesEvents.class);
        registerObjects(HeroesClass.class, HeroesHero.class);
        registerProperty(HeroesPlayerNPCExtension.class, dNPC.class, dPlayer.class);
        new HeroesXPCommand().activate().as("heroesxp")
                .withOptions("[add/remove/set] <heroesclass> quantity:<#.#>", 3);
    }
    
}
