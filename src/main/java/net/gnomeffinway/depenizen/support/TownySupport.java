package net.gnomeffinway.depenizen.support;

import org.bukkit.Bukkit;

import net.aufdemrand.denizen.tags.ObjectFetcher;
import net.gnomeffinway.depenizen.Depenizen;
import net.gnomeffinway.depenizen.objects.dNation;
import net.gnomeffinway.depenizen.objects.dTown;
import net.gnomeffinway.depenizen.tags.TownyTags;

public class TownySupport {

    public Depenizen depenizen;
    
    public TownySupport(Depenizen depenizen) {
        this.depenizen = depenizen;
    }

    public void register() {
        new TownyTags(depenizen);
        
        ObjectFetcher.registerWithObjectFetcher(dTown.class);
        ObjectFetcher.registerWithObjectFetcher(dNation.class);
        
        try {
            ObjectFetcher._initialize();
        } 
        catch (Exception e) {
            Bukkit.getLogger().severe("[Depenizen] Error loading Denizen ObjectFetcher. Towny tags may not function correctly.");
        }
    }
    
}
