package com.denizenscript.depenizen.bukkit.events.towny;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.depenizen.bukkit.objects.towny.TownTag;
import com.palmergames.bukkit.towny.event.TownRemoveResidentEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerLeavesTownScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // towny player leaves town
    //
    // @Triggers when a player is removed from a Towny town or when a Towny town is dissolved.
    //
    // @Context
    // <context.town> returns the TownTag that player is leaving.
    //
    // @Plugin Depenizen, Towny
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->

    public PlayerLeavesTownScriptEvent() {
        registerCouldMatcher("towny player leaves town");
    }

    public TownRemoveResidentEvent event;

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
    public void onPlayerJoinTown(TownRemoveResidentEvent event) {
        this.event = event;
        fire(event);
    }
}
