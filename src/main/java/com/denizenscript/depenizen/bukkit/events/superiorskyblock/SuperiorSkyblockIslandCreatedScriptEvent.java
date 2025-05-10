package com.denizenscript.depenizen.bukkit.events.superiorskyblock;

import com.bgsoftware.superiorskyblock.api.events.IslandCreateEvent;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.depenizen.bukkit.objects.superiorskyblock.SuperiorSkyblockIslandTag;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SuperiorSkyblockIslandCreatedScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // superiorskyblock island created
    //
    // @Triggers when an island is created
    //
    // @Cancellable true
    //
    // @Context
    // <context.island> returns a SuperiorSkyblockIslandTag of the island.
    //
    // @Determine
    // "NAME:<ElementTag>" to change the name of the island.
    //
    // @Plugin Depenizen, SuperiorSkyblock
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->

    public SuperiorSkyblockIslandCreatedScriptEvent() {
        registerCouldMatcher("superiorskyblock island created");
        this.<SuperiorSkyblockIslandCreatedScriptEvent, ElementTag>registerDetermination("name", ElementTag.class, (evt, context, name) -> {
            evt.event.getIsland().setName(name.asString());
        });
    }

    public IslandCreateEvent event;

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
    public void onIslandCreate(IslandCreateEvent event) {
        this.event = event;
        fire(event);
    }
}
