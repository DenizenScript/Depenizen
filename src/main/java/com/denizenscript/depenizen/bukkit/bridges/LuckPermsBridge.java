package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.tags.*;
import com.denizenscript.depenizen.bukkit.objects.luckperms.LuckPermsGroupTag;
import com.denizenscript.depenizen.bukkit.properties.luckperms.LuckPermsPlayerExtensions;
import com.denizenscript.depenizen.bukkit.objects.luckperms.LuckPermsTrackTag;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.denizencore.objects.ObjectFetcher;
import com.denizenscript.denizencore.objects.core.ListTag;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.track.Track;

public class LuckPermsBridge extends Bridge {

    static class LuckPermsBridgeTags extends PseudoObjectTagBase<LuckPermsBridgeTags> {

        public static LuckPermsBridgeTags instance;

        public LuckPermsBridgeTags() {
            instance = this;
            TagManager.registerStaticTagBaseHandler(LuckPermsBridgeTags.class, "luckperms", (t) -> instance);
        }

        public void register() {

            // <--[tag]
            // @attribute <luckperms.list_tracks>
            // @returns ListTag(LuckPermsTrackTag)
            // @plugin Depenizen, LuckPerms
            // @description
            // Returns a list of all tracks.
            // -->
            tagProcessor.registerTag(ListTag.class, "list_tracks", (attribute, object) -> {
                return new ListTag(luckPermsInstance.getTrackManager().getLoadedTracks(), LuckPermsTrackTag::new);
            });

            // <--[tag]
            // @attribute <luckperms.track[<track_name>]>
            // @returns LuckPermsTrackTag
            // @plugin Depenizen, LuckPerms
            // @description
            // Returns the track from the name given.
            // -->
            tagProcessor.registerTag(LuckPermsTrackTag.class, ElementTag.class, "track", (attribute, object, name) -> {
                Track track = luckPermsInstance.getTrackManager().getTrack(name.toString());
                return track != null ? new LuckPermsTrackTag(track) : null;
            });

            // <--[tag]
            // @attribute <luckperms.list_groups>
            // @returns ListTag(LuckPermsGrupTag)
            // @plugin Depenizen, LuckPerms
            // @description
            // Returns a list of all luckperms groups.
            // -->
            tagProcessor.registerTag(ListTag.class, "list_groups", (attribute, object) -> {
                return new ListTag(luckPermsInstance.getGroupManager().getLoadedGroups(), LuckPermsGroupTag::new);
            });
        }

    }

    public static LuckPerms luckPermsInstance;

    @Override
    public void init() {
        luckPermsInstance = LuckPermsProvider.get();
        LuckPermsPlayerExtensions.register();
        ObjectFetcher.registerWithObjectFetcher(LuckPermsGroupTag.class, LuckPermsGroupTag.tagProcessor).generateBaseTag();
        ObjectFetcher.registerWithObjectFetcher(LuckPermsTrackTag.class, LuckPermsTrackTag.tagProcessor).generateBaseTag();
        new LuckPermsBridgeTags();
    }
}
