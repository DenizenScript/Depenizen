package net.gnomeffinway.depenizen.events;

import com.herocraftonline.heroes.api.events.ClassChangeEvent;
import net.aufdemrand.denizen.events.EventManager;
import net.aufdemrand.denizen.objects.Element;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizen.objects.dNPC;
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
                npc, player, context).toUpperCase();

        if (determination.equals("CANCELLED")) {
            event.setCancelled(true);
        }

    }

}
