package net.gnomeffinway.depenizen.support;

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
    }
    
}
