package net.gnomeffinway.depenizen.support;

import net.gnomeffinway.depenizen.Depenizen;
import net.gnomeffinway.depenizen.events.VotifierEvents;

public class VotifierSupport {

public Depenizen depenizen;

    public VotifierSupport(Depenizen depenizen) {
        this.depenizen = depenizen;
    }

    public void register() {
        new VotifierEvents(depenizen);
    }

}
