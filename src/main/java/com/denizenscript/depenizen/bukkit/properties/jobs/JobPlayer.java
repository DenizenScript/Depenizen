package com.denizenscript.depenizen.bukkit.properties.jobs;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.depenizen.bukkit.objects.jobs.JobsJobTag;

public class JobPlayer implements Property {

    public static boolean describes(ObjectTag job) {
        return job instanceof JobsJobTag;
    }

    public static JobPlayer getFrom(ObjectTag job) {
        if (!describes(job)) {
            return null;
        }
        else {
            return new JobPlayer((JobsJobTag) job);
        }
    }

    ///////////////////
    // Instance Fields and Methods
    /////////////

    JobsJobTag job = null;

    private JobPlayer(JobsJobTag job) {
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
        // @input PlayerTag
        // @description
        // Sets the owner of the job, to enable player-required tags.
        // @tags
        // None
        // @Plugin Depenizen, Jobs
        // -->
        if (mechanism.matches("player") && mechanism.requireObject(PlayerTag.class)) {
            job.setOwner(mechanism.valueAsType(PlayerTag.class));
        }

    }
}
