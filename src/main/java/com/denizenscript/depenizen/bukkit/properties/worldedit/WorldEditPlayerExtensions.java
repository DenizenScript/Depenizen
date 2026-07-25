package com.denizenscript.depenizen.bukkit.properties.worldedit;

import com.denizenscript.denizen.objects.*;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.denizenscript.denizencore.utilities.debugging.DebugInternals;
import com.denizenscript.depenizen.bukkit.bridges.WorldEditBridge;
import com.sk89q.worldedit.LocalSession;
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
import com.sk89q.worldedit.regions.selector.CuboidRegionSelector;
import com.sk89q.worldedit.regions.selector.EllipsoidRegionSelector;
import com.sk89q.worldedit.regions.selector.Polygonal2DRegionSelector;
import com.sk89q.worldedit.world.item.ItemType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.stream.Collectors;

public class WorldEditPlayerExtensions {

    public static Material deLegacy(Material mat) {
        if (mat.isLegacy()) {
            return Bukkit.getUnsafe().fromLegacy(mat);
        }
        return mat;
    }

    public static void register() {

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
        PlayerTag.tagProcessor.registerTag(ListTag.class, "we_brush_info", (attribute, player) -> {
            WorldEditPlugin worldEdit = (WorldEditPlugin) WorldEditBridge.instance.plugin;
            ItemType itemType;
            if (attribute.hasParam()) {
                itemType = BukkitAdapter.asItemType(deLegacy(attribute.paramAsType(ItemTag.class).getBukkitMaterial()));
            }
            else {
                ItemStack itm = player.getPlayerEntity().getEquipment().getItemInMainHand();
                itemType = BukkitAdapter.asItemType(deLegacy(itm == null ? Material.AIR : itm.getType()));
            }
            Tool tool = worldEdit.getSession(player.getPlayerEntity()).getTool(itemType);
            if (!(tool instanceof BrushTool brush)) {
                return null;
            }
            Brush btype = brush.getBrush();
            String brushType = CoreUtilities.toLowerCase(DebugInternals.getClassNameOpti(btype.getClass()));
            String materialInfo = "unknown";
            Pattern materialPattern = brush.getMaterial();
            if (materialPattern instanceof BlockPattern blockPattern) {
                materialInfo = blockPattern.getBlock().getAsString();
            }
            // TODO: other patterns?
            // TODO: mask?
            ListTag info = new ListTag();
            info.add(brushType);
            info.add(String.valueOf(brush.getSize()));
            info.add(String.valueOf(brush.getRange()));
            info.add(materialInfo);
            return info;
        });

        // <--[tag]
        // @attribute <PlayerTag.we_selection>
        // @returns AreaObject
        // @mechanism PlayerTag.we_selection
        // @plugin Depenizen, WorldEdit
        // @description
        // Returns the player's current block area selection, as a CuboidTag, EllipsoidTag, or PolygonTag.
        // -->
        PlayerTag.tagProcessor.registerTag(AreaContainmentObject.class, "we_selection", (attribute, player) -> {
            WorldEditPlugin worldEdit = (WorldEditPlugin) WorldEditBridge.instance.plugin;
            RegionSelector selection = worldEdit.getSession(player.getPlayerEntity()).getRegionSelector(BukkitAdapter.adapt(player.getWorld()));
            try {
                if (selection != null && selection.isDefined()) {
                    if (selection instanceof EllipsoidRegionSelector ellipsoid) {
                        EllipsoidRegion region = ellipsoid.getRegion();
                        return new EllipsoidTag(new LocationTag(BukkitAdapter.adapt(BukkitAdapter.adapt(region.getWorld()), region.getCenter())), new LocationTag(BukkitAdapter.adapt(BukkitAdapter.adapt(region.getWorld()), region.getRadius())));
                    }
                    else if (selection instanceof Polygonal2DRegionSelector polygonal) {
                        Polygonal2DRegion region = polygonal.getRegion();
                        PolygonTag poly = new PolygonTag(new WorldTag(region.getWorld().getName()));
                        for (BlockVector2 vec2 : region.getPoints()) {
                            poly.corners.add(new PolygonTag.Corner(vec2.getX(), vec2.getZ()));
                        }
                        poly.yMin = region.getMinimumY();
                        poly.yMax = region.getMaximumY();
                        poly.recalculateBox();
                        return poly;
                    }
                    return new CuboidTag(BukkitAdapter.adapt(player.getWorld(), selection.getIncompleteRegion().getMinimumPoint()),
                            BukkitAdapter.adapt(player.getWorld(), selection.getIncompleteRegion().getMaximumPoint()));
                }
            }
            catch (Throwable ex) {
                attribute.echoError(ex);
            }
            return null;
        }, "selected_region");

        // <--[mechanism]
        // @object PlayerTag
        // @name we_selection
        // @plugin Depenizen, WorldEdit
        // @input AreaObject
        // @description
        // Sets the player's current block area selection, as a CuboidTag, EllipsoidTag, or PolygonTag.
        // @tags
        // <PlayerTag.we_selection>
        // -->
        PlayerTag.tagProcessor.registerMechanism("we_selection", false, AreaContainmentObject.class, (player, mechanism, input) -> {
            WorldEditPlugin worldEdit = (WorldEditPlugin) WorldEditBridge.instance.plugin;
            RegionSelector selector;
            if (input.canBeType(CuboidTag.class)) {
                CuboidTag area = input.asType(CuboidTag.class, mechanism.context);
                selector = new CuboidRegionSelector(BukkitAdapter.adapt(area.getWorld().getWorld()), BukkitAdapter.asBlockVector(area.getLow(0)), BukkitAdapter.asBlockVector(area.getHigh(0)));
            }
            else if (input.canBeType(EllipsoidTag.class)) {
                EllipsoidTag area = input.asType(EllipsoidTag.class, mechanism.context);
                selector = new EllipsoidRegionSelector(BukkitAdapter.adapt(area.center.getWorld()), BukkitAdapter.asBlockVector(area.center), BukkitAdapter.asVector(area.size));
            }
            else if (input.canBeType(PolygonTag.class)) {
                PolygonTag area = input.asType(PolygonTag.class, mechanism.context);
                selector = new Polygonal2DRegionSelector(BukkitAdapter.adapt(area.world.getWorld()), area.corners.stream().map(c -> BlockVector2.at(c.x, c.z)).collect(Collectors.toList()), (int) area.yMin, (int) area.yMax);
            }
            else {
                mechanism.echoError("Invalid we_selection input");
                return;
            }
            LocalSession session = worldEdit.getSession(player.getPlayerEntity());
            session.setRegionSelector(BukkitAdapter.adapt(player.getWorld()), selector);
            selector.explainRegionAdjust(BukkitAdapter.adapt(player.getPlayerEntity()), session);
        });
    }
}
