package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.properties.jobs.JobPlayer;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.commands.jobs.JobsCommand;
import com.denizenscript.depenizen.bukkit.objects.jobs.JobsJobTag;
import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.container.Job;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.DenizenAPI;
import com.denizenscript.denizencore.objects.ObjectFetcher;
import com.denizenscript.denizencore.tags.TagRunnable;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.denizencore.tags.ReplaceableTagEvent;
import com.denizenscript.denizen.utilities.debugging.Debug;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.depenizen.bukkit.properties.jobs.JobsPlayerProperties;
import com.denizenscript.denizencore.tags.TagManager;

public class JobsBridge extends Bridge {

    @Override
    public void init() {
        ObjectFetcher.registerWithObjectFetcher(JobsJobTag.class);
        PropertyParser.registerProperty(JobPlayer.class, JobsJobTag.class);
        PropertyParser.registerProperty(JobsPlayerProperties.class, PlayerTag.class);
        TagManager.registerTagHandler(new TagRunnable.RootForm() {
            @Override
            public void run(ReplaceableTagEvent event) {
                tagEvent(event);
            }
        }, "jobs");
        DenizenAPI.getCurrentInstance().getCommandRegistry().registerCoreMember(JobsCommand.class,
                "JOBS", "jobs [promote/demote/join/quit] [<job>] (<#>)", 2);
    }

    public void tagEvent(ReplaceableTagEvent event) {
        Attribute attribute = event.getAttributes();

        JobsJobTag j = null;

        if (attribute.hasContext(1)) {
            // Documented below.
            if (JobsJobTag.matches(attribute.getContext(1))) {
                j = JobsJobTag.valueOf(attribute.getContext(1));
            }
            else {
                Debug.echoError("Could not match '" + attribute.getContext(1) + "' to a valid job!");
                return;
            }
        }
        else {
            // <--[tag]
            // @attribute <jobs[(<name>)]>
            // @returns ListTag(dJob)/dJob
            // @plugin Depenizen, Jobs
            // @description
            // Returns a list of all known dJobs, or the job by the given input name if one is given.
            // -->
            ListTag jobList = new ListTag();
            for (Job jb : Jobs.getJobs()) {
                jobList.addObject(new JobsJobTag(jb));
            }
            event.setReplacedObject(jobList.getObjectAttribute(attribute.fulfill(1)));
        }

        if (j == null) {
            Debug.echoError("Invalid or missing job!");
            return;
        }

        event.setReplacedObject(j.getObjectAttribute(attribute.fulfill(1)));

    }
}
