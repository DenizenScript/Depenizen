package com.denizenscript.depenizen.bukkit.properties.luckperms;

import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.depenizen.bukkit.bridges.LuckPermsBridge;
import com.denizenscript.depenizen.bukkit.objects.luckperms.LuckPermsTrackTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.query.QueryOptions;
import net.luckperms.api.track.Track;

public class LuckPermsPlayerProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "LuckPermsPlayer";
    }

    public static boolean describes(ObjectTag object) {
        return object instanceof PlayerTag;
    }

    public static LuckPermsPlayerProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new LuckPermsPlayerProperties((PlayerTag) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "luckperms_tracks"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    public LuckPermsPlayerProperties(PlayerTag player) {
        this.player = player;
    }

    PlayerTag player;

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <PlayerTag.luckperms_tracks>
        // @returns ListTag(LuckPermsTrackTag)
        // @plugin Depenizen, LuckPerms
        // @description
        // Returns a list of tracks the player is in.
        // -->
        if (attribute.startsWith("luckperms_tracks")) {
            ListTag tracks = new ListTag();
            User user = LuckPermsBridge.luckPermsInstance.getUserManager().getUser(player.getUUID());
            if (user == null) {
                return null;
            }
            for (Track track : LuckPermsBridge.luckPermsInstance.getTrackManager().getLoadedTracks()) {
                for (String groupName : track.getGroups()) {
                    Group group = LuckPermsBridge.luckPermsInstance.getGroupManager().getGroup(groupName);
                    // Copied from https://github.com/LuckPerms/extension-legacy-api/blob/97f30ce63b32663a0481557f3a081a9554a5bca0/src/main/java/me/lucko/luckperms/extension/legacyapi/impl/permissionholders/PermissionHolderProxy.java#L205-L212
                    boolean inGroup = group != null && user.resolveInheritedNodes(QueryOptions.nonContextual()).stream()
                            .filter(NodeType.INHERITANCE::matches)
                            .map(NodeType.INHERITANCE::cast)
                            .filter(net.luckperms.api.node.Node::getValue)
                            .anyMatch(n -> n.getGroupName().equalsIgnoreCase(group.getName()));
                    if (inGroup) {
                        if (!tracks.contains(new LuckPermsTrackTag(track).identify())) {
                            tracks.addObject(new LuckPermsTrackTag(track));
                            break;
                        }
                    }
                }
            }
            return tracks.getObjectAttribute(attribute.fulfill(1));
        }

        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {
    }
}
