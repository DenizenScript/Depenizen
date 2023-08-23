package com.denizenscript.depenizen.bukkit.properties.luckperms;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.depenizen.bukkit.bridges.LuckPermsBridge;
import com.denizenscript.depenizen.bukkit.objects.luckperms.LuckPermsGroupTag;
import com.denizenscript.depenizen.bukkit.objects.luckperms.LuckPermsTrackTag;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.query.QueryOptions;
import net.luckperms.api.track.Track;

public class LuckPermsPlayerExtensions {

    public static void register() {

        // <--[tag]
        // @attribute <PlayerTag.luckperms_tracks>
        // @returns ListTag(LuckPermsTrackTag)
        // @plugin Depenizen, LuckPerms
        // @description
        // Returns a list of tracks the player is in.
        // -->
        PlayerTag.tagProcessor.registerTag(ListTag.class, "luckperms_tracks", (attribute, player) -> {
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
            return tracks;
        });

        // <--[tag]
        // @attribute <PlayerTag.luckperms_primary_group>
        // @returns LuckPermsGroupTag
        // @plugin Depenizen, LuckPerms
        // @description
        // Returns a player's primary group.
        // -->
        PlayerTag.tagProcessor.registerTag(LuckPermsGroupTag.class, "luckperms_primary_group", (attribute, player) -> {
            User user = LuckPermsBridge.luckPermsInstance.getUserManager().getUser(player.getUUID());
            if (user == null) {
                return null;
            }
            Group group = LuckPermsBridge.luckPermsInstance.getGroupManager().getGroup(user.getPrimaryGroup());
            if (group == null) {
                return null;
            }
            return new LuckPermsGroupTag(group);
        });

    }
}
