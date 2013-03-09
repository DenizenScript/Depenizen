package net.gnomeffinway.depenizen.factions;

import net.gnomeffinway.depenizen.Depenizen;

public class FactionsSupport {
    
    public Depenizen depenizen;
    
    public FactionsSupport(Depenizen depenizen) {
        this.depenizen = depenizen;
    }

    public void register() {
        new FactionsTags(depenizen);
    }
}
