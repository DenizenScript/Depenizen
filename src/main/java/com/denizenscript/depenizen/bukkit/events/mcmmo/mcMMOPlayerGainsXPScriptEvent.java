package com.denizenscript.depenizen.bukkit.events.mcmmo;

import com.gmail.nossr50.events.experience.McMMOPlayerXpGainEvent;
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

public class mcMMOPlayerGainsXPScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // mcmmo player gains xp for skill (in <area>)
    // mcmmo player gains xp for <skill> (in <area>)
    //
    // @Regex ^on mcmmo player gains xp for [^\s]+( in ((notable (cuboid|ellipsoid))|([^\s]+)))?$
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

    public mcMMOPlayerGainsXPScriptEvent() {
        instance = this;
    }

    public static mcMMOPlayerGainsXPScriptEvent instance;
    public McMMOPlayerXpGainEvent event;
    public PlayerTag player;
    public ElementTag skill;
    public ElementTag xp;
    public ElementTag cause;

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("mcmmo player gains xp for");
    }

    @Override
    public boolean matches(ScriptPath path) {
        String eSkill = path.eventArgLowerAt(5);
        if (!eSkill.equals("skill") && !eSkill.equals(CoreUtilities.toLowerCase(skill.asString()))) {
            return false;
        }

        if (!runInCheck(path, player.getLocation())) {
            return false;
        }

        return super.matches(path);
    }

    @Override
    public String getName() {
        return "mcMMOPlayerXPGain";
    }

    @Override
    public boolean applyDetermination(ScriptPath path, ObjectTag determinationObj) {
        if (determinationObj instanceof ElementTag && ((ElementTag) determinationObj).isFloat()) {
            xp = (ElementTag) determinationObj;
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
        if (name.equals("skill")) {
            return skill;
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
        if (EntityTag.isNPC(event.getPlayer())) {
            return;
        }
        player = PlayerTag.mirrorBukkitPlayer(event.getPlayer());
        cause = new ElementTag(event.getXpGainReason().toString());
        skill = new ElementTag(event.getSkill().getName());
        xp = new ElementTag(event.getRawXpGained());
        this.event = event;
        fire(event);
        event.setRawXpGained(xp.asFloat());
    }
}
