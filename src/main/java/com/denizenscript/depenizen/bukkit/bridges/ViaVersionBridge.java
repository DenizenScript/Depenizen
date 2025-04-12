package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.tags.PseudoObjectTagBase;
import com.denizenscript.denizencore.tags.TagManager;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.properties.viaversion.ViaVersionPlayerExtensions;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;

public class ViaVersionBridge extends Bridge {

    static class ViaVersionTagBase extends PseudoObjectTagBase<ViaVersionTagBase> {

        public static ViaVersionTagBase instance;

        public ViaVersionTagBase() {
            instance = this;
            TagManager.registerStaticTagBaseHandler(ViaVersionTagBase.class, "viaversion", (t) -> instance);
        }

        @Override
        public void register() {

            // <--[tag]
            // @attribute <viaversion.supported_protocol_versions>
            // @returns ListTag
            // @plugin Depenizen, ViaVersion
            // @description
            // Returns a list of all supported protocol versions on the server.
            // -->
            tagProcessor.registerTag(ListTag.class, "supported_protocol_versions", (attribute, object) -> {
                ListTag list = new ListTag();
                for (Object protocol : Via.getAPI().getSupportedProtocolVersions()) {
                    int version = ((ProtocolVersion) protocol).getVersion();
                    list.add(String.valueOf(version));
                }
                return list;
            });

            // <--[tag]
            // @attribute <viaversion.full_supported_versions>
            // @returns ListTag
            // @plugin Depenizen, ViaVersion
            // @description
            // Returns a list of all supported versions on the server.
            // -->
            tagProcessor.registerTag(ListTag.class, "supported_versions", (attribute, object) -> {
                ListTag list = new ListTag();
                for (Object protocol : Via.getAPI().getSupportedProtocolVersions()) {
                    list.add(String.valueOf(((ProtocolVersion) protocol).getName()));
                }
                return list;
            });
        }
    }

    @Override
    public void init() {
        ViaVersionPlayerExtensions.register();
        new ViaVersionTagBase();
    }
}
