package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizencore.DenizenCore;
import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.ObjectFetcher;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.tags.TagManager;
import com.denizenscript.denizencore.utilities.debugging.SlowWarning;
import com.denizenscript.denizencore.utilities.debugging.Warning;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.commands.jobs.JobsCommand;
import com.denizenscript.depenizen.bukkit.events.jobs.*;
import com.denizenscript.depenizen.bukkit.objects.jobs.JobsJobTag;
import com.denizenscript.depenizen.bukkit.properties.jobs.JobsPlayerProperties;
import com.gamingmesh.jobs.Jobs;

public class JobsBridge extends Bridge {

    public static Warning jobsDeprecatedConstructor = new SlowWarning("jobsDeprecatedConstructor", "The 'jobs' constructor from Depenizen/Jobs is deprecated: use 'jobs_job'");
    public static Warning jobsNameShort = new SlowWarning("jobsNameShort", "The tag 'JobsJobTag.name.short' from Depenizen/Jobs is deprecated: use 'JobsJobTag.short_name'");
    public static Warning jobsXpLevel = new SlowWarning("jobsXpLevel", "The tag 'JobsJobTag.xp.level' from Depenizen/Jobs is deprecated: use 'JobsJobTag.level'");
    public static Warning jobsXpMax = new SlowWarning("jobsXpMax", "The tag 'JobsJobTag.xp.max' from Depenizen/Jobs is deprecated: use 'JobsJobTag.max_xp'");
    public static Warning jobsSingleLineDescription = new SlowWarning("jobsSingleLineDescription", "'JobsJobTag.description' is deprecated, as single-line descriptions are deprecated in Jobs. Use 'JobsJobTag.full_description' instead.");

    @Override
    public void init() {
        ScriptEvent.registerScriptEvent(JobsJobsPaymentScriptEvent.class);
        ScriptEvent.registerScriptEvent(JobsJobsExpGainScriptEvent.class);
        ScriptEvent.registerScriptEvent(JobsJobsJoinScriptEvent.class);
        ScriptEvent.registerScriptEvent(JobsJobsLeaveScriptEvent.class);
        ScriptEvent.registerScriptEvent(JobsJobsLevelUpScriptEvent.class);
        ObjectFetcher.registerWithObjectFetcher(JobsJobTag.class, JobsJobTag.tagProcessor);
        DenizenCore.commandRegistry.registerCommand(JobsCommand.class);
        JobsPlayerProperties.register();

        // <--[tag]
        // @attribute <jobs>
        // @returns ListTag(JobsJobTag)
        // @plugin Depenizen, Jobs
        // @description
        // Returns the list of all jobs on the server.
        // -->
        TagManager.registerTagHandler(ObjectTag.class, "jobs", (attribute) -> {
            if (attribute.hasParam()) {
                jobsDeprecatedConstructor.warn(attribute.context);
                return JobsJobTag.valueOf(attribute.getParam(), attribute.context);
            }
            return new ListTag(Jobs.getJobs(), JobsJobTag::new);
        });

        // <--[tag]
        // @attribute <jobs_job[<name>]>
        // @returns JobsJobTag
        // @plugin Depenizen, Jobs
        // @description
        // Returns the job tag with the given name.
        // -->
        TagManager.registerTagHandler(JobsJobTag.class, JobsJobTag.class, "jobs_job", (attribute, param) -> param);
    }
}
