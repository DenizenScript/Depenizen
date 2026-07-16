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
import studio.magemonkey.fabled.api.event.PlayerSkillUpgradeEvent;

public class FabledPlayerUpgradesSkillScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // fabled player upgrades <'skill'>
    //
    // @Location true
    //
    // @Cancellable true
    //
    // @Triggers when a player upgrades a skill in Fabled.
    //
    // @Context
    // <context.level> returns the level the player went up to.
    // <context.cost> returns how much the upgrade cost.
    // <context.skill> returns the name of the skill upgraded.
    //
    // @Plugin Depenizen, Fabled
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->

    public FabledPlayerUpgradesSkillScriptEvent() {
        registerCouldMatcher("fabled|skillapi player upgrades <'skill'>");
    }

    public PlayerSkillUpgradeEvent event;
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
            case "level" -> new ElementTag(event.getUpgradedSkill().getLevel());
            case "cost" -> new ElementTag(event.getCost());
            case "skill" -> new ElementTag(skill, true);
            default -> super.getContext(name);
        };
    }

    @EventHandler
    public void onFabledPlayerUpgradesSkill(PlayerSkillUpgradeEvent event) {
        this.event = event;
        player = event.getPlayerData().getPlayer();
        skill = event.getUpgradedSkill().getData().getName();
        fire(event);
    }
}
