package com.denizenscript.depenizen.bukkit.events.mcmmo;

import com.gmail.nossr50.events.experience.McMMOPlayerLevelChangeEvent;
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
// mcmmo player skill level changes (in <area>)
// mcmmo player <skill> level changes (in <area>)
//
// @Regex ^on mcmmo player [^\s]+ level changes( in ((notable (cuboid|ellipsoid))|([^\s]+)))?$
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
// @Plugin DepenizenBukkit, mcMMO
//
// -->

public class mcMMOPlayerLevelChangeScriptEvent extends BukkitScriptEvent implements Listener {

    public mcMMOPlayerLevelChangeScriptEvent() {
        instance = this;
    }

    public static mcMMOPlayerLevelChangeScriptEvent instance;
    public McMMOPlayerLevelChangeEvent event;
    public dPlayer player;
    public Element skill;
    public int level;
    public String cause;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        return lower.startsWith("mcmmo player") && CoreUtilities.getXthArg(3, lower).equals("level");
    }

    @Override
    public boolean matches(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        String eSkill = CoreUtilities.getXthArg(2, lower);
        if (!eSkill.equals("skill") && !eSkill.equals(CoreUtilities.toLowerCase(skill.asString()))) {
            return false;
        }

        if (!runInCheck(scriptContainer, s, lower, player.getLocation())) {
            return false;
        }

        return true;
    }

    @Override
    public String getName() {
        return "mcMMOPlayerLevelChanges";
    }

    @Override
    public boolean applyDetermination(ScriptContainer container, String determination) {
        String lower = CoreUtilities.toLowerCase(determination);

        if (aH.matchesInteger(lower)) {
            level = aH.getIntegerFrom(lower);
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
        else if (name.equals("level")) {
            return new Element(level);
        }
        else if (name.equals("cause")) {
            return new Element(cause);
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onmcMMOPlayerLevelChanges(McMMOPlayerLevelChangeEvent event) {
        if (dEntity.isNPC(event.getPlayer())) {
            return;
        }
        player = dPlayer.mirrorBukkitPlayer(event.getPlayer());
        level = event.getSkillLevel();
        cause = event.getXpGainReason().toString();
        skill = new Element(event.getSkill().getName());
        cancelled = event.isCancelled();
        this.event = event;
        fire();
        event.setCancelled(cancelled);
    }
}
