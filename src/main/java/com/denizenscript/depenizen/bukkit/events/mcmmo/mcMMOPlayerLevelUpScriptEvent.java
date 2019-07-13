package com.denizenscript.depenizen.bukkit.events.mcmmo;

import com.gmail.nossr50.events.experience.McMMOPlayerLevelUpEvent;
import com.denizenscript.denizen.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ArgumentHelper;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.denizencore.scripts.containers.ScriptContainer;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class mcMMOPlayerLevelUpScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // mcmmo player levels up skill (in <area>)
    // mcmmo player levels up <skill> (in <area>)
    //
    // @Regex ^on mcmmo player levels up [^\s]+( in ((notable (cuboid|ellipsoid))|([^\s]+)))?$
    //
    // @Cancellable true
    //
    // @Triggers when a player levels up an mcMMO skill.
    //
    // @Context
    // <context.skill> returns the name of the skill that levelled up. (Based on the mcMMO language file).
    // <context.levels_gained> returns the number of levels gained.
    // <context.old_level> returns the old level of the skill.
    // <context.new_level> returns the new level of the skill.
    // <context.cause> returns the cause of the level gain.
    // Will be one of: 'PVP', 'PVE', 'VAMPIRISM', 'SHARED_PVP', 'SHARED_PVE', 'COMMAND', 'UNKNOWN'.
    //
    // @Determine
    // Element(Number) to set the number of levels to gain.
    //
    // @Plugin Depenizen, mcMMO
    //
    // -->

    public mcMMOPlayerLevelUpScriptEvent() {
        instance = this;
    }

    public static mcMMOPlayerLevelUpScriptEvent instance;
    public McMMOPlayerLevelUpEvent event;
    public PlayerTag player;
    public ElementTag skill;
    public int new_level;
    public int levels_gained;
    public String cause;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        return CoreUtilities.toLowerCase(s).startsWith("mcmmo player levels up");
    }

    @Override
    public boolean matches(ScriptPath path) {
        String eSkill = path.eventArgLowerAt(4);
        if (!eSkill.equals("skill") && !eSkill.equals(CoreUtilities.toLowerCase(skill.asString()))) {
            return false;
        }

        if (!runInCheck(path, player.getLocation())) {
            return false;
        }

        return true;
    }

    @Override
    public String getName() {
        return "mcMMOPlayerLevelsUp";
    }

    @Override
    public boolean applyDetermination(ScriptContainer container, String determination) {
        String lower = CoreUtilities.toLowerCase(determination);

        if (ArgumentHelper.matchesInteger(lower)) {
            levels_gained = ArgumentHelper.getIntegerFrom(lower);
            return true;
        }
        return super.applyDetermination(container, determination);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(player, null);
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("skill")) {
            return skill;
        }
        else if (name.equals("new_level")) {
            return new ElementTag(new_level);
        }
        else if (name.equals("old_level")) {
            return new ElementTag(new_level - levels_gained);
        }
        else if (name.equals("levels_gained")) {
            return new ElementTag(levels_gained);
        }
        else if (name.equals("cause")) {
            return new ElementTag(cause);
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onmcMMOPlayerLevelUp(McMMOPlayerLevelUpEvent event) {
        if (EntityTag.isNPC(event.getPlayer())) {
            return;
        }
        player = PlayerTag.mirrorBukkitPlayer(event.getPlayer());
        levels_gained = event.getLevelsGained();
        new_level = event.getSkillLevel();
        cause = event.getXpGainReason().toString();
        skill = new ElementTag(event.getSkill().getName());
        this.event = event;
        fire(event);
        event.setLevelsGained(levels_gained);
    }
}
