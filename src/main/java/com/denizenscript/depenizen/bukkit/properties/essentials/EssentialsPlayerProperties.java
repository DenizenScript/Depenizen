package com.denizenscript.depenizen.bukkit.properties.essentials;

import com.denizenscript.denizencore.objects.*;
import com.denizenscript.denizencore.objects.core.DurationTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.debugging.Debug;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.depenizen.bukkit.bridges.EssentialsBridge;

import java.util.GregorianCalendar;

public class EssentialsPlayerProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "EssentialsPlayer";
    }

    public static boolean describes(ObjectTag object) {
        return object instanceof PlayerTag && ((PlayerTag) object).isOnline();
    }

    public static EssentialsPlayerProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new EssentialsPlayerProperties((PlayerTag) object);
        }
    }

    ///////////////////
    // Instance Fields and Methods
    /////////////

    public static final String[] handledTags = new String[] {
            "god_mode", "has_home", "is_afk", "is_muted", "is_vanished", "home_list", "home_location_list",
            "ignored_players", "home_name_list", "mail_list", "mute_timout", "socialspy"
    };

    public static final String[] handledMechs = new String[] {
            "is_afk", "god_mode", "is_muted", "socialspy", "vanish", "essentials_ignore"
    };

    private EssentialsPlayerProperties(PlayerTag player) {
        this.player = player;
    }

    public User getUser() {
        return ((Essentials) EssentialsBridge.instance.plugin).getUser(player.getOfflinePlayer().getUniqueId());
    }

    PlayerTag player;

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <PlayerTag.god_mode>
        // @returns ElementTag(Boolean)
        // @mechanism PlayerTag.god_mode
        // @description
        // Returns whether the player is currently in god mode.
        // @Plugin Depenizen, Essentials
        // -->
        if (attribute.startsWith("god_mode")) {
            return new ElementTag(getUser().isGodModeEnabled()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <PlayerTag.has_home>
        // @returns ElementTag(Boolean)
        // @description
        // Returns whether the player has set at least one home.
        // @Plugin Depenizen, Essentials
        // -->
        if (attribute.startsWith("has_home")) {
            return new ElementTag(getUser().hasHome()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <PlayerTag.is_afk>
        // @returns ElementTag(Boolean)
        // @mechanism PlayerTag.is_afk
        // @description
        // Returns whether the player is AFK.
        // @Plugin Depenizen, Essentials
        // -->
        if (attribute.startsWith("is_afk")) {
            return new ElementTag(getUser().isAfk()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <PlayerTag.is_muted>
        // @returns ElementTag(Boolean)
        // @mechanism PlayerTag.is_muted
        // @description
        // Returns whether the player is muted.
        // @Plugin Depenizen, Essentials
        // -->
        if (attribute.startsWith("is_muted")) {
            return new ElementTag(getUser().isMuted()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <PlayerTag.is_vanished>
        // @returns ElementTag(Boolean)
        // @mechanism PlayerTag.is_vanished
        // @description
        // Returns whether the player is vanished.
        // @Plugin Depenizen, Essentials
        // -->
        if (attribute.startsWith("is_vanished")) {
            return new ElementTag(getUser().isVanished()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <PlayerTag.home_list>
        // @returns ListTag(Element/dLocation)
        // @description
        // Returns a list of the homes of the player, in the format "HomeName/l@x,y,z,world".
        // @Plugin Depenizen, Essentials
        // -->
        if (attribute.startsWith("home_list")) {
            ListTag homes = new ListTag();
            for (String home : getUser().getHomes()) {
                try {
                    homes.add(home + "/" + new LocationTag(getUser().getHome(home)).identifySimple());
                }
                catch (Exception e) {
                    if (!attribute.hasAlternative()) {
                        Debug.echoError(e);
                    }
                }
            }
            return homes.getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <PlayerTag.home_location_list>
        // @returns ListTag(LocationTag)
        // @description
        // Returns a list of the locations of homes of the player.
        // @Plugin Depenizen, Essentials
        // -->
        if (attribute.startsWith("home_location_list")) {
            ListTag homes = new ListTag();
            for (String home : getUser().getHomes()) {
                try {
                    homes.add(new LocationTag(getUser().getHome(home)).identifySimple());
                }
                catch (Exception e) {
                    if (!attribute.hasAlternative()) {
                        Debug.echoError(e);
                    }
                }
            }
            return homes.getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <PlayerTag.ignored_players>
        // @returns ListTag(PlayerTag)
        // @description
        // Returns a list of the ignored players of the player.
        // @Plugin Depenizen, Essentials
        // -->
        if (attribute.startsWith("ignored_players")) {
            ListTag players = new ListTag();
            Essentials ess = (Essentials) EssentialsBridge.instance.plugin;
            for (String player : getUser()._getIgnoredPlayers()) {
                try {
                    players.add(new PlayerTag(ess.getOfflineUser(player).getBase()).identifySimple());
                }
                catch (Exception e) {
                    if (!attribute.hasAlternative()) {
                        Debug.echoError(e);
                    }
                }
            }
            return players.getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <PlayerTag.home_name_list>
        // @returns ListTag(Element)
        // @description
        // Returns a list of the names of homes of the player.
        // @Plugin Depenizen, Essentials
        // -->
        if (attribute.startsWith("home_name_list")) {
            return new ListTag(getUser().getHomes()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <PlayerTag.mail_list>
        // @returns ListTag(Element)
        // @description
        // Returns a list of mail the player currently has.
        // @Plugin Depenizen, Essentials
        // -->
        if (attribute.startsWith("mail_list")) {
            return new ListTag(getUser().getMails()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <PlayerTag.mute_timeout>
        // @returns DurationTag
        // @description
        // Returns how much time is left until the player is muted.
        // @Plugin Depenizen, Essentials
        // -->
        if (attribute.startsWith("mute_timeout")) {
            return new DurationTag((int) (getUser().getMuteTimeout() - new GregorianCalendar().getTimeInMillis()) / 1000)
                    .getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <PlayerTag.socialspy>
        // @returns ElementTag(Boolean)
        // @mechanism PlayerTag.socialspy
        // @description
        // Returns whether the player has SocialSpy enabled.
        // @Plugin Depenizen, Essentials
        // -->
        if (attribute.startsWith("socialspy")) {
            return new ElementTag(getUser().isSocialSpyEnabled()).getAttribute(attribute.fulfill(1));
        }

        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {

        // <--[mechanism]
        // @object PlayerTag
        // @name is_afk
        // @input Element(Boolean)
        // @description
        // Sets whether the player is marked as AFK.
        // @tags
        // <PlayerTag.is_afk>
        // @Plugin Depenizen, Essentials
        // -->
        if ((mechanism.matches("afk") || mechanism.matches("is_afk")) && mechanism.requireBoolean()) {
            getUser().setAfk(mechanism.getValue().asBoolean());
        }

        // <--[mechanism]
        // @object PlayerTag
        // @name god_mode
        // @input Element(Boolean)
        // @description
        // Sets whether the player has god mode enabled.
        // @tags
        // <PlayerTag.god_mode>
        // @Plugin Depenizen, Essentials
        // -->
        if (mechanism.matches("god_mode") && mechanism.requireBoolean()) {
            getUser().setGodModeEnabled(mechanism.getValue().asBoolean());
        }

        // <--[mechanism]
        // @object PlayerTag
        // @name is_muted
        // @input Element(Boolean)(|Duration)
        // @description
        // Sets whether the player is muted. Optionally, you may also
        // specify a duration to set how long they are muted for.
        // @tags
        // <PlayerTag.is_muted>
        // <PlayerTag.mute_timeout>
        // @Plugin Depenizen, Essentials
        // -->
        if ((mechanism.matches("muted") || mechanism.matches("is_muted")) && mechanism.requireBoolean()) {
            if (mechanism.getValue().asString().length() > 0) {
                ListTag split = mechanism.valueAsType(ListTag.class);
                if (split.size() > 0 && new ElementTag(split.get(0)).isBoolean()) {
                    getUser().setMuted(new ElementTag(split.get(0)).asBoolean());
                    if (split.size() > 1 && DurationTag.matches(split.get(1))) {
                        getUser().setMuteTimeout(new GregorianCalendar().getTimeInMillis()
                                + DurationTag.valueOf(split.get(1)).getMillis());
                    }
                }
                else {
                    Debug.echoError("'" + split.get(0) + "' is not a valid boolean!");
                }
            }
        }

        // <--[mechanism]
        // @object PlayerTag
        // @name socialspy
        // @input Element(Boolean)
        // @description
        // Sets whether the player has SocialSpy enabled.
        // @tags
        // <PlayerTag.socialspy>
        // @Plugin Depenizen, Essentials
        // -->
        if (mechanism.matches("socialspy") && mechanism.requireBoolean()) {
            getUser().setSocialSpyEnabled(mechanism.getValue().asBoolean());
        }

        // <--[mechanism]
        // @object PlayerTag
        // @name vanish
        // @input Element(Boolean)
        // @description
        // Sets whether the player has vanish enabled.
        // @tags
        // <PlayerTag.is_vanished>
        // @Plugin Depenizen, Essentials
        // -->
        if (mechanism.matches("vanish") && mechanism.requireBoolean()) {
            getUser().setVanished(mechanism.getValue().asBoolean());
        }

        // <--[mechanism]
        // @object PlayerTag
        // @name essentials_ignore
        // @input Element(dPlayer)(|Boolean)
        // @description
        // Sets whether the player should ignore anther player. If no boolean is provided, it is by default true.
        // @tags
        // <PlayerTag.ignored_players>
        // @Plugin Depenizen, Essentials
        // -->
        if (mechanism.matches("essentials_ignore")) {
            Essentials ess = (Essentials) EssentialsBridge.instance.plugin;
            if (mechanism.getValue().asString().contains("|")) {
                int split = mechanism.getValue().asString().indexOf("|");
                int len = mechanism.getValue().asString().length();
                String after = mechanism.getValue().asString().substring(split + 1, len);
                String before = mechanism.getValue().asString().substring(0, split - 1);
                getUser().setIgnoredPlayer(ess.getUser(new ElementTag(before).asType(PlayerTag.class, mechanism.context).getOfflinePlayer().getUniqueId()), new ElementTag(after).asBoolean());
            }
            else {
                getUser().setIgnoredPlayer(ess.getUser(mechanism.valueAsType(PlayerTag.class).getOfflinePlayer().getUniqueId()), true);
            }
        }

    }
}