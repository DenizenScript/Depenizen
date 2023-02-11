package com.denizenscript.depenizen.bukkit.events.mcmmo;

import com.gmail.nossr50.events.experience.McMMOPlayerLevelUpEvent;
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

public class mcMMOPlayerLevelUpScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // mcmmo player levels up <'skill'>
    //
    // @Location true
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
    // ElementTag(Number) to set the number of levels to gain.
    //
    // @Plugin Depenizen, mcMMO
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->

    public mcMMOPlayerLevelUpScriptEvent() {
        registerCouldMatcher("mcmmo player levels up <'skill'>");
    }

    public McMMOPlayerLevelUpEvent event;
    public PlayerTag player;
    public ElementTag skill;
    public int new_level;
    public int levels_gained;
    public String cause;

    @Override
    public boolean matches(ScriptPath path) {
        String eSkill = path.eventArgLowerAt(4);
        if (!eSkill.equals("skill") && !eSkill.equals(CoreUtilities.toLowerCase(skill.asString()))) {
            return false;
        }

        if (!runInCheck(path, player.getLocation())) {
            return false;
        }

        return super.matches(path);
    }

    @Override
    public boolean applyDetermination(ScriptPath path, ObjectTag determinationObj) {
        if (determinationObj instanceof ElementTag element && element.isInt()) {
            levels_gained = element.asInt();
            event.setLevelsGained(levels_gained);
            return true;
        }
        return super.applyDetermination(path, determinationObj);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(player, null);
    }

    @Override
    public ObjectTag getContext(String name) {
        switch (name) {
            case "skill":
                return skill;
            case "new_level":
                return new ElementTag(new_level);
            case "old_level":
                return new ElementTag(new_level - levels_gained);
            case "levels_gained":
                return new ElementTag(levels_gained);
            case "cause":
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
    }
}
