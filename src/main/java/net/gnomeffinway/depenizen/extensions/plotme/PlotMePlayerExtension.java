package net.gnomeffinway.depenizen.extensions.plotme;

import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.bukkit.api.BukkitWorld;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.objects.dWorld;
import net.aufdemrand.denizencore.objects.*;
import net.aufdemrand.denizencore.tags.Attribute;
import net.gnomeffinway.depenizen.extensions.dObjectExtension;

public class PlotMePlayerExtension extends dObjectExtension {

    public static boolean describes(dObject pl) {
        return pl instanceof dPlayer;
    }

    public static PlotMePlayerExtension getFrom(dObject pl) {
        if (!describes(pl)) return null;
        else return new PlotMePlayerExtension((dPlayer) pl);
    }


    ///////////////////
    // Instance Fields and Methods
    /////////////

    private PlotMePlayerExtension(dPlayer pl) {
        player = pl;
    }

    dPlayer player;

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <p@player.plot_count[<world>]>
        // @returns Element(Number)
        // @description
        // Returns the number of plots a player has in a world.
        // @plugin Depenizen, PlotMe
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
