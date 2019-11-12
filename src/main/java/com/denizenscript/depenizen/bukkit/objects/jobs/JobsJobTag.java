package com.denizenscript.depenizen.bukkit.objects.jobs;

import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.container.Job;
import com.gamingmesh.jobs.container.JobProgression;
import com.gamingmesh.jobs.container.JobsPlayer;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.Fetchable;
import com.denizenscript.denizencore.objects.ObjectFetcher;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.TagContext;

import java.util.regex.Matcher;

public class JobsJobTag implements ObjectTag {

    // <--[language]
    // @name JobsJobTag Objects
    // @group Depenizen Object Types
    // @plugin Depenizen, Jobs
    // @description
    // A JobsJobTag represents a Jobs job.
    //
    // For format info, see <@link language job@>
    //
    // -->

    // <--[language]
    // @name job@
    // @group Depenizen Object Fetcher Types
    // @plugin Depenizen, Jobs
    // @description
    // job@ refers to the 'object identifier' of a JobsJobTag. The 'job@' is notation for Denizen's Object
    // Fetcher. The constructor for a JobsJobTag is <job_name>
    // For example, 'job@job_name'.
    //
    // For general info, see <@link language JobsJobTag Objects>
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

        ////////
        // Match job name

        JobsJobTag job = null;

        Matcher m = ObjectFetcher.DESCRIBED_PATTERN.matcher(string);
        if (m.matches()) {
            return ObjectFetcher.getObjectFrom(JobsJobTag.class, string, context);
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

    Job job = null;
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
    public String debug() {
        return (prefix + "='<A>" + identify() + "<G>' ");
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
                // @description
                // Returns the maximum experience a player can get in a specified job.
                // @Plugin Depenizen, Jobs
                // -->
                if (attribute.startsWith("max")) {
                    return new ElementTag(jobProgression.getMaxExperience())
                            .getAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <JobsJobTag.xp.level>
                // @returns ElementTag(Number)
                // @description
                // Returns the current experience level a player has in a specified job.
                // @Plugin Depenizen, Jobs
                // -->
                if (attribute.startsWith("level")) {
                    return new ElementTag(jobProgression.getLevel())
                            .getAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <JobsJobTag.xp>
                // @returns ElementTag(Decimal)
                // @description
                // Returns the current experience a player has in a specified job.
                // @Plugin Depenizen, Jobs
                // -->
                return new ElementTag(jobProgression.getExperience()).getAttribute(attribute);
            }
        }

        // <--[tag]
        // @attribute <JobsJobTag.color>
        // @returns ElementTag
        // @description
        // Returns the ChatColor of the job.
        // @Plugin Depenizen, Jobs
        // -->
        if (attribute.startsWith("color")) {
            return new ElementTag(job.getChatColor().toString())
                    .getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <JobsJobTag.description>
        // @returns ElementTag
        // @description
        // Returns the description of the job.
        // @Plugin Depenizen, Jobs
        // -->
        else if (attribute.startsWith("description")) {
            return new ElementTag(job.getDescription()).getAttribute(attribute.fulfill(1));
        }

        else if (attribute.startsWith("name")) {
            attribute = attribute.fulfill(1);

            // <--[tag]
            // @attribute <JobsJobTag.name.short>
            // @returns ElementTag
            // @description
            // Returns the shortened name of the job.
            // @Plugin Depenizen, Jobs
            // -->
            if (attribute.startsWith("short")) {
                return new ElementTag(job.getShortName())
                        .getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <JobsJobTag.name>
            // @returns ElementTag
            // @description
            // Returns the name of the job.
            // @Plugin Depenizen, Jobs
            // -->
            return new ElementTag(job.getName()).getAttribute(attribute);
        }

        // <--[tag]
        // @attribute <jobs@job.type>
        // @returns ElementTag
        // @description
        // Always returns 'Job' for JobsJob objects. All objects fetchable by the Object Fetcher will return the
        // type of object that is fulfilling this attribute.
        // @Plugin Depenizen, Jobs
        // -->
        if (attribute.startsWith("type")) {
            return new ElementTag("Job").getAttribute(attribute.fulfill(1));
        }

        return new ElementTag(identify()).getAttribute(attribute);

    }
}
