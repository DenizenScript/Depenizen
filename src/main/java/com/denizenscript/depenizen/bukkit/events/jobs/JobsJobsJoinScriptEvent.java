package com.denizenscript.depenizen.bukkit.events.jobs;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.depenizen.bukkit.objects.jobs.JobsJobTag;
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
    // @Triggers when a player joins the specified job.
    //
    // @Context
    // <context.job> Returns the job that the player is joining.
    //
    // @Plugin Depenizen, Jobs
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->

    public JobsJobsJoinScriptEvent() {
        registerCouldMatcher("jobs player joins <'job'>");
    }

    public JobsJoinEvent event;
    public JobsJobTag job;

    @Override
    public boolean matches(ScriptPath path) {
        if (!path.eventArgLowerAt(3).equals("job")
                && !runGenericCheck(path.eventArgAt(3), job.getJob().getName())) {
            return false;
        }
        return super.matches(path);
    }

    @Override
    public ObjectTag getContext(String name) {
        switch (name) {
            case "job":
                return job;
            default:
                return super.getContext(name);
        }
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(new PlayerTag(event.getPlayer().getPlayer()), null);
    }

    @EventHandler
    public void onJobsJobsJoin(JobsJoinEvent event) {
        job = new JobsJobTag(event.getJob(), event.getPlayer());
        this.event = event;
        fire(event);
    }
}
