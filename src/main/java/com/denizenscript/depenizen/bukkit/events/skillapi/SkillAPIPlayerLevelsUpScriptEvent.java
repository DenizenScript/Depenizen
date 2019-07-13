package com.denizenscript.depenizen.bukkit.events.skillapi;

import com.denizenscript.depenizen.bukkit.objects.skillapi.SkillAPIClass;
import com.sucy.skill.api.event.PlayerLevelUpEvent;
import com.denizenscript.denizen.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.dEntity;
import com.denizenscript.denizen.objects.dPlayer;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.denizencore.scripts.containers.ScriptContainer;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SkillAPIPlayerLevelsUpScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // skillapi player levels up (in <area>)
    //
    // @Regex ^on skillapi player levels up( in ((notable (cuboid|ellipsoid))|([^\s]+)))?$
    //
    // @Cancellable false
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
    // -->

    public SkillAPIPlayerLevelsUpScriptEvent() {
        instance = this;
    }

    public static SkillAPIPlayerLevelsUpScriptEvent instance;
    public PlayerLevelUpEvent event;
    public dPlayer player;
    public int level;
    public int gained;
    public SkillAPIClass skillAPIClass;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        return lower.startsWith("skillapi player levels up");
    }

    @Override
    public boolean matches(ScriptPath path) {

        if (!runInCheck(path, player.getLocation())) {
            return false;
        }

        return true;
    }

    @Override
    public String getName() {
        return "SkillAPIPlayerLevelsUp";
    }

    @Override
    public boolean applyDetermination(ScriptContainer container, String determination) {
        return super.applyDetermination(container, determination);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(player, null);
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("level")) {
            return new ElementTag(level);
        }
        else if (name.equals("gained")) {
            return new ElementTag(gained);
        }
        else if (name.equals("class")) {
            return skillAPIClass;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onSkillAPIPlayerLevelsUp(PlayerLevelUpEvent event) {
        if (!dEntity.isPlayer(event.getPlayerData().getPlayer())) {
            return;
        }
        player = dPlayer.mirrorBukkitPlayer(event.getPlayerData().getPlayer());
        level = event.getLevel();
        gained = event.getAmount();
        skillAPIClass = new SkillAPIClass(event.getPlayerClass().getData());
        this.event = event;
        fire(event);
    }
}
