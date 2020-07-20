package com.denizenscript.depenizen.bukkit.events.towny;

import com.denizenscript.depenizen.bukkit.objects.towny.TownTag;
import com.palmergames.bukkit.towny.event.PlayerLeaveTownEvent;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
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
        instance = this;
    }

    public static PlayerExitsTownScriptEvent instance;
    public PlayerLeaveTownEvent event;
    public TownTag town;

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("towny player exits");
    }

    @Override
    public boolean matches(ScriptPath path) {
        String name = path.eventArgLowerAt(3);
        if (!name.equals("town") && !town.equals(TownTag.valueOf(name))) {
            return false;
        }
        return super.matches(path);
    }

    @Override
    public String getName() {
        return "TownyPlayerExitsTown";
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
    public void onTownyPlayerExitsTown(PlayerLeaveTownEvent event) {
        try {
            if (!event.getTo().getTownyWorld().isUsingTowny()) {
                return;
            }
        }
        catch (NotRegisteredException e) {
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
