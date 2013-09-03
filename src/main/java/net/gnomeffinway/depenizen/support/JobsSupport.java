package net.gnomeffinway.depenizen.support;

import org.bukkit.Bukkit;

import net.aufdemrand.denizen.tags.ObjectFetcher;
import net.gnomeffinway.depenizen.Depenizen;
import net.gnomeffinway.depenizen.commands.JobsCommands;
import net.gnomeffinway.depenizen.objects.dJob;
import net.gnomeffinway.depenizen.tags.JobsTags;

public class JobsSupport {
    
    public Depenizen depenizen;
    
    public JobsSupport(Depenizen depenizen) {
        this.depenizen = depenizen;
    }
    
    public void register() {
        new JobsTags(depenizen);
        new JobsCommands().activate().as("jobs").withOptions("see documentation", 2);
        
        ObjectFetcher.registerWithObjectFetcher(dJob.class);
        
        try {
            ObjectFetcher._initialize();
        } 
        catch (Exception e) {
            Bukkit.getLogger().severe("[Depenizen] Error loading Denizen ObjectFetcher. Jobs tags may not function correctly.");
        }
    }

}
