package com.denizenscript.depenizen.bukkit.extensions.essentials;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizencore.objects.*;
import net.aufdemrand.denizencore.tags.Attribute;
import com.denizenscript.depenizen.bukkit.extensions.dObjectExtension;
import com.denizenscript.depenizen.bukkit.support.Support;
import com.denizenscript.depenizen.bukkit.support.plugins.EssentialsSupport;
import org.bukkit.entity.Player;

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

    private EssentialsPlayerExtension(dPlayer player) {
        // TODO: UUID
        Essentials essentials = Support.getPlugin(EssentialsSupport.class);
        this.essUser = essentials.getUser(player.getOfflinePlayer().getUniqueId());
    }

    User essUser = null;

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
            return new Element(essUser.isGodModeEnabled()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <p@player.has_home>
        // @returns Element(Boolean)
        // @description
        // Returns whether the player has set at least one home.
        // @Plugin DepenizenBukkit, Essentials
        // -->
        if (attribute.startsWith("has_home")) {
            return new Element(essUser.hasHome()).getAttribute(attribute.fulfill(1));
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
            return new Element(essUser.isAfk()).getAttribute(attribute.fulfill(1));
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
            return new Element(essUser.isMuted()).getAttribute(attribute.fulfill(1));
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
            return new Element(essUser.isVanished()).getAttribute(attribute.fulfill(1));
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
            for (String home : essUser.getHomes()) {
                try {
                    homes.add(home + "/" + new dLocation(essUser.getHome(home)).identifySimple());
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
            for (String home : essUser.getHomes()) {
                try {
                    homes.add(new dLocation(essUser.getHome(home)).identifySimple());
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
            Essentials essentials = Support.getPlugin(EssentialsSupport.class);
            for (String player : essUser._getIgnoredPlayers()) {
                try {
                    players.add(new dPlayer(essentials.getOfflineUser(player).getBase()).identifySimple());
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
            return new dList(essUser.getHomes()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <p@player.mail_list>
        // @returns dList(Element)
        // @description
        // Returns a list of mail the player currently has.
        // @Plugin DepenizenBukkit, Essentials
        // -->
        if (attribute.startsWith("mail_list")) {
            return new dList(essUser.getMails()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <p@player.mute_timeout>
        // @returns Duration
        // @description
        // Returns how much time is left until the player is muted.
        // @Plugin DepenizenBukkit, Essentials
        // -->
        if (attribute.startsWith("mute_timeout")) {
            return new Duration((int) (essUser.getMuteTimeout() - new GregorianCalendar().getTimeInMillis()) / 1000)
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
            return new Element(essUser.isSocialSpyEnabled()).getAttribute(attribute.fulfill(1));
        }

        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {

        Element value = mechanism.getValue();

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
            essUser.setAfk(value.asBoolean());
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
            essUser.setGodModeEnabled(value.asBoolean());
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
            if (value.asString().length() > 0) {
                String[] split = value.asString().split("[\\|" + dList.internal_escape + "]", 2);
                if (split.length > 0 && new Element(split[0]).isBoolean()) {
                    essUser.setMuted(new Element(split[0]).asBoolean());
                    if (split.length > 1 && Duration.matches(split[1])) {
                        essUser.setMuteTimeout(new GregorianCalendar().getTimeInMillis()
                                + Duration.valueOf(split[1]).getMillis());
                    }
                }
                else {
                    dB.echoError("'" + split[0] + "' is not a valid boolean!");
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
            essUser.setSocialSpyEnabled(value.asBoolean());
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
            essUser.setVanished(value.asBoolean());
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
            Essentials essentials = Support.getPlugin(EssentialsSupport.class);
            if (value.asString().contains("|")) {
                int split = value.asString().indexOf("|");
                int len = value.asString().length();
                String after = value.asString().substring(split + 1 , len);
                String before = value.asString().substring(0, split - 1);
                essUser.setIgnoredPlayer(essentials.getUser(new Element (before).asType(dPlayer.class).getOfflinePlayer().getUniqueId()) ,new Element (after).asBoolean());
            }
            else {
                essUser.setIgnoredPlayer(essentials.getUser(value.asType(dPlayer.class).getOfflinePlayer().getUniqueId()) ,true);
            }
        }


    }
}
