package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.properties.luckperms.LuckPermsPlayerProperties;
import com.denizenscript.depenizen.bukkit.objects.luckperms.LuckPermsTrackTag;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.ObjectFetcher;
import com.denizenscript.denizencore.tags.TagRunnable;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.ReplaceableTagEvent;
import com.denizenscript.denizencore.tags.TagManager;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.track.Track;

public class LuckPermsBridge extends Bridge {

    public static LuckPerms luckPermsInstance;

    @Override
    public void init() {
        luckPermsInstance = LuckPermsProvider.get();
        TagManager.registerTagHandler(new TagRunnable.RootForm() {
            @Override
            public void run(ReplaceableTagEvent event) {
                tagEvent(event);
            }
        }, "luckperms");
        ObjectFetcher.registerWithObjectFetcher(LuckPermsTrackTag.class);
        PropertyParser.registerProperty(LuckPermsPlayerProperties.class, PlayerTag.class);
    }

    public void tagEvent(ReplaceableTagEvent event) {
        Attribute attribute = event.getAttributes().fulfill(1);

        // <--[tag]
        // @attribute <luckperms.list_tracks>
        // @returns ListTag(luckpermstrack)
        // @plugin Depenizen, LuckPerms
        // @description
        // Returns a list of all tracks.
        // -->
        if (attribute.startsWith("list_tracks")) {
            ListTag tracks = new ListTag();
            for (Track track : luckPermsInstance.getTrackManager().getLoadedTracks()) {
                tracks.addObject(new LuckPermsTrackTag(track));
            }
            event.setReplacedObject(tracks.getObjectAttribute(attribute.fulfill(1)));
        }

        // <--[tag]
        // @attribute <luckperms.track[<track name>]>
        // @returns luckpermstrack
        // @plugin Depenizen, LuckPerms
        // @description
        // Returns the track from the name given.
        // -->
        if (attribute.startsWith("track")) {
            if (attribute.hasContext(1)) {
                event.setReplacedObject(LuckPermsTrackTag.valueOf(attribute.getContext(1)).getObjectAttribute(attribute.fulfill(1)));
            }
        }
    }
}
