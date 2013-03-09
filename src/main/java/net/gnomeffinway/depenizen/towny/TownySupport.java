package net.gnomeffinway.depenizen.towny;

import net.gnomeffinway.depenizen.Depenizen;

public class TownySupport {

    public Depenizen depenizen;
    
    public TownySupport(Depenizen depenizen) {
        this.depenizen = depenizen;
    }

    public void register() {
        new TownyTags(depenizen);
    }
    
}
