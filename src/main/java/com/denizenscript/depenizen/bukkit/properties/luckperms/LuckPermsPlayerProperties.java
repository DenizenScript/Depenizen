package com.denizenscript.depenizen.bukkit.properties.luckperms;

import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.depenizen.bukkit.objects.luckperms.LuckPermsTrack;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.Track;
import com.denizenscript.denizen.objects.dPlayer;
import com.denizenscript.denizencore.objects.ListTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;

public class LuckPermsPlayerProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "LuckPermsPlayer";
    }

    public static boolean describes(ObjectTag object) {
        return object instanceof dPlayer;
    }

    public static LuckPermsPlayerProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new LuckPermsPlayerProperties((dPlayer) object);
        }
    }

    ///////////////////
    // Instance Fields and Methods
    /////////////

    public static final String[] handledTags = new String[] {
            "luckperms_tracks"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    private LuckPermsPlayerProperties(dPlayer player) {
        this.player = player;
    }

    dPlayer player;

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <p@player.luckperms_tracks>
        // @returns ListTag(luckpermstrack)
        // @description
        // Returns a list of tracks the player is in.
        // @Plugin Depenizen, LuckPerms
        // -->
        if (attribute.startsWith("luckperms_tracks")) {
            ListTag tracks = new ListTag();
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
    }
}

