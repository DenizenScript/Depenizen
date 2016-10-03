package com.morphanone.depenizenbukkit.support.plugins;

import com.morphanone.depenizenbukkit.events.residence.PlayerEntersResidenceScriptEvent;
import com.morphanone.depenizenbukkit.events.residence.PlayerExitsResidenceScriptEvent;
import com.morphanone.depenizenbukkit.extensions.residence.ResidencePlayerExtension;
import com.morphanone.depenizenbukkit.objects.residence.dResidence;
import net.aufdemrand.denizen.objects.dPlayer;
import com.morphanone.depenizenbukkit.support.Support;

public class ResidenceSupport extends Support {

    public ResidenceSupport() {
        registerObjects(dResidence.class);
        registerProperty(ResidencePlayerExtension.class, dPlayer.class);
        registerScriptEvents(new PlayerEntersResidenceScriptEvent());
        registerScriptEvents(new PlayerExitsResidenceScriptEvent());
    }
}
