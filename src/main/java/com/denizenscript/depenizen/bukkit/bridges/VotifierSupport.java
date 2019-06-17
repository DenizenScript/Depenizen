package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.support.Support;
import com.denizenscript.depenizen.bukkit.events.VotifierEvents;

public class VotifierSupport extends Support {

    public VotifierSupport() {
        registerEvents(VotifierEvents.class);
    }
}
