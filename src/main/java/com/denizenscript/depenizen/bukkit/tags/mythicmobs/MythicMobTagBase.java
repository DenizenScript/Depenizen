package com.denizenscript.depenizen.bukkit.tags.mythicmobs;

import com.denizenscript.denizencore.tags.TagManager;
import com.denizenscript.depenizen.bukkit.objects.mythicmobs.MythicMobsMobTag;

public class MythicMobTagBase {

    public MythicMobTagBase() {
    // <--[tag]
    // @attribute <MythicSpawner[<name>]>
    // @returns MythicSpawnerTag
    // @description
    // Returns a MythicSpawnerTag based on the name inpuit
    // Refer to <@link language MythicSpawnerTag Objects>.
    // -->
        TagManager.registerTagHandler("mythicmob", (attribute) -> {
        if (!attribute.hasContext(1)) {
            attribute.echoError("MythicMob tag base must have input.");
            return null;
        }
        return MythicMobsMobTag.valueOf(attribute.getContext(1), attribute.context);
    });
}
}
