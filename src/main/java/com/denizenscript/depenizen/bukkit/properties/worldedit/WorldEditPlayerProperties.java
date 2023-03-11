package com.denizenscript.depenizen.bukkit.properties.worldedit;

import com.denizenscript.denizen.objects.*;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
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
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.stream.Collectors;

public class WorldEditPlayerProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "WorldEditPlayer";
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

    public static final String[] handledMechs = new String[] {
            "we_selection"
    };

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
        PropertyParser.registerTag(WorldEditPlayerProperties.class, ListTag.class, "we_brush_info", (attribute, object) -> {
            WorldEditPlugin worldEdit = (WorldEditPlugin) WorldEditBridge.instance.plugin;
            ItemType itemType;
            if (attribute.hasParam()) {
                itemType = BukkitAdapter.asItemType(deLegacy(attribute.paramAsType(ItemTag.class).getMaterial().getMaterial()));
            }
            else {
                ItemStack itm = object.player.getEquipment().getItemInMainHand();
                itemType = BukkitAdapter.asItemType(deLegacy(itm == null ? Material.AIR : itm.getType()));
            }
            Tool tool = worldEdit.getSession(object.player).getTool(itemType);
            if (!(tool instanceof BrushTool)) {
                return null;
            }
            BrushTool brush = (BrushTool) tool;
            Brush btype = brush.getBrush();
            String brushType = CoreUtilities.toLowerCase(DebugInternals.getClassNameOpti(btype.getClass()));
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
            return info;
        });

        // <--[tag]
        // @attribute <PlayerTag.we_selection>
        // @returns ObjectTag
        // @mechanism PlayerTag.we_selection
        // @plugin Depenizen, WorldEdit
        // @description
        // Returns the player's current block area selection, as a CuboidTag, EllipsoidTag, or PolygonTag.
        // -->
        PropertyParser.registerTag(WorldEditPlayerProperties.class, ObjectTag.class, "we_selection", (attribute, object) -> {
            WorldEditPlugin worldEdit = (WorldEditPlugin) WorldEditBridge.instance.plugin;
            RegionSelector selection = worldEdit.getSession(object.player).getRegionSelector(BukkitAdapter.adapt(object.player.getWorld()));
            try {
                if (selection != null && selection.isDefined()) {
                    if (selection instanceof EllipsoidRegionSelector) {
                        EllipsoidRegion region = ((EllipsoidRegionSelector) selection).getRegion();
                        return new EllipsoidTag(new LocationTag(BukkitAdapter.adapt(BukkitAdapter.adapt(region.getWorld()), region.getCenter())), new LocationTag(BukkitAdapter.adapt(BukkitAdapter.adapt(region.getWorld()), region.getRadius())));
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
                        return poly;
                    }
                    return new CuboidTag(BukkitAdapter.adapt(object.player.getWorld(), selection.getIncompleteRegion().getMinimumPoint()),
                            BukkitAdapter.adapt(object.player.getWorld(), selection.getIncompleteRegion().getMaximumPoint()));
                }
            }
            catch (Throwable ex) {
                Debug.echoError(ex);
            }
            return null;
        }, "selected_region");
    }

    @Override
    public void adjust(Mechanism mechanism) {

        // <--[mechanism]
        // @object PlayerTag
        // @name we_selection
        // @plugin Depenizen, WorldEdit
        // @input ObjectTag
        // @description
        // Sets the player's current block area selection, as a CuboidTag, EllipsoidTag, or PolygonTag.
        // @tags
        // <PlayerTag.we_selection>
        // -->
        if (mechanism.matches("we_selection")) {
            WorldEditPlugin worldEdit = (WorldEditPlugin) WorldEditBridge.instance.plugin;
            RegionSelector selector;
            if (mechanism.getValue().asString().startsWith("cu@")) {
                CuboidTag input = mechanism.valueAsType(CuboidTag.class);
                selector = new CuboidRegionSelector(BukkitAdapter.adapt(input.getWorld().getWorld()), BukkitAdapter.asBlockVector(input.getLow(0)), BukkitAdapter.asBlockVector(input.getHigh(0)));
            }
            else if (mechanism.getValue().asString().startsWith("ellipsoid@")) {
                EllipsoidTag input = mechanism.valueAsType(EllipsoidTag.class);
                selector = new EllipsoidRegionSelector(BukkitAdapter.adapt(input.center.getWorld()), BukkitAdapter.asBlockVector(input.center), BukkitAdapter.asVector(input.size));
            }
            else if (mechanism.getValue().asString().startsWith("polygon@")) {
                PolygonTag input = mechanism.valueAsType(PolygonTag.class);
                selector = new Polygonal2DRegionSelector(BukkitAdapter.adapt(input.world.getWorld()), input.corners.stream().map(c -> BlockVector2.at(c.x, c.z)).collect(Collectors.toList()), (int)input.yMin, (int)input.yMax);
            }
            else {
                Debug.echoError("Invalid we_selection input");
                return;
            }
            LocalSession session = worldEdit.getSession(player);
            session.setRegionSelector(BukkitAdapter.adapt(player.getWorld()), selector);
            selector.explainRegionAdjust(BukkitAdapter.adapt(player), session);
        }
    }
}
