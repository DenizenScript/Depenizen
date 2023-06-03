package com.denizenscript.depenizen.bukkit.events.jobs;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.denizenscript.depenizen.bukkit.objects.jobs.JobsJobTag;
import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.api.JobsPrePaymentEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class JobsJobsPaymentScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // jobs player earns money for <'job'>
    //
    // @Switch action:<action> to only process the event if it came from a specified action.
    //
    // @Cancellable true
    //
    // @Triggers when a player performs an action that would cause them to be paid for a certain job.
    //
    // @Context
    // <context.job> Returns the job that the player is being paid for.
    // <context.money> Returns the amount of money the player will be paid.
    // <context.points> Returns the amount of points the player will be paid.
    // <context.action> Returns the name of the action being paid for, which can be any of the strings from: <@link url https://github.com/Zrips/Jobs/blob/master/src/main/java/com/gamingmesh/jobs/container/ActionType.java>.
    //
    // @Determine
    // "MONEY:<ElementTag(Decimal)>" to change the amount of money this action should provide.
    // "POINTS:<ElementTag(Decimal)>" to change the amount of Jobs points this action should provide.
    //
    // @Plugin Depenizen, Jobs
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->

    public JobsJobsPaymentScriptEvent() {
        registerCouldMatcher("jobs player earns money for <'job'>");
        registerSwitches("action");
    }

    public JobsPrePaymentEvent event;
    public JobsJobTag job;

    @Override
    public boolean matches(ScriptPath path) {
        if (!path.eventArgLowerAt(5).equals("job")
                && !runGenericCheck(path.eventArgAt(4), job.getJob().getName())) {
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
            case "money":
                return new ElementTag(event.getAmount());
            case "points":
                return new ElementTag(event.getPoints());
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
    public boolean applyDetermination(ScriptPath path, ObjectTag determinationObj) {
        if (determinationObj instanceof ElementTag) {
            String determination = determinationObj.toString();
            String lower = CoreUtilities.toLowerCase(determination);
            if (lower.startsWith("money:")) {
                event.setAmount(Double.parseDouble(determination.substring("money:".length())));
                return true;
            } else if (lower.startsWith("points:")) {
                event.setPoints(Double.parseDouble(determination.substring("points:".length())));
                return true;
            }
        }
        return super.applyDetermination(path, determinationObj);
    }

    @EventHandler
    public void onJobsJobsPrepayment(JobsPrePaymentEvent event) {
        job = new JobsJobTag(event.getJob(), Jobs.getPlayerManager().getJobsPlayer(event.getPlayer().getUniqueId()));
        this.event = event;
        fire(event);
    }
}
