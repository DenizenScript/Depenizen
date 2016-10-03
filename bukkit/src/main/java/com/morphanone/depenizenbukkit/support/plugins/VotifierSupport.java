package com.morphanone.depenizenbukkit.support.plugins;

import com.morphanone.depenizenbukkit.events.VotifierEvents;
import com.morphanone.depenizenbukkit.support.Support;

public class VotifierSupport extends Support {

    public VotifierSupport() {
        registerEvents(VotifierEvents.class);
    }
}
