package com.denizenscript.depenizen.bukkit.properties.vivecraft;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.depenizen.bukkit.objects.vivecraft.ViveCraftPlayerTag;
import org.vivecraft.VSE;

public class ViveCraftPlayerProperties {

    public static void register() {

        // <--[tag]
        // @attribute <PlayerTag.is_vivecraft>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, ViveCraft
        // @description
        // Returns whether the player is running on VR or not.
        // -->
        PlayerTag.registerOnlineOnlyTag(ElementTag.class,"is_vivecraft", (attribute, object) -> {
            return new ElementTag(VSE.isVive(object.getPlayerEntity()));
        });

        // <--[tag]
        // @attribute <PlayerTag.vivecraft>
        // @returns ViveCraftPlayerTag
        // @plugin Depenizen, ViveCraft
        // @description
        // Returns the ViveCraftPlayerTag for this player.
        // -->
        PlayerTag.registerOnlineOnlyTag(ViveCraftPlayerTag.class, "vivecraft", (attribute, object) -> {
            return new ViveCraftPlayerTag(object.getPlayerEntity());
        });

    }
}
