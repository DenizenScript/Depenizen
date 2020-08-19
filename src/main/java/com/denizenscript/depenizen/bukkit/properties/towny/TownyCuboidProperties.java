package com.denizenscript.depenizen.bukkit.properties.towny;

import com.denizenscript.denizen.objects.CuboidTag;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.depenizen.bukkit.objects.towny.TownTag;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import org.bukkit.Location;

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

    public static boolean describes(ObjectTag object) {
        return object instanceof CuboidTag;
    }

    public static TownyCuboidProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new TownyCuboidProperties((CuboidTag) object);
        }
    }

    public static final String[] handledMechs = new String[] {
    }; // None

    private TownyCuboidProperties(CuboidTag cuboid) {
        this.cuboid = cuboid;
    }

    CuboidTag cuboid;

    public static void registerTags() {

        // <--[tag]
        // @attribute <CuboidTag.has_town>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Towny
        // @description
        // Returns whether the cuboid contains any town at all.
        // -->
        PropertyParser.<TownyCuboidProperties>registerTag("has_town", (attribute, object) -> {
            for (Location location : object.cuboid.getBlockLocationsUnfiltered()) {
                if (TownyUniverse.getTownName(location) != null) {
                    return new ElementTag(true);
                }
            }
            return new ElementTag(false);
        });

        // <--[tag]
        // @attribute <CuboidTag.towns>
        // @returns ListTag(TownTag)
        // @plugin Depenizen, Towny
        // @description
        // Returns all the towns within the cuboid.
        // -->
        PropertyParser.<TownyCuboidProperties>registerTag("towns", (attribute, object) -> {
            ListTag towns = new ListTag();
            for (Location location : object.cuboid.getBlockLocationsUnfiltered()) {
                String townName = TownyAPI.getInstance().getTownName(location);
                if (townName != null) {
                    TownTag town = TownTag.valueOf(townName);
                    if (!towns.contains(town)) {
                        towns.addObject(town);
                    }
                }
            }
            return towns;
        }, "list_towns");
    }
}
