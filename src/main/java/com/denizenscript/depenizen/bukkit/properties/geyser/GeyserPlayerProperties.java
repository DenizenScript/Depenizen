package com.denizenscript.depenizen.bukkit.properties.geyser;

import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizen.objects.PlayerTag;
import org.geysermc.geyser.api.GeyserApi;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;

public class GeyserPlayerProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "GeyserPlayer";
    }

    @Override
    public void adjust(Mechanism mechanism) {
        // None
    }

    public static boolean describes(ObjectTag object) {
        return object instanceof PlayerTag;
    }

    public static GeyserPlayerProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new GeyserPlayerProperties((PlayerTag) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "geyser"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    public GeyserPlayerProperties(PlayerTag object) { this.player = object; }

    PlayerTag player;

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {

        if (attribute.startsWith("geyser")) {
            attribute = attribute.fulfill(1);

            // <--[tag]
            // @attribute <PlayerTag.geyser.is_bedrock>
            // @returns ElementTag(Boolean)
            // @plugin Depenizen, Geyser
            // @description
            // Returns is player plays from Minecraft Bedrock Edition.
            // -->
            if (attribute.startsWith("is_bedrock")) {
                return new ElementTag(GeyserApi.api().connectionByUuid(player.getUUID()) != null).getObjectAttribute(attribute.fulfill(1));
            }
        }

        return null;
    }
}
