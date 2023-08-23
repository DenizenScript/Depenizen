package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizencore.objects.ObjectFetcher;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.tags.PseudoObjectTagBase;
import com.denizenscript.denizencore.tags.TagManager;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.objects.luckperms.LuckPermsGroupTag;
import com.denizenscript.depenizen.bukkit.objects.luckperms.LuckPermsTrackTag;
import com.denizenscript.depenizen.bukkit.properties.luckperms.LuckPermsPlayerExtensions;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;

public class LuckPermsBridge extends Bridge {

    static class LuckPermsTagBase extends PseudoObjectTagBase<LuckPermsTagBase> {

        public static LuckPermsTagBase instance;

        public LuckPermsTagBase() {
            instance = this;
            TagManager.registerStaticTagBaseHandler(LuckPermsTagBase.class, "luckperms", (t) -> instance);
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
            tagProcessor.registerTag(LuckPermsTrackTag.class, LuckPermsTrackTag.class, "track", (attribute, object, track) -> {
                return track;
            });

            // <--[tag]
            // @attribute <luckperms.list_groups>
            // @returns ListTag(LuckPermsGroupTag)
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
        ObjectFetcher.registerWithObjectFetcher(LuckPermsGroupTag.class, LuckPermsGroupTag.tagProcessor);
        ObjectFetcher.registerWithObjectFetcher(LuckPermsTrackTag.class, LuckPermsTrackTag.tagProcessor);
        new LuckPermsTagBase();

        // <--[tag]
        // @attribute <luckperms_group[<name>]>
        // @returns LuckPermsGroupTag
        // @plugin Depenizen, LuckPerms
        // @description
        // Returns the luckperms group tag with the given name.
        // -->
        TagManager.registerTagHandler(LuckPermsGroupTag.class, LuckPermsGroupTag.class, "luckperms_group", (attribute, param) -> param);
    }
}
