package com.denizenscript.depenizen.bukkit.properties.floodgate;

import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.depenizen.bukkit.bridges.EssentialsBridge;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

public class FloodgatePlayerProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "FloodgatePlayer";
    }

    @Override
    public void adjust(Mechanism mechanism) {
        // None
    }
    
    public static boolean describes(ObjectTag object) {
        return object instanceof PlayerTag;
    }

    public static FloodgatePlayerProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new FloodgatePlayerProperties((PlayerTag) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "floodgate"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    public FloodgatePlayerProperties(PlayerTag object) { this.player = object; }

    public FloodgatePlayer getFloodgatePlayer() {
        return FloodgateApi.getInstance().getPlayer(player.getUUID());
    }

    PlayerTag player;

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {

        if (getFloodgatePlayer() == null) {
            return null;
        }

        if (attribute.startsWith("floodgate")) {
            attribute = attribute.fulfill(1);

            // <--[tag]
            // @attribute <PlayerTag.floodgate.version>
            // @returns ElementTag(String)
            // @plugin Depenizen, Floodgate
            // @description
            // Returns the version of the Bedrock client.
            // -->
            if (attribute.startsWith("version")) {
                return new ElementTag(getFloodgatePlayer().getVersion()).getObjectAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <PlayerTag.floodgate.username>
            // @returns ElementTag(String)
            // @plugin Depenizen, Floodgate
            // @description
            // Returns the real username of the Bedrock client.
            // -->
            if (attribute.startsWith("username")) {
                return new ElementTag(getFloodgatePlayer().getUsername()).getObjectAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <PlayerTag.floodgate.device>
            // @returns ElementTag(String)
            // @plugin Depenizen, Floodgate
            // @description
            // Returns the Operating System of the Bedrock client.
            // -->
            if (attribute.startsWith("device")) {
                return new ElementTag(getFloodgatePlayer().getDeviceOs()).getObjectAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <PlayerTag.floodgate.locale>
            // @returns ElementTag(String)
            // @plugin Depenizen, Floodgate
            // @description
            // Returns the language code of the Bedrock client.
            // -->
            if (attribute.startsWith("locale")) {
                return new ElementTag(getFloodgatePlayer().getLanguageCode()).getObjectAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <PlayerTag.floodgate.xuid>
            // @returns ElementTag(String)
            // @plugin Depenizen, Floodgate
            // @description
            // Returns the Xbox Unique Identifier of the Bedrock client.
            // -->
            if (attribute.startsWith("xuid")) {
                return new ElementTag(getFloodgatePlayer().getXuid()).getObjectAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <PlayerTag.floodgate.is_from_proxy>
            // @returns ElementTag(Boolean)
            // @plugin Depenizen, Floodgate
            // @description
            // Returns if the Floodgate player is connected through a proxy.
            // -->
            if (attribute.startsWith("is_from_proxy")) {
                return new ElementTag(getFloodgatePlayer().isFromProxy()).getObjectAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <PlayerTag.floodgate.is_linked>
            // @returns ElementTag(Boolean)
            // @plugin Depenizen, Floodgate
            // @description
            // Returns if the player is linked to a Java account.
            // -->
            if (attribute.startsWith("is_linked")) {
                return new ElementTag(getFloodgatePlayer().isLinked()).getObjectAttribute(attribute.fulfill(1));
            }
        }

        return null;
    }
}
