package com.denizenscript.depenizen.bukkit.objects.jobs;

import com.denizenscript.denizencore.objects.*;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.tags.ObjectTagProcessor;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.container.Job;
import com.gamingmesh.jobs.container.JobProgression;
import com.gamingmesh.jobs.container.JobsPlayer;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.TagContext;

import java.util.HashMap;
import java.util.Map;

public class JobsJobTag implements ObjectTag, Adjustable {
    public static ObjectTagProcessor<JobsJobTag> tagProcessor = new ObjectTagProcessor<>();

    // <--[ObjectType]
    // @name JobsJobTag
    // @prefix job
    // @base ElementTag
    // @format
    // The identity format for jobs is <job_name>
    // For example, 'job@job_name'.
    //
    // @plugin Depenizen, Jobs
    // @description
    // A JobsJobTag represents a Jobs job, with a player's progression if created from a player.
    //
    // -->

    /////////////////////
    //   OBJECT FETCHER
    /////////////////

    public static JobsJobTag valueOf(String string) {
        return valueOf(string, null);
    }

    @Fetchable("job")
    public static JobsJobTag valueOf(String string, TagContext context) {
        if (string == null) {
            return null;
        }
        if (ObjectFetcher.isObjectWithProperties(string)) {
            return ObjectFetcher.getObjectFromWithProperties(JobsJobTag.class, string, context);
        }
        return new JobsJobTag(Jobs.getJob(string.replace("job@", "")));
    }

    public static boolean matches(String arg) {
        if (valueOf(arg) != null) {
            return true;
        }
        return false;
    }

    /////////////////////
    //   STATIC CONSTRUCTORS
    /////////////////

    Job job;
    JobProgression jobProgression = null;
    JobsPlayer jobOwner = null;

    public JobsJobTag(Job job) {
        this.job = job;
    }

    public JobsJobTag(Job job, JobsPlayer jobOwner) {
        this.job = job;
        this.jobProgression = jobOwner.getJobProgression(job);
        this.jobOwner = jobOwner;
    }

    /////////////////////
    //   INSTANCE FIELDS/METHODS
    /////////////////

    public Job getJob() {
        return job;
    }

    public JobsJobTag setJobProgression(JobProgression jobProgression) {
        this.jobProgression = jobProgression;
        return this;
    }

    public boolean hasOwner() {
        return jobOwner != null;
    }

    public PlayerTag getOwner() {
        if (jobOwner == null) {
            return null;
        }
        return new PlayerTag(jobOwner.playerUUID);
    }

    public void setOwner(PlayerTag player) {
        this.jobOwner = Jobs.getPlayerManager().getJobsPlayer(player.getName());
        this.jobProgression = jobOwner.getJobProgression(job);
    }

    /////////////////////
    //   ObjectTag Methods
    /////////////////

    private String prefix = "Job";

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public ObjectTag setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    @Override
    public boolean isUnique() {
        return true;
    }

    @Override
    public String getObjectType() {
        return "Job";
    }

    @Override
    public String identify() {
        return "job@" + job.getName() + PropertyParser.getPropertiesString(this);
    }

    @Override
    public String identifySimple() {
        return "job@" + job.getName();
    }

    @Override
    public String toString() {
        return identify();
    }

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {
        return tagProcessor.getObjectAttribute(this, attribute);
    }

    @Override
    public void applyProperty(Mechanism mechanism) {
        adjust(mechanism);
    }

    @Override
    public void adjust(Mechanism mechanism) {
        // <--[mechanism]
        // @object JobsJobTag
        // @name xp
        // @input ElementTag(Double)
        // @plugin Depenizen, Jobs
        // @description
        // Set the amount of job XP the player has in this job
        // -->
        if (mechanism.matches("xp") && mechanism.requireDouble()) {
            if (jobProgression == null) {
                mechanism.echoError("This mechanism requires the object to be linked to a player");
                return;
            }
            jobProgression.setExperience(mechanism.getValue().asDouble());
        }

        CoreUtilities.autoPropertyMechanism(this, mechanism);
    }

