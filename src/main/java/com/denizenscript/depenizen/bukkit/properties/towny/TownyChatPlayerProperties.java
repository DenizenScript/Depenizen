package com.denizenscript.depenizen.bukkit.properties.towny;

import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.depenizen.bukkit.bridges.TownyChatBridge;
import com.palmergames.bukkit.TownyChat.Chat;
import com.palmergames.bukkit.TownyChat.channels.Channel;
import com.palmergames.bukkit.TownyChat.channels.ChannelsHolder;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import com.denizenscript.denizen.objects.dPlayer;
import com.denizenscript.denizencore.objects.ElementTag;
import com.denizenscript.denizencore.objects.ListTag;
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
        return object instanceof dPlayer;
    }

    public static TownyChatPlayerProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        return new TownyChatPlayerProperties((dPlayer) object);
    }

    public static final String[] handledTags = new String[] {
            "townychat"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    private TownyChatPlayerProperties(dPlayer player) {
        this.player = player;
    }

    dPlayer player;

    @Override
    public String getAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        if (attribute.startsWith("townychat")) {
            Chat plugin = (Chat) TownyChatBridge.instance.plugin;
            ChannelsHolder holder = plugin.getChannelsHandler();
            attribute = attribute.fulfill(1);

            // <--[tag]
            // @attribute <p@player.townychat.channels>
            // @returns ListTag(Element)
            // @description
            // Returns a list of all channels the player is in.
            // @Plugin Depenizen, Towny
            // -->
            if (attribute.startsWith("channel")) {
                ListTag chans = new ListTag();
                for (Channel c : holder.getAllChannels().values()) {
                    chans.add(new ElementTag(c.getName()).identify());
                }
                return chans.getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <p@player.townychat.muted_in[<channel name>]>
            // @returns ElementTag(Boolean)
            // @description
            // Returns whether the player is muted in the specified channel.
            // @Plugin Depenizen, Towny
            // -->
            else if (attribute.startsWith("muted_in") && attribute.hasContext(1)) {
                Channel c = holder.getChannel(attribute.getAttribute(1));
                if (c == null) {
                    return null;
                }
                return new ElementTag(c.isMuted(player.getName())).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <p@player.townychat.has_permission[<channel name>]>
            // @returns ElementTag(Boolean)
            // @description
            // Returns whether the player has permissions to join the specified channel.
            // @Plugin Depenizen, Towny
            // -->
            else if (attribute.startsWith("has_permission") && attribute.hasContext(1)) {
                Channel c = holder.getChannel(attribute.getContext(1));
                if (c == null) {
                    return null;
                }
                String perm = c.getPermission();
                if (perm == null || !plugin.getTowny().isPermissions()) {
                    return new ElementTag(true).getAttribute(attribute.fulfill(1));
                }
                return new ElementTag(TownyUniverse.getPermissionSource().has(player.getPlayerEntity(), perm))
                        .getAttribute(attribute.fulfill(1));
            }

            return null;
        }
        return null;
    }
}
