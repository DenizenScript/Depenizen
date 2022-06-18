package com.denizenscript.depenizen.bukkit.properties.jobs;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.depenizen.bukkit.objects.jobs.JobsJobTag;

public class JobsJobProperties implements Property {
    public static boolean describes(ObjectTag job) {
        return job instanceof JobsJobTag;
    }

    public static JobsJobProperties getFrom(ObjectTag job) {
        if (!describes(job)) {
            return null;
        }
        else {
            return new JobsJobProperties((JobsJobTag) job);
        }
    }

    public static final String[] handledTags = new String[] {
            "player"
    };

    public static final String[] handledMechs = new String[] {
            "player"
    };

    JobsJobTag job;

    private JobsJobProperties(JobsJobTag job) {
        this.job = job;
    }

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
    public void adjust(Mechanism mechanism) {

        // <--[mechanism]
        // @object JobsJobTag
        // @name player
        // @input PlayerTag
        // @plugin Depenizen, Jobs
        // @description
        // Sets the owner of the job, to enable player-required tags.
        // -->
        if (mechanism.matches("player") && mechanism.requireObject(PlayerTag.class)) {
            job.setOwner(mechanism.valueAsType(PlayerTag.class));
        }
    }

    public static void registerTags() {
        // <--[tag]
        // @attribute <JobsJobTag.player>
        // @returns PlayerTag
        // @plugin Depenizen, Jobs
        // @description
        // Returns the player the job progression for this tag belongs to.
        // -->
        PropertyParser.<JobsJobProperties, PlayerTag>registerTag(PlayerTag.class, "player", (attribute, object) -> {
            return object.job.getOwner();
        });
    }
}
