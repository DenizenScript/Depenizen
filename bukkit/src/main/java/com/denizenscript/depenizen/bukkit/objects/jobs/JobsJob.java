package com.denizenscript.depenizen.bukkit.objects.jobs;

import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.container.Job;
import com.gamingmesh.jobs.container.JobProgression;
import com.gamingmesh.jobs.container.JobsPlayer;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.Fetchable;
import net.aufdemrand.denizencore.objects.ObjectFetcher;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.objects.properties.PropertyParser;
import net.aufdemrand.denizencore.tags.Attribute;
import net.aufdemrand.denizencore.tags.TagContext;

import java.util.regex.Matcher;

public class JobsJob implements dObject {

    /////////////////////
    //   OBJECT FETCHER
    /////////////////

    public static JobsJob valueOf(String string) {
        return valueOf(string, null);
    }

    @Fetchable("job")
    public static JobsJob valueOf(String string, TagContext context) {
        if (string == null) {
            return null;
        }

        ////////
        // Match job name

        JobsJob job = null;

        Matcher m = ObjectFetcher.DESCRIBED_PATTERN.matcher(string);
        if (m.matches()) {
            return ObjectFetcher.getObjectFrom(JobsJob.class, string);
        }
        return new JobsJob(Jobs.getJob(string.replace("job@", "")));
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

    public JobsJob(Job job) {
        this.job = job;
    }

    public JobsJob(Job job, JobsPlayer jobOwner) {
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

    public JobsJob setJobProgression(JobProgression jobProgression) {
        this.jobProgression = jobProgression;
        return this;
    }

    public boolean hasOwner() {
        return jobOwner != null;
    }

    public dPlayer getOwner() {
        return new dPlayer(jobOwner.getPlayerUUID());
    }

    public void setOwner(dPlayer player) {
        this.jobOwner = Jobs.getPlayerManager().getJobsPlayerOffline(player.getOfflinePlayer());
        this.jobProgression = jobOwner.getJobProgression(job);
    }

    /////////////////////
    //   dObject Methods
    /////////////////

    private String prefix = "Job";

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public dObject setPrefix(String prefix) {
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
    public String getAttribute(Attribute attribute) {

        if (jobProgression != null) {

            if (attribute.startsWith("xp")) {
                attribute = attribute.fulfill(1);

                // <--[tag]
                // @attribute <job@job[<player>].xp.max>
                // @returns Element(Number)
                // @description
                // Returns the maximum experience a player can get in a specified job.
                // @plugin Depenizen, Jobs
                // -->
                if (attribute.startsWith("max")) {
                    return new Element(jobProgression.getMaxExperience())
                            .getAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <job@job[<player>].xp.level>
                // @returns Element(Number)
                // @description
                // Returns the current experience level a player has in a specified job.
                // @plugin Depenizen, Jobs
                // -->
                if (attribute.startsWith("level")) {
                    return new Element(jobProgression.getLevel())
                            .getAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <job@job[<player>].xp>
                // @returns Element(Double)
                // @description
                // Returns the current experience a player has in a specified job.
                // @plugin Depenizen, Jobs
                // -->
                return new Element(jobProgression.getExperience()).getAttribute(attribute);
            }
        }

        // <--[tag]
        // @attribute <job@job.color>
        // @returns Element(ChatColor)
        // @description
        // Returns the ChatColor of the job.
        // @plugin Depenizen, Jobs
        // -->
        if (attribute.startsWith("color")) {
            return new Element(job.getChatColor().toString())
                    .getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <job@job.description>
        // @returns Element
        // @description
        // Returns the description of the job.
        // @plugin Depenizen, Jobs
        // -->
        else if (attribute.startsWith("description")) {
            return new Element(job.getDescription()).getAttribute(attribute.fulfill(1));
        }

        else if (attribute.startsWith("name")) {
            attribute = attribute.fulfill(1);

            // <--[tag]
            // @attribute <job@job.name.short>
            // @returns Element
            // @description
            // Returns the shortened name of the job.
            // @plugin Depenizen, Jobs
            // -->
            if (attribute.startsWith("short")) {
                return new Element(job.getShortName())
                        .getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <job@job.name>
            // @returns Element
            // @description
            // Returns the name of the job.
            // @plugin Depenizen, Jobs
            // -->
            return new Element(job.getName()).getAttribute(attribute);
        }

        // <--[tag]
        // @attribute <jobs@job.type>
        // @returns Element
        // @description
        // Always returns 'Job' for JobsJob objects. All objects fetchable by the Object Fetcher will return the
        // type of object that is fulfilling this attribute.
        // @plugin Depenizen, Jobs
        // -->
        if (attribute.startsWith("type")) {
            return new Element("Job").getAttribute(attribute.fulfill(1));
        }

        return new Element(identify()).getAttribute(attribute);

    }
}
