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
import com.gamingmesh.jobs.container.ActionType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class JobsJobsExpGainScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // jobs player earns exp for <'job'> (action <'action'>)
    //
    // @Cancellable true
    //
    // @Triggers when a player performs an action that would cause them to earn Jobs exp for a certain job
    //
    // @Context
    // <context.job> Returns the job that the player is gaining exp for
    // <context.experience> Returns the amount of exp the player will earn
    // <context.action> Returns the name of the action being paid for, which can be: Break, StripLogs, TNTBreak, Place, Kill, MMKill, Fish, Craft, VTrade, Smelt, Brew, Enchant, Repair, Breed, Tame, Dye, Shear, Milk, Explore, Eat, custom-kill, Collect, Bake.
    //
    // @Determine
    // "EXP:" + ElementTag(Decimal) to change the amount of Jobs exp this action should provide
    //
    // @Plugin Depenizen, Jobs
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->

    public JobsJobsExpGainScriptEvent() {
        instance = this;
        registerCouldMatcher("jobs player earns exp for <'job'> (action <'action'>)");
    }

    public static JobsJobsExpGainScriptEvent instance;
    public JobsExpGainEvent event;
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
    public boolean matches(ScriptEvent.ScriptPath path) {
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
        return "JobsExpGain";
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("job")) {
            return job;
        }
        else if (name.equals("experience")) {
            return new ElementTag(event.getExp());
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
