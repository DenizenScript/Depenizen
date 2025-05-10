package com.denizenscript.depenizen.bukkit.events.superiorskyblock;

import com.bgsoftware.superiorskyblock.api.events.IslandDisbandEvent;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.depenizen.bukkit.objects.superiorskyblock.SuperiorSkyblockIslandTag;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SuperiorSkyblockIslandDisbandedScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // superiorskyblock island disbanded
    //
    // @Triggers when an island is disbanded
    //
    // @Cancellable true
    //
    // @Context
    // <context.island> returns a SuperiorSkyblockIslandTag of the island.
    //
    // @Plugin Depenizen, SuperiorSkyblock
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->

    public SuperiorSkyblockIslandDisbandedScriptEvent() {
        registerCouldMatcher("superiorskyblock island disbanded");
    }

    public IslandDisbandEvent event;

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(event.getPlayer().asPlayer());
    }

    @Override
    public ObjectTag getContext(String name) {
        return switch (name) {
            case "island" -> new SuperiorSkyblockIslandTag(event.getIsland());
            default -> super.getContext(name);
        };
    }

    @EventHandler
    public void onIslandDisband(IslandDisbandEvent event) {
        this.event = event;
        fire(event);
    }
}
