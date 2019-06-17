package com.denizenscript.depenizen.bukkit.support.plugins;

import com.denizenscript.depenizen.bukkit.events.HeroesEvents;
import com.denizenscript.depenizen.bukkit.objects.heroes.HeroesClass;
import com.denizenscript.depenizen.bukkit.objects.heroes.HeroesHero;
import com.denizenscript.depenizen.bukkit.support.Support;
import com.denizenscript.depenizen.bukkit.commands.heroes.HeroesXPCommand;
import com.denizenscript.depenizen.bukkit.extensions.heroes.HeroesPlayerNPCExtension;
import net.aufdemrand.denizen.objects.dNPC;
import net.aufdemrand.denizen.objects.dPlayer;

public class HeroesSupport extends Support {

    public HeroesSupport() {
        registerEvents(HeroesEvents.class);
        registerObjects(HeroesClass.class, HeroesHero.class);
        registerProperty(HeroesPlayerNPCExtension.class, dNPC.class);
        registerProperty(HeroesPlayerNPCExtension.class, dPlayer.class);
        new HeroesXPCommand().activate().as("heroesxp")
                .withOptions("[add/remove/set] <heroesclass> quantity:<#.#>", 3);
    }

}
