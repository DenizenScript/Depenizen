package com.denizenscript.depenizen.bukkit.bridges;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.ObjectFetcher;
import com.denizenscript.denizencore.tags.TagManager;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.events.superiorskyblock.*;
import com.denizenscript.depenizen.bukkit.objects.superiorskyblock.SuperiorSkyblockIslandTag;
import com.denizenscript.depenizen.bukkit.properties.superiorskyblock.*;

public class SuperiorSkyblockBridge extends Bridge {

    public static SuperiorPlayer getSuperiorPlayer(PlayerTag player) {
        return SuperiorSkyblockAPI.getPlayer(player.getUUID());
    }

    @Override
    public void init() {
        ScriptEvent.registerScriptEvent(SuperiorSkyblockIslandCreatedScriptEvent.class);
        ScriptEvent.registerScriptEvent(SuperiorSkyblockIslandDisbandedScriptEvent.class);
        SuperiorSkyblockLocationExtensions.register();
        SuperiorSkyblockPlayerExtensions.register();
        ObjectFetcher.registerWithObjectFetcher(SuperiorSkyblockIslandTag.class, SuperiorSkyblockIslandTag.tagProcessor);

        // <--[tag]
        // @attribute <superiorskyblock_island[<uuid>]>
        // @returns SuperiorSkyblockIslandTag
        // @plugin Depenizen, SuperiorSkyblock
        // @description
        // Returns the superiorskyblock island tag with the given uuid.
        // Refer to <@link objecttype SuperiorSkyblockIslandTag> for more information.
        // -->
        TagManager.registerTagHandler(SuperiorSkyblockIslandTag.class, SuperiorSkyblockIslandTag.class, "superiorskyblock_island", (attribute, param) -> {
            return param;
        });
    }
}
