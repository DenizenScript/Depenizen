package com.denizenscript.depenizen.bukkit.events.skillapi;

import com.sucy.skill.api.event.PlayerSkillUpgradeEvent;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SkillAPIPlayerUpgradesSkillScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // skillapi player upgrades <'skill'>
    //
    // @Location true
    //
    // @Triggers when a player upgrades a skill in SkillAPI.
    //
    // @Context
    // <context.level> returns the level the player went up to.
    // <context.cost> returns how much the upgrade cost.
    // <context.skill_name> returns the name of the skill upgraded.
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

    public SkillAPIPlayerUpgradesSkillScriptEvent() {
        registerCouldMatcher("skillapi player upgrades <'skill'>");
    }

    public PlayerSkillUpgradeEvent event;
    public PlayerTag player;
    public ElementTag level;
    public ElementTag skill;
    public ElementTag cost;

    @Override
    public boolean matches(ScriptPath path) {
        String skill = path.eventArgLowerAt(3);

        if (!skill.equals("skill") && !skill.equals(CoreUtilities.toLowerCase(this.skill.asString()))) {
            return false;
        }

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
                return level;
            case "cost":
                return cost;
            case "skill_name":
                return skill;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onSkillAPIPlayerUpgradesSkill(PlayerSkillUpgradeEvent event) {
        if (!EntityTag.isPlayer(event.getPlayerData().getPlayer())) {
            return;
        }
        player = PlayerTag.mirrorBukkitPlayer(event.getPlayerData().getPlayer());
        level = new ElementTag(event.getUpgradedSkill().getLevel());
        cost = new ElementTag(event.getCost());
        skill = new ElementTag(event.getUpgradedSkill().getData().getName());
        this.event = event;
        fire(event);
    }
}
