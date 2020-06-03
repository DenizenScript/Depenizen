package com.denizenscript.depenizen.bukkit.properties.jobs;

import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.depenizen.bukkit.objects.jobs.JobsJobTag;
import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.container.Job;
import com.gamingmesh.jobs.container.JobsPlayer;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.debugging.Debug;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;

public class JobsPlayerProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "JobsPlayer";
    }

    @Override
    public void adjust(Mechanism mechanism) {
        // None
    }

    public static boolean describes(ObjectTag object) {
        return object instanceof PlayerTag;
    }

    public static JobsPlayerProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new JobsPlayerProperties((PlayerTag) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "job", "jobs", "current_jobs"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    private JobsPlayerProperties(PlayerTag player) {
        this.player = Jobs.getPlayerManager().getJobsPlayer(player.getName());
    }

    JobsPlayer player;

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <PlayerTag.current_jobs>
        // @returns ListTag(JobsJobTag)
        // @plugin Depenizen, Jobs
        // @description
        // Returns a list of all jobs that the player is in.
        // -->
        if (attribute.startsWith("current_jobs")) {
            ListTag jobList = new ListTag();
            for (Job jb : Jobs.getJobs()) {
                if (player.isInJob(jb)) {
                    jobList.addObject(new JobsJobTag(jb, player));
                }
            }
            return jobList.getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <PlayerTag.job[<job>]>
        // @returns JobsJobTag
        // @plugin Depenizen, Jobs
        // @description
        // Returns the job specified with the player's information attached.
        // -->
        if (attribute.startsWith("job") || attribute.startsWith("jobs")) {
            Job job = null;
            if (attribute.hasContext(1)) {
                job = Jobs.getJob(attribute.getContext(1));
            }
            if (job == null) {
                if (!attribute.hasAlternative()) {
                    Debug.echoError("Invalid or missing job specified!");
                }
                return null;
            }
            return new JobsJobTag(job, player).getAttribute(attribute.fulfill(1));
        }

        return null;

    }
}
