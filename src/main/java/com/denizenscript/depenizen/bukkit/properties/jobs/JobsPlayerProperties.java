package com.denizenscript.depenizen.bukkit.properties.jobs;

import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.depenizen.bukkit.objects.jobs.JobsJobTag;
import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.container.JobProgression;
import com.gamingmesh.jobs.container.JobsPlayer;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.ObjectTag;

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
            "job", "current_jobs"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    private JobsPlayerProperties(PlayerTag player) {
        this.player = Jobs.getPlayerManager().getJobsPlayer(player.getName());
    }

    JobsPlayer player;

    public static void register() {

        // <--[tag]
        // @attribute <PlayerTag.job[<job>]>
        // @returns JobsJobTag
        // @plugin Depenizen, Jobs
        // @description
        // Returns the job specified with the player's information attached.
        // -->
        PropertyParser.registerTag(JobsPlayerProperties.class, JobsJobTag.class, "job", (attribute, object) -> {
            if (attribute.hasParam()) {
                JobsJobTag job = JobsJobTag.valueOf(attribute.getParam());
                return new JobsJobTag(job.getJob(), object.player);
            }
            attribute.echoError("Invalid or missing job specified.");
            return null;
        });

        // <--[tag]
        // @attribute <PlayerTag.current_jobs>
        // @returns ListTag(JobsJobTag)
        // @plugin Depenizen, Jobs
        // @description
        // Returns a list of all jobs that the player is in.
        // -->
        PropertyParser.registerTag(JobsPlayerProperties.class, ListTag.class, "current_jobs", (attribute, object) -> {
            ListTag response = new ListTag();
            for (JobProgression progress : object.player.getJobProgression()) {
                response.addObject(new JobsJobTag(progress.getJob(), object.player));
            }
            return response;
        });
    }
}
