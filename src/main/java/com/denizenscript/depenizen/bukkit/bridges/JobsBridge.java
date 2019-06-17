package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.properties.jobs.JobPlayer;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.commands.jobs.JobsCommand;
import com.denizenscript.depenizen.bukkit.objects.jobs.JobsJob;
import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.container.Job;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.utilities.DenizenAPI;
import net.aufdemrand.denizencore.objects.ObjectFetcher;
import net.aufdemrand.denizencore.objects.TagRunnable;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.objects.properties.PropertyParser;
import net.aufdemrand.denizencore.tags.ReplaceableTagEvent;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizencore.tags.Attribute;
import com.denizenscript.depenizen.bukkit.properties.jobs.JobsPlayerProperties;
import net.aufdemrand.denizencore.tags.TagManager;

public class JobsBridge extends Bridge {

    @Override
    public void init() {
        ObjectFetcher.registerWithObjectFetcher(JobsJob.class);
        PropertyParser.registerProperty(JobPlayer.class, JobsJob.class);
        PropertyParser.registerProperty(JobsPlayerProperties.class, dPlayer.class);
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

        JobsJob j = null;

        if (attribute.hasContext(1)) {
            // Documented below.
            if (JobsJob.matches(attribute.getContext(1))) {
                j = JobsJob.valueOf(attribute.getContext(1));
            }
            else {
                dB.echoError("Could not match '" + attribute.getContext(1) + "' to a valid job!");
                return;
            }
        }
        else {
            // <--[tag]
            // @attribute <jobs[(<name>)]>
            // @returns dList(dJob)/dJob
            // @description
            // Returns a list of all known dJobs, or the job by the given input name if one is given.
            // @Plugin Depenizen, Jobs
            // -->
            dList jobList = new dList();
            for (Job jb : Jobs.getJobs()) {
                jobList.add(new JobsJob(jb).identify());
            }
            event.setReplacedObject(jobList.getObjectAttribute(attribute.fulfill(1)));
        }

        if (j == null) {
            dB.echoError("Invalid or missing job!");
            return;
        }

        event.setReplacedObject(j.getObjectAttribute(attribute.fulfill(1)));

    }
}
