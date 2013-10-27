package net.gnomeffinway.depenizen.tags;

import me.zford.jobs.Jobs;
import me.zford.jobs.container.Job;
import me.zford.jobs.container.JobsPlayer;
import net.aufdemrand.denizen.events.ReplaceableTagEvent;
import net.aufdemrand.denizen.objects.Element;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.tags.Attribute;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.gnomeffinway.depenizen.Depenizen;
import net.gnomeffinway.depenizen.objects.dJob;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class JobsTags implements Listener {
    
    public JobsTags(Depenizen depenizen) {
        depenizen.getServer().getPluginManager().registerEvents(this, depenizen);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void jobsTags(ReplaceableTagEvent event) {

        // Build a new attribute out of the raw_tag supplied in the script to be fulfilled
        Attribute attribute = new Attribute(event.raw_tag, event.getScriptEntry());
                
        /////////////////////
        //   PLAYER TAGS
        /////////////////
        
        if (event.matches("player, pl")) {
                    
            // PlayerTags require a... dPlayer!
            dPlayer p = event.getPlayer();

            // Player tag may specify a new player in the <player[context]...> portion of the tag.
            if (attribute.hasContext(1))
                // Check if this is a valid player and update the dPlayer object reference.
                if (dPlayer.matches(attribute.getContext(1))) {
                    p = dPlayer.valueOf(attribute.getContext(1));
                }
                else {
                    dB.echoDebug("Could not match '"
                            + attribute.getContext(1) + "' to a valid player!");
                    return;
                }

            if (p == null || !p.isValid()) {
                dB.echoDebug("Invalid or missing player for tag <" + event.raw_tag + ">!");
                event.setReplaced("null");
                return;
            }
            
            JobsPlayer player = new JobsPlayer(p.getName());
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
                    dB.echoDebug("Invalid or missing job specified in tag <" + event.raw_tag + ">!");
                    return;
                }
                event.setReplaced(new dJob(job, player)
                        .getAttribute(attribute.fulfill(1)));
            }
            
        }
        
        /////////////////////
        //   JOB TAGS
        /////////////////
        
        else if (event.matches("jobs")) {
            
            // JobsTags require a... dJob!
            dJob j = null;

            // Job tag may specify a new job in the <jobs[context]...> portion of the tag.
            if (attribute.hasContext(1))
                // Check if this is a valid player and update the dPlayer object reference.
                if (dJob.matches(attribute.getContext(1))) {
                    j = dJob.valueOf(attribute.getContext(1));
                }
                else {
                    dB.echoDebug("Could not match '"
                            + attribute.getContext(1) + "' to a valid job!");
                    return;
                }

            if (j == null) {
                dB.echoDebug("Invalid or missing job for tag <" + event.raw_tag + ">!");
                event.setReplaced(new Element("null").getAttribute(attribute.fulfill(1)));
                return;
            }
            
            event.setReplaced(j.getAttribute(attribute.fulfill(1)));
            
        }
        
    }
    
}
