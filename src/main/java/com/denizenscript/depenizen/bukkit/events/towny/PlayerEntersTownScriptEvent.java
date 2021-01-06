package com.denizenscript.depenizen.bukkit.events.towny;

import com.denizenscript.depenizen.bukkit.objects.towny.TownTag;
import com.palmergames.bukkit.towny.event.PlayerEnterTownEvent;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerEntersTownScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // towny player enters town
    // towny player enters <town>
    //
    // @Regex ^on towny player enters [^\s]+$
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
        instance = this;
    }

    public static PlayerEntersTownScriptEvent instance;
    public PlayerEnterTownEvent event;
    public TownTag town;

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("towny player enters");
    }

    @Override
    public boolean matches(ScriptPath path) {
        String name = path.eventArgAt(3);
        if (!name.equals("town") && !town.equals(TownTag.valueOf(name))) {
            return false;
        }
        return super.matches(path);
    }

    @Override
    public String getName() {
        return "TownyPlayerEntersTown";
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
    public void onTownyPlayerEntersTown(PlayerEnterTownEvent event) {
        try {
            if (!event.getTo().getTownyWorld().isUsingTowny()) {
                return;
            }
        }
        catch (NotRegisteredException e) {
            return;
        }
        town = TownTag.fromWorldCoord(event.getTo());
        this.event = event;
        fire(event);
    }
}
