package com.denizenscript.depenizen.bukkit.events.towny;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.depenizen.bukkit.objects.towny.TownTag;
import com.palmergames.bukkit.towny.event.TownAddResidentEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerJoinsTownScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // towny player joins town
    //
    // @Triggers when a player joins a Towny town.
    //
    // @Context
    // <context.town> returns the TownTag that the player is joining.
    //
    // @Plugin Depenizen, Towny
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->

    public PlayerJoinsTownScriptEvent() {
        registerCouldMatcher("towny player joins town");
    }

    public TownAddResidentEvent event;

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(new PlayerTag(event.getResident().getUUID()), null);
    }

    @Override
    public ObjectTag getContext(String name) {
        switch (name) {
            case "town": return new TownTag(event.getTown());
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onPlayerJoinTown(TownAddResidentEvent event) {
        this.event = event;
        fire(event);
    }
}
