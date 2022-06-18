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
import com.gamingmesh.jobs.container.ActionType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class JobsJobsPaymentScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // jobs player earns money for <'job'> (action <'action'>)
    //
    // @Cancellable true
    //
    // @Triggers when a player performs an action that would cause them to be paid for a certain job
    //
    // @Context
    // <context.job> Returns the job that the player is being paid for
    // <context.money> Returns the amount of money the player will be paid
    // <context.points> Returns the amount of points the player will be paid
    // <context.action> Returns the name of the action being paid for, which can be: Break, StripLogs, TNTBreak, Place, Kill, MMKill, Fish, Craft, VTrade, Smelt, Brew, Enchant, Repair, Breed, Tame, Dye, Shear, Milk, Explore, Eat, custom-kill, Collect, Bake.
    //
    // @Determine
    // "MONEY:" + ElementTag(Decimal) to change the amount of money this action should provide
    // "POINTS:" + ElementTag(Decimal) to change the amount of Jobs points this action should provide
    //
    // @Plugin Depenizen, Jobs
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->

    public JobsJobsPaymentScriptEvent() {
        instance = this;
        registerCouldMatcher("jobs player earns money for <'job'> (action <'action'>)");
    }

    public static JobsJobsPaymentScriptEvent instance;
    public JobsPrePaymentEvent event;
    public JobsJobTag job;

    @Override
    public boolean couldMatch(ScriptPath path) {
        if (!super.couldMatch(path)) {
            return false;
        }
        if (!path.eventArgLowerAt(5).equals("job")
            && Jobs.getJob(path.eventArgAt(5)) == null) {
            return false;
        }
        if (!path.eventArgAt(7).isEmpty() && !path.eventArgLowerAt(7).equals("action")
            && ActionType.getByName(path.eventArgAt(7)) == null) {
            return false;
        }
        return true;
    }

    @Override
    public boolean matches(ScriptPath path) {
        if (!path.eventArgLowerAt(5).equals("job")
            && !job.getJob().getName().equalsIgnoreCase(path.eventArgAt(5))) {
            return false;
        }
        if (!path.eventArgAt(7).isEmpty() && !path.eventArgAt(7).equals("action")
            && !event.getActionInfo().getType().name().equalsIgnoreCase(path.eventArgAt(7))) {
            return false;
        }
        return super.matches(path);
    }

    @Override
    public String getName() {
        return "JobsPrePayment";
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("job")) {
            return job;
        }
        else if (name.equals("money")) {
            return new ElementTag(event.getAmount());
        }
        else if (name.equals("points")) {
            return new ElementTag(event.getPoints());
        }
        else if (name.equals("action")) {
            return new ElementTag(event.getActionInfo().getType().getName());
        }
        return super.getContext(name);
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
