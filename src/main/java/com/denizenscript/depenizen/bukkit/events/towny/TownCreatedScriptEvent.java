package com.denizenscript.depenizen.bukkit.events.towny;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.depenizen.bukkit.objects.towny.TownTag;
import com.palmergames.bukkit.towny.event.NewTownEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TownCreatedScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // towny town created
    //
    // @Triggers after all checks are complete and a Towny town is fully created.
    //
    // @Context
    // <context.town> Returns the town that was created.
    //
    // @Plugin Depenizen, Towny
    //
    // @Group Depenizen
    //
    // -->

    public TownCreatedScriptEvent() {
        registerCouldMatcher("towny town created");
    }

    public NewTownEvent event;

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("town")) {
            return new TownTag(event.getTown());
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onTownyTownCreated(NewTownEvent event) {
        this.event = event;
        fire(event);
    }
}
