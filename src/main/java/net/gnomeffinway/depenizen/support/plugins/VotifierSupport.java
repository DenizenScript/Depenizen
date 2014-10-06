package net.gnomeffinway.depenizen.support.plugins;

import net.gnomeffinway.depenizen.events.VotifierEvents;
import net.gnomeffinway.depenizen.support.Support;

public class VotifierSupport extends Support {

    public VotifierSupport() {
        registerEvents(VotifierEvents.class);
    }
}
