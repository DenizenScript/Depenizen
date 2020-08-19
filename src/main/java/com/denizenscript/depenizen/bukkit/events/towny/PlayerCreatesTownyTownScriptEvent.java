package com.denizenscript.depenizen.bukkit.events.towny;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.palmergames.bukkit.towny.event.PreNewTownEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerCreatesTownyTownScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // towny player creates town
    //
    // @Regex ^on towny player creates town$
    //
    // @Triggers when a player tries to create a new town. This fires just before the town is created allowing you to cancel the event.
    //
    // @Context
    // <context.town_name> Returns the name of the town.
    //
    // @Player Always
    //
    // @Cancellable true
    //
    // @Plugin Depenizen, Towny
    //
    // @Group Depenizen
    //
    // -->

    public PlayerCreatesTownyTownScriptEvent() {
        instance = this;
    }

    public static PlayerCreatesTownyTownScriptEvent instance;
    public PreNewTownEvent event;

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("towny player creates town");
    }

    @Override
    public boolean matches(ScriptPath path) {
        return super.matches(path);
    }

    @Override
    public String getName() {
        return "PlayerCreatesTownyTown";
    }

    @Override
    public boolean applyDetermination(ScriptPath path, ObjectTag determinationObj) {
        String determination = determinationObj.toString();
        String lower = CoreUtilities.toLowerCase(determination);
        if (lower.startsWith("cancel_message:")) {
            event.setCancelMessage(determination.substring("cancel_message:".length()));
        }
        return super.applyDetermination(path, determinationObj);
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("town_name")) {
            return new ElementTag(event.getTownName());
        }
        else if (name.equals("cancel_message")) {
            return new ElementTag(event.getCancelMessage());
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onPreTownCreated(PreNewTownEvent event) {
        this.event = event;
        fire(event);
    }
}
