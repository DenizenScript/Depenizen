package com.denizenscript.depenizen.bukkit.tags.mythicmobs;

import com.denizenscript.denizencore.tags.TagManager;
import com.denizenscript.depenizen.bukkit.objects.mythicmobs.MythicSpawnerTag;

public class MythicSpawnerTagBase {

    public MythicSpawnerTagBase() {

        // <--[tag]
        // @attribute <mythicspawner[<name>]>
        // @returns MythicSpawnerTag
        // @description
        // Returns a MythicSpawnerTag based on the name input.
        // Refer to <@link language MythicSpawnerTag Objects>.
        // -->
        TagManager.registerTagHandler("mythicspawner", (attribute) -> {
            if (!attribute.hasContext(1)) {
                attribute.echoError("MythicSpawner tag base must have input.");
                return null;
            }
            return MythicSpawnerTag.valueOf(attribute.getContext(1), attribute.context);
        });
    }
}
