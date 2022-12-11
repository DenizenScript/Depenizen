package com.denizenscript.depenizen.bukkit.properties.vivecraft;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.depenizen.bukkit.objects.vivecraft.ViveCraftPlayerTag;
import org.vivecraft.VSE;

public class ViveCraftPlayerProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "ViveCraftPlayer";
    }

    public static boolean describes(ObjectTag object) {
        return object instanceof PlayerTag;
    }

    public static ViveCraftPlayerProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new ViveCraftPlayerProperties((PlayerTag) object);
        }
    }

    public static final String[] handledMechs = new String[] {
    }; // None

    private ViveCraftPlayerProperties(PlayerTag player) {
        this.player = player;
    }

    PlayerTag player;


    public static void registerTags() {

        // <--[tag]
        // @attribute <PlayerTag.is_vivecraft>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, ViveCraft
        // @description
        // Returns whether the player is running on VR or not.
        // -->
        PropertyParser.registerTag(ViveCraftPlayerProperties.class, ElementTag.class,"is_vivecraft", (attribute, object) -> {
            return new ElementTag(VSE.isVive(object.player.getPlayerEntity()));
        });

        // <--[tag]
        // @attribute <PlayerTag.vivecraft>
        // @returns ViveCraftPlayerTag
        // @plugin Depenizen, ViveCraft
        // @description
        // Returns the ViveCraftPlayerTag for this player.
        // -->
        PropertyParser.registerTag(ViveCraftPlayerProperties.class, ViveCraftPlayerTag.class, "vivecraft", (attribute, object) -> {
            return new ViveCraftPlayerTag(object.player.getPlayerEntity());
        });

    }
}
