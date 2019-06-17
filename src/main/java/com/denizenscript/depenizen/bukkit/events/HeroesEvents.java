package com.denizenscript.depenizen.bukkit.events;

import com.herocraftonline.heroes.api.events.ClassChangeEvent;
import com.herocraftonline.heroes.api.events.ExperienceChangeEvent;
import com.herocraftonline.heroes.api.events.HeroChangeLevelEvent;
import com.denizenscript.depenizen.bukkit.objects.heroes.HeroesClass;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.objects.dNPC;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.events.OldEventManager;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dObject;
import com.denizenscript.depenizen.bukkit.objects.heroes.HeroesHero;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HeroesEvents implements Listener {

    // <--[event]
    // @Events
    // hero changes class (to <class>)
    //
    // @Regex ^on hero changes class( to [^\s]+)?$
    //
    // @Triggers when a Hero changes classes.
    //
    // @Context
    // <context.class> returns the class that the Hero is changing to.
    // <context.cost> returns the cost of changing the class.
    // <context.from> returns the class that the Hero is changing from.
    // <context.hero> returns the Hero changing classes.
    //
    // @Determine
    // "CANCELLED" to stop the hero from changing classes.
    //
    // @Plugin DepenizenBukkit, Heroes
    // -->
    @EventHandler
    public void changeClass(ClassChangeEvent event) {

        HeroesHero hero = new HeroesHero(event.getHero());
        dPlayer player = null;
        dNPC npc = null;

        Map<String, dObject> context = new HashMap<String, dObject>();
        context.put("class", new HeroesClass(event.getTo()));
        context.put("cost", new Element(event.getCost()));
        context.put("from", new HeroesClass(event.getFrom()));
        context.put("hero", hero);

        if (hero.isNPC()) {
            npc = (dNPC) hero.getDenizenObject();
        }
        else if (hero.isPlayer()) {
            player = (dPlayer) hero.getDenizenObject();
        }

        List<String> determinations = OldEventManager.doEvents(Arrays.asList
                        ("hero changes class",
                                "hero changes class to " + event.getTo().getName()),
                new BukkitScriptEntryData(player, npc), context);

        for (String determination : determinations) {
            determination = determination.toUpperCase();
            if (determination.equals("CANCELLED")) {
                event.setCancelled(true);
            }
        }
    }

    // <--[event]
    // @Events
    // hero changes experience
    //
    // @Regex ^on hero changes experience$
    //
    // @Triggers when a Hero changes the experience.
    //
    // @Context
    // <context.amount> returns the amount the Hero's experience is changing.
    // <context.class> returns the class that the Hero is changing experience in.
    // <context.hero> returns the Hero that is changing experience.
    // <context.reason> returns the reason the Hero is changing experience.
    //
    // @Determine
    // "CANCELLED" to stop the hero from gaining experience.
    //
    // @Plugin DepenizenBukkit, Heroes
    // -->
    @EventHandler
    public void changeExperience(ExperienceChangeEvent event) {

        HeroesHero hero = new HeroesHero(event.getHero());
        dPlayer player = null;
        dNPC npc = null;

        Map<String, dObject> context = new HashMap<String, dObject>();
        context.put("amount", new Element(event.getExpChange()));
        context.put("class", new HeroesClass(event.getHeroClass()));
        context.put("hero", hero);
        context.put("reason", new Element(event.getSource().name()));

        if (hero.isNPC()) {
            npc = (dNPC) hero.getDenizenObject();
        }
        else if (hero.isPlayer()) {
            player = (dPlayer) hero.getDenizenObject();
        }

        List<String> determinations = OldEventManager.doEvents(Arrays.asList("hero changes experience"),
                new BukkitScriptEntryData(player, npc), context);

        for (String determination : determinations) {
            determination = determination.toUpperCase();
            if (determination.equals("CANCELLED")) {
                event.setCancelled(true);
            }
        }
    }

    // <--[event]
    // @Events
    // hero changes level (to <level>)
    //
    // @Regex ^on hero changes levels( to [^\s]+)?$
    //
    // @Triggers when a Hero changes the level.
    //
    // @Context
    // <context.class> returns the class that the Hero is changing levels in.
    // <context.from> returns the level that the Hero is changing from.
    // <context.hero> returns the Hero that is changing levels.
    // <context.level> returns the level the Hero is changing to.
    //
    // @Determine
    // None
    //
    // @Plugin DepenizenBukkit, Heroes
    // -->
    @EventHandler
    public void changeLevel(HeroChangeLevelEvent event) {

        HeroesHero hero = new HeroesHero(event.getHero());
        dPlayer player = null;
        dNPC npc = null;

        Map<String, dObject> context = new HashMap<String, dObject>();
        context.put("class", new HeroesClass(event.getHeroClass()));
        context.put("from", new Element(event.getFrom()));
        context.put("hero", hero);
        context.put("level", new Element(event.getTo()));

        if (hero.isNPC()) {
            npc = (dNPC) hero.getDenizenObject();
        }
        else if (hero.isPlayer()) {
            player = (dPlayer) hero.getDenizenObject();
        }

        List<String> determinations = OldEventManager.doEvents(Arrays.asList
                        ("hero changes level",
                                "hero changes level to " + event.getTo()),
                new BukkitScriptEntryData(player, npc), context);
    }
}
