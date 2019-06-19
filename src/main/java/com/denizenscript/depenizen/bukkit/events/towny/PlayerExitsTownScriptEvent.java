package com.denizenscript.depenizen.bukkit.events.towny;

import com.denizenscript.depenizen.bukkit.objects.towny.dTown;
import com.palmergames.bukkit.towny.event.PlayerLeaveTownEvent;
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

public class PlayerExitsTownScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // towny player exits town
    // towny player exits <town>
    //
    // @Regex ^on towny player exits [^\s]+$
    //
    // @Cancellable false
    //
    // @Triggers when a player exits a Towny Town.
    //
    // @Context
    // <context.town> Returns the town the player exited.
    //
    // @Plugin Depenizen, Towny
    //
    // -->

    public PlayerExitsTownScriptEvent() {
        instance = this;
    }

    public static PlayerExitsTownScriptEvent instance;
    public PlayerLeaveTownEvent event;
    public dTown town;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        return CoreUtilities.toLowerCase(s).startsWith("towny player exits");
    }

    @Override
    public boolean matches(ScriptPath path) {
        String name = path.eventArgLowerAt(3);
        dTown eventTown = dTown.fromWorldCoord(event.getFrom());
        if (name.equals("town") && eventTown != null) {
            return true;
        }
        dTown givenTown = dTown.valueOf(name);
        return eventTown != null && givenTown != null && eventTown.equals(givenTown);
    }

    @Override
    public String getName() {
        return "TownyPlayerExitsTown";
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
    public void onTownyPlayerExitsTown(PlayerLeaveTownEvent event) {
        try {
            if (!event.getTo().getTownyWorld().isUsingTowny()) {
                return;
            }
        }
        catch (NotRegisteredException e) {
            return;
        }
        town = dTown.fromWorldCoord(event.getFrom());
        this.event = event;
        fire(event);
    }
}
