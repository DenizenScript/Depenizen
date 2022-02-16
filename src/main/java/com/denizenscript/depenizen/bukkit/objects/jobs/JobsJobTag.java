package com.denizenscript.depenizen.bukkit.objects.jobs;

import com.denizenscript.denizencore.objects.*;
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

public class JobsJobTag implements ObjectTag, Adjustable {

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
    // A JobsJobTag represents a Jobs job.
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
        return new PlayerTag(jobOwner.getPlayerUUID());
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
    public String getAttribute(Attribute attribute) {

        if (jobProgression != null) {

            if (attribute.startsWith("xp")) {
                attribute = attribute.fulfill(1);

                // <--[tag]
                // @attribute <JobsJobTag.xp.max>
                // @returns ElementTag(Number)
                // @plugin Depenizen, Jobs
                // @description
                // Returns the maximum experience a player can get in a specified job.
                // -->
                if (attribute.startsWith("max")) {
                    return new ElementTag(jobProgression.getMaxExperience())
                            .getAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <JobsJobTag.xp.level>
                // @returns ElementTag(Number)
                // @plugin Depenizen, Jobs
                // @description
                // Returns the current experience level a player has in a specified job.
                // -->
                if (attribute.startsWith("level")) {
                    return new ElementTag(jobProgression.getLevel())
                            .getAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <JobsJobTag.xp>
                // @returns ElementTag(Decimal)
                // @plugin Depenizen, Jobs
                // @description
                // Returns the current experience a player has in a specified job.
                // -->
                return new ElementTag(jobProgression.getExperience()).getAttribute(attribute);
            }
        }

        // <--[tag]
        // @attribute <JobsJobTag.color>
        // @returns ElementTag
        // @plugin Depenizen, Jobs
        // @description
        // Returns the ChatColor of the job.
        // -->
        if (attribute.startsWith("color")) {
            return new ElementTag(job.getChatColor().toString())
                    .getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <JobsJobTag.description>
        // @returns ElementTag
        // @plugin Depenizen, Jobs
        // @description
        // Returns the description of the job.
        // -->
        else if (attribute.startsWith("description")) {
            return new ElementTag(job.getDescription()).getAttribute(attribute.fulfill(1));
        }

        else if (attribute.startsWith("name")) {
            attribute = attribute.fulfill(1);

            // <--[tag]
            // @attribute <JobsJobTag.name.short>
            // @returns ElementTag
            // @plugin Depenizen, Jobs
            // @description
            // Returns the shortened name of the job.
            // -->
            if (attribute.startsWith("short")) {
                return new ElementTag(job.getShortName())
                        .getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <JobsJobTag.name>
            // @returns ElementTag
            // @plugin Depenizen, Jobs
            // @description
            // Returns the name of the job.
            // -->
            return new ElementTag(job.getName()).getAttribute(attribute);
        }

        return new ElementTag(identify()).getAttribute(attribute);
    }

    @Override
    public void applyProperty(Mechanism mechanism) {
        adjust(mechanism);
    }

    @Override
    public void adjust(Mechanism mechanism) {
        CoreUtilities.autoPropertyMechanism(this, mechanism);
    }
}
