package com.denizenscript.depenizen.bukkit.events.fabled;

import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import studio.magemonkey.fabled.api.event.PlayerSkillDowngradeEvent;

public class FabledPlayerDowngradesSkillScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // fabled player downgrades
    //
    // @Location true
    //
    // @Cancellable true
    //
    // @Triggers when a player downgrades a skill in Fabled.
    //
    // @Switch skill:<skill> to only process the event if the skill matches the specified matcher.
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
        registerCouldMatcher("fabled player downgrades");
    }

    public PlayerSkillDowngradeEvent event;

    @Override
    public boolean matches(ScriptPath path) {
        if (!runGenericSwitchCheck(path, "skill", event.getDowngradedSkill().toString())) {
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
            case "level" -> new ElementTag(event.getDowngradedSkill().getLevel());
            case "refund" -> new ElementTag(event.getRefund());
            case "skill" -> new ElementTag(event.getDowngradedSkill().getData().getName(), true);
            default -> super.getContext(name);
        };
    }

    @EventHandler
    public void onFabledPlayerDowngradesSkill(PlayerSkillDowngradeEvent event) {
        this.event = event;
        fire(event);
    }
}
