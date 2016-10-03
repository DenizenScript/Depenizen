package com.denizenscript.depenizen.bukkit.events.skillapi;

import com.sucy.skill.api.event.PlayerSkillUnlockEvent;
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

public class SkillAPIPlayerUnlocksSkillScriptEvent extends BukkitScriptEvent implements Listener {

    public SkillAPIPlayerUnlocksSkillScriptEvent() {
        instance = this;
    }

    public static SkillAPIPlayerUnlocksSkillScriptEvent instance;
    public PlayerSkillUnlockEvent event;
    public dPlayer player;
    public Element skill;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        return lower.startsWith("skillapi player unlocks");
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
        return "SkillAPIPlayerUnlocksSkill";
    }

    @Override
    public void init() {
        Bukkit.getServer().getPluginManager().registerEvents(this, DenizenAPI.getCurrentInstance());
    }

    @Override
    public void destroy() {
        PlayerSkillUnlockEvent.getHandlerList().unregister(this);
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
        if (name.equals("skill_name")) {
            return skill;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onSkillAPIPlayerUnlocksSkill(PlayerSkillUnlockEvent event) {
        if (!dEntity.isPlayer(event.getPlayerData().getPlayer())) {
            return;
        }
        player = dPlayer.mirrorBukkitPlayer(event.getPlayerData().getPlayer());
        skill = new Element(event.getUnlockedSkill().getData().getName());
        this.event = event;
        fire();
    }
}
