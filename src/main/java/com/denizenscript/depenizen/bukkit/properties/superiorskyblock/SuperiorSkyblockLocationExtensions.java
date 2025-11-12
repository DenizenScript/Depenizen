package com.denizenscript.depenizen.bukkit.properties.superiorskyblock;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.depenizen.bukkit.objects.superiorskyblock.SuperiorSkyblockIslandTag;

public class SuperiorSkyblockLocationExtensions {

    public static void register() {

        // <--[tag]
        // @attribute <LocationTag.superiorskyblock_island>
        // @returns SuperiorSkyblockIslandTag
        // @plugin Depenizen, SuperiorSkyblock
        // @description
        // Returns the island that is at the provided location, if any.
        // -->
        LocationTag.tagProcessor.registerTag(SuperiorSkyblockIslandTag.class, "superiorskyblock_island", (attribute, location) -> {
            Island island = SuperiorSkyblockAPI.getIslandAt(location);
            return island != null ? new SuperiorSkyblockIslandTag(island) : null;
        });
    }
}
