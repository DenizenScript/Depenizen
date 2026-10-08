package com.denizenscript.depenizen.bukkit.events.superiorskyblock;

import com.bgsoftware.superiorskyblock.api.events.IslandLeaveEvent;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.depenizen.bukkit.objects.superiorskyblock.SuperiorSkyblockIslandTag;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SuperiorSkyblockPlayerLeavesIslandScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // superiorskyblock player leaves island
    //
    // @Triggers when a player leaves the boundaries of a SuperiorSkyblock island
    //
    // @Cancellable true
    //
    // @Context
    // <context.island> returns a SuperiorSkyblockIslandTag of the island.
    // <context.cause> returns why the player left the area. Possible causes can be found at <@link url https://bg-software.com/superiorskyblock/api/com/bgsoftware/superiorskyblock/api/events/IslandLeaveEvent.LeaveCause.html>.
    //
    // @Plugin Depenizen, SuperiorSkyblock
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->

    public SuperiorSkyblockPlayerLeavesIslandScriptEvent() {
        registerCouldMatcher("superiorskyblock player leaves island");
    }

    public IslandLeaveEvent event;

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(event.getPlayer().asPlayer());
    }

    @Override
    public ObjectTag getContext(String name) {
        return switch (name) {
            case "island" -> new SuperiorSkyblockIslandTag(event.getIsland());
            case "cause" -> new ElementTag(event.getCause());
            default -> super.getContext(name);
        };
    }

    @EventHandler
    public void onIslandLeave(IslandLeaveEvent event) {
        this.event = event;
        fire(event);
    }
}
