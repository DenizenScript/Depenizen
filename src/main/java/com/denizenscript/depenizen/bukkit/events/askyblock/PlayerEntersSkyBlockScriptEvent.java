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
    // player enters skyblock
    //
    // @Regex ^on player enters skyblock$
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
    // -->

    public PlayerEntersSkyBlockScriptEvent instance;
    public IslandEnterEvent event;
    public LocationTag location;
    public LocationTag island_location;
    public PlayerTag owner;

    public PlayerEntersSkyBlockScriptEvent() {
        instance = this;
    }

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("player enters skyblock");
    }

    @Override
    public String getName() {
        return "PlayerEntersSkyBlock";
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(new PlayerTag(event.getPlayer()), null);
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("owner")) {
            return owner;
        }
        else if (name.equals("island_location")) {
            return island_location;
        }
        else if (name.equals("location")) {
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
