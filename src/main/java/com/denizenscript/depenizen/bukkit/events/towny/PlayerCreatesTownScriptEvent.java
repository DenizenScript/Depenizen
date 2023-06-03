package com.denizenscript.depenizen.bukkit.events.towny;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.denizenscript.depenizen.bukkit.objects.towny.TownTag;
import com.palmergames.bukkit.towny.event.PreNewTownEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerCreatesTownScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // towny player creates town
    //
    // @Location true
    //
    // @Cancellable true
    //
    // @Triggers when a player tries to create a Towny town.
    //
    // @Context
    // <context.town_name> Returns the name of the town the player is creating.
    // <context.cuboid> Returns the cuboid that will be claimed by the town.
    //
    // @Determine
    // "CANCEL_MESSAGE:<ElementTag>" to set the message Towny sends when cancelled.
    //
    // @Plugin Depenizen, Towny
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->

    public PlayerCreatesTownScriptEvent() {
        registerCouldMatcher("towny player creates town");
    }

    public PreNewTownEvent event;

    @Override
    public boolean matches(ScriptPath path) {
        if (!runInCheck(path, event.getPlayer().getLocation())) {
            return false;
        }
        return super.matches(path);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(event.getPlayer());
    }

    @Override
    public ObjectTag getContext(String name) {
        switch (name) {
            case "town_name":
                return new ElementTag(event.getTownName());
            case "cuboid":
                return TownTag.getCuboid(event.getTownLocation().getWorld(), event.getTownWorldCoord().getX(), event.getTownWorldCoord().getZ());
        }
        return super.getContext(name);
    }

    @Override
    public boolean applyDetermination(ScriptPath path, ObjectTag determinationObj) {
        if (determinationObj instanceof ElementTag) {
            String determination = determinationObj.toString();
            String lower = CoreUtilities.toLowerCase(determination);
            if (lower.startsWith("cancel_message:")) {
                event.setCancelMessage(determination.substring("cancel_message:".length()));
                return true;
            }
        }
        return super.applyDetermination(path, determinationObj);
    }

    @EventHandler
    public void onTownyPlayerCreatesTown(PreNewTownEvent event) {
        this.event = event;
        fire(event);
    }
}
