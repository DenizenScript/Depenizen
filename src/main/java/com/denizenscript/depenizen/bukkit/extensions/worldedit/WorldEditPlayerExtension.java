package com.denizenscript.depenizen.bukkit.extensions.worldedit;

import com.denizenscript.depenizen.bukkit.extensions.dObjectExtension;
import com.denizenscript.depenizen.bukkit.support.Support;
import com.denizenscript.depenizen.bukkit.bridges.WorldEditSupport;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.command.tool.BrushTool;
import com.sk89q.worldedit.command.tool.InvalidToolBindException;
import com.sk89q.worldedit.command.tool.brush.Brush;
import com.sk89q.worldedit.function.pattern.BlockPattern;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.regions.RegionSelector;
import com.sk89q.worldedit.world.item.ItemType;
import net.aufdemrand.denizen.nms.NMSHandler;
import net.aufdemrand.denizen.nms.NMSVersion;
import net.aufdemrand.denizen.objects.dCuboid;
import net.aufdemrand.denizen.objects.dItem;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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

    public static final String[] handledTags = new String[] {
            "we_brush_info", "selected_region"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    private WorldEditPlayerExtension(dPlayer player) {
        this.player = player.getPlayerEntity();
    }

    Player player = null;

    public static Material deLegacy(Material mat) {
        if (NMSHandler.getVersion().isAtLeast(NMSVersion.v1_13_R2) && mat.isLegacy()) {
            return Bukkit.getUnsafe().fromLegacy(mat);
        }
        return mat;
    }

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <p@player.we_brush_info[(<item>)]>
        // @returns dList
        // @description
        // Returns information about a player's current brush for an item.
        // If no item is specified, will be based on their held item.
        // Output is in format: type|size|range|material
        //
        // Note that some values may be listed as "unknown" or strange values due to WorldEdit having a messy API (no way to automatically stringify brush data).
        // @Plugin DepenizenBukkit, WorldEdit
        // -->
        if (attribute.startsWith("we_brush_info")) {
            WorldEditPlugin worldEdit = Support.getPlugin(WorldEditSupport.class);
            ItemType itemType;
            if (attribute.hasContext(1)) {
                itemType = BukkitAdapter.asItemType(deLegacy(dItem.valueOf(attribute.getContext(1)).getMaterial().getMaterial()));
            }
            else {
                ItemStack itm = player.getEquipment().getItemInMainHand();
                itemType = BukkitAdapter.asItemType(deLegacy(itm == null ? Material.AIR : itm.getType()));
            }
            try {
                BrushTool brush = worldEdit.getSession(player).getBrushTool(itemType);
                Brush btype = brush.getBrush();
                String brushType = CoreUtilities.toLowerCase(btype.getClass().getSimpleName());
                String materialInfo = "unknown";
                Pattern materialPattern = brush.getMaterial();
                if (materialPattern instanceof BlockPattern) {
                    materialInfo = ((BlockPattern) materialPattern).getBlock().getAsString();
                }
                // TODO: other patterns?
                // TODO: mask?
                dList info = new dList();
                info.add(brushType);
                info.add(String.valueOf(brush.getSize()));
                info.add(String.valueOf(brush.getRange()));
                info.add(materialInfo);
                return info.getAttribute(attribute.fulfill(1));
            }
            catch (InvalidToolBindException ex) {
                if (!attribute.hasAlternative()) {
                    dB.echoError("Player " + player.getName() + " does not have a WE brush for " + itemType.getName());
                }
            }
        }

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
