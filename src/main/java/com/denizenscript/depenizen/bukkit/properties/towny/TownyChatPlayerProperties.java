package com.denizenscript.depenizen.bukkit.properties.towny;

import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.depenizen.bukkit.bridges.TownyChatBridge;
import com.palmergames.bukkit.TownyChat.Chat;
import com.palmergames.bukkit.TownyChat.channels.Channel;
import com.palmergames.bukkit.TownyChat.channels.ChannelsHolder;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;

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

    public TownyChatPlayerProperties(PlayerTag player) {
        this.player = player;
    }

    PlayerTag player;

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        if (attribute.startsWith("townychat")) {
            Chat plugin = (Chat) TownyChatBridge.instance.plugin;
            ChannelsHolder holder = plugin.getChannelsHandler();
            attribute = attribute.fulfill(1);

            // <--[tag]
            // @attribute <PlayerTag.townychat.channels>
            // @returns ListTag
            // @plugin Depenizen, Towny
            // @description
            // Returns a list of all channels the player is in.
            // -->
            if (attribute.startsWith("channels") || attribute.startsWith("channel")) {
                ListTag chans = new ListTag();
                for (Channel c : holder.getAllChannels().values()) {
                    chans.addObject(new ElementTag(c.getName()));
                }
                return chans.getObjectAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <PlayerTag.townychat.muted_in[<channel_name>]>
            // @returns ElementTag(Boolean)
            // @plugin Depenizen, Towny
            // @description
            // Returns whether the player is muted in the specified channel.
            // -->
            else if (attribute.startsWith("muted_in") && attribute.hasParam()) {
                Channel c = holder.getChannel(attribute.getAttribute(1));
                if (c == null) {
                    return null;
                }
                return new ElementTag(c.isMuted(player.getName())).getObjectAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <PlayerTag.townychat.has_permission[<channel_name>]>
            // @returns ElementTag(Boolean)
            // @plugin Depenizen, Towny
            // @description
            // Returns whether the player has permissions to join the specified channel.
            // -->
            else if (attribute.startsWith("has_permission") && attribute.hasParam()) {
                Channel c = holder.getChannel(attribute.getParam());
                if (c == null) {
                    return null;
                }
                String perm = c.getPermission();
                if (perm == null) {
                    return new ElementTag(true).getObjectAttribute(attribute.fulfill(1));
                }
                return new ElementTag(TownyUniverse.getInstance().getPermissionSource().has(player.getPlayerEntity(), perm))
                        .getObjectAttribute(attribute.fulfill(1));
            }

            return null;
        }
        return null;
    }
}
