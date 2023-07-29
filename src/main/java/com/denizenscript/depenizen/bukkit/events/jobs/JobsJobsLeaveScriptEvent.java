package com.denizenscript.depenizen.bukkit.events.jobs;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.depenizen.bukkit.objects.jobs.JobsJobTag;
import com.gamingmesh.jobs.api.JobsLeaveEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class JobsJobsLeaveScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // jobs player leaves <'job'>
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

    public JobsJobsLeaveScriptEvent() {
        registerCouldMatcher("jobs player leaves <'job'>");
    }

    public JobsLeaveEvent event;
    public JobsJobTag job;

    @Override
    public boolean matches(ScriptPath path) {
        if (!path.tryArgObject(3, job)) {
            return false;
        }
        return super.matches(path);
    }

    @Override
    public ObjectTag getContext(String name) {
        return switch (name) {
            case "job" -> job;
            default -> super.getContext(name);
        };
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(new PlayerTag(event.getPlayer().getPlayer()), null);
    }

    @EventHandler
    public void onJobsJobsLeave(JobsLeaveEvent event) {
        job = new JobsJobTag(event.getJob(), event.getPlayer());
        this.event = event;
        fire(event);
    }
}
