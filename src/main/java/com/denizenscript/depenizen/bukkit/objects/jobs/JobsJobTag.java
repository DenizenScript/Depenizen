package com.denizenscript.depenizen.bukkit.objects.jobs;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.Adjustable;
import com.denizenscript.denizencore.objects.Fetchable;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.ObjectTagProcessor;
import com.denizenscript.denizencore.tags.TagContext;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.depenizen.bukkit.bridges.JobsBridge;
import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.container.Job;
import com.gamingmesh.jobs.container.JobProgression;
import com.gamingmesh.jobs.container.JobsPlayer;

import java.util.UUID;

public class JobsJobTag implements ObjectTag, Adjustable {

    // <--[ObjectType]
    // @name JobsJobTag
    // @prefix job
    // @base ElementTag
    // @format
    // The identity format for jobs is the player UUID (optional), followed by the job name
    // For example: job@460e96b9-7a0e-416d-b2c3-4508164b8b1b,job_name
    // Or: job@job_name
    //
    // @plugin Depenizen, Jobs
    // @description
    // A JobsJobTag represents a Jobs job, with a player's progression if specified.
    //
    // @Matchable
    // JobsJobTag matchers, sometimes identified as "<job>":
    // "job" plaintext: always matches.
    // Job name: matches if the job name matches the input, using advanced matchers.
    // -->

    /////////////////////
    //   OBJECT FETCHER
    /////////////////

    @Fetchable("job")
    public static JobsJobTag valueOf(String string, TagContext context) {
        if (string.startsWith("job@")) {
            string = string.substring("job@".length());
        }
        int comma = string.indexOf(',');
        UUID playerUUID = null;
        if (comma > 0) {
            try {
                playerUUID = UUID.fromString(string.substring(0, comma));
                string = string.substring(comma + 1);
            }
            catch (IllegalArgumentException iae) {
                if (context == null || context.showErrors()) {
                    Debug.echoError("valueOf JobsJobTag returning null: Invalid UUID '" + string.substring(0, comma) + "' specified.");
                }
                return null;
            }
        }
        Job job = Jobs.getJob(string);
        if (job == null) {
            if (context == null || context.showErrors()) {
                Debug.echoError("valueOf JobsJobTag returning null: Invalid job '" + string + "' specified.");
            }
            return null;
        }
        JobsJobTag jobTag = new JobsJobTag(job);
        if (playerUUID != null) {
            jobTag.setOwner(playerUUID);
            if (!jobTag.hasOwner()) {
                Debug.echoError("Player specified in JobsJobTag is not valid");
            }
        }
        return jobTag;
    }

