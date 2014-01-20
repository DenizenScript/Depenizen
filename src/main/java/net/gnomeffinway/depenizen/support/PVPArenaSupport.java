package net.gnomeffinway.depenizen.support;

import net.gnomeffinway.depenizen.Depenizen;
import net.gnomeffinway.depenizen.events.PVPArenaEvents;
import net.gnomeffinway.depenizen.tags.PVPArenaTags;

public class PVPArenaSupport {

    public Depenizen depenizen;

    public PVPArenaSupport(Depenizen depenizen) {
        this.depenizen = depenizen;
    }

    public void register() {
        new PVPArenaTags(depenizen);
        new PVPArenaEvents(depenizen);
    }

}
