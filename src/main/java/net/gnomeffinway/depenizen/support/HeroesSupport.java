package net.gnomeffinway.depenizen.support;

import net.gnomeffinway.depenizen.Depenizen;
import net.gnomeffinway.depenizen.events.HeroesEvents;

public class HeroesSupport {
    
    public Depenizen depenizen;
    
    public HeroesSupport(Depenizen depenizen) {
        this.depenizen = depenizen;
    }
    
    public void register() {
        new HeroesEvents(depenizen);
    }

}
