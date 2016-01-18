package net.gnomeffinway.depenizen.events.mcMMO;

import com.gmail.nossr50.datatypes.skills.SkillType;
import com.gmail.nossr50.events.experience.McMMOPlayerLevelChangeEvent;
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
// mcmmo player skill changes
// mcmmo player <skill> changes
//
// @Regex ^on mcmmo player [^\s]+$ changes
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
// -->

public class mcMMOPlayerLevelChangeScriptEvent extends BukkitScriptEvent implements Listener {

    public mcMMOPlayerLevelChangeScriptEvent() {
        instance = this;
    }

    public static mcMMOPlayerLevelChangeScriptEvent instance;

    public static McMMOPlayerLevelChangeEvent event;
    public dPlayer player;
    public String skill;
    public int level;
    public String cause;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        return lower.startsWith("mcmmo player") && CoreUtilities.getXthArg(3, lower).equals("changes");
    }

    @Override
    public boolean matches(ScriptContainer scriptContainer, String s) {
        String arg = CoreUtilities.getXthArg(2, s).toUpperCase();
        SkillType eventSkill = SkillType.valueOf(arg);
        if (arg.equals("SKILL") || (eventSkill != null && eventSkill == event.getSkill())) {
            return true;
        }
        return false;
    }

    @Override
    public String getName() {
        return "mcMMOPlayerLevelChanges";
    }

    @Override
    public void init() {
        Bukkit.getServer().getPluginManager().registerEvents(this, DenizenAPI.getCurrentInstance());
    }

    @Override
    public void destroy() {
        McMMOPlayerLevelChangeEvent.getHandlerList().unregister(this);
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
        return new BukkitScriptEntryData(dEntity.isPlayer(event.getPlayer()) ? dEntity.getPlayerFrom(event.getPlayer()) : null, null);
    }

    @Override
    public dObject getContext(String name) {
        if (name.equals("skill")) {
            return new Element(skill);
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
        level = event.getSkillLevel();
        cause = event.getXpGainReason().toString();
        skill = event.getSkill().getName();
        cancelled = event.isCancelled();
        this.event = event;
        fire();
        event.setCancelled(cancelled);
    }
}
