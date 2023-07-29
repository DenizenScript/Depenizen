package com.denizenscript.depenizen.bukkit.properties.jobs;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.depenizen.bukkit.objects.jobs.JobsJobTag;
import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.container.JobsPlayer;

public class JobsPlayerProperties {

    public static void register() {

        // <--[tag]
        // @attribute <PlayerTag.job[<job>]>
        // @returns JobsJobTag
        // @plugin Depenizen, Jobs
        // @description
        // Returns the job specified with the player's information attached.
        // -->
        PlayerTag.tagProcessor.registerTag(JobsJobTag.class, JobsJobTag.class, "job", (attribute, object, job) -> {
            return new JobsJobTag(job.getJob(), Jobs.getPlayerManager().getJobsPlayer(object.getUUID()));
        });

        // <--[tag]
        // @attribute <PlayerTag.current_jobs>
        // @returns ListTag(JobsJobTag)
        // @plugin Depenizen, Jobs
        // @description
        // Returns a list of all jobs that the player is in.
        // -->
        PlayerTag.tagProcessor.registerTag(ListTag.class, "current_jobs", (attribute, object) -> {
            JobsPlayer jobsPlayer = Jobs.getPlayerManager().getJobsPlayer(object.getUUID());
            return new ListTag(jobsPlayer.getJobProgression(), jobProgression -> new JobsJobTag(jobProgression.getJob(), jobsPlayer));
        });
    }
}
