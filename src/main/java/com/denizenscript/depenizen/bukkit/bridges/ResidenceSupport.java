package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.events.residence.PlayerExitsResidenceScriptEvent;
import com.denizenscript.depenizen.bukkit.extensions.residence.ResidenceLocationExtension;
import com.denizenscript.depenizen.bukkit.extensions.residence.ResidencePlayerExtension;
import com.denizenscript.depenizen.bukkit.support.Support;
import com.denizenscript.depenizen.bukkit.events.residence.PlayerEntersResidenceScriptEvent;
import com.denizenscript.depenizen.bukkit.objects.residence.dResidence;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.objects.dPlayer;

public class ResidenceSupport extends Support {

    public ResidenceSupport() {
        registerObjects(dResidence.class);
        registerProperty(ResidencePlayerExtension.class, dPlayer.class);
        registerProperty(ResidenceLocationExtension.class, dLocation.class);
        registerScriptEvents(new PlayerEntersResidenceScriptEvent());
        registerScriptEvents(new PlayerExitsResidenceScriptEvent());
    }
}
