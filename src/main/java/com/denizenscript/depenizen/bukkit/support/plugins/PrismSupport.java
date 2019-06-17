package com.denizenscript.depenizen.bukkit.support.plugins;

import com.denizenscript.depenizen.bukkit.objects.prism.PrismAction;
import com.denizenscript.depenizen.bukkit.objects.prism.properties.PrismActionAggregate;
import com.denizenscript.depenizen.bukkit.objects.prism.properties.PrismActionBlock;
import com.denizenscript.depenizen.bukkit.objects.prism.properties.PrismActionLocation;
import com.denizenscript.depenizen.bukkit.objects.prism.properties.PrismActionPlayer;
import com.denizenscript.depenizen.bukkit.support.Support;
import com.denizenscript.depenizen.bukkit.extensions.prism.PrismLocationExtension;
import net.aufdemrand.denizen.objects.dLocation;

public class PrismSupport extends Support {

    public PrismSupport() {
        registerObjects(PrismAction.class);

        registerProperty(PrismLocationExtension.class, dLocation.class);

        registerProperty(PrismActionBlock.class, PrismAction.class);
        registerProperty(PrismActionPlayer.class, PrismAction.class);
        registerProperty(PrismActionAggregate.class, PrismAction.class);
        // Keep this last (so applyProperty works correctly)
        registerProperty(PrismActionLocation.class, PrismAction.class);
    }

}
