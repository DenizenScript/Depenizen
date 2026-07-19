package com.denizenscript.depenizen.bukkit.properties.coordinateoffset;

import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.jtprince.coordinateoffset.FixedOffset;
import com.jtprince.coordinateoffset.Offset;
import com.jtprince.coordinateoffset.api.CoordinateOffset;
import com.jtprince.coordinateoffset.adapter.OffsetPlayer;

public class CoordinateOffsetPlayerExtensions {

    public static CoordinateOffset getApi() {
        return CoordinateOffset.api();
    }

    public static OffsetPlayer getOffsetPlayer(PlayerTag player) {
        return getApi().adaptPlayer(player.getPlayerEntity());
    }

    public static void register() {

        // <--[tag]
        // @attribute <PlayerTag.coordinate_offset>
        // @returns LocationTag
        // @mechanism PlayerTag.coordinate_offset
        // @plugin Depenizen, CoordinateOffset
        // @description
        // Returns the player's current coordinate offset as a LocationTag (x, y, z).
        // -->
        PlayerTag.registerOnlineOnlyTag(LocationTag.class, "coordinate_offset", (attribute, player) -> {
            OffsetPlayer offsetPlayer = getOffsetPlayer(player);
            FixedOffset offset = getApi().getOffset(offsetPlayer);
            if (offset.isZero()) {
                return null;
            }
            return new LocationTag(null, offset.x(), offset.y(), offset.z());
        });

        // <--[mechanism]
        // @object PlayerTag
        // @name coordinate_offset
        // @input LocationTag
        // @plugin Depenizen, CoordinateOffset
        // @description
        // Sets the player's coordinate offset.
        // @tags
        // <PlayerTag.coordinate_offset>
        // -->
        PlayerTag.registerOnlineOnlyMechanism("coordinate_offset", LocationTag.class, (player, mechanism, loc) -> {
            int x = (int) Math.round(loc.getX());
            int y = (int) Math.round(loc.getY());
            int z = (int) Math.round(loc.getZ());
            
            // Align using ConstantOffsetProvider logic (multiples of configured blocks)
            int alignedX = Offset.alignComponentToConfiguredMultiple(x);
            int alignedZ = Offset.alignComponentToConfiguredMultiple(z);
            
            OffsetPlayer offsetPlayer = getOffsetPlayer(player);
            getApi().setOffset(offsetPlayer, Offset.scalable(alignedX, y, alignedZ));
        });
    }
}
