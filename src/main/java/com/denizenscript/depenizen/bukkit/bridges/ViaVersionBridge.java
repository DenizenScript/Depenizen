package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.tags.PseudoObjectTagBase;
import com.denizenscript.denizencore.tags.TagManager;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.properties.viaversion.ViaVersionPlayerExtensions;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.ViaAPI;
import org.bukkit.entity.Player;

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
            // Versions blocked in the ViaVersion config.yml file will not be shown in this list.
            // -->
            tagProcessor.registerTag(ListTag.class, "supported_protocol_versions", (attribute, object) -> {
                return new ListTag(getViaAPI().getSupportedProtocolVersions(), versions -> new ElementTag(versions.getVersion()));
            });

            // <--[tag]
            // @attribute <viaversion.supported_versions>
            // @returns ListTag
            // @plugin Depenizen, ViaVersion
            // @description
            // Returns a list of all supported versions on the server.
            // Versions blocked in the ViaVersion config.yml file will not be shown in this list.
            // -->
            tagProcessor.registerTag(ListTag.class, "supported_versions", (attribute, object) -> {
                return new ListTag(getViaAPI().getSupportedProtocolVersions(), versions -> new ElementTag(versions.getName(), true));
            });
        }
    }

    @Override
    public void init() {
        ViaVersionPlayerExtensions.register();
        new ViaVersionTagBase();
    }

    public static ViaAPI<Player> getViaAPI() {
        return Via.getAPI();
    }
}
