package com.denizenscript.depenizen.bukkit.properties.jobs;

import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.Mechanism;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.objects.properties.Property;
import net.aufdemrand.denizencore.tags.Attribute;
import com.denizenscript.depenizen.bukkit.objects.jobs.JobsJob;

public class JobPlayer implements Property {

    public static boolean describes(dObject job) {
        return job instanceof JobsJob;
    }

    public static JobPlayer getFrom(dObject job) {
        if (!describes(job)) {
            return null;
        }
        else {
            return new JobPlayer((JobsJob) job);
        }
    }

    ///////////////////
    // Instance Fields and Methods
    /////////////

    JobsJob job = null;

    private JobPlayer(JobsJob job) {
        this.job = job;
    }

    /////////
    // Property Methods
    ///////

    @Override
    public String getPropertyString() {
        if (job.hasOwner()) {
            return job.getOwner().identify();
        }
        else {
            return null;
        }
    }

    @Override
    public String getPropertyId() {
        return "player";
    }

    @Override
    public String getAttribute(Attribute attribute) {
        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {

        // <--[mechanism]
        // @object dJob
        // @name player
        // @input dPlayer
        // @description
        // Sets the owner of the job, to enable player-required tags.
        // @tags
        // None
        // @Plugin Depenizen, Jobs
        // -->
        if (mechanism.matches("player") && mechanism.requireObject(dPlayer.class)) {
            job.setOwner(mechanism.valueAsType(dPlayer.class));
        }

    }
}
