package net.gnomeffinway.depenizen.support;

import org.bukkit.Bukkit;

import net.aufdemrand.denizen.tags.ObjectFetcher;
import net.gnomeffinway.depenizen.Depenizen;
import net.gnomeffinway.depenizen.objects.dFaction;
import net.gnomeffinway.depenizen.tags.FactionsTags;

public class FactionsSupport {
    
    public Depenizen depenizen;
    
    public FactionsSupport(Depenizen depenizen) {
        this.depenizen = depenizen;
    }

    public void register() {
        new FactionsTags(depenizen);
        
        ObjectFetcher.registerWithObjectFetcher(dFaction.class);
        
        try {
            ObjectFetcher._initialize();
        } 
        catch (Exception e) {
            Bukkit.getLogger().severe("[Depenizen] Error loading Denizen ObjectFetcher. Factions tags may not function correctly.");
        }
    }
}
