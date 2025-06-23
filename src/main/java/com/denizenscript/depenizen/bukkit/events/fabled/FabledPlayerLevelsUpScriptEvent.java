package com.denizenscript.depenizen.bukkit.events.fabled;

import com.denizenscript.depenizen.bukkit.objects.fabled.FabledClassTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import studio.magemonkey.fabled.api.event.PlayerLevelUpEvent;

public class FabledPlayerLevelsUpScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // fabled player levels up
    //
    // @Location true
    //
    // @Triggers when a player levels up in Fabled.
    //
    // @Context
    // <context.level> returns the level the player went up to.
    // <context.gained> returns how many levels the player gained.
    // <context.class> returns the FabledClass the player is leveling up in.
    //
    // @Plugin Depenizen, Fabled
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->

    public FabledPlayerLevelsUpScriptEvent() {
        registerCouldMatcher("fabled player levels up");
    }

    public PlayerLevelUpEvent event;

    @Override
    public boolean matches(ScriptPath path) {
        if (!runInCheck(path, event.getPlayerData().getPlayer().getLocation())) {
            return false;
        }
        return super.matches(path);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(event.getPlayerData().getPlayer());
    }

    @Override
    public ObjectTag getContext(String name) {
        return switch (name) {
            case "level" -> new ElementTag(event.getLevel());
            case "gained" -> new ElementTag(event.getAmount());
            case "class" -> new FabledClassTag(event.getPlayerClass().getData());
            default -> super.getContext(name);
        };
    }

    @EventHandler
    public void onFabledPlayerLevelsUp(PlayerLevelUpEvent event) {
        this.event = event;
        fire(event);
    }
}
