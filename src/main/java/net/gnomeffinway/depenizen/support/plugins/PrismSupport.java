package net.gnomeffinway.depenizen.support.plugins;

import net.aufdemrand.denizen.objects.dLocation;
import net.gnomeffinway.depenizen.extensions.prism.PrismLocationExtension;
import net.gnomeffinway.depenizen.objects.prism.PrismAction;
import net.gnomeffinway.depenizen.objects.prism.properties.PrismActionAggregate;
import net.gnomeffinway.depenizen.objects.prism.properties.PrismActionBlock;
import net.gnomeffinway.depenizen.objects.prism.properties.PrismActionLocation;
import net.gnomeffinway.depenizen.objects.prism.properties.PrismActionPlayer;
import net.gnomeffinway.depenizen.support.Support;

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
