package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.properties.luckperms.LuckPermsPlayerProperties;
import com.denizenscript.depenizen.bukkit.objects.luckperms.LuckPermsTrack;
import com.denizenscript.depenizen.bukkit.Bridge;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.Track;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.ObjectFetcher;
import com.denizenscript.denizencore.objects.TagRunnable;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.ReplaceableTagEvent;
import com.denizenscript.denizencore.tags.TagManager;

public class LuckPermsBridge extends Bridge {

    @Override
    public void init() {
        TagManager.registerTagHandler(new TagRunnable.RootForm() {
            @Override
            public void run(ReplaceableTagEvent event) {
                tagEvent(event);
            }
        }, "luckperms");
        ObjectFetcher.registerWithObjectFetcher(LuckPermsTrack.class);
        PropertyParser.registerProperty(LuckPermsPlayerProperties.class, PlayerTag.class);
    }

    public void tagEvent(ReplaceableTagEvent event) {
        Attribute attribute = event.getAttributes().fulfill(1);

        // <--[tag]
        // @attribute <luckperms.list_tracks>
        // @returns ListTag(luckpermstrack)
        // @description
        // Returns a list of all tracks.
        // @Plugin Depenizen, LuckPerms
        // -->
        if (attribute.startsWith("list_tracks")) {
            ListTag tracks = new ListTag();
            LuckPermsApi api = LuckPerms.getApi();
            for (Track track : api.getTracks()) {
                tracks.add(new LuckPermsTrack(track).identify());
            }
            event.setReplacedObject(tracks.getObjectAttribute(attribute.fulfill(1)));
        }

        // <--[tag]
        // @attribute <luckperms.track[<track name>]>
        // @returns luckpermstrack
        // @description
        // Returns the track from the name given.
        // @Plugin Depenizen, LuckPerms
        // -->
        if (attribute.startsWith("track")) {
            if (attribute.hasContext(1)) {
                LuckPermsApi api = LuckPerms.getApi();
                event.setReplacedObject(new LuckPermsTrack(api.getTrack(attribute.getContext(1))).getObjectAttribute(attribute.fulfill(1)));
            }
        }
    }
}
