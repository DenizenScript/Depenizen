package com.denizenscript.depenizen.bukkit.extensions.luckperms;

import com.denizenscript.depenizen.bukkit.extensions.dObjectExtension;
import com.denizenscript.depenizen.bukkit.objects.luckperms.LuckPermsTrack;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.Track;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.Mechanism;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;

public class LuckPermsPlayerExtension extends dObjectExtension {

    public static boolean describes(dObject object) {
        return object instanceof dPlayer;
    }

    public static LuckPermsPlayerExtension getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new LuckPermsPlayerExtension((dPlayer) object);
        }
    }


    ///////////////////
    // Instance Fields and Methods
    /////////////

    public static final String[] handledTags = new String[]{
            "luckperms_tracks"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    private LuckPermsPlayerExtension(dPlayer player) {
        this.player = player;
    }

    dPlayer player;

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <p@player.luckperms_tracks>
        // @returns dList(luckpermstrack)
        // @description
        // Returns a list of tracks the player is in.
        // @Plugin DepenizenBukkit, LuckPerms
        // -->
        if (attribute.startsWith("luckperms_tracks")) {
            dList tracks = new dList();
            LuckPermsApi api = LuckPerms.getApi();
            for (Track track : api.getTracks()) {
                for (String group : track.getGroups()) {
                    if (api.getUser(player.getOfflinePlayer().getUniqueId()).inheritsGroup(api.getGroup(group))) {
                        if (!tracks.contains(new LuckPermsTrack(track).identify())) {
                            tracks.add(new LuckPermsTrack(track).identify());
                        }
                    }
                }
            }
            return tracks.getAttribute(attribute.fulfill(1));
        }


        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {
        Element value = mechanism.getValue();
    }
}

