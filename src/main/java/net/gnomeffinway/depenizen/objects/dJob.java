package net.gnomeffinway.depenizen.objects;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.zford.jobs.Jobs;
import me.zford.jobs.container.Job;
import me.zford.jobs.container.JobProgression;
import me.zford.jobs.container.JobsPlayer;

import net.aufdemrand.denizen.objects.Element;
import net.aufdemrand.denizen.objects.ObjectFetcher;
import net.aufdemrand.denizen.objects.dObject;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.tags.Attribute;
import net.aufdemrand.denizen.utilities.debugging.dB;

public class dJob implements dObject {

    /////////////////////
    //   PATTERNS
    /////////////////

    final static Pattern job_with_player = Pattern.compile("(job@)(.+)(\\[(.+)\\])", Pattern.CASE_INSENSITIVE);
    
    /////////////////////
    //   OBJECT FETCHER
    /////////////////
    
    @ObjectFetcher("job")
    public static dJob valueOf(String string) {
        if (string == null) return null;        
        
        ////////
        // Match job name
        
        dJob job = null;
        
        Matcher m = job_with_player.matcher(string);
        if (m.matches()) {
            if (Jobs.getJob(m.group(2)) != null) {
                job = new dJob(Jobs.getJob(m.group(2)));
                job.setJobProgression(Jobs.getPlayerManager().getJobsPlayer(m.group(4)).getJobProgression(job.getJob()));
            }
            return job;
        }
        string = string.replace("job@", "");
        return new dJob(Jobs.getJob(string));
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
    
    public dJob(Job job) {
        if (job != null)
            this.job = job;
        else
            dB.echoError("Job referenced is null!");
    }
    
    public dJob(JobProgression jobProgression) {
        if (jobProgression != null) {
            this.jobProgression = jobProgression;
            this.job = jobProgression.getJob();
        }
        else
            dB.echoError("Job referenced is null!");
    }
    
    public dJob(Job job, JobsPlayer jobOwner) {
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
    
    public dJob setJobProgression(JobProgression jobProgression) {
        this.jobProgression = jobProgression;
        return this;
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
    public String getType() {
        return "Job";
    }

    @Override
    public String identify() {
        return "job@" + job.getName() + (jobOwner == null ? "" : ("[" + jobOwner.getName() + "]"));
    }

    @Override
    public String getAttribute(Attribute attribute) {
        
        if (jobProgression != null) {
            if (attribute.startsWith("xp")) {
                attribute = attribute.fulfill(1);
                
                if (attribute.startsWith("max")) {
                    return new Element(jobProgression.getMaxExperience())
                            .getAttribute(attribute.fulfill(1));
                }
                
                if (attribute.startsWith("level")) {
                    return new Element(jobProgression.getLevel())
                            .getAttribute(attribute.fulfill(1));
                }
                
                return new Element(jobProgression.getExperience()).getAttribute(attribute);
            }
        }
        
        if (attribute.startsWith("description"))
            return new Element(job.getDescription()).getAttribute(attribute.fulfill(1));
        
        else if (attribute.startsWith("name")) {
            attribute = attribute.fulfill(1);
            
            if (attribute.startsWith("short")) {
                return new Element(job.getShortName())
                        .getAttribute(attribute.fulfill(1));
            }
            
            return new Element(job.getName()).getAttribute(attribute);
        }
        
        else if (attribute.startsWith("color"))
            return new Element(job.getChatColor().toString())
                .getAttribute(attribute.fulfill(1));
        
        return new Element(identify()).getAttribute(attribute.fulfill(1));
        
    }
    
}
