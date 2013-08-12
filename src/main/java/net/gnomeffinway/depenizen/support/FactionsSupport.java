package net.gnomeffinway.depenizen.support;

import net.gnomeffinway.depenizen.Depenizen;
import net.gnomeffinway.depenizen.tags.FactionsTags;

public class FactionsSupport {
    
    public Depenizen depenizen;
    
    public FactionsSupport(Depenizen depenizen) {
        this.depenizen = depenizen;
    }

    public void register() {
        new FactionsTags(depenizen);
    }
}
