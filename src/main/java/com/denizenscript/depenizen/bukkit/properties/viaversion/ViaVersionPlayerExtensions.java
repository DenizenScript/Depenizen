package com.denizenscript.depenizen.bukkit.properties.viaversion;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.depenizen.bukkit.bridges.ViaVersionBridge;
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
        // Note: When getting the players protocol, join events may not have the protocol linked with the player in time. It is suggested to delay this check by a few ticks.
        // -->
        PlayerTag.tagProcessor.registerTag(ElementTag.class, "viaversion_protocol", (attribute, object) -> {
            return new ElementTag(ViaVersionBridge.viaVersionInstance.getPlayerVersion(object.getUUID()));
        }, "viaversion");

        // <--[tag]
        // @attribute <PlayerTag.viaversion_version>
        // @returns ElementTag
        // @plugin Depenizen, ViaVersion
        // @description
        // Returns the version based on the protocol version number of the player's client.
        // Note: When getting the players protocol, join events may not have the protocol linked with the player in time. It is suggested to delay this check by a few ticks.
        // -->
        PlayerTag.tagProcessor.registerTag(ElementTag.class, "viaversion_version", (attribute, object) -> {
            int version = ViaVersionBridge.viaVersionInstance.getPlayerVersion(object.getUUID());
            return new ElementTag(ProtocolVersion.getProtocol(version).getName(), true);
        });
    }

}
