package com.denizenscript.depenizen.bukkit.properties.worldguard;

import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.depenizen.bukkit.bridges.WorldGuardBridge;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionQuery;

public class WorldGuardPlayerExtensions {

    public static void register() {

        // <--[tag]
        // @attribute <PlayerTag.worldguard_can_build[<location>]>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, WorldGuard
        // @description
        // Whether WorldGuard allows to build at a location.
        // -->
        PlayerTag.tagProcessor.registerTag(ElementTag.class, LocationTag.class, "worldguard_can_build", (attribute, player, loc) -> {
            WorldGuardPlugin worldGuard = (WorldGuardPlugin) WorldGuardBridge.instance.plugin;
            return new ElementTag(WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery().testBuild(BukkitAdapter.adapt(loc), worldGuard.wrapPlayer(player.getPlayerEntity())));
        });

        // <--[tag]
        // @attribute <PlayerTag.worldguard.test_flag[<name>]>
        // @returns ElementTag
        // @plugin Depenizen, WorldGuard
        // @description
        // Returns the boolean state of a flag for that player at their location.
        // For non-state tags, returns the current value of the flag.
        // @example
        // # Returns 'true' if the player can be attacked (according to WG) or 'false' if not.
        // - narrate <player.worldguard.test_flag[pvp]>
        // -->
        PlayerTag.tagProcessor.registerTag(ElementTag.class, ElementTag.class, "worldguard_test_flag", (attribute, player, name) -> {
            WorldGuardPlugin worldGuard = (WorldGuardPlugin) WorldGuardBridge.instance.plugin;
            Flag<?> flag = Flags.fuzzyMatchFlag(WorldGuard.getInstance().getFlagRegistry(), name.asString());
            if (flag == null) {
                Debug.echoError("The tag PlayerTag.worlduard_test_flag[...] has an invalid value: " + attribute.getParam());
                return null;
            }
            LocationTag loc = player.getLocation();

            // <--[tag]
            // @attribute <PlayerTag.worldguard_test_flag[<name>].at[<location>]>
            // @returns ElementTag
            // @plugin Depenizen, WorldGuard
            // @description
            // Returns the boolean state of a flag for that player at the specified location.
            // For non-state tags, returns the current value of the flag.
            // -->
            if (attribute.startsWith("at", 2) && attribute.hasContext(2)) {
                loc = attribute.contextAsType(2, LocationTag.class);
                if (loc == null) {
                    return null;
                }
                attribute.fulfill(1);
            }

            RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
            if (flag instanceof StateFlag) {
                return new ElementTag(query.testState(BukkitAdapter.adapt(loc), worldGuard.wrapPlayer(player.getPlayerEntity()), (StateFlag) flag));
            }
            else {
                return CoreUtilities.objectToTagForm(query.queryValue(BukkitAdapter.adapt(loc), worldGuard.wrapPlayer(player.getPlayerEntity()), flag), attribute.context).asElement();
            }
        });

    }


}
