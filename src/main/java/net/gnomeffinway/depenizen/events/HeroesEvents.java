package net.gnomeffinway.depenizen.events;

import com.herocraftonline.heroes.api.events.ClassChangeEvent;
import com.herocraftonline.heroes.api.events.HeroChangeLevelEvent;
import com.herocraftonline.heroes.api.events.HeroEnterCombatEvent;
import com.herocraftonline.heroes.api.events.SkillUseEvent;

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
        else if (hero.isPlayer()){
            player = hero.getPlayer();
        }
        String determination = EventManager.doEvents(Arrays.asList
                ("hero changes class",
                        "hero changes class to " + event.getTo().getName()),
                npc, new dPlayer(player), context).toUpperCase();

        if (determination.equals("CANCELLED")) {
            event.setCancelled(true);
        }

    }


    //<--[event]
    // @Events
    // hero changes level
	// @Triggers when a Hero changes his level.
	// @Context
	// <context.level> returns the current level the Hero has.
	// <context.to> returns the level the Hero got to.
	// <context.from> returns the level the hero came from.
	// @Plugin Heroes
	// -->

@EventHandler
public void changeLevel(HeroChangeLevelEvent event) {

    dEntity hero = new dEntity(event.getHero().getEntity());
    Player player = null;
    dNPC npc = null;
   
    
    Map<String, dObject> context = new HashMap<String, dObject>();
    context.put("level", new Element(event.getHero().getLevel()));
    context.put("to", new Element(event.getTo()));
    context.put("from", new Element(event.getFrom()));
    
    	if (hero.isNPC())
    		npc = hero.getDenizenNPC();
    	else if (hero.isPlayer()); {
    		player = hero.getPlayer();
    	}	
    	String determination = EventManager.doEvents(Arrays.asList
    			(new String[] { "hero changes level",}),
    			npc, player, context).toUpperCase();
    	
    	
    	 if (determination.equals("CANCELLED")) {
    		 ((Cancellable) event).setCancelled(true);
    	
    	 }
    }

  
	//<--[event]
	// @Events
	// hero changes level
	// @Triggers when a Hero enters the combat mode.
	// @Context
	// <context.reason> returns the reason why the event fires.
	// <context.damage> returns the damage the hero dealt.
	// <context.target> returns the entity target that got damaged.
	//<context.attacker> returns the attacking hero.
	// @Plugin Heroes
	// -->


@EventHandler
public void enterCombat(HeroEnterCombatEvent event) {

		dEntity hero = new dEntity(event.getHero().getEntity());
		Player player = null;
		dNPC npc = null;


		Map<String, dObject> context = new HashMap<String, dObject>();
		context.put("reason", new Element(event.getReason().name()));
		context.put("damage", new Element(event.getTarget().getLastDamage()));
		context.put("target", new Element(event.getTarget().toString()));
		context.put("attacker", new Element(event.getHero().getName()));

		if (hero.isNPC())
			npc = hero.getDenizenNPC();
		else if (hero.isPlayer()); {
			player = hero.getPlayer();
		}
		String determination = EventManager.doEvents(Arrays.asList
				(new String[] { "hero entered combat",}),
		npc, player, context).toUpperCase();
 
		if (determination.equals("CANCELLED")) {
			((Cancellable) event).setCancelled(true);

	 
		}
	}


	//<--[event]
	// @Events
	// hero changes level
	// @Triggers when a Hero enters the combat mode.
	// @Context
	// <context.caster> returns the hero that used the skill.
	// <context.skill> returns the skill name.
	// <context.healthcost> returns the healthcost of this skill.
	//<context.manacost> returns the manacost of this skill.
	// @Plugin Heroes
	// -->


@EventHandler
public void useSkill(SkillUseEvent event) {

	dEntity hero = new dEntity(event.getHero().getEntity());
	Player player = null;
	dNPC npc = null;


	Map<String, dObject> context = new HashMap<String, dObject>();
	context.put("caster", new Element(event.getHero().getName()));
	context.put("skill", new Element(event.getSkill().getName()));
	context.put("healthcost", new Element(event.getHealthCost()));
	context.put("manacost", new Element(event.getManaCost()));

	if (hero.isNPC())
		npc = hero.getDenizenNPC();
	else if (hero.isPlayer()); {
		player = hero.getPlayer();
	}
	String determination = EventManager.doEvents(Arrays.asList
			(new String[] { "hero used skill",}),
						npc, player, context).toUpperCase();
 
	if (determination.equals("CANCELLED")) {
		((Cancellable) event).setCancelled(true);

	 	}
	 }
  }


