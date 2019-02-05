package com.denizenscript.depenizen.bukkit.extensions.plotme;

import com.denizenscript.depenizen.bukkit.extensions.dObjectExtension;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.bukkit.api.BukkitWorld;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.objects.dWorld;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.Mechanism;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;

public class PlotMePlayerExtension extends dObjectExtension {

    public static boolean describes(dObject object) {
        return object instanceof dPlayer;
    }

    public static PlotMePlayerExtension getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new PlotMePlayerExtension((dPlayer) object);
        }
    }


    ///////////////////
    // Instance Fields and Methods
    /////////////

    public static final String[] handledTags = new String[] {
            "plot_count"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    private PlotMePlayerExtension(dPlayer player) {
        this.player = player;
    }

    dPlayer player;

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <p@player.plot_count[<world>]>
        // @returns Element(Number)
        // @description
        // Returns the number of plots a player has in a world.
        // @Plugin DepenizenBukkit, PlotMe
        // -->
        if (attribute.startsWith("plot_count")
                && attribute.hasContext(1)) {
            dWorld world = dWorld.valueOf(attribute.getContext(1));
            if (world == null) {
                return null;
            }
            BukkitWorld tworld = new BukkitWorld(world.getWorld());
            return new Element(PlotMeCoreManager.getInstance().getOwnedPlotCount(player.getOfflinePlayer().getUniqueId(),
                    tworld)).getAttribute(attribute.fulfill(1));
        }

        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {
        Element value = mechanism.getValue();
    }
}
