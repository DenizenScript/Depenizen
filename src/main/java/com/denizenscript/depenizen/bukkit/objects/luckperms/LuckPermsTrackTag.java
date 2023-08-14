package com.denizenscript.depenizen.bukkit.objects.luckperms;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.tags.ObjectTagProcessor;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.Fetchable;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.TagContext;
import com.denizenscript.depenizen.bukkit.bridges.LuckPermsBridge;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;
import net.luckperms.api.query.QueryOptions;
import net.luckperms.api.track.Track;

import java.util.List;

public class LuckPermsTrackTag implements ObjectTag {

    // <--[ObjectType]
    // @name LuckPermsTrackTag
    // @prefix luckpermstrack
    // @base ElementTag
    // @format
    // The identity format for tracks is <track_name>
    // For example, 'luckpermstrack@my_track'.
    //
    // @plugin Depenizen, LuckPerms
    // @description
    // A LuckPermsTrackTag represents a LuckPerms track.
    //
    // -->

    /////////////////////
    //   OBJECT FETCHER
    /////////////////

    @Fetchable("luckpermstrack")
    public static LuckPermsTrackTag valueOf(String string, TagContext context) {
        if (string.startsWith("luckpermstrack@")) {
            string = string.substring("luckpermstrack@".length());
        }
        try {
            Track track = LuckPermsBridge.luckPermsInstance.getTrackManager().getTrack(string);
            if (track == null) {
                return null;
            }
            return new LuckPermsTrackTag(track);
        }
        catch (Throwable e) {
            Debug.echoError(e);
        }
        return null;
    }

    public static boolean matches(String arg) {
        if (arg.startsWith("luckpermstrack@")) {
            return true;
        }
        return LuckPermsBridge.luckPermsInstance.getTrackManager().isLoaded(arg);
    }

    /////////////////////
    //   STATIC CONSTRUCTORS
    /////////////////

    Track track;

    public LuckPermsTrackTag(Track t) {
        track = t;
    }

    /////////////////////
    //   ObjectTag Methods
    /////////////////

    private String prefix = "LuckPermsTrack";

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public LuckPermsTrackTag setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    @Override
    public int hashCode() {
        return track.hashCode();
    }

    @Override
    public boolean equals(Object a) {
        if (a instanceof LuckPermsTrackTag) {
            return ((LuckPermsTrackTag) a).track.getName().equals(track.getName());
        }
        return false;
    }

    @Override
    public boolean isUnique() {
        return true;
    }

    @Override
    public String identify() {
        return "luckpermstrack@" + track.getName();
    }

    @Override
    public String identifySimple() {
        return identify();
    }

    @Override
    public String toString() {
        return identify();
    }

    public Track getTrack() {
        return track;
    }

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {
        return tagProcessor.getObjectAttribute(this, attribute);
    }

    public static void register() {

        // <--[tag]
        // @attribute <LuckPermsTrackTag.name>
        // @returns ElementTag
        // @plugin Depenizen, LuckPerms
        // @description
        // Returns the name of the track.
        // -->
        tagProcessor.registerTag(ElementTag.class, "name", (attribute, object) -> {
            return new ElementTag(object.getTrack().getName(), true);
        });

        // <--[tag]
        // @attribute <LuckPermsTrackTag.groups[(<player>)]>
        // @returns ListTag
        // @plugin Depenizen, LuckPerms
        // @description
        // This returns names of the groups instead of <@link objecttype LuckPermsGroupTag>s, as groups can be unloaded from LuckPerms but still be part of the track.
        // If a player input is specified, limits to only the groups that the player is in.
        // -->
        tagProcessor.registerTag(ListTag.class,"groups", (attribute, object) -> {
            List<String> trackGroups = object.getTrack().getGroups();
            if (!attribute.hasParam()) {
                return new ListTag(trackGroups, true);
            }
            PlayerTag player = attribute.paramAsType(PlayerTag.class);
            if (player == null) {
                attribute.echoError("Invalid player input for 'group' tag.");
                return null;
            }
            User user = LuckPermsBridge.luckPermsInstance.getUserManager().getUser(player.getUUID());
            if (user == null) {
                return null;
            }
            List<String> memberGroups = user.resolveInheritedNodes(QueryOptions.nonContextual()).stream()
                    .filter(NodeType.INHERITANCE::matches)
                    .map(NodeType.INHERITANCE::cast)
                    .filter(net.luckperms.api.node.Node::getValue)
                    .map(InheritanceNode::getGroupName)
                    .filter(trackGroups::contains).toList();
            return new ListTag(memberGroups, true);
        });

        // <--[tag]
        // @attribute <LuckPermsTrackTag.loaded_groups[(<player>)]>
        // @returns ListTag(LuckPermsGroupTag)
        // @plugin Depenizen, LuckPerms
        // @description
        // Returns the list of loaded groups in the track.
        // If a player input is specified, limits to only the groups that the player is in.
        // -->
        tagProcessor.registerTag(ListTag.class, "loaded_groups", (attribute, object) -> {
            ListTag groups = new ListTag();
            List<String> trackGroups = object.getTrack().getGroups();
            if (!attribute.hasParam()) {
                for (String groupName : trackGroups) {
                    Group group = LuckPermsBridge.luckPermsInstance.getGroupManager().getGroup(groupName);
                    if (group != null) {
                        groups.addObject(new LuckPermsGroupTag(group));
                    }
                }
                return groups;
            }
            PlayerTag player = attribute.paramAsType(PlayerTag.class);
            if (player == null) {
                attribute.echoError("Invalid player input for 'group' tag.");
                return null;
            }
            User user = LuckPermsBridge.luckPermsInstance.getUserManager().getUser(player.getUUID());
            if (user == null) {
                return null;
            }
            List<String> memberGroups = user.resolveInheritedNodes(QueryOptions.nonContextual()).stream()
                    .filter(NodeType.INHERITANCE::matches)
                    .map(NodeType.INHERITANCE::cast)
                    .filter(net.luckperms.api.node.Node::getValue)
                    .map(InheritanceNode::getGroupName)
                    .filter(trackGroups::contains).toList();
            for (String groupName : memberGroups) {
                Group group = LuckPermsBridge.luckPermsInstance.getGroupManager().getGroup(groupName);
                if (group != null) {
                    groups.addObject(new LuckPermsGroupTag(group));
                }
            }
            return groups;
        });
    }

    public static final ObjectTagProcessor<LuckPermsTrackTag> tagProcessor = new ObjectTagProcessor<>();

}
