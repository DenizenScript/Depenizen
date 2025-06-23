package com.denizenscript.depenizen.bukkit.events.fabled;

import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import studio.magemonkey.fabled.api.event.PlayerSkillUnlockEvent;

public class FabledPlayerUnlocksSkillScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // fabled player unlocks skill
    //
    // @Location true
    //
    // @Triggers when a player unlocks a skill in Fabled.
    //
    // @Switch name:<skill> to only process the event if the skill matches the specified matcher.
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
        registerCouldMatcher("fabled player unlocks skill");
    }

    public PlayerSkillUnlockEvent event;

    @Override
    public boolean matches(ScriptPath path) {
        if (!runGenericSwitchCheck(path, "name", event.getUnlockedSkill().toString())) {
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
            case "skill" -> new ElementTag(event.getUnlockedSkill().getData().getName(), true);
            default -> super.getContext(name);
        };
    }

    @EventHandler
    public void onFabledPlayerUnlocksSkill(PlayerSkillUnlockEvent event) {
        this.event = event;
        fire(event);
    }
}
