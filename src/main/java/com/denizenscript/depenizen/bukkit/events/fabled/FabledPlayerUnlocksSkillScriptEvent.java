package com.denizenscript.depenizen.bukkit.events.fabled;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.depenizen.bukkit.bridges.FabledBridge;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import studio.magemonkey.fabled.api.event.PlayerSkillUnlockEvent;

public class FabledPlayerUnlocksSkillScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // fabled player unlocks <'skill'>
    //
    // @Location true
    //
    // @Triggers when a player unlocks a skill in Fabled.
    //
    // @Context
    // <context.skill> returns the name of the skill unlocked.
    //
    // @Plugin Depenizen, Fabled
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->

    public FabledPlayerUnlocksSkillScriptEvent() {
        registerCouldMatcher("fabled|skillapi player unlocks <'skill'>");
    }

    public PlayerSkillUnlockEvent event;
    public Player player;
    public String skill;

    @Override
    public boolean matches(ScriptPath path) {
        if (path.eventArgLowerAt(0).equals("skillapi")) {
            FabledBridge.oldSkillApiEvents.warn();
        }
        String skill = path.eventArgLowerAt(3);
        if (!skill.equals("skill") && !skill.equals(this.skill)) {
            return false;
        }
        if (!runInCheck(path, player.getLocation())) {
            return false;
        }
        return super.matches(path);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(player);
    }

    @Override
    public ObjectTag getContext(String name) {
        return switch (name) {
            case "skill" -> new ElementTag(skill, true);
            default -> super.getContext(name);
        };
    }

    @EventHandler
    public void onFabledPlayerUnlocksSkill(PlayerSkillUnlockEvent event) {
        this.event = event;
        player = event.getPlayerData().getPlayer();
        skill = event.getUnlockedSkill().getData().getName();
        fire(event);
    }
}
