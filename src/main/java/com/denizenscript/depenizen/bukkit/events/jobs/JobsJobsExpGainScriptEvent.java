package com.denizenscript.depenizen.bukkit.events.jobs;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.denizenscript.depenizen.bukkit.objects.jobs.JobsJobTag;
import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.api.JobsExpGainEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class JobsJobsExpGainScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // jobs player earns exp for <'job'>
    //
    // @Switch action:<action> to only process the event if it came from a specified action.
    //
    // @Cancellable true
    //
    // @Triggers when a player performs an action that would cause them to earn Jobs exp for a certain job.
    //
    // @Context
    // <context.job> Returns the job that the player is gaining exp for.
    // <context.experience> Returns the amount of exp the player will earn.
    // <context.action> Returns the name of the action being paid for, which can be any of the strings from: <@link url https://github.com/Zrips/Jobs/blob/master/src/main/java/com/gamingmesh/jobs/container/ActionType.java>.
    //
    // @Determine
    // "EXP:<ElementTag(Decimal)>" to change the amount of Jobs exp this action should provide.
    //
    // @Plugin Depenizen, Jobs
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->

    public JobsJobsExpGainScriptEvent() {
        registerCouldMatcher("jobs player earns exp for <'job'>");
        registerSwitches("action");
    }

    public JobsExpGainEvent event;
    public JobsJobTag job;

    @Override
    public boolean matches(ScriptPath path) {
        if (!path.eventArgLowerAt(5).equals("job")
            && !runGenericCheck(path.eventArgAt(5), job.getJob().getName())) {
            return false;
        }
        if (!runGenericSwitchCheck(path, "action", event.getActionInfo().getType().getName())) {
            return false;
        }
        return super.matches(path);
    }

    @Override
    public ObjectTag getContext(String name) {
        switch (name) {
            case "job":
                return job;
            case "experience":
                return new ElementTag(event.getExp());
            case "action":
                return new ElementTag(event.getActionInfo().getType().getName());
            default:
                return super.getContext(name);
        }
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(new PlayerTag(event.getPlayer()), null);
    }

    @Override
    public boolean applyDetermination(ScriptEvent.ScriptPath path, ObjectTag determinationObj) {
        if (determinationObj instanceof ElementTag) {
            String determination = determinationObj.toString();
            String lower = CoreUtilities.toLowerCase(determination);
            if (lower.startsWith("exp:")) {
                event.setExp(Double.parseDouble(determination.substring("exp:".length())));
                return true;
            }
        }
        return super.applyDetermination(path, determinationObj);
    }

    @EventHandler
    public void onJobsExpGain(JobsExpGainEvent event) {
        job = new JobsJobTag(event.getJob(), Jobs.getPlayerManager().getJobsPlayer(event.getPlayer().getUniqueId()));
        this.event = event;
        fire(event);
    }
}
