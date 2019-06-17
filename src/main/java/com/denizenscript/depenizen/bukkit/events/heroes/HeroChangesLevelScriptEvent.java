package com.denizenscript.depenizen.bukkit.events.heroes;

import com.denizenscript.depenizen.bukkit.objects.heroes.HeroesClass;
import com.denizenscript.depenizen.bukkit.objects.heroes.HeroesHero;
import com.herocraftonline.heroes.api.events.HeroChangeLevelEvent;
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

public class HeroChangesLevelScriptEvent extends BukkitScriptEvent implements Listener {

    public HeroChangesLevelScriptEvent() {
        instance = this;
    }

    public static HeroChangesLevelScriptEvent instance;

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
    // @Plugin Depenizen, Heroes
    // -->

    public HeroesHero hero;
    public HeroChangeLevelEvent event;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        return s.startsWith("hero changes level");
    }

    @Override
    public boolean matches(ScriptPath path) {
        if (path.eventArgLowerAt(3).equals("to")) {
            String newLevel = path.eventArgLowerAt(4);
            if (!newLevel.equals(String.valueOf(event.getTo()))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String getName() {
        return "HeroChangesLevel";
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
        else if (name.equals("from")) {
            return new Element(event.getFrom());
        }
        else if (name.equals("level")) {
            return new Element(event.getTo());
        }
        else if (name.equals("hero")) {
            return hero;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void changeLevel(HeroChangeLevelEvent event) {
        this.event = event;
        hero = new HeroesHero(event.getHero());
        fire(event);
    }
}
