package com.denizenscript.depenizen.bukkit.events.askyblock;

import com.wasteofplastic.askyblock.events.IslandNewEvent;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SkyBlockCreatedScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // skyblock island created
    //
    // @Regex ^on skyblock island created$
    //
    // @Triggers when a new skyblock is created.
    //
    // @Context
    // <context.location> Returns the location of the island.
    // <context.schematic> Returns the name of the schematic used for the island.
    //
    // @Plugin Depenizen, A SkyBlock
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->

    public IslandNewEvent event;
    public LocationTag location;
    public ElementTag schematic;
    public PlayerTag owner;

    public SkyBlockCreatedScriptEvent() {
    }

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("skyblock island created");
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(owner, null);
    }

    @Override
    public ObjectTag getContext(String name) {
        return switch (name) {
            case "location" -> location;
            case "schematic" -> schematic;
            default -> super.getContext(name);
        };
    }

    @EventHandler
    public void onSkyBlockCreated(IslandNewEvent event) {
        location = new LocationTag(event.getIslandLocation());
        schematic = new ElementTag(event.getSchematicName().getName());
        owner = PlayerTag.mirrorBukkitPlayer(event.getPlayer());
        this.event = event;
        fire(event);
    }
}
