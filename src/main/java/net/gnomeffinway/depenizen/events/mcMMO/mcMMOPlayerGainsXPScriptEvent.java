package net.gnomeffinway.depenizen.events.mcMMO;

import com.gmail.nossr50.datatypes.skills.SkillType;
import com.gmail.nossr50.events.experience.McMMOPlayerXpGainEvent;
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

import java.util.List;

// <--[event]
// @Events
// mcmmo player gains xp (for <skill>)
//
// @Regex ^on mcmmo player gains xp( for [^\s]+)?$
//
// @Cancellable true
//
// @Triggers when a player gains mcMMO xp.
//
// @Context
// <context.skill> returns the name of the skill that the player gained xp for.
// (Based on the mcMMO language file).
// <context.xp> returns the amount of xp gained.
// <context.cause> returns the cause of the xp gain.
// Will be one of: 'PVP', 'PVE', 'VAMPIRISM', 'SHARED_PVP', 'SHARED_PVE', 'COMMAND', 'UNKNOWN'.
//
// @Plugin Depenizen, mcMMO
//
// -->

public class mcMMOPlayerGainsXPScriptEvent extends BukkitScriptEvent implements Listener {

    public mcMMOPlayerGainsXPScriptEvent() {
        instance = this;
    }

    public static mcMMOPlayerGainsXPScriptEvent instance;

    public static McMMOPlayerXpGainEvent event;
    public dPlayer player;
    public SkillType skill;
    public Element xp;
    public Element cause;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        return lower.startsWith("mcmmo player gains xp");
    }

    @Override
    public boolean matches(ScriptContainer scriptContainer, String s) {
        List<String> args = CoreUtilities.split(s.toUpperCase(), ' ');
        if (args.size() == 4) {
            return true;
        }
        SkillType eventSkill = SkillType.valueOf(args.get(5));
        if (args.size() == 6 && args.get(4).equals("FOR") && eventSkill != null && eventSkill == skill) {
            return true;
        }
        return false;
    }

    @Override
    public String getName() {
        return "mcMMOPlayerXPGain";
    }

    @Override
    public void init() {
        Bukkit.getServer().getPluginManager().registerEvents(this, DenizenAPI.getCurrentInstance());
    }

    @Override
    public void destroy() {
        McMMOPlayerXpGainEvent.getHandlerList().unregister(this);
    }

    @Override
    public boolean applyDetermination(ScriptContainer container, String determination) {
        String lower = CoreUtilities.toLowerCase(determination);

        if (aH.Argument.valueOf(lower).matchesPrimitive(aH.PrimitiveType.Float)) {
            xp = new Element(lower);
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
            return new Element(skill.getName());
        }
        else if (name.equals("xp")) {
            return xp;
        }
        else if (name.equals("cause")) {
            return cause;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onmcMMOPlayerLevelChanges(McMMOPlayerXpGainEvent event) {
        if (dEntity.isNPC(event.getPlayer())) {
            return;
        }
        cause = new Element(event.getXpGainReason().toString());
        skill = event.getSkill();
        xp = new Element(event.getRawXpGained());
        cancelled = event.isCancelled();
        this.event = event;
        fire();
        event.setCancelled(cancelled);
        event.setRawXpGained(xp.asFloat());
    }
}
