package com.denizenscript.depenizen.bukkit.events.jobs;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.depenizen.bukkit.objects.jobs.JobsJobTag;
import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.api.JobsJoinEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class JobsJobsJoinScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // jobs player joins <'job'>
    //
    // @Cancellable true
    //
    // @Triggers when a player joins the specified job
    //
    // @Context
    // <context.job> Returns the job that the player is joining
    //
    // @Plugin Depenizen, Jobs
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->

    public JobsJobsJoinScriptEvent() {
        instance = this;
        registerCouldMatcher("jobs player joins <'job'>");
    }

    public static JobsJobsJoinScriptEvent instance;
    public JobsJoinEvent event;
    public JobsJobTag job;

    @Override
    public boolean couldMatch(ScriptEvent.ScriptPath path) {
        if (!super.couldMatch(path)) {
            return false;
        }
        if (!path.eventArgLowerAt(3).equals("job")
            && Jobs.getJob(path.eventArgAt(3)) == null) {
            return false;
        }
        return true;
    }

    @Override
    public boolean matches(ScriptEvent.ScriptPath path) {
        if (!path.eventArgLowerAt(3).equals("job")
            && !job.getJob().getName().equalsIgnoreCase(path.eventArgAt(3))) {
            return false;
        }
        return super.matches(path);
    }

    @Override
    public String getName() {
        return "JobsJoin";
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("job")) {
            return job;
        }
        return super.getContext(name);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(new PlayerTag(event.getPlayer()
            .getPlayer()), null);
    }

    @EventHandler
    public void onJobsJobsJoin(JobsJoinEvent event) {
        job = new JobsJobTag(event.getJob(), event.getPlayer());
        this.event = event;
        fire(event);
    }
}
