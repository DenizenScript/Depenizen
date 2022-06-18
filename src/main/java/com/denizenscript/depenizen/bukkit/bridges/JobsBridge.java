package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizencore.DenizenCore;
import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.depenizen.bukkit.events.jobs.*;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.commands.jobs.JobsCommand;
import com.denizenscript.depenizen.bukkit.objects.jobs.JobsJobTag;
import com.denizenscript.depenizen.bukkit.properties.jobs.JobsJobProperties;
import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.container.Job;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.ObjectFetcher;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.depenizen.bukkit.properties.jobs.JobsPlayerProperties;
import com.denizenscript.denizencore.tags.TagManager;

public class JobsBridge extends Bridge {

    @Override
    public void init() {
        /////////////////
        // Register Events
        /////////////////
        ScriptEvent.registerScriptEvent(JobsJobsPaymentScriptEvent.class);
        ScriptEvent.registerScriptEvent(JobsJobsExpGainScriptEvent.class);
        ScriptEvent.registerScriptEvent(JobsJobsJoinScriptEvent.class);
        ScriptEvent.registerScriptEvent(JobsJobsLeaveScriptEvent.class);
        ScriptEvent.registerScriptEvent(JobsJobsLevelUpScriptEvent.class);
        /////////////////
        // Register Tag and Properties
        /////////////////
        ObjectFetcher.registerWithObjectFetcher(JobsJobTag.class, JobsJobTag.tagProcessor);
        PropertyParser.registerProperty(JobsPlayerProperties.class, PlayerTag.class);
        PropertyParser.registerProperty(JobsJobProperties.class, JobsJobTag.class);

        /////////////////
        // Register root tag handler
        /////////////////

        // <--[tag]
        // @attribute <jobs[<name>]>
        // @returns JobsJobTag
        // @plugin Depenizen, Jobs
        // @description
        // Returns the job tag with the given name
        // -->
        TagManager.registerTagHandler(ObjectTag.class, "jobs", (attribute) -> {
            if (attribute.hasParam()) {
                return JobsJobTag.valueOf(attribute.getParam(), attribute.context);
            }
            attribute.fulfill(1);
            // <--[tag]
            // @attribute <jobs.server_jobs>
            // @returns ListTag(JobsJobTag)
            // @plugin Depenizen, Jobs
            // @description
            // Returns the list of all jobs on the server
            // -->
            if (attribute.startsWith("server_jobs")) {
                ListTag jobsList = new ListTag();
                for (Job job : Jobs.getJobs()) {
                    jobsList.addObject(new JobsJobTag(job));
                }
                return jobsList;
            }
            // <--[tag]
            // @attribute <jobs>
            // @returns ListTag(JobsJobTag)
            // @plugin Depenizen, Jobs
            // @deprecated Use <jobs.server_jobs> instead
            // @description
            // Returns the list of all jobs on the server
            // -->
            attribute.echoError("<jobs> for retrieving the list of jobs is deprecated, please use <jobs.server_jobs> instead.");
            ListTag jobsList = new ListTag();
            for (Job job : Jobs.getJobs()) {
                jobsList.addObject(new JobsJobTag(job));
            }
            return jobsList;
        });
        DenizenCore.commandRegistry.registerCommand(JobsCommand.class);
    }
}