    public static void registerTags() {
        PropertyParser.registerPropertyTagHandlers(JobsJobTag.class, tagProcessor);
        // <--[tag]
        // @attribute <JobsJobTag.description>
        // @returns ElementTag
        // @plugin Depenizen, Jobs
        // @deprecated Use 'JobsJobTag.full_description'
        // @description
        // Returns the description of the job.
        // Single line description is deprecated in Jobs. Use <@link tag JobsJobTag.full_description> instead.
        // -->
        tagProcessor.registerTag(ElementTag.class, "description", (attribute, object) -> {
            return new ElementTag(object.getJob().getDescription());
        });

        // <--[tag]
        // @attribute <JobsJobTag.full_description>
        // @returns ElementTag
        // @plugin Depenizen, Jobs
        // @description
        // Returns the full description of the job.
        // -->
        tagProcessor.registerTag(ListTag.class, "full_description", (attribute, object) -> {
            return new ListTag(object.getJob().getFullDescription());
        });

        // <--[tag]
        // @attribute <JobsJobTag.name.short>
        // @returns ElementTag
        // @plugin Depenizen, Jobs
        // @deprecated Use 'JobsJobTag.short_name'
        // @description
        // Returns the shortened name of the job.
        // Deprecated in favor of <@link tag JobsJobTag.short_name>
        // -->

        // <--[tag]
        // @attribute <JobsJobTag.name>
        // @returns ElementTag
        // @plugin Depenizen, Jobs
        // @description
        // Returns the name of the job.
        // -->
        tagProcessor.registerTag(ElementTag.class, "name", (attribute, object) -> {
            if (attribute.startsWith("short", 1)) {
                attribute.fulfill(1);
                return new ElementTag(object.getJob().getShortName());
            }
            return new ElementTag(object.getJob().getName());
        });

        // <--[tag]
        // @attribute <JobsJobTag.short_name>
        // @returns ElementTag
        // @plugin Depenizen, Jobs
        // @description
        // Returns the shortened name of the job.
        // -->
        tagProcessor.registerTag(ElementTag.class, "short_name", (attribute, object) -> {
            return new ElementTag(object.getJob().getShortName());
        });

        // <--[tag]
        // @attribute <JobsJobTag.xp.max>
        // @returns ElementTag(Number)
        // @plugin Depenizen, Jobs
        // @deprecated Use 'JobsJobTag.max_xp'
        // @description
        // Returns the maximum experience a player can get in a specified job at their current level.
        // Deprecated in favor of <@link tag JobsJobTag.max_xp>
        // -->

        // <--[tag]
        // @attribute <JobsJobTag.xp.level>
        // @returns ElementTag(Number)
        // @plugin Depenizen, Jobs
        // @deprecated Use 'JobsJobTag.level'
        // @description
        // Returns the current level a player has in a specified job.
        // Deprecated in favor of <@link tag JobsJobTag.level>
        // -->

        // <--[tag]
        // @attribute <JobsJobTag.xp>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, Jobs
        // @description
        // Returns the current experience a player has for the current level in a specified job.
        // -->
        tagProcessor.registerTag(ElementTag.class, "xp", (attribute, object) -> {
            if (object.jobProgression != null) {
                if (attribute.startsWith("max", 2)) {
                    attribute.fulfill(1);
                    return new ElementTag(object.jobProgression.getMaxExperience());
                }
                if (attribute.startsWith("level", 2)) {
                    attribute.fulfill(1);
                    return new ElementTag(object.jobProgression.getLevel());
                }
                return new ElementTag(object.jobProgression.getExperience());
            }
            return null;
        });

        // <--[tag]
        // @attribute <JobsJobTag.level>
        // @returns ElementTag(Number)
        // @plugin Depenizen, Jobs
        // @description
        // Returns the current level a player has in a specified job.
        // -->
        tagProcessor.registerTag(ElementTag.class, "level", (attribute, object) -> {
            if (object.jobProgression != null) {
                return new ElementTag(object.jobProgression.getLevel());
            }
            return null;
        });

        // <--[tag]
        // @attribute <JobsJobTag.max_xp[(<level>)]>
        // @returns ElementTag(Number)
        // @plugin Depenizen, Jobs
        // @description
        // Returns the amount of experience required for the specified job level.
        // If the level is not specified, uses the current level of the player.
        // -->
        tagProcessor.registerTag(ElementTag.class, "max_xp", (attribute, object) -> {
            if (object.jobProgression != null) {
                if (attribute.hasParam()) {
                    return new ElementTag(object.jobProgression.getMaxExperience(attribute.getIntParam()));
                }
                return new ElementTag(object.jobProgression.getMaxExperience());
            }
            return null;
        });
    }
}
