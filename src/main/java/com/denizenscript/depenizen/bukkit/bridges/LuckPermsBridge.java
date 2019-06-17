package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.extensions.luckperms.LuckPermsPlayerExtension;
import com.denizenscript.depenizen.bukkit.objects.luckperms.LuckPermsTrack;
import com.denizenscript.depenizen.bukkit.Bridge;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.Track;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.ObjectFetcher;
import net.aufdemrand.denizencore.objects.TagRunnable;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.objects.properties.PropertyParser;
import net.aufdemrand.denizencore.tags.Attribute;
import net.aufdemrand.denizencore.tags.ReplaceableTagEvent;
import net.aufdemrand.denizencore.tags.TagManager;

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
        PropertyParser.registerProperty(LuckPermsPlayerExtension.class, dPlayer.class);
    }

    public void tagEvent(ReplaceableTagEvent event) {
        Attribute attribute = event.getAttributes().fulfill(1);

        // <--[tag]
        // @attribute <luckperms.list_tracks>
        // @returns dList(luckpermstrack)
        // @description
        // Returns a list of all tracks.
        // @Plugin DepenizenBukkit, LuckPerms
        // -->
        if (attribute.startsWith("list_tracks")) {
            dList tracks = new dList();
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
        // @Plugin DepenizenBukkit, LuckPerms
        // -->
        if (attribute.startsWith("track")) {
            if (attribute.hasContext(1)) {
                LuckPermsApi api = LuckPerms.getApi();
                event.setReplacedObject(new LuckPermsTrack(api.getTrack(attribute.getContext(1))).getObjectAttribute(attribute.fulfill(1)));
            }
        }
    }
}
