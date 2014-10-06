package net.gnomeffinway.depenizen.objects.jobs;

import me.zford.jobs.Jobs;
import me.zford.jobs.container.Job;
import me.zford.jobs.container.JobProgression;
import me.zford.jobs.container.JobsPlayer;
import net.aufdemrand.denizen.objects.*;
import net.aufdemrand.denizen.objects.properties.PropertyParser;
import net.aufdemrand.denizen.tags.Attribute;
import net.aufdemrand.denizen.utilities.debugging.dB;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JobsJob implements dObject {

    /////////////////////
    //   PATTERNS
    /////////////////

    final static Pattern job_with_player = Pattern.compile("(job@)?(.+)(\\[(.+)\\])", Pattern.CASE_INSENSITIVE);

    /////////////////////
    //   OBJECT FETCHER
    /////////////////

    @Fetchable("job")
    public static JobsJob valueOf(String string) {
        if (string == null) return null;

        ////////
        // Match job name

        JobsJob job = null;

        Matcher m = job_with_player.matcher(string);
        if (m.matches()) {
            if (Jobs.getJob(m.group(2)) != null) {
                job = new JobsJob(Jobs.getJob(m.group(2)));
                job.setJobProgression(Jobs.getPlayerManager().getJobsPlayerOffline(dPlayer.valueOf(m.group(4))
                        .getOfflinePlayer()).getJobProgression(job.getJob()));
            }
            return job;
        }
        string = string.replace("job@", "");
        return new JobsJob(Jobs.getJob(string));
    }

    public static boolean matches(String arg) {
        if (valueOf(arg) != null)
            return true;

        return false;
    }

    /////////////////////
    //   STATIC CONSTRUCTORS
    /////////////////

    Job job = null;
    JobProgression jobProgression = null;
    JobsPlayer jobOwner = null;

    public JobsJob(Job job) {
        if (job != null)
            this.job = job;
        else
            dB.echoError("Job referenced is null!");
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
                // @plugin Jobs
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
                // @plugin Jobs
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
                // @plugin Jobs
                // -->
                return new Element(jobProgression.getExperience()).getAttribute(attribute);
            }
        }

        // <--[tag]
        // @attribute <job@job.color>
        // @returns Element(ChatColor)
        // @description
        // Returns the ChatColor of the job.
        // @plugin Jobs
        // -->
        if (attribute.startsWith("color"))
            return new Element(job.getChatColor().toString())
                    .getAttribute(attribute.fulfill(1));

        // <--[tag]
        // @attribute <job@job.description>
        // @returns Element
        // @description
        // Returns the description of the job.
        // @plugin Jobs
        // -->
        else if (attribute.startsWith("description"))
            return new Element(job.getDescription()).getAttribute(attribute.fulfill(1));

        else if (attribute.startsWith("name")) {
            attribute = attribute.fulfill(1);

            // <--[tag]
            // @attribute <job@job.name.short>
            // @returns Element
            // @description
            // Returns the shortened name of the job.
            // @plugin Jobs
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
            // @plugin Jobs
            // -->
            return new Element(job.getName()).getAttribute(attribute);
        }

        return new Element(identify()).getAttribute(attribute.fulfill(1));

    }
}
