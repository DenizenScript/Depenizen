package com.denizenscript.depenizen.bukkit.events.jobs;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.depenizen.bukkit.objects.jobs.JobsJobTag;
import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.api.JobsLevelUpEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class JobsJobsLevelUpScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // jobs player levels up <'job'>
    //
    // @Cancellable true
    //
    // @Triggers when a player levels up in the specified job
    //
    // @Context
    // <context.job> Returns the job that the player is levelling up in
    //
    // @Plugin Depenizen, Jobs
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->

    public JobsJobsLevelUpScriptEvent() {
        instance = this;
        registerCouldMatcher("jobs player levels up <'job'>");
    }

    public static JobsJobsLevelUpScriptEvent instance;
    public JobsLevelUpEvent event;
    public JobsJobTag job;

    @Override
    public boolean couldMatch(ScriptEvent.ScriptPath path) {
        if (!super.couldMatch(path)) {
            return false;
        }
        if (!path.eventArgLowerAt(4).equals("job")
            && Jobs.getJob(path.eventArgAt(4)) == null) {
            return false;
        }
        return true;
    }

    @Override
    public boolean matches(ScriptEvent.ScriptPath path) {
        if (!path.eventArgLowerAt(4).equals("job")
            && !job.getJob().getName().equalsIgnoreCase(path.eventArgAt(4))) {
            return false;
        }
        return super.matches(path);
    }

    @Override
    public String getName() {
        return "JobsLevel";
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
    public void onJobsJobsLevelUp(JobsLevelUpEvent event) {
        job = new JobsJobTag(event.getJob(), event.getPlayer());
        this.event = event;
        fire(event);
    }
}