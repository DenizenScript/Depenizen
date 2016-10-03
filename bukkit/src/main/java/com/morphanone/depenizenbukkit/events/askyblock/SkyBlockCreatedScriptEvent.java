package com.morphanone.depenizenbukkit.events.askyblock;

import com.wasteofplastic.askyblock.events.IslandNewEvent;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.utilities.DenizenAPI;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.ScriptEntryData;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

// <--[event]
// @Events
// skyblock island created
//
// @Regex ^on skyblock island created$
//
// @Cancellable false
//
// @Triggers when a new skyblock is created.
//
// @Context
// <context.owner> Returns the owner of the island.
// <context.location> Returns the location of the island.
// <context.schematic> Returns the name of the schematic used for the island.
//
// @Plugin Depenizen, A SkyBlock
//
// -->

public class SkyBlockCreatedScriptEvent extends BukkitScriptEvent implements Listener {

    public static SkyBlockCreatedScriptEvent instance;
    public IslandNewEvent event;
    public dLocation location;
    public Element schematic;
    public dPlayer owner;

    public SkyBlockCreatedScriptEvent() {
        instance = this;
    }

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        return CoreUtilities.toLowerCase(s).startsWith("skyblock island created");
    }

    @Override
    public boolean matches(ScriptContainer scriptContainer, String s) {
        return true;
    }

    @Override
    public String getName() {
        return "SkyBlockCreated";
    }

    @Override
    public void init() {
        Bukkit.getServer().getPluginManager().registerEvents(this, DenizenAPI.getCurrentInstance());
    }

    @Override
    public void destroy() {
        IslandNewEvent.getHandlerList().unregister(this);
    }

    @Override
    public boolean applyDetermination(ScriptContainer container, String determination) {
        return super.applyDetermination(container, determination);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(null, null);
    }

    @Override
    public dObject getContext(String name) {
        if (name.equals("owner")) {
            return owner;
        }
        else if (name.equals("location")) {
            return location;
        }
        else if (name.equals("schematic")) {
            return schematic;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onSkyBlockCreated(IslandNewEvent event) {
        location = new dLocation(event.getIslandLocation());
        schematic = new Element(event.getSchematicName().getName());
        owner = dPlayer.mirrorBukkitPlayer(event.getPlayer());
        this.event = event;
        fire();
    }

}
