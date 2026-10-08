package com.denizenscript.depenizen.bukkit.events.superiorskyblock;

import com.bgsoftware.superiorskyblock.api.events.IslandInviteEvent;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.depenizen.bukkit.objects.superiorskyblock.SuperiorSkyblockIslandTag;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SuperiorSkyblockIslandInviteScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // superiorskyblock player invited to island
    //
    // @Triggers when a player is invited to join a SuperiorSkyblock island
    //
    // @Cancellable true
    //
    // @Context
    // <context.island> returns a SuperiorSkyblockIslandTag of the island.
    // <context.inviter> returns a PlayerTag of the island inviter.
    //
    // @Plugin Depenizen, SuperiorSkyblock
    //
    // @Player Always (the invitee).
    //
    // @Group Depenizen
    //
    // -->

    public SuperiorSkyblockIslandInviteScriptEvent() {
        registerCouldMatcher("superiorskyblock player invited to island");
    }

    public IslandInviteEvent event;

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(event.getTarget().asPlayer());
    }

    @Override
    public ObjectTag getContext(String name) {
        return switch (name) {
            case "island" -> new SuperiorSkyblockIslandTag(event.getIsland());
            case "inviter" -> new PlayerTag(event.getPlayer().asPlayer());
            default -> super.getContext(name);
        };
    }

    @EventHandler
    public void onIslandInvite(IslandInviteEvent event) {
        this.event = event;
        fire(event);
    }
}
