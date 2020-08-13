package com.denizenscript.depenizen.bukkit.tags.mythicmobs;

import com.denizenscript.denizencore.tags.TagManager;
import com.denizenscript.depenizen.bukkit.objects.mythicmobs.MythicMobsMobTag;

public class MythicMobTagBase {

    public MythicMobTagBase() {

        // <--[tag]
        // @attribute <mythicmob[<name>]>
        // @returns MythicMobsMobTag
        // @description
        // Returns a MythicMobsMobTg based on the name input.
        // Refer to <@link language MythicMobsMobTag Objects>.
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
