package com.denizenscript.depenizen.bukkit.objects.luckperms;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.Fetchable;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.TagContext;
import com.denizenscript.depenizen.bukkit.bridges.LuckPermsBridge;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;
import net.luckperms.api.query.QueryOptions;
import net.luckperms.api.track.Track;

import java.util.List;
import java.util.stream.Collectors;

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
        return arg.startsWith("luckpermstrack@");
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

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <LuckPermsTrackTag.name>
        // @returns ElementTag
        // @plugin Depenizen, LuckPerms
        // @description
        // Returns the name of the track.
        // -->
        if (attribute.startsWith("name")) {
            return new ElementTag(track.getName()).getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <LuckPermsTrackTag.groups[(<player>)]>
        // @returns ListTag
        // @plugin Depenizen, LuckPerms
        // @description
        // Returns the list of groups in the track.
        // If a player input is specified, limits to only the groups that the player is in.
        // -->
        if (attribute.startsWith("groups")) {
            ListTag groups = new ListTag();
            if (attribute.hasParam()) {
                PlayerTag player = attribute.paramAsType(PlayerTag.class);
                if (player == null) {
                    attribute.echoError("Invalid player input for 'group' tag.");
                    return null;
                }
                User user = LuckPermsBridge.luckPermsInstance.getUserManager().getUser(player.getUUID());
                List<String> trackGroups = track.getGroups();
                List<String> memberGroups = user.resolveInheritedNodes(QueryOptions.nonContextual()).stream()
                        .filter(NodeType.INHERITANCE::matches)
                        .map(NodeType.INHERITANCE::cast)
                        .filter(net.luckperms.api.node.Node::getValue)
                        .filter(n -> trackGroups.contains(n.getGroupName()))
                        .map(InheritanceNode::getGroupName).collect(Collectors.toList());
                groups.addAll(memberGroups);
            }
            else {
                groups.addAll(track.getGroups());
            }
            return groups.getObjectAttribute(attribute.fulfill(1));
        }

        return new ElementTag(identify()).getObjectAttribute(attribute);

    }
}
