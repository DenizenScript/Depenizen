package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.extensions.luckperms.LuckPermsPlayerExtension;
import com.denizenscript.depenizen.bukkit.objects.luckperms.LuckPermsTrack;
import com.denizenscript.depenizen.bukkit.support.Support;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.Track;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.tags.Attribute;
import net.aufdemrand.denizencore.tags.TagContext;

public class LuckPermsSupport extends Support {

    public LuckPermsSupport() {
        registerAdditionalTags("luckperms");
        registerObjects(LuckPermsTrack.class);
        registerProperty(LuckPermsPlayerExtension.class, dPlayer.class);
    }

    @Override
    public String additionalTags(Attribute attribute, TagContext tagContext) {
        if (attribute == null) {
            return null;
        }

        if (attribute.startsWith("luckperms")) {

            attribute = attribute.fulfill(1);

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
                return tracks.getAttribute(attribute.fulfill(1));
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
                    return new LuckPermsTrack(api.getTrack(attribute.getContext(1))).getAttribute(attribute.fulfill(1));
                }
            }
        }

        return null;
    }
}
