package com.denizenscript.depenizen.bukkit.properties.worldguard;

import com.denizenscript.denizen.objects.NPCTag;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.depenizen.bukkit.objects.worldguard.WorldGuardRegionTag;
import net.citizensnpcs.trait.waypoint.WanderWaypointProvider;
import net.citizensnpcs.trait.waypoint.Waypoints;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class WorldGuardNPCProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "WorldGuardNPC";
    }

    public static boolean describes(ObjectTag object) {
        return object instanceof NPCTag;
    }

    public static WorldGuardNPCProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new WorldGuardNPCProperties((NPCTag) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "worldguard"
    };

    public static final String[] handledMechs = new String[] {
            "wander_worldguardregion"
    };

    public WorldGuardNPCProperties(NPCTag npc) {
        this.npc = npc;
    }

    NPCTag npc;

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        if (attribute.startsWith("worldguard")) {
            Waypoints waypoints = npc.getCitizen().getOrAddTrait(Waypoints.class);

            // <--[tag]
            // @attribute <NPCTag.worldguard.wander_region>
            // @returns WorldGuardRegionTag
            // @plugin Depenizen, WorldGuard
            // @description
            // Returns the worldguardRegion for the NPC's wander Waypoint Provider, if that provider is in use.
            // -->
            if ((attribute.startsWith("worldguard.wander_region")) && (waypoints.getCurrentProvider() instanceof WanderWaypointProvider wanderWaypointProvider)) {
                Object worldGuardRegion = wanderWaypointProvider.getWorldGuardRegion();
                org.bukkit.World world = npc.getWorld();
                if ((worldGuardRegion != null) && (world != null)) {
                    return new WorldGuardRegionTag((ProtectedRegion) worldGuardRegion, world);
                }
                return null;
            }
        }
        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {
        
        // <--[mechanism]
        // @object NPCTag
        // @name wander_worldguardregion
        // @input ElementTag
        // @description
        // Sets the worldguardRegion for an NPC's wander Waypoints Provider, if that provider is in use. 
        // @tags
        // <NPCTag.wander_worldguardregion>
        // -->
        if (mechanism.matches("wander_worldguardregion") && mechanism.requireObject(ElementTag.class)) {
            Waypoints waypoints = npc.getCitizen().getOrAddTrait(Waypoints.class);
            if (waypoints.getCurrentProvider() instanceof WanderWaypointProvider wanderWaypointProvider) {
                ElementTag region = mechanism.valueAsType(ElementTag.class);
                wanderWaypointProvider.setWorldGuardRegion(region.asString());
            }
        }
    }
}
