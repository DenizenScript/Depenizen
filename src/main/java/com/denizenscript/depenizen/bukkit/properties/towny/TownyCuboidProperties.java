package com.denizenscript.depenizen.bukkit.properties.towny;

import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.depenizen.bukkit.objects.towny.dTown;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import com.denizenscript.denizen.objects.dCuboid;
import com.denizenscript.denizencore.objects.Element;
import com.denizenscript.denizencore.objects.dList;
import com.denizenscript.denizencore.objects.dObject;
import com.denizenscript.denizencore.tags.Attribute;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class TownyCuboidProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "TownyCuboid";
    }

    @Override
    public void adjust(Mechanism mechanism) {
        // None
    }

    public static boolean describes(dObject object) {
        return object instanceof dCuboid;
    }

    public static TownyCuboidProperties getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new TownyCuboidProperties((dCuboid) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "has_town", "list_towns"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    private TownyCuboidProperties(dCuboid cuboid) {
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
        // @Plugin Depenizen, Towny
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
        // @Plugin Depenizen, Towny
        // -->
        if (attribute.startsWith("list_towns")) {
            dList list = new dList();
            List<String> towns = new ArrayList<>();
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
