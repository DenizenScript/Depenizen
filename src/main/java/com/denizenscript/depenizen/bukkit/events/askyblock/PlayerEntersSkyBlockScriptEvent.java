package com.denizenscript.depenizen.bukkit.events.askyblock;

import com.wasteofplastic.askyblock.events.IslandEnterEvent;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerEntersSkyBlockScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // skyblock player enters skyblock
    //
    // @Regex ^on skyblock player enters skyblock$
    //
    // @Triggers when a player goes to a skyblock island.
    //
    // @Context
    // <context.owner> Returns the owner of the island.
    // <context.island_location> Returns the location of the island.
    // <context.location> Returns the location the player entered at.
    //
    // @Plugin Depenizen, A SkyBlock
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->

    public PlayerEntersSkyBlockScriptEvent instance;
    public IslandEnterEvent event;
    public LocationTag location;
    public LocationTag island_location;
    public PlayerTag owner;

    public PlayerEntersSkyBlockScriptEvent() {
    }

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("skyblock player enters skyblock");
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(new PlayerTag(event.getPlayer()), null);
    }

    @Override
    public ObjectTag getContext(String name) {
        switch (name) {
            case "owner":
                return owner;
            case "island_location":
                return island_location;
            case "location":
                return location;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onPlayerEntersSkyblock(IslandEnterEvent event) {
        island_location = new LocationTag(event.getIslandLocation());
        location = new LocationTag(event.getLocation());
        owner = new PlayerTag(event.getIslandOwner());
        this.event = event;
        fire(event);
    }
}
