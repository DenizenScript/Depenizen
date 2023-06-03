package com.denizenscript.depenizen.bukkit.events.towny;

import com.denizenscript.depenizen.bukkit.objects.towny.TownTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.palmergames.bukkit.towny.event.player.PlayerExitsFromTownBorderEvent;
import com.palmergames.bukkit.towny.object.TownyWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerExitsTownScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // towny player exits <'town'>
    //
    // @Triggers when a player exits a Towny Town.
    //
    // @Context
    // <context.town> Returns the town the player exited.
    //
    // @Plugin Depenizen, Towny
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->

    public PlayerExitsTownScriptEvent() {
        registerCouldMatcher("towny player exits <'town'>");
    }

    public PlayerExitsFromTownBorderEvent event;
    public TownTag town;

    @Override
    public boolean matches(ScriptPath path) {
        String name = path.eventArgLowerAt(3);
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
    public void onTownyPlayerExitsTown(PlayerExitsFromTownBorderEvent event) {
        TownyWorld world = event.getTo().getTownyWorld();
        if (world == null || !world.isUsingTowny()) {
            return;
        }
        town = TownTag.fromWorldCoord(event.getFrom());
        if (town == null) {
            return;
        }
        this.event = event;
        fire(event);
    }
}
