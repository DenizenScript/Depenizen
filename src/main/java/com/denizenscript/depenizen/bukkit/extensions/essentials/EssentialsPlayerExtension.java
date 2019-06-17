package com.denizenscript.depenizen.bukkit.extensions.essentials;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizencore.objects.*;
import net.aufdemrand.denizencore.tags.Attribute;
import com.denizenscript.depenizen.bukkit.extensions.dObjectExtension;
import com.denizenscript.depenizen.bukkit.bridges.EssentialsBridge;

import java.util.GregorianCalendar;

public class EssentialsPlayerExtension extends dObjectExtension {

    public static boolean describes(dObject object) {
        return object instanceof dPlayer && ((dPlayer) object).isOnline();
    }

    public static EssentialsPlayerExtension getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new EssentialsPlayerExtension((dPlayer) object);
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

    private EssentialsPlayerExtension(dPlayer player) {
        this.player = player;
    }

    public User getUser() {
        return ((Essentials) EssentialsBridge.instance.plugin).getUser(player.getOfflinePlayer().getUniqueId());
    }

    dPlayer player;

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <p@player.god_mode>
        // @returns Element(Boolean)
        // @mechanism dPlayer.god_mode
        // @description
        // Returns whether the player is currently in god mode.
        // @Plugin DepenizenBukkit, Essentials
        // -->
        if (attribute.startsWith("god_mode")) {
            return new Element(getUser().isGodModeEnabled()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <p@player.has_home>
        // @returns Element(Boolean)
        // @description
        // Returns whether the player has set at least one home.
        // @Plugin DepenizenBukkit, Essentials
        // -->
        if (attribute.startsWith("has_home")) {
            return new Element(getUser().hasHome()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <p@player.is_afk>
        // @returns Element(Boolean)
        // @mechanism dPlayer.is_afk
        // @description
        // Returns whether the player is AFK.
        // @Plugin DepenizenBukkit, Essentials
        // -->
        if (attribute.startsWith("is_afk")) {
            return new Element(getUser().isAfk()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <p@player.is_muted>
        // @returns Element(Boolean)
        // @mechanism dPlayer.is_muted
        // @description
        // Returns whether the player is muted.
        // @Plugin DepenizenBukkit, Essentials
        // -->
        if (attribute.startsWith("is_muted")) {
            return new Element(getUser().isMuted()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <p@player.is_vanished>
        // @returns Element(Boolean)
        // @mechanism dPlayer.is_vanished
        // @description
        // Returns whether the player is vanished.
        // @Plugin DepenizenBukkit, Essentials
        // -->
        if (attribute.startsWith("is_vanished")) {
            return new Element(getUser().isVanished()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <p@player.home_list>
        // @returns dList(Element/dLocation)
        // @description
        // Returns a list of the homes of the player, in the format "HomeName/l@x,y,z,world".
        // @Plugin DepenizenBukkit, Essentials
        // -->
        if (attribute.startsWith("home_list")) {
            dList homes = new dList();
            for (String home : getUser().getHomes()) {
                try {
                    homes.add(home + "/" + new dLocation(getUser().getHome(home)).identifySimple());
                }
                catch (Exception e) {
                    if (!attribute.hasAlternative()) {
                        dB.echoError(e);
                    }
                }
            }
            return homes.getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <p@player.home_location_list>
        // @returns dList(dLocation)
        // @description
        // Returns a list of the locations of homes of the player.
        // @Plugin DepenizenBukkit, Essentials
        // -->
        if (attribute.startsWith("home_location_list")) {
            dList homes = new dList();
            for (String home : getUser().getHomes()) {
                try {
                    homes.add(new dLocation(getUser().getHome(home)).identifySimple());
                }
                catch (Exception e) {
                    if (!attribute.hasAlternative()) {
                        dB.echoError(e);
                    }
                }
            }
            return homes.getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <p@player.ignored_players>
        // @returns dList(dPlayer)
        // @description
        // Returns a list of the ignored players of the player.
        // @Plugin DepenizenBukkit, Essentials
        // -->
        if (attribute.startsWith("ignored_players")) {
            dList players = new dList();
            Essentials ess = (Essentials) EssentialsBridge.instance.plugin;
            for (String player : getUser()._getIgnoredPlayers()) {
                try {
                    players.add(new dPlayer(ess.getOfflineUser(player).getBase()).identifySimple());
                }
                catch (Exception e) {
                    if (!attribute.hasAlternative()) {
                        dB.echoError(e);
                    }
                }
            }
            return players.getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <p@player.home_name_list>
        // @returns dList(Element)
        // @description
        // Returns a list of the names of homes of the player.
        // @Plugin DepenizenBukkit, Essentials
        // -->
        if (attribute.startsWith("home_name_list")) {
            return new dList(getUser().getHomes()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <p@player.mail_list>
        // @returns dList(Element)
        // @description
        // Returns a list of mail the player currently has.
        // @Plugin DepenizenBukkit, Essentials
        // -->
        if (attribute.startsWith("mail_list")) {
            return new dList(getUser().getMails()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <p@player.mute_timeout>
        // @returns Duration
        // @description
        // Returns how much time is left until the player is muted.
        // @Plugin DepenizenBukkit, Essentials
        // -->
        if (attribute.startsWith("mute_timeout")) {
            return new Duration((int) (getUser().getMuteTimeout() - new GregorianCalendar().getTimeInMillis()) / 1000)
                    .getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <p@player.socialspy>
        // @returns Element(Boolean)
        // @mechanism dPlayer.socialspy
        // @description
        // Returns whether the player has SocialSpy enabled.
        // @Plugin DepenizenBukkit, Essentials
        // -->
        if (attribute.startsWith("socialspy")) {
            return new Element(getUser().isSocialSpyEnabled()).getAttribute(attribute.fulfill(1));
        }

        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {

        // <--[mechanism]
        // @object dPlayer
        // @name is_afk
        // @input Element(Boolean)
        // @description
        // Sets whether the player is marked as AFK.
        // @tags
        // <p@player.is_afk>
        // @Plugin DepenizenBukkit, Essentials
        // -->
        if ((mechanism.matches("afk") || mechanism.matches("is_afk")) && mechanism.requireBoolean()) {
            getUser().setAfk(mechanism.getValue().asBoolean());
        }

        // <--[mechanism]
        // @object dPlayer
        // @name god_mode
        // @input Element(Boolean)
        // @description
        // Sets whether the player has god mode enabled.
        // @tags
        // <p@player.god_mode>
        // @Plugin DepenizenBukkit, Essentials
        // -->
        if (mechanism.matches("god_mode") && mechanism.requireBoolean()) {
            getUser().setGodModeEnabled(mechanism.getValue().asBoolean());
        }

        // <--[mechanism]
        // @object dPlayer
        // @name is_muted
        // @input Element(Boolean)(|Duration)
        // @description
        // Sets whether the player is muted. Optionally, you may also
        // specify a duration to set how long they are muted for.
        // @tags
        // <p@player.is_muted>
        // <p@player.mute_timeout>
        // @Plugin DepenizenBukkit, Essentials
        // -->
        if ((mechanism.matches("muted") || mechanism.matches("is_muted")) && mechanism.requireBoolean()) {
            if (mechanism.getValue().asString().length() > 0) {
                dList split = mechanism.valueAsType(dList.class);
                if (split.size() > 0 && new Element(split.get(0)).isBoolean()) {
                    getUser().setMuted(new Element(split.get(0)).asBoolean());
                    if (split.size() > 1 && Duration.matches(split.get(1))) {
                        getUser().setMuteTimeout(new GregorianCalendar().getTimeInMillis()
                                + Duration.valueOf(split.get(1)).getMillis());
                    }
                }
                else {
                    dB.echoError("'" + split.get(0) + "' is not a valid boolean!");
                }
            }
        }

        // <--[mechanism]
        // @object dPlayer
        // @name socialspy
        // @input Element(Boolean)
        // @description
        // Sets whether the player has SocialSpy enabled.
        // @tags
        // <p@player.socialspy>
        // @Plugin DepenizenBukkit, Essentials
        // -->
        if (mechanism.matches("socialspy") && mechanism.requireBoolean()) {
            getUser().setSocialSpyEnabled(mechanism.getValue().asBoolean());
        }

        // <--[mechanism]
        // @object dPlayer
        // @name vanish
        // @input Element(Boolean)
        // @description
        // Sets whether the player has vanish enabled.
        // @tags
        // <p@player.is_vanished>
        // @Plugin DepenizenBukkit, Essentials
        // -->
        if (mechanism.matches("vanish") && mechanism.requireBoolean()) {
            getUser().setVanished(mechanism.getValue().asBoolean());
        }


        // <--[mechanism]
        // @object dPlayer
        // @name essentials_ignore
        // @input Element(dPlayer)(|Boolean)
        // @description
        // Sets whether the player should ignore anther player. If no boolean is provided, it is by default true.
        // @tags
        // <p@player.ignored_players>
        // @Plugin DepenizenBukkit, Essentials
        // -->
        if (mechanism.matches("essentials_ignore")) {
            Essentials ess = (Essentials) EssentialsBridge.instance.plugin;
            if (mechanism.getValue().asString().contains("|")) {
                int split = mechanism.getValue().asString().indexOf("|");
                int len = mechanism.getValue().asString().length();
                String after = mechanism.getValue().asString().substring(split + 1, len);
                String before = mechanism.getValue().asString().substring(0, split - 1);
                getUser().setIgnoredPlayer(ess.getUser(new Element(before).asType(dPlayer.class, mechanism.context).getOfflinePlayer().getUniqueId()), new Element(after).asBoolean());
            }
            else {
                getUser().setIgnoredPlayer(ess.getUser(mechanism.valueAsType(dPlayer.class).getOfflinePlayer().getUniqueId()), true);
            }
        }


    }
}
