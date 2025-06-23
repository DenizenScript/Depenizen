package com.denizenscript.depenizen.bukkit.events.fabled;

import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import studio.magemonkey.fabled.api.event.PlayerSkillUpgradeEvent;

public class FabledPlayerUpgradesSkillScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // fabled player upgrades skill
    //
    // @Location true
    //
    // @Cancellable true
    //
    // @Triggers when a player upgrades a skill in Fabled.
    //
    // @Switch name:<skill> to only process the event if the skill matches the specified matcher.
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
        registerCouldMatcher("fabled player upgrades skill");
    }

    public PlayerSkillUpgradeEvent event;

    @Override
    public boolean matches(ScriptPath path) {
        if (!runGenericSwitchCheck(path, "name", event.getUpgradedSkill().getData().getName())) {
            return false;
        }
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
            case "level" -> new ElementTag(event.getUpgradedSkill().getLevel());
            case "cost" -> new ElementTag(event.getCost());
            case "skill" -> new ElementTag(event.getUpgradedSkill().getData().getName(), true);
            default -> super.getContext(name);
        };
    }

    @EventHandler
    public void onFabledPlayerUpgradesSkill(PlayerSkillUpgradeEvent event) {
        this.event = event;
        fire(event);
    }
}
