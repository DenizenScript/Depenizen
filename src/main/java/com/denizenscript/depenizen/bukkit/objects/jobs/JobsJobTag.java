package com.denizenscript.depenizen.bukkit.objects.jobs;

import com.denizenscript.denizencore.objects.*;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.tags.ObjectTagProcessor;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.denizencore.utilities.debugging.SlowWarning;
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
    public static ObjectTagProcessor<JobsJobTag> tagProcessor = new ObjectTagProcessor<>();

    // <--[ObjectType]
    // @name JobsJobTag
    // @prefix job
    // @base ElementTag
    // @format
    // The identity format for jobs is the player UUID (optional), followed by the job name
    // For example: job@05f57b6e-77ba-4546-b214-b58dacc30356,job_name
    // Or: job@job_name
    //
    // @plugin Depenizen, Jobs
    // @description
    // A JobsJobTag represents a Jobs job, with a player's progression if specified.
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
        if (string.startsWith("job@")) {
            string = string.substring("job@".length());
        }
        if (string.contains("@")) {
            return null;
        }
        int comma = string.indexOf(',');
        PlayerTag player = null;
        if (comma > 0) {
            //player = new PlayerTag(UUID.fromString(string.substring(0, comma)));
            player = PlayerTag.valueOf(string.substring(0, comma), context);
            string = string.substring(comma + 1);
        }
        JobsJobTag job = new JobsJobTag(Jobs.getJob(string));
        if (player != null) {
            if (player.isValid()) {
                job.setOwner(player);
            } else {
                Debug.echoError("Player specified in JobsJobTag is not valid");
            }
        }
        return job;
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
    public String identify() {
        if (jobOwner != null) {
            return "job@" + jobOwner.playerUUID + "," + job.getName();
        }
        return "job@" + job.getName();
    }

    @Override
    public String identifySimple() {
        return identify();
    }

    @Override
    public String toString() {
        return identify();
    }

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {
        return tagProcessor.getObjectAttribute(this, attribute);
    }

    public static SlowWarning nameShortTag = new SlowWarning("jobsNameShort", "The tag 'JobsJobTag.name.short' from Depenizen/Jobs is deprecated: use 'JobsJobTag.short_name'");
    public static SlowWarning xpLevelTag = new SlowWarning("jobsXpLevel", "The tag 'JobsJobTag.xp.level' from Depenizen/Jobs is deprecated: use 'JobsJobTag.level'");
    public static SlowWarning xpMaxTag = new SlowWarning("jobsXpMax", "The tag 'JobsJobTag.xp.max' from Depenizen/Jobs is deprecated: use 'JobsJobTag.max_xp'");

    @Override
    public void applyProperty(Mechanism mechanism) {
        mechanism.echoError("Cannot apply Properties to a Jobs Job!");
    }

    public static void register() {
        PropertyParser.registerPropertyTagHandlers(JobsJobTag.class, tagProcessor);

        // <--[tag]
        // @attribute <JobsJobTag.description>
        // @returns ElementTag
        // @plugin Depenizen, Jobs
        // @deprecated Use 'JobsJobTag.full_description'
        // @description
        // Single line description is deprecated in Jobs. Use <@link tag JobsJobTag.full_description> instead.
        // -->
        tagProcessor.registerTag(ElementTag.class, "description", (attribute, object) -> {
            return new ElementTag(object.getJob().getDescription());
        });

        // <--[tag]
        // @attribute <JobsJobTag.full_description>
        // @returns ListTag
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
        // Deprecated in favor of <@link tag JobsJobTag.short_name>.
        // -->

        // <--[tag]
        // @attribute <JobsJobTag.name>
        // @returns ElementTag
        // @plugin Depenizen, Jobs
        // @description
        // Returns the name of the job.
        // -->
        tagProcessor.registerTag(ElementTag.class, "name", (attribute, object) -> {
            if (attribute.startsWith("short", 2)) {
                nameShortTag.warn(attribute.context);
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
        // Deprecated in favor of <@link tag JobsJobTag.max_xp>.
        // -->

        // <--[tag]
        // @attribute <JobsJobTag.xp.level>
        // @returns ElementTag(Number)
        // @plugin Depenizen, Jobs
        // @deprecated Use 'JobsJobTag.level'
        // @description
        // Deprecated in favor of <@link tag JobsJobTag.level>.
        // -->

        // <--[tag]
        // @attribute <JobsJobTag.xp>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, Jobs
        // @mechanism JobsJobTag.xp
        // @description
        // Returns the current experience a player has for the current level in a specified job.
        // -->
        tagProcessor.registerTag(ElementTag.class, "xp", (attribute, object) -> {
            if (object.jobProgression != null) {
                if (attribute.startsWith("max", 2)) {
                    xpMaxTag.warn(attribute.context);
                    attribute.fulfill(1);
                    return new ElementTag(object.jobProgression.getMaxExperience());
                }
                if (attribute.startsWith("level", 2)) {
                    xpLevelTag.warn(attribute.context);
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

        // <--[tag]
        // @attribute <JobsJobTag.player>
        // @returns PlayerTag
        // @plugin Depenizen, Jobs
        // @description
        // Returns the player the job progression for this tag belongs to.
        // -->
        tagProcessor.registerTag(PlayerTag.class, "player", (attribute, object) -> {
            return object.getOwner();
        });
    }

    @Override
    public void adjust(Mechanism mechanism) {

        // <--[mechanism]
        // @object JobsJobTag
        // @name xp
        // @input ElementTag(Decimal)
        // @plugin Depenizen, Jobs
        // @description
        // Set the amount of job XP the player has in this job.
        // @tags
        // <JobsJobTag.xp>
        // -->
        if (!mechanism.isProperty && mechanism.matches("xp") && mechanism.requireDouble()) {
            if (jobProgression == null) {
                mechanism.echoError("This mechanism requires the object to be linked to a player.");
                return;
            }
            jobProgression.setExperience(mechanism.getValue().asDouble());
        }

        tagProcessor.processMechanism(this, mechanism);
        CoreUtilities.autoPropertyMechanism(this, mechanism);
    }
}
