package com.denizenscript.depenizen.bukkit.events.towny;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.depenizen.bukkit.objects.towny.TownTag;
import com.palmergames.bukkit.towny.event.NewTownEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TownyTownCreatedScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // towny town created
    //
    // @Regex ^on towny town created$
    //
    // @Triggers when a new town is created.
    //
    // @Context
    // <context.town> returns the town that was created.
    //
    // @Plugin Depenizen, Towny
    //
    // @Group Depenizen
    //
    // -->

    public TownyTownCreatedScriptEvent() {
        instance = this;
    }

    public static TownyTownCreatedScriptEvent instance;
    public NewTownEvent event;

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("towny town created");
    }

    @Override
    public String getName() {
        return "TownyTownCreated";
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("town")) {
            return new TownTag(event.getTown());
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onPreTownCreated(NewTownEvent event) {
        this.event = event;
        fire(event);
    }
}
