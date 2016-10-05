package com.denizenscript.depenizen.bukkit.events.skillapi;

import com.sucy.skill.api.event.PlayerSkillDowngradeEvent;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.utilities.DenizenAPI;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.ScriptEntryData;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

// <--[event]
// @Events
// skillapi player downgrades skill (in <area>)
// skillapi player downgrades <skill> (in <area>)
//
// @Regex ^on skillapi player downgrades [^\s]+( in ((notable (cuboid|ellipsoid))|([^\s]+)))?$
//
// @Cancellable false
//
// @Triggers when a player downgrades a skill in SkillAPI.
//
// @Context
// <context.level> returns the level the player went down to.
// <context.refund> returns how much the the player was refunded.
// <context.skill_name> returns the name of the skill downgraded.
//
// @Determine
// None
//
// @Plugin DepenizenBukkit, SkillAPI
// -->

public class SkillAPIPlayerDowngradesSkillScriptEvent extends BukkitScriptEvent implements Listener {

    public SkillAPIPlayerDowngradesSkillScriptEvent() {
        instance = this;
    }

    public static SkillAPIPlayerDowngradesSkillScriptEvent instance;
    public PlayerSkillDowngradeEvent event;
    public dPlayer player;
    public Element level;
    public Element skill;
    public Element refund;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        return lower.startsWith("skillapi player downgrades");
    }

    @Override
    public boolean matches(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        String skill = CoreUtilities.getXthArg(3, lower);

        if (!skill.equals("skill") && !skill.equals(CoreUtilities.toLowerCase(this.skill.asString()))) {
            return false;
        }

        if (!runInCheck(scriptContainer, s, lower, player.getLocation())) {
            return false;
        }

        return true;
    }

    @Override
    public String getName() {
        return "SkillAPIPlayerDowngradesSkill";
    }

    @Override
    public void init() {
        Bukkit.getServer().getPluginManager().registerEvents(this, DenizenAPI.getCurrentInstance());
    }

    @Override
    public void destroy() {
        PlayerSkillDowngradeEvent.getHandlerList().unregister(this);
    }

    @Override
    public boolean applyDetermination(ScriptContainer container, String determination) {
        return super.applyDetermination(container, determination);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(player, null);
    }

    @Override
    public dObject getContext(String name) {
        if (name.equals("level")) {
            return level;
        }
        else if (name.equals("refund")) {
            return refund;
        }
        else if (name.equals("skill_name")) {
            return skill;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onSkillAPIPlayerDowngradesSkill(PlayerSkillDowngradeEvent event) {
        if (!dEntity.isPlayer(event.getPlayerData().getPlayer())) {
            return;
        }
        player = dPlayer.mirrorBukkitPlayer(event.getPlayerData().getPlayer());
        level = new Element(event.getDowngradedSkill().getLevel());
        refund = new Element(event.getRefund());
        skill = new Element(event.getDowngradedSkill().getData().getName());
        cancelled = event.isCancelled();
        this.event = event;
        fire();
        event.setCancelled(cancelled);
    }
}
