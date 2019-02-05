package com.denizenscript.depenizen.bukkit.extensions.askyblock;

import com.denizenscript.depenizen.bukkit.extensions.dObjectExtension;
import com.wasteofplastic.askyblock.ASkyBlockAPI;
import com.wasteofplastic.askyblock.Island;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;

public class ASkyBlockLocationExtension extends dObjectExtension {

    public static boolean describes(dObject pl) {
        return pl instanceof dLocation;
    }

    public static ASkyBlockLocationExtension getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        return new ASkyBlockLocationExtension((dLocation) object);
    }

    public static final String[] handledTags = new String[] {
            "skyblock"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    public ASkyBlockLocationExtension(dLocation location) {
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
            // @Plugin DepenizenBukkit, A SkyBlock
            // -->
            if (attribute.startsWith("has_skyblock")) {
                return new Element(api.getIslandAt(location) != null).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <l@location.skyblock.get_owner>
            // @returns dPlayer
            // @description
            // Returns whether the owner of the skyblock at the location.
            // @Plugin DepenizenBukkit, A SkyBlock
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
