package com.denizenscript.depenizen.bukkit.extensions.towny;

import com.denizenscript.depenizen.bukkit.extensions.dObjectExtension;
import com.denizenscript.depenizen.bukkit.objects.dTown;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import net.aufdemrand.denizen.objects.dCuboid;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class TownyCuboidExtension extends dObjectExtension {

    public static boolean describes(dObject object) {
        return object instanceof dCuboid;
    }

    public static TownyCuboidExtension getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new TownyCuboidExtension((dCuboid) object);
        }
    }

    public static final String[] handledTags = new String[]{
            "has_town", "list_towns"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    private TownyCuboidExtension(dCuboid cuboid) {
        this.cuboid = cuboid;
    }

    dCuboid cuboid = null;

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <cu@cuboid.has_town>
        // @returns Element(Boolean)
        // @description
        // Returns whether the cuboid contains any town at all.
        // @Plugin DepenizenBukkit, Towny
        // -->
        if (attribute.startsWith("has_town")) {
            for (Location location : cuboid.getBlockLocations()) {
                if (TownyUniverse.getTownName(location) != null) {
                    return new Element(true).getAttribute(attribute.fulfill(1));
                }
            }
            return new Element(false).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <cu@cuboid.list_towns>
        // @returns dList(dTown)
        // @description
        // Returns all the towns within the cuboid.
        // @Plugin DepenizenBukkit, Towny
        // -->
        if (attribute.startsWith("list_towns")) {
            dList list = new dList();
            List<String> towns = new ArrayList<String>();
            try {
                for (Location location : cuboid.getBlockLocations()) {
                    String townName = TownyUniverse.getTownName(location);
                    if (townName != null && !towns.contains(townName)) {
                        list.add(new dTown(TownyUniverse.getTownBlock(location).getTown()).identify());
                        towns.add(townName);
                    }
                }
            }
            catch (NotRegisteredException e) {
            }
            return list.getAttribute(attribute.fulfill(1));
        }

        return null;
    }
}
