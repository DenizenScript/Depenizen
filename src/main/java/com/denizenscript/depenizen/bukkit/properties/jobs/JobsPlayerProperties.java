package com.denizenscript.depenizen.bukkit.properties.jobs;

import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.depenizen.bukkit.objects.jobs.JobsJob;
import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.container.Job;
import com.gamingmesh.jobs.container.JobsPlayer;
import com.denizenscript.denizen.objects.dPlayer;
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
        return object instanceof dPlayer;
    }

    public static JobsPlayerProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new JobsPlayerProperties((dPlayer) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "jobs"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    private JobsPlayerProperties(dPlayer player) {
        this.player = Jobs.getPlayerManager().getJobsPlayer(player.getName());
    }

    JobsPlayer player = null;

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <p@player.jobs[<job>]>
        // @returns dJob
        // @description
        // Returns the job specified with the player's information attached.
        // @Plugin Depenizen, Jobs
        // -->
        if (attribute.startsWith("jobs")) {
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
            return new JobsJob(job, player).getAttribute(attribute.fulfill(1));
        }

        return null;

    }
}
