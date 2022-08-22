package com.denizenscript.depenizen.bukkit.events.skillapi;

import com.denizenscript.depenizen.bukkit.objects.skillapi.SkillAPIClassTag;
import com.sucy.skill.api.event.PlayerLevelUpEvent;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SkillAPIPlayerLevelsUpScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // skillapi player levels up
    //
    // @Location true
    //
    // @Triggers when a player levels up in SkillAPI.
    //
    // @Context
    // <context.level> returns the level the player went up to.
    // <context.gained> returns how many levels the player gained.
    // <context.class> returns the SkillAPIClass the player is leveling up in.
    //
    // @Determine
    // None
    //
    // @Plugin Depenizen, SkillAPI
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->

    public SkillAPIPlayerLevelsUpScriptEvent() {
        registerCouldMatcher("skillapi player levels up");
    }

    public PlayerLevelUpEvent event;
    public PlayerTag player;
    public int level;
    public int gained;
    public SkillAPIClassTag skillAPIClass;

    @Override
    public boolean matches(ScriptPath path) {

        if (!runInCheck(path, player.getLocation())) {
            return false;
        }

        return super.matches(path);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(player, null);
    }

    @Override
    public ObjectTag getContext(String name) {
        switch (name) {
            case "level":
                return new ElementTag(level);
            case "gained":
                return new ElementTag(gained);
            case "class":
                return skillAPIClass;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onSkillAPIPlayerLevelsUp(PlayerLevelUpEvent event) {
        if (!EntityTag.isPlayer(event.getPlayerData().getPlayer())) {
            return;
        }
        player = PlayerTag.mirrorBukkitPlayer(event.getPlayerData().getPlayer());
        level = event.getLevel();
        gained = event.getAmount();
        skillAPIClass = new SkillAPIClassTag(event.getPlayerClass().getData());
        this.event = event;
        fire(event);
    }
}
