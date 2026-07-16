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
import studio.magemonkey.fabled.api.event.PlayerSkillDowngradeEvent;

public class FabledPlayerDowngradesSkillScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // fabled player downgrades <'skill'>
    //
    // @Location true
    //
    // @Cancellable true
    //
    // @Triggers when a player downgrades a skill in Fabled.
    //
    // @Context
    // <context.level> returns the level the player went down to.
    // <context.refund> returns how much the player was refunded.
    // <context.skill> returns the name of the skill downgraded.
    //
    // @Plugin Depenizen, Fabled
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->

    public FabledPlayerDowngradesSkillScriptEvent() {
        registerCouldMatcher("fabled|skillapi player downgrades <'skill'>");
    }

    public PlayerSkillDowngradeEvent event;
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
            case "level" -> new ElementTag(event.getDowngradedSkill().getLevel());
            case "refund" -> new ElementTag(event.getRefund());
            case "skill" -> new ElementTag(skill, true);
            default -> super.getContext(name);
        };
    }

    @EventHandler
    public void onFabledPlayerDowngradesSkill(PlayerSkillDowngradeEvent event) {
        this.event = event;
        player = event.getPlayerData().getPlayer();
        skill = event.getDowngradedSkill().getData().getName();
        fire(event);
    }
}
