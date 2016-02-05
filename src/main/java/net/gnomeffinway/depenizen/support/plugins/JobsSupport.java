package net.gnomeffinway.depenizen.support.plugins;

import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizencore.tags.Attribute;
import net.gnomeffinway.depenizen.commands.JobsCommands;
import net.gnomeffinway.depenizen.extensions.jobs.JobsPlayerExtension;
import net.gnomeffinway.depenizen.objects.jobs.JobsJob;
import net.gnomeffinway.depenizen.objects.jobs.properties.JobPlayer;
import net.gnomeffinway.depenizen.support.Support;

public class JobsSupport extends Support {

    public JobsSupport() {
        registerObjects(JobsJob.class);
        registerProperty(JobPlayer.class, JobsJob.class);
        registerProperty(JobsPlayerExtension.class, dPlayer.class);
        registerAdditionalTags("jobs");
        new JobsCommands().activate().as("jobs").withOptions("See Documentation.", 2);
    }

    @Override
    public String additionalTags(Attribute attribute) {

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
