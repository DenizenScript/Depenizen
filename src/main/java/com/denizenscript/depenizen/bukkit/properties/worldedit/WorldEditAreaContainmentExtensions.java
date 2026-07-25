package com.denizenscript.depenizen.bukkit.properties.worldedit;

import com.denizenscript.denizen.objects.*;
import com.denizenscript.denizencore.tags.ObjectTagProcessor;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.regions.*;
import com.sk89q.worldedit.world.World;

import java.util.ArrayList;
import java.util.List;

public interface WorldEditAreaContainmentExtensions {

    static <T extends AreaContainmentObject> void register(Class<T> type, ObjectTagProcessor<T> processor) {

        // <--[mechanism]
        // @object AreaObject
        // @name we_regenerate
        // @plugin Depenizen, WorldEdit
        // @input None
        // @description
        // Regenerates an area to its original state.
        // Areas can be either CuboidTags, EllipsoidTags, or PolygonTags.
        // Areas covering over 500,000 blocks run the risk of hanging your server and will not run. Use a mechanism value of 'force' to bypass this.
        // @example
        // # Regenerates a small area to its original state.
        // - adjust <cuboid[world,1,1,1,10,10,10]> we_regenerate
        // @example
        // # Regenerates a large area to its original state.
        // # NOTE: This runs the risk of hanging your server and should be avoided.
        // - adjust <cuboid[world,1,1,1,100,100,100]> we_regenerate:force
        // -->
        processor.registerMechanism("we_regenerate", false, (area, mechanism) -> {
            World world = BukkitAdapter.adapt(area.getWorld().getWorld());
            Region region = null;
            if (area instanceof CuboidTag cuboid) {
                LocationTag lowCorner = cuboid.pairs.get(0).low;
                LocationTag highCorner = cuboid.pairs.get(0).high;
                region = new CuboidRegion(world, BlockVector3.at(lowCorner.getX(), lowCorner.getY(), lowCorner.getZ()),
                                                BlockVector3.at(highCorner.getX(), highCorner.getY(), highCorner.getZ()));
            }
            else if (area instanceof EllipsoidTag ellipsoid) {
                LocationTag center = ellipsoid.center;
                LocationTag radius = ellipsoid.size;
                region = new EllipsoidRegion(world, BlockVector3.at(center.getX(), center.getY(), center.getZ()),
                                                        Vector3.at(radius.getX(), radius.getY(), radius.getZ()));
            }
            else if (area instanceof PolygonTag polygon) {
                List<BlockVector2> points = new ArrayList<>(polygon.corners.size());
                for (PolygonTag.Corner corner : polygon.corners) {
                    points.add(BlockVector2.at(corner.x, corner.z));
                }
                region = new Polygonal2DRegion(world, points, (int) Math.round(polygon.yMin), (int) Math.round(polygon.yMax));
            }
            if (region.getVolume() <= 500000 || (mechanism.hasValue() && mechanism.getValue().asLowerString().equals("force"))) {
                world.regenerate(region, world);
            }
            else {
                mechanism.echoError("This regeneration involves over 500,000 blocks and may hang your server. " +
                        "If you want to do this, use 'we_regenerate:force'.");
            }
        });
    }
}
