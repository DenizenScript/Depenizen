package net.gnomeffinway.depenizen.support.plugins;

import me.zford.jobs.Jobs;
import me.zford.jobs.container.Job;
import me.zford.jobs.container.JobsPlayer;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.tags.Attribute;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.gnomeffinway.depenizen.commands.JobsCommands;
import net.gnomeffinway.depenizen.objects.jobs.JobsJob;
import net.gnomeffinway.depenizen.objects.properties.jobs.JobPlayer;
import net.gnomeffinway.depenizen.support.Support;

public class JobsSupport extends Support {

    public JobsSupport() {
        registerObjects(JobsJob.class);
        registerProperty(JobPlayer.class, JobsJob.class);
        registerAdditionalTags("jobs");
        new JobsCommands().activate().as("jobs").withOptions("see documentation", 2);
    }

    @Override
    public String playerTags(dPlayer pl, Attribute attribute) {

        JobsPlayer player = new JobsPlayer(pl.getName());
        attribute = attribute.fulfill(1);

        // <--[tag]
        // @attribute <p@player.jobs[<job>]>
        // @returns dJob
        // @description
        // Returns the job specified with the player's information attached.
        // @plugin Jobs
        // -->
        if (attribute.startsWith("jobs")) {
            Job job = null;
            if (attribute.hasContext(1)) {
                job = Jobs.getJob(attribute.getContext(1));
            }
            if (job == null) {
                dB.echoError("Invalid or missing job specified!");
                return null;
            }
            return new JobsJob(job, player).getAttribute(attribute.fulfill(1));
        }

        return null;

    }

    @Override
    public String additionalTags(Attribute attribute) {

        if (attribute.startsWith("jobs")) {

            // JobsTags require a... dJob!
            JobsJob j = null;

            // Job tag may specify a new job in the <jobs[context]...> portion of the tag.
            if (attribute.hasContext(1))
                // Check if this is a valid player and update the dPlayer object reference.
                if (JobsJob.matches(attribute.getContext(1))) {
                    j = JobsJob.valueOf(attribute.getContext(1));
                }
                else {
                    dB.echoError("Could not match '" + attribute.getContext(1) + "' to a valid job!");
                    return null;
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
