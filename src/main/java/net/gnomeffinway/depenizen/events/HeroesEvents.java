package net.gnomeffinway.depenizen.events;

import com.herocraftonline.heroes.api.events.ClassChangeEvent;
import com.herocraftonline.heroes.api.events.ExperienceChangeEvent;
import com.herocraftonline.heroes.api.events.HeroChangeLevelEvent;

import net.aufdemrand.denizen.events.EventManager;
import net.aufdemrand.denizen.objects.Element;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizen.objects.dNPC;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.objects.dObject;
import net.gnomeffinway.depenizen.Depenizen;

import net.gnomeffinway.depenizen.objects.dHero;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HeroesEvents implements Listener {

    public HeroesEvents(Depenizen depenizen) {
        depenizen.getServer().getPluginManager().registerEvents(this, depenizen);
    }

    // <--[event]
    // @Events
    // hero changes class (to <class>)
    // @Triggers when a Hero changes classes.
    // @Context
    // <context.class> returns the class that the Hero is changing to.
    // <context.cost> returns the cost of changing the class.
    // <context.from> returns the class that the Hero is changing from.
    // <context.hero> returns the Hero changing classes.
    // @Determine
    // "CANCELLED" to stop the hero from changing classes.
    // @Plugin Heroes
    // -->

    @EventHandler
    public void changeClass(ClassChangeEvent event) {

        dHero hero = new dHero(event.getHero());
        dPlayer player = null;
        dNPC npc = null;

        Map<String, dObject> context = new HashMap<String, dObject>();
        context.put("class", new Element(event.getTo().getName()));
        context.put("cost", new Element(event.getCost()));
        context.put("from", new Element(event.getFrom().getName()));
        context.put("hero", hero);

        if (hero.isNPC())
            npc = hero.getDenizenNPC();
        else if (hero.isPlayer())
            player = hero.getDenizenPlayer();

        String determination = EventManager.doEvents(Arrays.asList
                ("hero changes class",
                        "hero changes class to " + event.getTo().getName()),
                npc, player, context).toUpperCase();

        if (determination.equals("CANCELLED")) {
            event.setCancelled(true);
        }

    }
    
    // <--[event]
    // @Events
    // hero changes experience
    // @Triggers when a Hero changes the experience.
    // @Context
    // <context.amount> returns the amount the Hero's experience is changing.
    // <context.class> returns the class that the Hero is changing experience in.
    // <context.hero> returns the Hero that is changing experience.
    // <context.reason> returns the reason the Hero is changing experience.
    // @Determine
    // "CANCELLED" to stop the hero from gaining experience.
    // @Plugin Heroes
    // -->

    @EventHandler
    public void changeExperience(ExperienceChangeEvent event) {

        dHero hero = new dHero(event.getHero());
        dPlayer player = null;
        dNPC npc = null;

        Map<String, dObject> context = new HashMap<String, dObject>();
        context.put("amount", new Element(event.getExpChange()));
        context.put("class", new Element(event.getHeroClass().getName()));
        context.put("hero", hero);
        context.put("reason", new Element(event.getSource().name()));

        if (hero.isNPC())
            npc = hero.getDenizenNPC();
        else if (hero.isPlayer())
            player = hero.getDenizenPlayer();

        String determination = EventManager.doEvents(Arrays.asList
                ("hero changes experience"),
                		npc, player, context).toUpperCase();

        if (determination.equals("CANCELLED")) {
            event.setCancelled(true);
        }

    }
    
    // <--[event]
    // @Events
    // hero changes level (to <level>)
    // @Triggers when a Hero changes the level.
    // @Context
    // <context.class> returns the class that the Hero is changing levels in.
    // <context.from> returns the level that the Hero is changing from.
    // <context.hero> returns the Hero that is changing levels.
    // <context.level> returns the level the Hero is changing to.
    // @Determine
    // None
    // @Plugin Heroes
    // -->

    @EventHandler
    public void changeLevel(HeroChangeLevelEvent event) {

        dHero hero = new dHero(event.getHero());
        dPlayer player = null;
        dNPC npc = null;

        Map<String, dObject> context = new HashMap<String, dObject>();
        context.put("class", new Element(event.getHeroClass().getName()));
        context.put("from", new Element(event.getFrom()));
        context.put("hero", hero);
        context.put("level", new Element(event.getTo()));

        if (hero.isNPC())
            npc = hero.getDenizenNPC();
        else if (hero.isPlayer())
            player = hero.getDenizenPlayer();

        String determination = EventManager.doEvents(Arrays.asList
                	("hero changes level",
                			"hero changes level to " + event.getTo()),
                				npc, player, context).toUpperCase();

        if (determination.equals("CANCELLED")) {
            // TODO: find a workaround for no event.setCancelled()
        }

    }

}
