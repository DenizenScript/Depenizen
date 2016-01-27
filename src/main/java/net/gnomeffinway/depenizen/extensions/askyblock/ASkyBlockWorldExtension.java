package net.gnomeffinway.depenizen.extensions.askyblock;

import com.wasteofplastic.askyblock.ASkyBlockAPI;
import net.aufdemrand.denizen.objects.dWorld;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;
import net.gnomeffinway.depenizen.extensions.dObjectExtension;

public class ASkyBlockWorldExtension extends dObjectExtension {

    public static boolean describes(dObject object) {
        return object instanceof dWorld;
    }

    public static ASkyBlockWorldExtension getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new ASkyBlockWorldExtension((dWorld) object);
        }
    }

    public ASkyBlockWorldExtension(dWorld world) {
        this.world = world;
    }

    dWorld world;
    ASkyBlockAPI api = ASkyBlockAPI.getInstance();

    @Override
    public String getAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        // <--[tag]
        // @attribute <w@world.is_skyblock_world>
        // @returns Element(Boolean)
        // @description
        // Returns whether the world is used by A SkyBlock.
        // @plugin Depenizen, A SkyBlock
        // -->
        if (attribute.startsWith("is_skyblock_world")) {
            return new Element(api.getIslandWorld() == world.getWorld() ||
                    api.getNetherWorld() == world.getWorld()).getAttribute(attribute.fulfill(1));
        }

        return null;
    }

}
