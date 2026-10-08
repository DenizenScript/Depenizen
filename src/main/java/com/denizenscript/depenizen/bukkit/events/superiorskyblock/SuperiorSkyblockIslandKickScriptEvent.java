package com.denizenscript.depenizen.bukkit.events.superiorskyblock;

import com.bgsoftware.superiorskyblock.api.events.IslandKickEvent;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.depenizen.bukkit.objects.superiorskyblock.SuperiorSkyblockIslandTag;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SuperiorSkyblockIslandKickScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // superiorskyblock player kicked from island
    //
    // @Triggers when a player is kicked from a SuperiorSkyblock island
    //
    // @Cancellable true
    //
    // @Context
    // <context.island> returns a SuperiorSkyblockIslandTag of the island.
    // <context.kicker> returns a PlayerTag of who kicked the player.
    //
    // @Plugin Depenizen, SuperiorSkyblock
    //
    // @Player Always (the kicked).
    //
    // @Group Depenizen
    //
    // -->

    public SuperiorSkyblockIslandKickScriptEvent() {
        registerCouldMatcher("superiorskyblock player kicked from island");
    }

    public IslandKickEvent event;

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(event.getTarget().asPlayer());
    }

    @Override
    public ObjectTag getContext(String name) {
        return switch (name) {
            case "island" -> new SuperiorSkyblockIslandTag(event.getIsland());
            case "kicker" -> new PlayerTag(event.getPlayer().asPlayer());
            default -> super.getContext(name);
        };
    }

    @EventHandler
    public void onIslandKick(IslandKickEvent event) {
        this.event = event;
        fire(event);
    }
}
