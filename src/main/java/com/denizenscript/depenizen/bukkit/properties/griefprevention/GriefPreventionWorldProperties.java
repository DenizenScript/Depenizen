package com.denizenscript.depenizen.bukkit.properties.griefprevention;

import com.denizenscript.denizen.objects.WorldTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.depenizen.bukkit.objects.griefprevention.GriefPreventionClaimTag;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;

public class GriefPreventionWorldProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "GriefPreventionWorld";
    }

    public static boolean describes(ObjectTag object) {
        return object instanceof WorldTag;
    }

    public static GriefPreventionWorldProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new GriefPreventionWorldProperties((WorldTag) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "griefprevention"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    private GriefPreventionWorldProperties(WorldTag world) {
        this.world = world;
    }

    WorldTag world;

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        if (attribute.startsWith("grief_prevention")
                || attribute.startsWith("gp")
                || attribute.startsWith("griefprevention")) {
            attribute = attribute.fulfill(1);

            // <--[tag]
            // @attribute <WorldTag.griefprevention.claims>
            // @returns ListTag(GriefPreventionClaimTag)
            // @plugin Depenizen, GriefPrevention
            // @description
            // Returns a list of GriefPreventionClaim in this world.
            // -->
            if (attribute.startsWith("claims")) {
                ListTag result = new ListTag();
                for (Claim claim : GriefPrevention.instance.dataStore.getClaims()) {
                    if (world.getWorld().equals(claim.getLesserBoundaryCorner().getWorld())) {
                        result.addObject(new GriefPreventionClaimTag(claim));
                    }
                }
                return result.getObjectAttribute(attribute.fulfill(1));
            }
        }

        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {
    }
}
