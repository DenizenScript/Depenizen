package com.denizenscript.depenizen.bukkit.events.mcmmo;

import com.gmail.nossr50.events.experience.McMMOPlayerLevelChangeEvent;
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

public class mcMMOPlayerLevelChangeScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // mcmmo player <'skill'> level changes
    //
    // @Location true
    //
    // @Cancellable true
    //
    // @Triggers when a player's mcmmo skill level changes.
    //
    // @Context
    // <context.skill> returns the name of the skill that changed level. (Based on the mcMMO language file).
    // <context.level> returns the level the skill changed to.
    // <context.cause> returns the cause of the level change.
    // Will be one of: 'PVP', 'PVE', 'VAMPIRISM', 'SHARED_PVP', 'SHARED_PVE', 'COMMAND', 'UNKNOWN'.
    //
    // @Plugin Depenizen, mcMMO
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->

    public mcMMOPlayerLevelChangeScriptEvent() {
        registerCouldMatcher("mcmmo player <'skill'> level changes");
    }

    public McMMOPlayerLevelChangeEvent event;
    public PlayerTag player;
    public ElementTag skill;
    public int level;
    public String cause;

    @Override
    public boolean matches(ScriptPath path) {
        String eSkill = path.eventArgLowerAt(2);
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
            level = element.asInt();
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
            case "level":
                return new ElementTag(level);
            case "cause":
                return new ElementTag(cause);
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onmcMMOPlayerLevelChanges(McMMOPlayerLevelChangeEvent event) {
        if (EntityTag.isNPC(event.getPlayer())) {
            return;
        }
        player = PlayerTag.mirrorBukkitPlayer(event.getPlayer());
        level = event.getSkillLevel();
        cause = event.getXpGainReason().toString();
        skill = new ElementTag(event.getSkill().getName());
        this.event = event;
        fire(event);
    }
}
