package com.morphanone.depenizenbukkit.support.plugins;

import com.morphanone.depenizenbukkit.extensions.prism.PrismLocationExtension;
import com.morphanone.depenizenbukkit.objects.prism.properties.PrismActionBlock;
import com.morphanone.depenizenbukkit.objects.prism.properties.PrismActionLocation;
import net.aufdemrand.denizen.objects.dLocation;
import com.morphanone.depenizenbukkit.objects.prism.PrismAction;
import com.morphanone.depenizenbukkit.objects.prism.properties.PrismActionAggregate;
import com.morphanone.depenizenbukkit.objects.prism.properties.PrismActionPlayer;
import com.morphanone.depenizenbukkit.support.Support;

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
