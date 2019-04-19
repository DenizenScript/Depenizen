package com.denizenscript.depenizen.bukkit.events.skillapi;

import com.sucy.skill.api.event.PlayerSkillUpgradeEvent;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.ScriptEntryData;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

// <--[event]
// @Events
// skillapi player upgrades skill (in <area>)
// skillapi player upgrades <skill> (in <area>)
//
// @Regex ^on skillapi player upgrades [^\s]+( in ((notable (cuboid|ellipsoid))|([^\s]+)))?$
//
// @Cancellable false
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
// @Plugin DepenizenBukkit, SkillAPI
// -->

public class SkillAPIPlayerUpgradesSkillScriptEvent extends BukkitScriptEvent implements Listener {

    public SkillAPIPlayerUpgradesSkillScriptEvent() {
        instance = this;
    }

    public static SkillAPIPlayerUpgradesSkillScriptEvent instance;
    public PlayerSkillUpgradeEvent event;
    public dPlayer player;
    public Element level;
    public Element skill;
    public Element cost;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        return lower.startsWith("skillapi player upgrades");
    }

    @Override
    public boolean matches(ScriptPath path) {
        String skill = path.eventArgLowerAt(3);

        if (!skill.equals("skill") && !skill.equals(CoreUtilities.toLowerCase(this.skill.asString()))) {
            return false;
        }

        if (!runInCheck(path, player.getLocation())) {
            return false;
        }

        return true;
    }

    @Override
    public String getName() {
        return "SkillAPIPlayerUpgradesSkill";
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
            return level;
        }
        else if (name.equals("cost")) {
            return cost;
        }
        else if (name.equals("skill_name")) {
            return skill;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onSkillAPIPlayerUpgradesSkill(PlayerSkillUpgradeEvent event) {
        if (!dEntity.isPlayer(event.getPlayerData().getPlayer())) {
            return;
        }
        player = dPlayer.mirrorBukkitPlayer(event.getPlayerData().getPlayer());
        level = new Element(event.getUpgradedSkill().getLevel());
        cost = new Element(event.getCost());
        skill = new Element(event.getUpgradedSkill().getData().getName());
        cancelled = event.isCancelled();
        this.event = event;
        fire(event);
         fire(event);;
    }
}
