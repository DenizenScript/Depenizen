package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizen.Denizen;
import com.denizenscript.denizencore.DenizenCore;
import com.denizenscript.depenizen.bukkit.properties.jobs.JobPlayer;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.commands.jobs.JobsCommand;
import com.denizenscript.depenizen.bukkit.objects.jobs.JobsJobTag;
import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.container.Job;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.ObjectFetcher;
import com.denizenscript.denizencore.tags.TagRunnable;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.denizencore.tags.ReplaceableTagEvent;
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
        DenizenCore.commandRegistry.registerCommand(JobsCommand.class);
    }

    public void tagEvent(ReplaceableTagEvent event) {
        Attribute attribute = event.getAttributes();
        JobsJobTag j = null;
        if (attribute.hasContext(1)) {
            // Documented below.
            if (JobsJobTag.matches(attribute.getContext(1))) {
                j = attribute.contextAsType(1, JobsJobTag.class);
            }
            else {
                attribute.echoError("Could not match '" + attribute.getContext(1) + "' to a valid job!");
                return;
            }
        }
        else {
            // <--[tag]
            // @attribute <jobs[(<name>)]>
            // @returns ListTag(JobsJobTag)/JobsJobTag
            // @plugin Depenizen, Jobs
            // @description
            // Returns a list of all known jobs, or the job by the given input name if one is given.
            // -->
            ListTag jobList = new ListTag();
            for (Job jb : Jobs.getJobs()) {
                jobList.addObject(new JobsJobTag(jb));
            }
            event.setReplacedObject(jobList.getObjectAttribute(attribute.fulfill(1)));
        }
        if (j == null) {
            attribute.echoError("Invalid or missing job!");
            return;
        }
        event.setReplacedObject(j.getObjectAttribute(attribute.fulfill(1)));

    }
}
