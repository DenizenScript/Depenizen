package com.denizenscript.depenizen.bukkit.events.heroes;

import com.denizenscript.depenizen.bukkit.objects.heroes.HeroesClass;
import com.denizenscript.depenizen.bukkit.objects.heroes.HeroesHero;
import com.herocraftonline.heroes.api.events.ExperienceChangeEvent;
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

public class HeroChangesExperienceScriptEvent extends BukkitScriptEvent implements Listener {

    public HeroChangesExperienceScriptEvent() {
        instance = this;
    }

    public static HeroChangesExperienceScriptEvent instance;

    // <--[event]
    // @Events
    // hero changes experience
    //
    // @Regex ^on hero changes experience$
    //
    // @Cancellable true
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
    // @Plugin Depenizen, Heroes
    // -->

    public HeroesHero hero;
    public ExperienceChangeEvent event;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        return s.startsWith("hero changes experience");
    }

    @Override
    public boolean matches(ScriptPath path) {
        return true;
    }

    @Override
    public String getName() {
        return "HeroChangesExperience";
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(hero.isPlayer() ? (dPlayer) hero.getDenizenObject() : null, hero.isNPC() ? (dNPC) hero.getDenizenObject() : null);
    }

    @Override
    public dObject getContext(String name) {
        if (name.equals("class")) {
            return new HeroesClass(event.getHeroClass());
        }
        else if (name.equals("reason")) {
            return new Element(event.getSource().name());
        }
        else if (name.equals("amount")) {
            return new Element(event.getExpChange());
        }
        else if (name.equals("hero")) {
            return hero;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void changeExperience(ExperienceChangeEvent event) {
        this.event = event;
        hero = new HeroesHero(event.getHero());
        fire(event);
    }
}
