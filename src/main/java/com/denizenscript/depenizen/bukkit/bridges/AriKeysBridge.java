package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.events.arikeys.AriKeysKeyPressScriptEvent;
import com.denizenscript.depenizen.bukkit.events.arikeys.AriKeysKeyReleaseScriptEvent;


public class AriKeysBridge extends Bridge {

    @Override
    public void init() {
        ScriptEvent.registerScriptEvent(AriKeysKeyReleaseScriptEvent.class);
        ScriptEvent.registerScriptEvent(AriKeysKeyPressScriptEvent.class);
    }
}
