package com.denizenscript.depenizen.bukkit.properties.bungee;

import com.denizenscript.depenizen.bukkit.bungee.BungeeHelpers;
import com.denizenscript.denizen.objects.dPlayer;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.tags.Attribute;

public class BungeePlayerProperties implements Property {

        public static boolean describes(ObjectTag entity) {
            return entity instanceof dPlayer;
        }

        public static BungeePlayerProperties getFrom(ObjectTag player) {
            if (!describes(player)) {
                return null;
            }
            else {
                return new BungeePlayerProperties((dPlayer) player);
            }
        }

        public static final String[] handledTags = new String[] {
        };

        public static final String[] handledMechs = new String[] {
                "send_to"
        };


        ///////////////////
        // Instance Fields and Methods
        /////////////

        private BungeePlayerProperties(dPlayer plr) {
            player = plr;
        }

        dPlayer player;

        /////////
        // Property Methods
        ///////

        @Override
        public String getPropertyString() {
            return null;
        }

        @Override
        public String getPropertyId() {
            return "BungeePlayer";
        }


        ///////////
        // ObjectTag Attributes
        ////////

        @Override
        public String getAttribute(Attribute attribute) {
            return null;
        }

        @Override
        public void adjust(Mechanism mechanism) {

            // <--[mechanism]
            // @object dPlayer
            // @name send_to
            // @input Element
            // @description
            // Sends the player to the specified Bungee server.
            // @Plugin Depenizen, BungeeCord
            // -->
            if ((mechanism.matches("send_to") && mechanism.hasValue())) {
                BungeeHelpers.sendPlayer(player.getOfflinePlayer().getUniqueId(), mechanism.getValue().asString());
            }
        }
    }
