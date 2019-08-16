package com.denizenscript.depenizen.bukkit.properties.worldedit;

import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.depenizen.bukkit.bridges.WorldEditBridge;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.command.tool.BrushTool;
import com.sk89q.worldedit.command.tool.InvalidToolBindException;
import com.sk89q.worldedit.command.tool.brush.Brush;
import com.sk89q.worldedit.function.pattern.BlockPattern;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.regions.RegionSelector;
import com.sk89q.worldedit.world.item.ItemType;
import com.denizenscript.denizen.nms.NMSHandler;
import com.denizenscript.denizen.nms.NMSVersion;
import com.denizenscript.denizen.objects.CuboidTag;
import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.debugging.Debug;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class WorldEditPlayerProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "WorldEditPlayer";
    }

    @Override
    public void adjust(Mechanism mechanism) {
        // None
    }

    public static boolean describes(ObjectTag object) {
        return object instanceof PlayerTag
                && ((PlayerTag) object).isOnline();
    }

    public static WorldEditPlayerProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new WorldEditPlayerProperties((PlayerTag) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "we_brush_info", "selected_region", "we_selection"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    private WorldEditPlayerProperties(PlayerTag player) {
        this.player = player.getPlayerEntity();
    }

    Player player = null;

    public static Material deLegacy(Material mat) {
        if (NMSHandler.getVersion().isAtLeast(NMSVersion.v1_13) && mat.isLegacy()) {
            return Bukkit.getUnsafe().fromLegacy(mat);
        }
        return mat;
    }

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <PlayerTag.we_brush_info[(<item>)]>
        // @returns ListTag
        // @description
        // Returns information about a player's current brush for an item.
        // If no item is specified, will be based on their held item.
        // Output is in format: type|size|range|material
        //
        // Note that some values may be listed as "unknown" or strange values due to WorldEdit having a messy API (no way to automatically stringify brush data).
        // @Plugin Depenizen, WorldEdit
        // -->
        if (attribute.startsWith("we_brush_info")) {
            WorldEditPlugin worldEdit = (WorldEditPlugin) WorldEditBridge.instance.plugin;
            ItemType itemType;
            if (attribute.hasContext(1)) {
                itemType = BukkitAdapter.asItemType(deLegacy(ItemTag.valueOf(attribute.getContext(1)).getMaterial().getMaterial()));
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
                ListTag info = new ListTag();
                info.add(brushType);
                info.add(String.valueOf(brush.getSize()));
                info.add(String.valueOf(brush.getRange()));
                info.add(materialInfo);
                return info.getAttribute(attribute.fulfill(1));
            }
            catch (InvalidToolBindException ex) {
                if (!attribute.hasAlternative()) {
                    Debug.echoError("Player " + player.getName() + " does not have a WE brush for " + itemType.getName());
                }
            }
        }

        // <--[tag]
        // @attribute <PlayerTag.we_selection>
        // @returns CuboidTag
        // @description
        // Returns the player's current block area selection, as a CuboidTag.
        // @Plugin Depenizen, WorldEdit
        // -->
        if (attribute.startsWith("we_selection") || attribute.startsWith("selected_region")) {
            WorldEditPlugin worldEdit = (WorldEditPlugin) WorldEditBridge.instance.plugin;
            RegionSelector selection = worldEdit.getSession(player).getRegionSelector(BukkitAdapter.adapt(player.getWorld()));
            if (selection != null) {
                return new CuboidTag(BukkitAdapter.adapt(player.getWorld(), selection.getIncompleteRegion().getMinimumPoint()),
                        BukkitAdapter.adapt(player.getWorld(), selection.getIncompleteRegion().getMaximumPoint()))
                        .getAttribute(attribute.fulfill(1));
            }
        }

        return null;

    }
}
