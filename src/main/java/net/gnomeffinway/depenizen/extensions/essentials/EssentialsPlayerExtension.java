package net.gnomeffinway.depenizen.extensions.essentials;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import net.aufdemrand.denizen.objects.*;
import net.aufdemrand.denizen.tags.Attribute;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.gnomeffinway.depenizen.extensions.dObjectExtension;
import net.gnomeffinway.depenizen.support.Supported;

import java.util.GregorianCalendar;

public class EssentialsPlayerExtension extends dObjectExtension {

    public static boolean describes(dObject pl) {
        return pl instanceof dPlayer && ((dPlayer) pl).isOnline();
    }

    public static EssentialsPlayerExtension getFrom(dObject pl) {
        if (!describes(pl)) return null;
        else return new EssentialsPlayerExtension((dPlayer) pl);
    }


    ///////////////////
    // Instance Fields and Methods
    /////////////

    private EssentialsPlayerExtension(dPlayer pl) {
        // TODO: UUID
        Essentials essentials = Supported.get("ESSENTIALS").getPlugin();
        this.essUser = essentials.getUser(pl.getPlayerEntity());
    }

    User essUser = null;

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <p@player.god_mode>
        // @returns Element(Boolean)
        // @description
        // Returns whether the player is currently in god mode.
        // @plugin Essentials
        // -->
        if (attribute.startsWith("god_mode")) {
            return new Element(essUser.isGodModeEnabled()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <p@player.has_home>
        // @returns Element(Boolean)
        // @description
        // Returns whether the player has set at least one home.
        // @plugin Essentials
        // -->
        if (attribute.startsWith("has_home")) {
            return new Element(essUser.hasHome()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <p@player.is_afk>
        // @returns Element(Boolean)
        // @description
        // Returns whether the player is AFK.
        // @plugin Essentials
        // -->
        if (attribute.startsWith("is_afk")) {
            return new Element(essUser.isAfk()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <p@player.is_muted>
        // @returns Element(Boolean)
        // @description
        // Returns whether the player is muted.
        // @plugin Essentials
        // -->
        if (attribute.startsWith("is_muted")) {
            return new Element(essUser.isMuted()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <p@player.home_list>
        // @returns dList(Element/dLocation)
        // @description
        // Returns a list of the homes of the player, in the format "HomeName/l@x,y,z,world".
        // @plugin Essentials
        // -->
        if (attribute.startsWith("home_list")) {
            dList homes = new dList();
            for (String home : essUser.getHomes()) {
                try {
                    homes.add(home + "/" + new dLocation(essUser.getHome(home)).identifySimple());
                } catch (Exception e) {
                    dB.echoError(e);
                }
            }
            return homes.getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <p@player.home_location_list>
        // @returns dList(dLocation)
        // @description
        // Returns a list of the locations of homes of the player.
        // @plugin Essentials
        // -->
        if (attribute.startsWith("home_location_list")) {
            dList homes = new dList();
            for (String home : essUser.getHomes()) {
                try {
                    homes.add(new dLocation(essUser.getHome(home)).identifySimple());
                } catch (Exception e) {
                    dB.echoError(e);
                }
            }
            return homes.getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <p@player.home_name_list>
        // @returns dList(Element)
        // @description
        // Returns a list of the names of homes of the player.
        // @plugin Essentials
        // -->
        if (attribute.startsWith("home_name_list")) {
            return new dList(essUser.getHomes()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <p@player.mail_list>
        // @returns dList(Element)
        // @description
        // Returns a list of mail the player currently has.
        // @plugin Essentials
        // -->
        if (attribute.startsWith("mail_list")) {
            return new dList(essUser.getMails()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <p@player.mute_timeout>
        // @returns Duration
        // @description
        // Returns how much time is left until the player is muted.
        // @plugin Essentials
        // -->
        if (attribute.startsWith("mute_timeout")) {
            return new Duration((int) (essUser.getMuteTimeout() - new GregorianCalendar().getTimeInMillis())/1000)
                    .getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <p@player.socialspy>
        // @returns Element(Boolean)
        // @description
        // Returns whether the player has SocialSpy enabled.
        // @plugin Essentials
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
        // @name afk
        // @input Element(Boolean)
        // @description
        // Sets whether the player is marked as AFK.
        // @extensions
        // <player.is_afk>
        // @plugin Essentials
        // -->
        if (mechanism.matches("afk") && mechanism.requireBoolean()) {
            essUser.setAfk(value.asBoolean());
        }

        // <--[mechanism]
        // @object dPlayer
        // @name god_mode
        // @input Element(Boolean)
        // @description
        // Sets whether the player has god mode enabled.
        // @extensions
        // <player.god_mode>
        // @plugin Essentials
        // -->
        if (mechanism.matches("god_mode") && mechanism.requireBoolean()) {
            essUser.setGodModeEnabled(value.asBoolean());
        }

        // <--[mechanism]
        // @object dPlayer
        // @name muted
        // @input Element(Boolean)(|Duration)
        // @description
        // Sets whether the player is muted. Optionally, you may also
        // specify a duration to set how long they are muted for.
        // @extensions
        // <player.is_muted>
        // <player.mute_timeout>
        // @plugin Essentials
        // -->
        if (mechanism.matches("muted") && mechanism.requireBoolean()) {
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
        // @extensions
        // <player.socialspy>
        // @plugin Essentials
        // -->
        if (mechanism.matches("socialspy") && mechanism.requireBoolean()) {
            essUser.setSocialSpyEnabled(value.asBoolean());
        }

    }
}
