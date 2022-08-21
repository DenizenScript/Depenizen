package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizencore.DenizenCore;
import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.utilities.debugging.SlowWarning;
import com.denizenscript.depenizen.bukkit.events.jobs.*;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.commands.jobs.JobsCommand;
import com.denizenscript.depenizen.bukkit.objects.jobs.JobsJobTag;
import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.container.Job;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.ObjectFetcher;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.depenizen.bukkit.properties.jobs.JobsPlayerProperties;
import com.denizenscript.denizencore.tags.TagManager;

public class JobsBridge extends Bridge {

    public static SlowWarning blankJobsConstructor = new SlowWarning("jobsEmptyConstructor", "The tag 'jobs' from Depenizen/Jobs is deprecated: use 'jobs.server_jobs'");

    @Override
    public void init() {
        ScriptEvent.registerScriptEvent(JobsJobsPaymentScriptEvent.class);
        ScriptEvent.registerScriptEvent(JobsJobsExpGainScriptEvent.class);
        ScriptEvent.registerScriptEvent(JobsJobsJoinScriptEvent.class);
        ScriptEvent.registerScriptEvent(JobsJobsLeaveScriptEvent.class);
        ScriptEvent.registerScriptEvent(JobsJobsLevelUpScriptEvent.class);
        ObjectFetcher.registerWithObjectFetcher(JobsJobTag.class, JobsJobTag.tagProcessor);
        PropertyParser.registerProperty(JobsPlayerProperties.class, PlayerTag.class);

        // <--[tag]
        // @attribute <jobs>
        // @returns ListTag(JobsJobTag)
        // @plugin Depenizen, Jobs
        // @description
        // Returns the list of all jobs on the server.
        // -->
        TagManager.registerTagHandler(ObjectTag.class, "jobs", (attribute) -> {
            ListTag jobsList = new ListTag();
            for (Job job : Jobs.getJobs()) {
                jobsList.addObject(new JobsJobTag(job));
            }
            return jobsList;
        });

        // <--[tag]
        // @attribute <jobs_job[<name>]>
        // @returns JobsJobTag
        // @plugin Depenizen, Jobs
        // @description
        // Returns the job tag with the given name.
        // -->
        TagManager.registerTagHandler(JobsJobTag.class, "jobs_job", attribute -> {
            if (attribute.hasParam()) {
                return JobsJobTag.valueOf(attribute.getParam(), attribute.context);
            }
            return null;
        });
        DenizenCore.commandRegistry.registerCommand(JobsCommand.class);
    }
}
