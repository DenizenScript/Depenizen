package net.gnomeffinway.depenizen.extensions.protocolsupport;

import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.tags.Attribute;
import net.gnomeffinway.depenizen.extensions.dObjectExtension;
import org.bukkit.entity.Player;
import protocolsupport.api.ProtocolSupportAPI;

public class ProtocolSupportExtension extends dObjectExtension {

    public static boolean describes(dObject pl) {
        return pl instanceof dPlayer;
    }

    public static ProtocolSupportExtension getFrom(dObject pl) {
        if (!describes(pl)) return null;
        else return new ProtocolSupportExtension((dPlayer) pl);
    }

    public ProtocolSupportExtension(dPlayer player) {
        this.player = player.getPlayerEntity();
    }

    Player player = null;

    @Override
    public String getAttribute(Attribute attribute) {
        // <--[tag]
        // @attribute <p@player.version>
        // @returns Element
        // @description
        // Returns the version of player.
        // @plugin Depenizen, ProtocolSupport
        // -->
        if (attribute.startsWith("version")) {
            if(player == null)
                return null;

            attribute = attribute.fulfill(1);

            // <--[tag]
            // @attribute <p@player.version>
            // @returns Element(Integer)
            // @description
            // Returns the protocol version of player.
            // @plugin Depenizen, ProtocolSupport
            // -->
            if (attribute.startsWith("protocol")) {
                return new Element(ProtocolSupportAPI.getProtocolVersion(player).getId()).getAttribute(attribute.fulfill(1));
            }

        }

        return new Element(String.valueOf(ProtocolSupportAPI.getProtocolVersion(player))).getAttribute(attribute.fulfill(1));

    }
    
}
