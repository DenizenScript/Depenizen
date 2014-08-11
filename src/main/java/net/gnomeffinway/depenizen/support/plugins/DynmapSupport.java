package net.gnomeffinway.depenizen.support.plugins;

import net.gnomeffinway.depenizen.events.DynmapEvents;
import net.gnomeffinway.depenizen.support.Support;

public class DynmapSupport extends Support {

    public DynmapSupport() {
        registerEvents(DynmapEvents.class);
    }

}
