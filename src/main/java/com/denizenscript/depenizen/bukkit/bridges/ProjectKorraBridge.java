package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.events.projectkorra.*;

public class ProjectKorraBridge extends Bridge {

    @Override
    public void init() {
        if (!plugin.isEnabled()) {
            Debug.log("ProjectKorra plugin is not enabled or not present. ProjectKorra events will not be available.");
            return;
        }
        ScriptEvent.registerScriptEvent(EntityBendingDeathScriptEvent.class);
        ScriptEvent.registerScriptEvent(PlayerAbilityDamageEntityScriptEvent.class);
        ScriptEvent.registerScriptEvent(PlayerAbilityEndScriptEvent.class);
        ScriptEvent.registerScriptEvent(PlayerAbilityProgressScriptEvent.class);
        ScriptEvent.registerScriptEvent(PlayerAbilityStartScriptEvent.class);
    }
}
