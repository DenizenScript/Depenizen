package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.events.mythickeys.MythicKeysKeyPressScriptEvent;
import com.denizenscript.depenizen.bukkit.events.mythickeys.MythicKeysKeyReleaseScriptEvent;

public class MythicKeysBridge extends Bridge {

    @Override
    public void init() {
        ScriptEvent.registerScriptEvent(MythicKeysKeyPressScriptEvent.class);
        ScriptEvent.registerScriptEvent(MythicKeysKeyReleaseScriptEvent.class);
    }
}
