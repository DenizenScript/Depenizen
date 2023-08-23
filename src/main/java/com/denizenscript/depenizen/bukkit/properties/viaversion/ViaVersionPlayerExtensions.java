package com.denizenscript.depenizen.bukkit.properties.viaversion;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;

public class ViaVersionPlayerExtensions {

    public static void register() {

        // <--[tag]
        // @attribute  <PlayerTag.viaversion>
        // @returns ElementTag(Number)
        // @plugin Depenizen, ViaVersion
        // @deprecated Use 'PlayerTag.viaversion_protocol'
        // @description
        // Deprecated in favor of <@link tag PlayerTag.viaversion_protocol>.
        // -->

        // <--[tag]
        // @attribute <PlayerTag.viaversion_protocol>
        // @returns ElementTag(Number)
        // @plugin Depenizen, ViaVersion
        // @description
        // Returns the protocol version number of the player's client.
        // See <@link url https://wiki.vg/Protocol_version_numbers> as a reference list.
        // See also <@link tag PlayerTag.viaversion_version>
        // -->
        PlayerTag.registerOnlineOnlyTag(ElementTag.class, "viaversion_protocol", (attribute, object) -> {
            return new ElementTag(Via.getAPI().getPlayerVersion(object.getUUID()));
        }, "viaversion");

        // <--[tag]
        // @attribute <PlayerTag.viaversion_version>
        // @returns ElementTag
        // @plugin Depenizen, ViaVersion
        // @description
        // Returns the player's client version ("1.19.4", "1.18.2"...).
        // See also <@link tag PlayerTag.viaversion_protocol>
        // -->
        PlayerTag.registerOnlineOnlyTag(ElementTag.class, "viaversion_version", (attribute, object) -> {
            int version = Via.getAPI().getPlayerVersion(object.getUUID());
            return new ElementTag(ProtocolVersion.getProtocol(version).getName(), true);
        });
    }

}
