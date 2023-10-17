package com.denizenscript.depenizen.bukkit.properties.worldguard;

import com.denizenscript.denizen.objects.NPCTag;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.depenizen.bukkit.bridges.MythicMobsBridge;
import com.denizenscript.depenizen.bukkit.objects.worldguard.WorldGuardRegionTag;
import com.denizenscript.depenizen.bukkit.properties.mythicmobs.MythicMobsPlayerProperties;

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
            "worldguard_wanderregion"
    };

    public static final String[] handledMechs = new String[] {
            "wander_worldguardregion"
    };

    public WorldGuardNPCProperties(NPCTag npc) {
        this.npc = npc;
    }

    NPCTag npc;

    public static void register() {

        // <--[tag]
        // @attribute <NPCTag.worldguard_wanderregion>
        // @returns WorldGuardRegionTag
        // @plugin Depenizen, WorldGuard
        // @description
        // Returns the worldguardRegion for the NPC's wander Waypoint Provider, if that provider is in use.
        // -->
        PropertyParser.registerTag(WorldGuardNPCProperties.class, WorldGuardRegionTag.class, "worldguard_wanderregion", (attribute, object) -> {
            Waypoints waypoints = object.npc.getCitizen().getOrAddTrait(Waypoints.class);
            if (waypoints.getCurrentProvider() instanceof WanderWaypointProvider wanderWaypointProvider) {
                Object worldGuardRegion = wanderWaypointProvider.getWorldGuardRegion();
                org.bukkit.World world = object.npc.getWorld();
                if ((worldGuardRegion != null) && (world != null)) {
                    return new WorldGuardRegionTag((ProtectedRegion) worldGuardRegion, world);
                }
            }
            return null;
        });           
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
