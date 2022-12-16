package com.denizenscript.depenizen.bukkit.properties.griefprevention;

import com.denizenscript.denizen.objects.WorldTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.depenizen.bukkit.objects.griefprevention.GriefPreventionClaimTag;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import com.denizenscript.denizencore.objects.ObjectTag;

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

    public static final String[] handledMechs = new String[] {
    }; // None

    private GriefPreventionWorldProperties(WorldTag world) {
        this.world = world;
    }

    WorldTag world;

    public static void register() {

        // <--[tag]
        // @attribute <WorldTag.griefprevention_claims>
        // @returns ListTag(GriefPreventionClaimTag)
        // @plugin Depenizen, GriefPrevention
        // @description
        // Returns a list of GriefPreventionClaim in this world.
        // -->
        PropertyParser.registerTag(GriefPreventionWorldProperties.class, ListTag.class, "griefprevention_claims", (attribute, property) -> {
            ListTag result = new ListTag();
            for (Claim claim : GriefPrevention.instance.dataStore.getClaims()) {
                if (property.world.getWorld().equals(claim.getLesserBoundaryCorner().getWorld())) {
                    result.addObject(new GriefPreventionClaimTag(claim));
                }
            }
            return result;
        });
    }

    @Override
    public void adjust(Mechanism mechanism) {
    }
}
