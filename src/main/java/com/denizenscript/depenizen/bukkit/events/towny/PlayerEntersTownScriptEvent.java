package com.denizenscript.depenizen.bukkit.events.towny;

import com.denizenscript.depenizen.bukkit.objects.towny.TownTag;
import com.palmergames.bukkit.towny.event.PlayerEnterTownEvent;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.denizenscript.denizen.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.denizencore.scripts.containers.ScriptContainer;
import com.denizenscript.denizencore.utilities.CoreUtilities;
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
    // @Cancellable false
    //
    // @Triggers when a player enters a Towny Town.
    //
    // @Context
    // <context.town> Returns the town the player entered.
    //
    // @Plugin Depenizen, Towny
    //
    // -->

    public PlayerEntersTownScriptEvent() {
        instance = this;
    }

    public static PlayerEntersTownScriptEvent instance;
    public PlayerEnterTownEvent event;
    public TownTag town;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        return CoreUtilities.toLowerCase(s).startsWith("towny player enters");
    }

    @Override
    public boolean matches(ScriptPath path) {
        String name = path.eventArgAt(3);
        TownTag eventTown = TownTag.fromWorldCoord(event.getTo());
        if (name.equals("town") && eventTown != null) {
            return true;
        }
        TownTag givenTown = TownTag.valueOf(name);
        return eventTown != null && givenTown != null && eventTown.equals(givenTown);
    }

    @Override
    public String getName() {
        return "TownyPlayerEntersTown";
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(new PlayerTag(event.getPlayer()), null);
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
