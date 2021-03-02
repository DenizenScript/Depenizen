package com.denizenscript.depenizen.bukkit.properties.worldedit;

import com.denizenscript.denizen.objects.*;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.depenizen.bukkit.bridges.WorldEditBridge;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.command.tool.BrushTool;
import com.sk89q.worldedit.command.tool.Tool;
import com.sk89q.worldedit.command.tool.brush.Brush;
import com.sk89q.worldedit.function.pattern.BlockPattern;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.regions.EllipsoidRegion;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import com.sk89q.worldedit.regions.RegionSelector;
import com.sk89q.worldedit.regions.selector.EllipsoidRegionSelector;
import com.sk89q.worldedit.regions.selector.Polygonal2DRegionSelector;
import com.sk89q.worldedit.world.item.ItemType;
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

    Player player;

    public static Material deLegacy(Material mat) {
        if (mat.isLegacy()) {
            return Bukkit.getUnsafe().fromLegacy(mat);
        }
        return mat;
    }

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <PlayerTag.we_brush_info[(<item>)]>
        // @returns ListTag
        // @plugin Depenizen, WorldEdit
        // @description
        // Returns information about a player's current brush for an item.
        // If no item is specified, will be based on their held item.
        // Output is in format: type|size|range|material
        //
        // Note that some values may be listed as "unknown" or strange values due to WorldEdit having a messy API (no way to automatically stringify brush data).
        // -->
        if (attribute.startsWith("we_brush_info")) {
            WorldEditPlugin worldEdit = (WorldEditPlugin) WorldEditBridge.instance.plugin;
            ItemType itemType;
            if (attribute.hasContext(1)) {
                itemType = BukkitAdapter.asItemType(deLegacy(attribute.contextAsType(1, ItemTag.class).getMaterial().getMaterial()));
            }
            else {
                ItemStack itm = player.getEquipment().getItemInMainHand();
                itemType = BukkitAdapter.asItemType(deLegacy(itm == null ? Material.AIR : itm.getType()));
            }
            Tool tool = worldEdit.getSession(player).getTool(itemType);
            if (!(tool instanceof BrushTool)) {
                return null;
            }
            BrushTool brush = (BrushTool) tool;
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

        // <--[tag]
        // @attribute <PlayerTag.we_selection>
        // @returns ObjectTag
        // @plugin Depenizen, WorldEdit
        // @description
        // Returns the player's current block area selection, as a CuboidTag, EllipsoidTag, or PolygonTag.
        // -->
        if (attribute.startsWith("we_selection") || attribute.startsWith("selected_region")) {
            WorldEditPlugin worldEdit = (WorldEditPlugin) WorldEditBridge.instance.plugin;
            RegionSelector selection = worldEdit.getSession(player).getRegionSelector(BukkitAdapter.adapt(player.getWorld()));
            try {
                if (selection != null && selection.isDefined()) {
                    if (selection instanceof EllipsoidRegionSelector) {
                        EllipsoidRegion region = ((EllipsoidRegionSelector) selection).getRegion();
                        return new EllipsoidTag(new LocationTag(BukkitAdapter.adapt(BukkitAdapter.adapt(region.getWorld()), region.getCenter())), new LocationTag(BukkitAdapter.adapt(BukkitAdapter.adapt(region.getWorld()), region.getRadius())))
                                .getAttribute(attribute.fulfill(1));
                    }
                    else if (selection instanceof Polygonal2DRegionSelector) {
                        Polygonal2DRegion region = ((Polygonal2DRegionSelector) selection).getRegion();
                        PolygonTag poly = new PolygonTag(new WorldTag(region.getWorld().getName()));
                        for (BlockVector2 vec2 : region.getPoints()) {
                            poly.corners.add(new PolygonTag.Corner(vec2.getX(), vec2.getZ()));
                        }
                        poly.yMin = region.getMinimumY();
                        poly.yMax = region.getMaximumY();
                        poly.recalculateBox();
                        return poly.getAttribute(attribute.fulfill(1));
                    }
                    return new CuboidTag(BukkitAdapter.adapt(player.getWorld(), selection.getIncompleteRegion().getMinimumPoint()),
                            BukkitAdapter.adapt(player.getWorld(), selection.getIncompleteRegion().getMaximumPoint()))
                            .getAttribute(attribute.fulfill(1));
                }
            }
            catch (Throwable ex) {
                Debug.echoError(ex);
            }
        }

        return null;

    }
}
