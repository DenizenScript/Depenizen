package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.events.heroes.HeroChangesClassScriptEvent;
import com.denizenscript.depenizen.bukkit.events.heroes.HeroChangesExperienceScriptEvent;
import com.denizenscript.depenizen.bukkit.events.heroes.HeroChangesLevelScriptEvent;
import com.denizenscript.depenizen.bukkit.objects.heroes.HeroesClass;
import com.denizenscript.depenizen.bukkit.objects.heroes.HeroesHero;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.commands.heroes.HeroesXPCommand;
import com.denizenscript.depenizen.bukkit.extensions.heroes.HeroesPlayerNPCExtension;
import net.aufdemrand.denizen.objects.dNPC;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.events.ScriptEvent;
import net.aufdemrand.denizencore.objects.ObjectFetcher;
import net.aufdemrand.denizencore.objects.properties.PropertyParser;

public class HeroesBridge extends Bridge {

    public static HeroesBridge instance;

    @Override
    public void init() {
        instance = this;
        ScriptEvent.registerScriptEvent(new HeroChangesClassScriptEvent());
        ScriptEvent.registerScriptEvent(new HeroChangesExperienceScriptEvent());
        ScriptEvent.registerScriptEvent(new HeroChangesLevelScriptEvent());
        ObjectFetcher.registerWithObjectFetcher(HeroesClass.class);
        ObjectFetcher.registerWithObjectFetcher(HeroesHero.class);
        PropertyParser.registerProperty(HeroesPlayerNPCExtension.class, dNPC.class);
        PropertyParser.registerProperty(HeroesPlayerNPCExtension.class, dPlayer.class);
        new HeroesXPCommand().activate().as("heroesxp")
                .withOptions("[add/remove/set] <heroesclass> quantity:<#.#>", 3);
    }
}
