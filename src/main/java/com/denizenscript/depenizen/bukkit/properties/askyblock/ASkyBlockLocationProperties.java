package com.denizenscript.depenizen.bukkit.properties.askyblock;

import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.wasteofplastic.askyblock.ASkyBlockAPI;
import com.wasteofplastic.askyblock.Island;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.objects.dPlayer;
import com.denizenscript.denizencore.objects.Element;
import com.denizenscript.denizencore.objects.dObject;
import com.denizenscript.denizencore.tags.Attribute;

public class ASkyBlockLocationProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "ASkyBlockLocation";
    }

    @Override
    public void adjust(Mechanism mechanism) {
        // None
    }

    public static boolean describes(dObject pl) {
        return pl instanceof dLocation;
    }

    public static ASkyBlockLocationProperties getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        return new ASkyBlockLocationProperties((dLocation) object);
    }

    public static final String[] handledTags = new String[] {
            "skyblock"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    public ASkyBlockLocationProperties(dLocation location) {
        this.location = location;
    }

    dLocation location;
    ASkyBlockAPI api = ASkyBlockAPI.getInstance();

    @Override
    public String getAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        if (attribute.startsWith("skyblock")) {
            attribute = attribute.fulfill(1);

            // <--[tag]
            // @attribute <l@location.skyblock.has_skyblock>
            // @returns Element(Boolean)
            // @description
            // Returns whether the location has a skyblock.
            // @Plugin Depenizen, A SkyBlock
            // -->
            if (attribute.startsWith("has_skyblock")) {
                return new Element(api.getIslandAt(location) != null).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <l@location.skyblock.get_owner>
            // @returns dPlayer
            // @description
            // Returns whether the owner of the skyblock at the location.
            // @Plugin Depenizen, A SkyBlock
            // -->
            else if (attribute.startsWith("get_owner")) {
                Island i = api.getIslandAt(location);
                if (i == null) {
                    return null;
                }
                return new dPlayer(i.getOwner()).getAttribute(attribute.fulfill(1));
            }
        }
        return null;
    }
}
