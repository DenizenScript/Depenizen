package com.denizenscript.depenizen.bukkit.events.towny;

import com.denizenscript.depenizen.bukkit.objects.towny.TownTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.palmergames.bukkit.towny.event.player.PlayerEntersIntoTownBorderEvent;
import com.palmergames.bukkit.towny.object.TownyWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerEntersTownScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // towny player enters <'town'>
    //
    // @Triggers when a player enters a Towny Town.
    //
    // @Context
    // <context.town> Returns the town the player entered.
    //
    // @Plugin Depenizen, Towny
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->

    public PlayerEntersTownScriptEvent() {
        registerCouldMatcher("towny player enters <'town'>");
    }

    public PlayerEntersIntoTownBorderEvent event;
    public TownTag town;

    @Override
    public boolean matches(ScriptPath path) {
        String name = path.eventArgAt(3);
        if (!name.equals("town") && !town.equals(TownTag.valueOf(name, getTagContext(path)))) {
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
        if (name.equals("town")) {
            return town;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onTownyPlayerEntersTown(PlayerEntersIntoTownBorderEvent event) {
        TownyWorld world = event.getTo().getTownyWorld();
        if (world == null || !world.isUsingTowny()) {
            return;
        }
        town = TownTag.fromWorldCoord(event.getTo());
        this.event = event;
        fire(event);
    }
}
