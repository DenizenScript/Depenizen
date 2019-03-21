package com.denizenscript.depenizen.bukkit.events.mcmmo;

import com.gmail.nossr50.events.experience.McMMOPlayerLevelDownEvent;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.ScriptEntryData;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

// <--[event]
// @Events
// mcmmo player levels down skill (in <area>)
// mcmmo player levels down <skill> (in <area>)
//
// @Regex ^on mcmmo player levels down [^\s]+( in ((notable (cuboid|ellipsoid))|([^\s]+)))?$
//
// @Cancellable true
//
// @Triggers when a player loses levels for an mcMMO skill.
//
// @Context
// <context.skill> returns the name of the skill that lost levels. (Based on the mcMMO language file).
// <context.levels_lost> returns the number of levels lost.
// <context.old_level> returns the old level of the skill.
// <context.new_level> returns the new level of the skill.
// <context.cause> returns the cause of the level loss.
// Will be one of: 'PVP', 'PVE', 'VAMPIRISM', 'SHARED_PVP', 'SHARED_PVE', 'COMMAND', 'UNKNOWN'.
//
// @Determine
// Element(Number) to set the number of levels to gain.
//
// @Plugin DepenizenBukkit, mcMMO
// -->

public class mcMMOPlayerLevelDownScriptEvent extends BukkitScriptEvent implements Listener {

    public mcMMOPlayerLevelDownScriptEvent() {
        instance = this;
    }

    public static mcMMOPlayerLevelDownScriptEvent instance;
    public McMMOPlayerLevelDownEvent event;
    public dPlayer player;
    public Element skill;
    public int new_level;
    public int levels_lost;
    public Element cause;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        return CoreUtilities.toLowerCase(s).startsWith("mcmmo player levels down");
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
        return "mcMMOPlayerLevelsDown";
    }

    @Override
    public boolean applyDetermination(ScriptContainer container, String determination) {
        String lower = CoreUtilities.toLowerCase(determination);

        if (aH.matchesInteger(lower)) {
            levels_lost = aH.getIntegerFrom(lower);
            return true;
        }
        return super.applyDetermination(container, determination);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(player, null);
    }

    @Override
    public dObject getContext(String name) {
        if (name.equals("skill")) {
            return skill;
        }
        else if (name.equals("new_level")) {
            return new Element(new_level);
        }
        else if (name.equals("old_level")) {
            return new Element(new_level + levels_lost);
        }
        else if (name.equals("levels_lost")) {
            return new Element(levels_lost);
        }
        else if (name.equals("cause")) {
            return cause;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onmcMMOPlayerLevelDown(McMMOPlayerLevelDownEvent event) {
        if (dEntity.isNPC(event.getPlayer())) {
            return;
        }
        player = dPlayer.mirrorBukkitPlayer(event.getPlayer());
        levels_lost = event.getLevelsLost();
        new_level = event.getSkillLevel();
        cause = new Element(event.getXpGainReason().toString());
        skill = new Element(event.getSkill().getName());
        cancelled = event.isCancelled();
        this.event = event;
        fire();
        event.setCancelled(cancelled);
        event.setLevelsLost(levels_lost);
    }
}
