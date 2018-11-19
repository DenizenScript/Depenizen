package com.denizenscript.depenizen.bukkit.extensions.worldedit;

import com.denizenscript.depenizen.bukkit.extensions.dObjectExtension;
import com.denizenscript.depenizen.bukkit.support.Support;
import com.denizenscript.depenizen.bukkit.support.plugins.WorldEditSupport;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.regions.RegionSelector;
import net.aufdemrand.denizen.objects.dCuboid;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;
import org.bukkit.entity.Player;

public class WorldEditPlayerExtension extends dObjectExtension {

    public static boolean describes(dObject object) {
        return object instanceof dPlayer
                && ((dPlayer) object).isOnline();
    }

    public static WorldEditPlayerExtension getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new WorldEditPlayerExtension((dPlayer) object);
        }
    }

    private WorldEditPlayerExtension(dPlayer player) {
        this.player = player.getPlayerEntity();
    }

    Player player = null;

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <p@player.selected_region>
        // @returns dCuboid
        // @description
        // Returns the player's current region selection, as a dCuboid.
        // @Plugin DepenizenBukkit, WorldEdit
        // -->
        if (attribute.startsWith("selected_region")) {
            WorldEditPlugin worldEdit = Support.getPlugin(WorldEditSupport.class);
            RegionSelector selection = worldEdit.getSession(player).getRegionSelector(BukkitAdapter.adapt(player.getWorld()));
            if (selection != null) {
                return new dCuboid(BukkitAdapter.adapt(player.getWorld(), selection.getIncompleteRegion().getMinimumPoint()),
                        BukkitAdapter.adapt(player.getWorld(), selection.getIncompleteRegion().getMaximumPoint()))
                            .getAttribute(attribute.fulfill(1));
            }
        }

        return null;

    }

}
