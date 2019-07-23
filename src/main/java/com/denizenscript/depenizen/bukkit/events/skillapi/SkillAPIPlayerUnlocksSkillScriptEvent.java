package com.denizenscript.depenizen.bukkit.events.skillapi;

import com.sucy.skill.api.event.PlayerSkillUnlockEvent;
import com.denizenscript.denizen.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.denizencore.scripts.containers.ScriptContainer;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SkillAPIPlayerUnlocksSkillScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // skillapi player unlocks skill (in <area>)
    // skillapi player unlocks <skill> (in <area>)
    //
    // @Regex ^on skillapi player unlocks [^\s]+( in ((notable (cuboid|ellipsoid))|([^\s]+)))?$
    //
    // @Cancellable false
    //
    // @Triggers when a player unlocks a skill in SkillAPI.
    //
    // @Context
    // <context.skill_name> returns the name of the skill unlocked.
    //
    // @Determine
    // None
    //
    // @Plugin Depenizen, SkillAPI
    // -->

    public SkillAPIPlayerUnlocksSkillScriptEvent() {
        instance = this;
    }

    public static SkillAPIPlayerUnlocksSkillScriptEvent instance;
    public PlayerSkillUnlockEvent event;
    public PlayerTag player;
    public ElementTag skill;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        return lower.startsWith("skillapi player unlocks");
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
        return "SkillAPIPlayerUnlocksSkill";
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(player, null);
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("skill_name")) {
            return skill;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onSkillAPIPlayerUnlocksSkill(PlayerSkillUnlockEvent event) {
        if (!EntityTag.isPlayer(event.getPlayerData().getPlayer())) {
            return;
        }
        player = PlayerTag.mirrorBukkitPlayer(event.getPlayerData().getPlayer());
        skill = new ElementTag(event.getUnlockedSkill().getData().getName());
        this.event = event;
        fire(event);
    }
}
