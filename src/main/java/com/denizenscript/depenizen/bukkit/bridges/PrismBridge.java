package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.objects.prism.PrismAction;
import com.denizenscript.depenizen.bukkit.properties.prism.PrismActionAggregate;
import com.denizenscript.depenizen.bukkit.properties.prism.PrismActionBlock;
import com.denizenscript.depenizen.bukkit.properties.prism.PrismActionLocation;
import com.denizenscript.depenizen.bukkit.properties.prism.PrismActionPlayer;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.properties.prism.PrismLocationProperties;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizencore.objects.ObjectFetcher;
import net.aufdemrand.denizencore.objects.properties.PropertyParser;

public class PrismBridge extends Bridge {

    public static PrismBridge instance;

    @Override
    public void init() {
        instance = this;
        ObjectFetcher.registerWithObjectFetcher(PrismAction.class);

        PropertyParser.registerProperty(PrismLocationProperties.class, dLocation.class);

        PropertyParser.registerProperty(PrismActionBlock.class, PrismAction.class);
        PropertyParser.registerProperty(PrismActionPlayer.class, PrismAction.class);
        PropertyParser.registerProperty(PrismActionAggregate.class, PrismAction.class);
        // Keep this last (so applyProperty works correctly)
        PropertyParser.registerProperty(PrismActionLocation.class, PrismAction.class);
    }
}
