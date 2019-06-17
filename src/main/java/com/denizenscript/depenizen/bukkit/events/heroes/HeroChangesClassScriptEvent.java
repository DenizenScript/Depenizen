package com.denizenscript.depenizen.bukkit.events.heroes;

import com.denizenscript.depenizen.bukkit.objects.heroes.HeroesClass;
import com.denizenscript.depenizen.bukkit.objects.heroes.HeroesHero;
import com.herocraftonline.heroes.api.events.ClassChangeEvent;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizen.objects.dNPC;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.ScriptEntryData;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class HeroChangesClassScriptEvent extends BukkitScriptEvent implements Listener {

    public HeroChangesClassScriptEvent() {
        instance = this;
    }

    public static HeroChangesClassScriptEvent instance;

    // <--[event]
    // @Events
    // hero changes class (to <class>)
    //
    // @Regex ^on hero changes class( to [^\s]+)?$
    //
    // @Cancellable true
    //
    // @Triggers when a Hero changes classes.
    //
    // @Context
    // <context.class> returns the class that the Hero is changing to.
    // <context.cost> returns the cost of changing the class.
    // <context.from> returns the class that the Hero is changing from.
    // <context.hero> returns the Hero changing classes.
    //
    // @Plugin Depenizen, Heroes
    // -->

    public HeroesHero hero;
    public HeroesClass to;
    public HeroesClass from;
    public ClassChangeEvent event;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        return s.startsWith("hero changes class");
    }

    @Override
    public boolean matches(ScriptPath path) {
        if (path.eventArgLowerAt(3).equals("to")) {
            String newClass = path.eventArgLowerAt(4);
            if (!to.getHeroClass().getName().equalsIgnoreCase(newClass)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String getName() {
        return "HeroChangesClass";
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(hero.isPlayer() ? (dPlayer) hero.getDenizenObject() : null, hero.isNPC() ? (dNPC) hero.getDenizenObject() : null);
    }

    @Override
    public dObject getContext(String name) {
        if (name.equals("class")) {
            return to;
        }
        else if (name.equals("from")) {
            return from;
        }
        else if (name.equals("cost")) {
            return new Element(event.getCost());
        }
        else if (name.equals("hero")) {
            return hero;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void changeClass(ClassChangeEvent event) {
        this.event = event;
        hero = new HeroesHero(event.getHero());
        to = new HeroesClass(event.getTo());
        from = new HeroesClass(event.getFrom());
        fire(event);
    }
}
