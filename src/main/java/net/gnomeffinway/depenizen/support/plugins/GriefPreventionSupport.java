package net.gnomeffinway.depenizen.support.plugins;

import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.objects.dPlayer;
import net.gnomeffinway.depenizen.events.griefprevention.GPClaimEnterEvent;
import net.gnomeffinway.depenizen.extensions.griefprevention.GriefPreventionLocationExtension;
import net.gnomeffinway.depenizen.extensions.griefprevention.GriefPreventionPlayerExtension;
import net.gnomeffinway.depenizen.objects.griefprevention.GriefPreventionClaim;
import net.gnomeffinway.depenizen.support.Support;

public class GriefPreventionSupport extends Support {

    public GriefPreventionSupport() {
        registerObjects(GriefPreventionClaim.class);
        registerProperty(GriefPreventionPlayerExtension.class, dPlayer.class);
        registerProperty(GriefPreventionLocationExtension.class, dLocation.class);
        registerScriptEvents(new GPClaimEnterEvent());
    }
}
