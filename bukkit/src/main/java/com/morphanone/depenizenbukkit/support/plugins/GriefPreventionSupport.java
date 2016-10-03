package com.morphanone.depenizenbukkit.support.plugins;

import com.morphanone.depenizenbukkit.extensions.griefprevention.GriefPreventionPlayerExtension;
import com.morphanone.depenizenbukkit.objects.griefprevention.GriefPreventionClaim;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.objects.dPlayer;
import com.morphanone.depenizenbukkit.events.griefprevention.GPClaimEnterEvent;
import com.morphanone.depenizenbukkit.extensions.griefprevention.GriefPreventionLocationExtension;
import com.morphanone.depenizenbukkit.support.Support;

public class GriefPreventionSupport extends Support {

    public GriefPreventionSupport() {
        registerObjects(GriefPreventionClaim.class);
        registerProperty(GriefPreventionPlayerExtension.class, dPlayer.class);
        registerProperty(GriefPreventionLocationExtension.class, dLocation.class);
        registerScriptEvents(new GPClaimEnterEvent());
    }
}
