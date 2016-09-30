package net.gnomeffinway.depenizen.support.plugins;

import net.aufdemrand.denizen.objects.dPlayer;
import net.gnomeffinway.depenizen.events.residence.PlayerEntersResidenceScriptEvent;
import net.gnomeffinway.depenizen.events.residence.PlayerExitsResidenceScriptEvent;
import net.gnomeffinway.depenizen.extensions.residence.ResidencePlayerExtension;
import net.gnomeffinway.depenizen.objects.residence.dResidence;
import net.gnomeffinway.depenizen.support.Support;

public class ResidenceSupport extends Support {

    public ResidenceSupport() {
        registerObjects(dResidence.class);
        registerProperty(ResidencePlayerExtension.class, dPlayer.class);
        registerScriptEvents(new PlayerEntersResidenceScriptEvent());
        registerScriptEvents(new PlayerExitsResidenceScriptEvent());
    }
}
