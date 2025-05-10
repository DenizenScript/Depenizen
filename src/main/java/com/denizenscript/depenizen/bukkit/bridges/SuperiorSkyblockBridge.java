package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.ObjectFetcher;
import com.denizenscript.denizencore.tags.TagManager;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.events.superiorskyblock.*;
import com.denizenscript.depenizen.bukkit.objects.superiorskyblock.SuperiorSkyblockIslandTag;
import com.denizenscript.depenizen.bukkit.properties.superiorskyblock.SuperiorSkyblockPlayerExtensions;

public class SuperiorSkyblockBridge extends Bridge {

    @Override
    public void init() {
        ScriptEvent.registerScriptEvent(SuperiorSkyblockIslandCreatedScriptEvent.class);
        ScriptEvent.registerScriptEvent(SuperiorSkyblockIslandDisbandedScriptEvent.class);
        SuperiorSkyblockPlayerExtensions.register();
        ObjectFetcher.registerWithObjectFetcher(SuperiorSkyblockIslandTag.class, SuperiorSkyblockIslandTag.tagProcessor);

        // <--[tag]
        // @attribute <superiorskyblock_island[<name>]>
        // @returns SuperiorSkyblockIslandTag
        // @plugin Depenizen, SuperiorSkyblock
        // @description
        // Returns the superiorskyblock island tag with the given name.
        // -->
        TagManager.registerTagHandler(SuperiorSkyblockIslandTag.class, SuperiorSkyblockIslandTag.class, "superiorskyblock_island", (attribute, param) -> {
            return param;
        });
    }
}
