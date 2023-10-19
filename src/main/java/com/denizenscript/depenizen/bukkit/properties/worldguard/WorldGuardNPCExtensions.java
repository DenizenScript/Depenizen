package com.denizenscript.depenizen.bukkit.properties.worldguard;

import com.denizenscript.denizen.objects.NPCTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.depenizen.bukkit.objects.worldguard.WorldGuardRegionTag;
import net.citizensnpcs.trait.waypoint.WanderWaypointProvider;
import net.citizensnpcs.trait.waypoint.Waypoints;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class WorldGuardNPCExtensions {

    public static void register() {

        // <--[tag]
        // @attribute <NPCTag.worldguard_wanderregion>
        // @returns WorldGuardRegionTag
        // @plugin Depenizen, WorldGuard
        // @description
        // Returns the worldguardRegion for the NPC's wander Waypoint Provider, if that provider is in use.
        // -->
        NPCTag.tagProcessor.registerTag(WorldGuardRegionTag.class, "worldguard_wanderregion", (attribute, object) -> {
            Waypoints waypoints = object.npc.getOrAddTrait(Waypoints.class);
            if (waypoints.getCurrentProvider() instanceof WanderWaypointProvider wanderWaypointProvider) {
                Object worldGuardRegion = wanderWaypointProvider.getWorldGuardRegion();
                org.bukkit.World world = object.getWorld();
                if ((worldGuardRegion != null) && (world != null)) {
                    return new WorldGuardRegionTag((ProtectedRegion) worldGuardRegion, world);
                }
            }
            return null;
        });           

        // <--[mechanism]
        // @object NPCTag
        // @name wander_worldguardregion
        // @input ElementTag
        // @plugin Depenizen, WorldGuard
        // @description
        // Sets the worldguardRegion for an NPC's wander Waypoints Provider, if that provider is in use. 
        // @tags
        // <NPCTag.wander_worldguardregion>
        // -->
        NPCTag.tagProcessor.registerMechanism("wander_worldguardregion", false, ElementTag.class, (object, mechanism, input) -> {
            Waypoints waypoints = object.npc.getOrAddTrait(Waypoints.class);
            if (waypoints.getCurrentProvider() instanceof WanderWaypointProvider wanderWaypointProvider) {
                wanderWaypointProvider.setWorldGuardRegion(input.asString());
            }
        });
    }
}