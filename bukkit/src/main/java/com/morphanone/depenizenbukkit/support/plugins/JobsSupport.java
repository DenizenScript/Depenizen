package com.morphanone.depenizenbukkit.support.plugins;

import com.morphanone.depenizenbukkit.commands.JobsCommands;
import com.morphanone.depenizenbukkit.objects.jobs.JobsJob;
import com.morphanone.depenizenbukkit.objects.jobs.properties.JobPlayer;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.tags.TagContext;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizencore.tags.Attribute;
import com.morphanone.depenizenbukkit.extensions.jobs.JobsPlayerExtension;
import com.morphanone.depenizenbukkit.support.Support;

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
