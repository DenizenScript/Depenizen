package com.denizenscript.depenizen.bukkit.support.plugins;

import com.denizenscript.depenizen.bukkit.objects.jobs.properties.JobPlayer;
import com.denizenscript.depenizen.bukkit.support.Support;
import com.denizenscript.depenizen.bukkit.commands.JobsCommands;
import com.denizenscript.depenizen.bukkit.objects.jobs.JobsJob;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.tags.TagContext;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizencore.tags.Attribute;
import com.denizenscript.depenizen.bukkit.extensions.jobs.JobsPlayerExtension;

public class JobsSupport extends Support {

    public JobsSupport() {
        registerObjects(JobsJob.class);
        registerProperty(JobPlayer.class, JobsJob.class);
        registerProperty(JobsPlayerExtension.class, dPlayer.class);
        registerAdditionalTags("jobs");
        new JobsCommands().activate().as("jobs").withOptions("See Documentation.", 2);
    }

    @Override
    public String additionalTags(Attribute attribute, TagContext tagContext) {

        if (attribute.startsWith("jobs")) {

            // JobsTags require a... dJob!
            JobsJob j = null;

            // Job tag may specify a new job in the <jobs[context]...> portion of the tag.

            // Check if this is a valid player and update the dPlayer object reference.
            if (attribute.hasContext(1)) {
                if (JobsJob.matches(attribute.getContext(1))) {
                    j = JobsJob.valueOf(attribute.getContext(1));
                }
                else {
                    dB.echoError("Could not match '" + attribute.getContext(1) + "' to a valid job!");
                    return null;
                }
            }

            if (j == null) {
                dB.echoError("Invalid or missing job!");
                return null;
            }

            return j.getAttribute(attribute.fulfill(1));

        }

        return null;

    }
}
