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
    // mcmmo player gains xp for <'skill'>
    //
    // @Location true
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
    // @Determine
    // ElementTag(Decimal) to set the XP gained.
    //
    // @Plugin Depenizen, mcMMO
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->

    public mcMMOPlayerGainsXPScriptEvent() {
        registerCouldMatcher("mcmmo player gains xp for <'skill'>");
    }

    public McMMOPlayerXpGainEvent event;
    public PlayerTag player;
    public ElementTag skill;
    public ElementTag xp;
    public ElementTag cause;

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
    public boolean applyDetermination(ScriptPath path, ObjectTag determinationObj) {
        if (determinationObj instanceof ElementTag element && element.isFloat()) {
            xp = element;
            event.setRawXpGained(xp.asFloat());
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
            case "xp":
                return xp;
            case "cause":
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
    }
}
