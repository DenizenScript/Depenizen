package com.denizenscript.depenizen.bukkit.support.plugins;

import com.denizenscript.depenizen.bukkit.events.sentinel.SentinelAttackScriptEvent;
import com.denizenscript.depenizen.bukkit.support.Support;

public class SentinelSupport extends Support {

    public SentinelSupport() {
        registerScriptEvents(new SentinelAttackScriptEvent());
    }
}
