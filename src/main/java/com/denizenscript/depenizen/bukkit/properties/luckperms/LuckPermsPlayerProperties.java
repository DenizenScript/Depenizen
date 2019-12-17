package com.denizenscript.depenizen.bukkit.properties.luckperms;

import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.depenizen.bukkit.objects.luckperms.LuckPermsTrackTag;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.Track;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ListTag;
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
        return object instanceof PlayerTag;
    }

    public static LuckPermsPlayerProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new LuckPermsPlayerProperties((PlayerTag) object);
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

    private LuckPermsPlayerProperties(PlayerTag player) {
        this.player = player;
    }

    PlayerTag player;

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <PlayerTag.luckperms_tracks>
        // @returns ListTag(luckpermstrack)
        // @plugin Depenizen, LuckPerms
        // @description
        // Returns a list of tracks the player is in.
        // -->
        if (attribute.startsWith("luckperms_tracks")) {
            ListTag tracks = new ListTag();
            LuckPermsApi api = LuckPerms.getApi();
            for (Track track : api.getTracks()) {
                for (String group : track.getGroups()) {
                    if (api.getUser(player.getOfflinePlayer().getUniqueId()).inheritsGroup(api.getGroup(group))) {
                        if (!tracks.contains(new LuckPermsTrackTag(track).identify())) {
                            tracks.addObject(new LuckPermsTrackTag(track));
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

