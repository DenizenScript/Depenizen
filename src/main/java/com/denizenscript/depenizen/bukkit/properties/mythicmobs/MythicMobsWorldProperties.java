package com.denizenscript.depenizen.bukkit.properties.mythicmobs;

import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.depenizen.bukkit.bridges.MythicMobsBridge;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.depenizen.bukkit.objects.mythicmobs.MythicMobsMobTag;

public class MythicMobsWorldProperties implements Property {
    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "MythicMobsWorld";
    }

    @Override
    public void adjust(Mechanism mechanism) {

    }
    public static void registerTags() {
        // <--[tag]
        // @attribute <WorldTag.variable[<name>]>
        // @returns ElementTag
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns the value of a MythicMobs variable for this world.
        // -->
        PropertyParser.registerTag(MythicMobsWorldProperties.class, ElementTag.class, "variable", (attribute, object) -> {
            return null;
        });
    }
}
