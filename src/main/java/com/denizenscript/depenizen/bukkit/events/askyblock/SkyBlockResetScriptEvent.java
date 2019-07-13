package com.denizenscript.depenizen.bukkit.events.askyblock;

import com.wasteofplastic.askyblock.events.IslandResetEvent;
import com.denizenscript.denizen.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.dLocation;
import com.denizenscript.denizen.objects.dPlayer;
import com.denizenscript.denizencore.objects.dObject;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.denizencore.scripts.containers.ScriptContainer;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SkyBlockResetScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // skyblock island reset
    //
    // @Regex ^on skyblock island reset$
    //
    // @Cancellable false
    //
    // @Triggers when a new skyblock is reset.
    //
    // @Context
    // <context.owner> Returns the owner of the island.
    // <context.location> Returns the location of the island.
    //
    // @Plugin Depenizen, A SkyBlock
    //
    // -->

    public static SkyBlockResetScriptEvent instance;
    public IslandResetEvent event;
    public dLocation location;
    public dPlayer owner;

    public SkyBlockResetScriptEvent() {
        instance = this;
    }

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        return CoreUtilities.toLowerCase(s).startsWith("skyblock island reset");
    }

    @Override
    public boolean matches(ScriptPath path) {
        return true;
    }

    @Override
    public String getName() {
        return "SkyBlockReset";
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
        return super.getContext(name);
    }

    @EventHandler
    public void onSkyBlockReset(IslandResetEvent event) {
        location = new dLocation(event.getLocation());
        owner = dPlayer.mirrorBukkitPlayer(event.getPlayer());
        this.event = event;
        fire(event);
    }
}
