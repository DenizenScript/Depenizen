package com.denizenscript.depenizen.bukkit.properties.towny;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.debugging.Debug;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.depenizen.bukkit.bridges.TownyChatBridge;
import com.palmergames.bukkit.TownyChat.channels.Channel;
import com.palmergames.bukkit.towny.TownyUniverse;

public class TownyChatPlayerProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "TownyChatPlayer";
    }

    @Override
    public void adjust(Mechanism mechanism) {
        // None
    }

    public static boolean describes(ObjectTag object) {
        return object instanceof PlayerTag;
    }

    public static TownyChatPlayerProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        return new TownyChatPlayerProperties((PlayerTag) object);
    }

    public static final String[] handledTags = new String[] {
            "townychat"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    private TownyChatPlayerProperties(PlayerTag player) {
        this.player = player;
    }

    PlayerTag player;

    public static void registerTags() {
        PropertyParser.<TownyChatPlayerProperties>registerTag("townychat", (attribute, object) -> {
            attribute = attribute.fulfill(1);

            // <--[tag]
            // @attribute <PlayerTag.townychat.channels>
            // @returns ListTag
            // @plugin Depenizen, Towny
            // @description
            // Returns a list of all channels the player is in.
            // -->
            if (attribute.startsWith("channels")) {
                ListTag channels = new ListTag();
                for (Channel channel : TownyChatBridge.channelsHolder.getAllChannels().values()) {
                    channels.addObject(new ElementTag(channel.getName()));
                }
                return channels;
            }

            else if (attribute.startsWith("muted_in")) {
                if (attribute.hasContext(1)) {
                    Channel channel = TownyChatBridge.channelsHolder.getChannel(attribute.getContext(1));
                    if (channel != null) {
                        return new ElementTag(channel.isMuted(object.player.getName()));
                    }
                    Debug.echoError("Unable to match '" + attribute.getContext(1) + "' to a valid chat channel!");
                    return null;
                }
                Debug.echoError("A channel must be specified for <PlayerTag.townychat.muted_in[<channel>]>!");
                return null;
            }

            // <--[tag]
            // @attribute <PlayerTag.townychat.has_permission[<channel>]>
            // @returns ElementTag(Boolean)
            // @plugin Depenizen, Towny
            // @description
            // Returns whether the player has permissions to join the specified channel.
            // -->
            else if (attribute.startsWith("has_permission")) {
                if (attribute.hasContext(1)) {
                    Channel channel = TownyChatBridge.channelsHolder.getChannel(attribute.getContext(1));
                    if (channel != null) {
                        return new ElementTag(channel.getPermission() != null &&
                                TownyUniverse.getInstance().getPermissionSource().has(object.player.getPlayerEntity(), channel.getPermission()));
                    }
                }
                else {
                    Debug.echoError("You must specify a channel for tag <PlayerTag.townychat.has_permission[<channel>]>!");
                    return null;
                }
            }
            return null;
        });
    }
}
