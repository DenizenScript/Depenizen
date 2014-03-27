package net.gnomeffinway.depenizen.events;

import com.herocraftonline.heroes.api.events.ClassChangeEvent;
import com.herocraftonline.heroes.api.events.ExperienceChangeEvent;
import com.herocraftonline.heroes.api.events.HeroChangeLevelEvent;
import com.herocraftonline.heroes.api.events.HeroKillCharacterEvent;

import net.aufdemrand.denizen.events.EventManager;
import net.aufdemrand.denizen.objects.Element;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizen.objects.dNPC;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.objects.dObject;
import net.gnomeffinway.depenizen.Depenizen;

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
    // <context.class> returns the class that the Hero changed to.
    // <context.cost> returns the cost of changing the class.
    // <context.entity> returns the Hero changing classes.
    // @Plugin Heroes
    // -->


	@EventHandler
    public void changeClass(ClassChangeEvent event) {

        dEntity hero = new dEntity(event.getHero().getEntity());
        Player player = null;
        dNPC npc = null;

        Map<String, dObject> context = new HashMap<String, dObject>();
        context.put("class", new Element(event.getTo().getName()));
        context.put("cost", new Element(event.getCost()));
        context.put("entity", hero);

        if (hero.isNPC())
            npc = hero.getDenizenNPC();
        else if (hero.isPlayer())
            player = hero.getPlayer();

        String determination = EventManager.doEvents(Arrays.asList
                ("hero changes class",
                        "hero changes class to " + event.getTo().getName()),
                npc, new dPlayer(player), context).toUpperCase();

        if (determination.equals("CANCELLED")) {
            event.setCancelled(true);
        }

    }

    // <--[event]
    // @Events
    // hero changes level (to <level>)
    // @Triggers when a Hero changes level.
    // @Context
    // <context.from> returns the level that the Hero changed from.
    // <context.entity> returns the Hero changing level.
    // @Plugin Heroes
    // -->


	@EventHandler
    public void changeLevel(HeroChangeLevelEvent event) {

        dEntity hero = new dEntity(event.getHero().getEntity());
        Player player = null;
        dNPC npc = null;

        Map<String, dObject> context = new HashMap<String, dObject>();
        context.put("from", new Element(event.getFrom()));
        context.put("entity", hero);

        if (hero.isNPC())
            npc = hero.getDenizenNPC();
        else if (hero.isPlayer())
            player = hero.getPlayer();

         EventManager.doEvents(Arrays.asList
                ("hero changes level",
                        "hero changes level to " + event.getTo()),
                npc, new dPlayer(player), context).toUpperCase();


    }
	
    // <--[event]
    // @Events
    // hero kills character (with <damage>)
    // @Triggers when a Hero changes classes.
    // @Context
    // <context.attacker> returns the Hero that killed the player.
    // <context.defender> returns the Hero that got killed.
    // @Plugin Heroes
    // -->


	@EventHandler
    public void killHero(HeroKillCharacterEvent event) {

        dEntity hero = new dEntity(event.getAttacker().getEntity());
        Player player = null;
        dNPC npc = null;

        Map<String, dObject> context = new HashMap<String, dObject>();
        context.put("attacker", new Element(event.getAttacker().getName()));
        context.put("defender", new Element(event.getDefender().getName()));

        if (hero.isNPC())
            npc = hero.getDenizenNPC();
        else if (hero.isPlayer())
            player = hero.getPlayer();

        EventManager.doEvents(Arrays.asList
                ("hero kills character",
                        "hero kills character with " + event.getAttacker().getLastDamageCause()),
                npc, new dPlayer(player), context).toUpperCase();

	}
	
    // <--[event]
    // @Events
    // hero changes class (to <class>)
    // @Triggers when a Hero changes classes.
    // @Context
    // <context.expchange> returns the exp the Hero changed to.
    // <context.entity> returns the Hero changing exp.
    // @Plugin Heroes
    // -->


	@EventHandler
    public void changeEXP(ExperienceChangeEvent event) {

        dEntity hero = new dEntity(event.getHero().getEntity());
        Player player = null;
        dNPC npc = null;

        Map<String, dObject> context = new HashMap<String, dObject>();
        context.put("expchange", new Element(event.getExpChange()));
        context.put("entity", hero);

        if (hero.isNPC())
            npc = hero.getDenizenNPC();
        else if (hero.isPlayer())
            player = hero.getPlayer();

        String determination = EventManager.doEvents(Arrays.asList
                ("hero changes exp",
                        "hero changes exp at " + event.getLocation().toString()),
                npc, new dPlayer(player), context).toUpperCase();

        if (determination.equals("CANCELLED")) {
            event.setCancelled(true);
        }

    }

}