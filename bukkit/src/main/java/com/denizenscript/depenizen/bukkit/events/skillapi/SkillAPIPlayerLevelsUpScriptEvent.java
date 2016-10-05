package com.denizenscript.depenizen.bukkit.events.skillapi;

import com.denizenscript.depenizen.bukkit.objects.skillapi.SkillAPIClass;
import com.sucy.skill.api.event.PlayerLevelUpEvent;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.utilities.DenizenAPI;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.ScriptEntryData;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

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
// @Plugin DepenizenBukkit, SkillAPI
// -->

public class SkillAPIPlayerLevelsUpScriptEvent extends BukkitScriptEvent implements Listener {

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
    public boolean matches(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);

        if (!runInCheck(scriptContainer, s, lower, player.getLocation())) {
            return false;
        }

        return true;
    }

    @Override
    public String getName() {
        return "SkillAPIPlayerLevelsUp";
    }

    @Override
    public void init() {
        Bukkit.getServer().getPluginManager().registerEvents(this, DenizenAPI.getCurrentInstance());
    }

    @Override
    public void destroy() {
        PlayerLevelUpEvent.getHandlerList().unregister(this);
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
    public dObject getContext(String name) {
        if (name.equals("level")) {
            return new Element(level);
        }
        else if (name.equals("gained")) {
            return new Element(gained);
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
        fire();
    }
}
