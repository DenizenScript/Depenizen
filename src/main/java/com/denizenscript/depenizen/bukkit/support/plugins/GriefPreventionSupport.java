package com.denizenscript.depenizen.bukkit.support.plugins;

import com.denizenscript.depenizen.bukkit.support.Support;
import com.denizenscript.depenizen.bukkit.extensions.griefprevention.GriefPreventionPlayerExtension;
import com.denizenscript.depenizen.bukkit.objects.griefprevention.GriefPreventionClaim;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.objects.dPlayer;
import com.denizenscript.depenizen.bukkit.events.griefprevention.GPClaimEnterEvent;
import com.denizenscript.depenizen.bukkit.extensions.griefprevention.GriefPreventionLocationExtension;

public class GriefPreventionSupport extends Support {

    public GriefPreventionSupport() {
        registerObjects(GriefPreventionClaim.class);
        registerProperty(GriefPreventionPlayerExtension.class, dPlayer.class);
        registerProperty(GriefPreventionLocationExtension.class, dLocation.class);
        registerScriptEvents(new GPClaimEnterEvent());
    }
}
