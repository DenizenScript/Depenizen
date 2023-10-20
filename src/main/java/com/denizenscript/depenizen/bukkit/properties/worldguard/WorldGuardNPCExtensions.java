package com.denizenscript.depenizen.bukkit.properties.worldguard;

import com.denizenscript.denizen.objects.NPCTag;
import com.denizenscript.depenizen.bukkit.objects.worldguard.WorldGuardRegionTag;
import net.citizensnpcs.trait.waypoint.WanderWaypointProvider;
import net.citizensnpcs.trait.waypoint.Waypoints;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class WorldGuardNPCExtensions {

    public static void register() {

        // <--[tag]
        // @attribute <NPCTag.worldguard_wander_region>
        // @returns WorldGuardRegionTag
        // @mechanism NPCTag.worldguard_wander_region
        // @plugin Depenizen, WorldGuard
        // @description
        // If the NPC's waypoint provider is set to wander (<@link tag NPCTag.waypoint_provider>),
        // returns the current WorldGuard region that wandering is restricted to, if any.
        // -->
        NPCTag.tagProcessor.registerTag(WorldGuardRegionTag.class, "worldguard_wander_region", (attribute, object) -> {
            Waypoints waypoints = object.npc.getOrAddTrait(Waypoints.class);
            if (waypoints.getCurrentProvider() instanceof WanderWaypointProvider wanderWaypointProvider) {
                Object worldGuardRegion = wanderWaypointProvider.getWorldGuardRegion();
                org.bukkit.World world = object.getWorld();
                if (worldGuardRegion != null && world != null) {
                    return new WorldGuardRegionTag((ProtectedRegion) worldGuardRegion, world);
                }
            }
            return null;
        });

        // <--[mechanism]
        // @object NPCTag
        // @name worldguard_wander_region
        // @input WorldGuardRegionTag
        // @plugin Depenizen, WorldGuard
        // @description
        // If the NPC's waypoint provider is set to wander (<@link mechanism NPCTag.waypoint_provider>),
        // sets the current WorldGuard region that wandering is restricted to.
        // Provide no input to remove the region restriction.
        // @tags
        // <NPCTag.worldguard_wander_region>
        // -->
        NPCTag.tagProcessor.registerMechanism("worldguard_wander_region", false, (object, mechanism) -> {
            Waypoints waypoints = object.npc.getOrAddTrait(Waypoints.class);
            if (waypoints.getCurrentProvider() instanceof WanderWaypointProvider wanderWaypointProvider) {
                if (mechanism.hasValue() && mechanism.requireObject(WorldGuardRegionTag.class)) {
                    WorldGuardRegionTag input = mechanism.valueAsType(WorldGuardRegionTag.class);
                    wanderWaypointProvider.setWorldGuardRegion(input.getRegion().getId());
                }
                else {
                    wanderWaypointProvider.setWorldGuardRegion(null);
                }
            }
        });
    }
}