    public static boolean matches(String arg) {
        if (arg.startsWith("job@")) {
            return true;
        }
        return valueOf(arg, CoreUtilities.noDebugContext) != null;
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

    public UUID getOwner() {
        if (jobOwner == null) {
            return null;
        }
        return jobOwner.getUniqueId();
    }

    public void setOwner(UUID playerUUID) {
        this.jobOwner = Jobs.getPlayerManager().getJobsPlayer(playerUUID);
        if (jobOwner == null) {
            return;
        }
        this.jobProgression = jobOwner.getJobProgression(job);
    }

    /////////////////////
    //   ObjectTag Methods
    /////////////////

    public static void register() {

        // <--[tag]
        // @attribute <JobsJobTag.description>
        // @returns ElementTag
        // @plugin Depenizen, Jobs
        // @deprecated Use 'JobsJobTag.full_description'
        // @description
        // Single line description is deprecated in Jobs. Use <@link tag JobsJobTag.full_description> instead.
        // -->
        tagProcessor.registerTag(ElementTag.class, "description", (attribute, object) -> {
            JobsBridge.jobsSingleLineDescription.warn(attribute.context);
            return new ElementTag(object.getJob().getDescription(), true);
        });

        // <--[tag]
        // @attribute <JobsJobTag.full_description>
        // @returns ListTag
        // @plugin Depenizen, Jobs
        // @description
        // Returns the full description of the job.
        // -->
        tagProcessor.registerTag(ListTag.class, "full_description", (attribute, object) -> {
            return new ListTag(object.getJob().getFullDescription(), true);
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
                JobsBridge.jobsNameShort.warn(attribute.context);
                attribute.fulfill(1);
                return new ElementTag(object.getJob().getShortName(), true);
            }
            return new ElementTag(object.getJob().getName(), true);
        });

        // <--[tag]
        // @attribute <JobsJobTag.short_name>
        // @returns ElementTag
        // @plugin Depenizen, Jobs
        // @description
        // Returns the shortened name of the job.
        // -->
        tagProcessor.registerTag(ElementTag.class, "short_name", (attribute, object) -> {
            return new ElementTag(object.getJob().getShortName(), true);
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
            if (object.jobProgression == null) {
                return null;
            }
            if (attribute.startsWith("max", 2)) {
                JobsBridge.jobsXpMax.warn(attribute.context);
                attribute.fulfill(1);
                return new ElementTag(object.jobProgression.getMaxExperience());
            }
            if (attribute.startsWith("level", 2)) {
                JobsBridge.jobsXpLevel.warn(attribute.context);
                attribute.fulfill(1);
                return new ElementTag(object.jobProgression.getLevel());
            }
            return new ElementTag(object.jobProgression.getExperience());
        });

        // <--[tag]
        // @attribute <JobsJobTag.level>
        // @returns ElementTag(Number)
        // @plugin Depenizen, Jobs
        // @description
        // Returns the current level a player has in a specified job.
        // -->
        tagProcessor.registerTag(ElementTag.class, "level", (attribute, object) -> {
            return object.jobProgression != null ? new ElementTag(object.jobProgression.getLevel()) : null;
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
            if (object.jobProgression == null) {
                return null;
            }
            if (attribute.hasParam()) {
                return new ElementTag(object.jobProgression.getMaxExperience(attribute.getIntParam()));
            }
            return new ElementTag(object.jobProgression.getMaxExperience());
        });

        // <--[tag]
        // @attribute <JobsJobTag.player>
        // @returns PlayerTag
        // @plugin Depenizen, Jobs
        // @description
        // Returns the player the job progression for this tag belongs to.
        // -->
        tagProcessor.registerTag(PlayerTag.class, "player", (attribute, object) -> {
            UUID ownerUUID = object.getOwner();
            return ownerUUID != null ? new PlayerTag(ownerUUID) : null;
        });

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
        tagProcessor.registerMechanism("xp", false, ElementTag.class, (object, mechanism, input) -> {
            if (!mechanism.requireDouble()) {
                return;
            }
            if (object.jobProgression == null) {
                mechanism.echoError("This mechanism requires the object to be linked to a player.");
                return;
            }
            object.jobProgression.setExperience(input.asDouble());
        });
    }

    public static final ObjectTagProcessor<JobsJobTag> tagProcessor = new ObjectTagProcessor<>();

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {
        return tagProcessor.getObjectAttribute(this, attribute);
    }

    @Override
    public void adjust(Mechanism mechanism) {
        tagProcessor.processMechanism(this, mechanism);
    }

    @Override
    public void applyProperty(Mechanism mechanism) {
        mechanism.echoError("Cannot apply Properties to a Jobs Job!");
    }

    @Override
    public String identify() {
        return identify("job@", ",");
    }

    @Override
    public String debuggable() {
        return identify("<LG>job@<Y>", "<LG>,<Y>");
    }

    public String identify(String prefix, String separator) {
        if (jobOwner != null) {
            return prefix + jobOwner.getUniqueId() + separator + job.getName();
        }
        return prefix + job.getName();
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
    public boolean advancedMatches(String matcher) {
        String lowerMatcher = CoreUtilities.toLowerCase(matcher);
        if (lowerMatcher.equals("job")) {
            return true;
        }
        return ScriptEvent.createMatcher(lowerMatcher).doesMatch(getJob().getName());
    }

    @Override
    public boolean isUnique() {
        return true;
    }

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
}
