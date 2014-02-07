package net.gnomeffinway.depenizen.support;

import net.gnomeffinway.depenizen.Depenizen;
import net.gnomeffinway.depenizen.events.DynmapEvents;

public class DynmapSupport {

    public Depenizen depenizen;

    public DynmapSupport(Depenizen depenizen) {
        this.depenizen = depenizen;
    }

    public void register() {
        new DynmapEvents(depenizen);
    }

}
