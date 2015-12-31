package net.gnomeffinway.depenizen.events.mcMMO;

import com.gmail.nossr50.datatypes.skills.SkillType;
import com.gmail.nossr50.events.experience.McMMOPlayerLevelDownEvent;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.utilities.DenizenAPI;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.ScriptEntryData;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

// <--[event]
// @Events
// mcmmo player levels down skill
// mcmmo player levels down <skill>
//
// @Regex ^on mcmmo player levels down [^\s]+$
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
// -->

public class mcMMOPlayerLevelDownScriptEvent extends BukkitScriptEvent implements Listener {

    public mcMMOPlayerLevelDownScriptEvent() {
        instance = this;
    }

    public static mcMMOPlayerLevelDownScriptEvent instance;

    public static McMMOPlayerLevelDownEvent event;
    public dPlayer player;
    public String skill;
    public int new_level;
    public int levels_lost;
    public String cause;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        return CoreUtilities.toLowerCase(s).startsWith("mcmmo player levels down");
    }

    @Override
    public boolean matches(ScriptContainer scriptContainer, String s) {
        String arg = CoreUtilities.getXthArg(4, s).toUpperCase();
        SkillType eventSkill = SkillType.valueOf(arg);
        if (arg.equals("SKILL") || (eventSkill != null && eventSkill == event.getSkill())) {
            return true;
        }
        return false;
    }

    @Override
    public String getName() {
        return "mcMMOPlayerLevelsDown";
    }

    @Override
    public void init() {
        Bukkit.getServer().getPluginManager().registerEvents(this, DenizenAPI.getCurrentInstance());
    }

    @Override
    public void destroy() {
        McMMOPlayerLevelDownEvent.getHandlerList().unregister(this);
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
        return new BukkitScriptEntryData(dEntity.isPlayer(event.getPlayer()) ? dEntity.getPlayerFrom(event.getPlayer()) : null, null);
    }

    @Override
    public dObject getContext(String name) {
        if (name.equals("skill")) {
            return new Element(skill);
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
            return new Element(cause);
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onmcMMOPlayerLevelDown(McMMOPlayerLevelDownEvent event) {
        if (dEntity.isNPC(event.getPlayer())) {
            return;
        }
        levels_lost = event.getLevelsLost();
        new_level = event.getSkillLevel();
        cause = event.getXpGainReason().toString();
        skill = event.getSkill().getName();
        cancelled = event.isCancelled();
        this.event = event;
        fire();
        event.setCancelled(cancelled);
        event.setLevelsLost(levels_lost);
    }
}
