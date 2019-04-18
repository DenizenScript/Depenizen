package com.denizenscript.depenizen.bukkit.events.towny;

import com.denizenscript.depenizen.bukkit.objects.dTown;
import com.palmergames.bukkit.towny.event.PlayerEnterTownEvent;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.ScriptEntryData;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

// <--[event]
// @Events
// towny player enters town
// towny player enters <town>
//
// @Regex ^on towny player enters [^\s]+$
//
// @Cancellable false
//
// @Triggers when a player enters a Towny Town.
//
// @Context
// <context.town> Returns the town the player entered.
//
// @Plugin DepenizenBukkit, Towny
//
// -->

public class PlayerEntersTownScriptEvent extends BukkitScriptEvent implements Listener {

    public PlayerEntersTownScriptEvent() {
        instance = this;
    }

    public static PlayerEntersTownScriptEvent instance;
    public PlayerEnterTownEvent event;
    public dTown town;


    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        return CoreUtilities.toLowerCase(s).startsWith("towny player enters");
    }

    @Override
    public boolean matches(ScriptPath path) {
        String name = path.eventArgAt(3);
        dTown eventTown = dTown.fromWorldCoord(event.getTo());
        if (name.equals("town") && eventTown != null) {
            return true;
        }
        dTown givenTown = dTown.valueOf(name);
        return eventTown != null && givenTown != null && eventTown.equals(givenTown);
    }

    @Override
    public String getName() {
        return "TownyPlayerEntersTown";
    }

    @Override
    public boolean applyDetermination(ScriptContainer container, String determination) {
        return super.applyDetermination(container, determination);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(new dPlayer(event.getPlayer()), null);
    }

    @Override
    public dObject getContext(String name) {
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
        town = dTown.fromWorldCoord(event.getTo());
        this.event = event;
        fire();
    }
}
