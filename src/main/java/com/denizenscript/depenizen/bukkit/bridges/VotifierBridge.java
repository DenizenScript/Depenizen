package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.events.votifier.VotifierVoteScriptEvent;
import net.aufdemrand.denizencore.events.ScriptEvent;

public class VotifierBridge extends Bridge {

    @Override
    public void init() {
        ScriptEvent.registerScriptEvent(new VotifierVoteScriptEvent());
    }
}
