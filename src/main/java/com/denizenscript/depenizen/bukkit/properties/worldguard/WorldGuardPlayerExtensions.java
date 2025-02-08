package com.denizenscript.depenizen.bukkit.properties.worldguard;

import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.MapTag;
import com.denizenscript.denizencore.tags.TagRunnable;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.denizencore.utilities.debugging.SlowWarning;
import com.denizenscript.denizencore.utilities.debugging.Warning;
import com.denizenscript.depenizen.bukkit.bridges.WorldGuardBridge;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionQuery;

public class WorldGuardPlayerExtensions {

    public static Warning worldguardCanBuild = new SlowWarning("worldguardCanBuild", "The tag 'PlayerTag.worldguard.can_build' from Depenizen/Worldguard is deprecated: use 'PlayerTag.worldguard_can_build'");
    public static Warning worldguardTestFlag = new SlowWarning("worldguardTestFlag", "The tag 'PlayerTag.worldguard.test_flag' from Depenizen/Worldguard is deprecated: use 'PlayerTag.worldguard_flag'");

    public static void register() {

        TagRunnable.ObjectInterface<PlayerTag, ObjectTag> runnable = (attribute, player) -> {

            // <--[tag]
            // @attribute <PlayerTag.worldguard.can_build[<location>]>
            // @returns ElementTag(Boolean)
            // @plugin Depenizen, WorldGuard
            // @deprecated Use 'PlayerTag.worldguard_can_build'
            // @description
            // Deprecated in favor of <@link tag PlayerTag.worldguard_can_build>
            // -->
            if (attribute.startsWith("can_build", 2) && attribute.hasContext(2)) {
                worldguardCanBuild.warn(attribute.context);
                LocationTag loc = attribute.contextAsType(2, LocationTag.class);
                attribute.fulfill(1);
                if (loc == null) {
                    return null;
                }
                WorldGuardPlugin worldGuard = (WorldGuardPlugin) WorldGuardBridge.instance.plugin;
                return new ElementTag(WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery().testBuild(BukkitAdapter.adapt(loc), worldGuard.wrapPlayer(player.getPlayerEntity())));
            }

            // <--[tag]
            // @attribute <PlayerTag.worldguard.test_flag[<name>]>
            // @returns ObjectTag
            // @plugin Depenizen, WorldGuard
            // @deprecated Use 'PlayerTag.worldguard_flag'
            // @description
            // Deprecated in favor of <@link tag PlayerTag.worldguard_flag>
            // -->
            if (attribute.startsWith("test_flag", 2) && attribute.hasContext(2)) {
                worldguardTestFlag.warn(attribute.context);
                ElementTag name = attribute.contextAsType(2, ElementTag.class);
                attribute.fulfill(1);
                Flag<?> flag = Flags.fuzzyMatchFlag(WorldGuard.getInstance().getFlagRegistry(), name.asString());
                if (flag == null) {
                    Debug.echoError("The tag PlayerTag.worldguard.test_flag[...] has an invalid value: " + name.asString());
                    return null;
                }
                LocationTag loc = player.getLocation();

                // <--[tag]
                // @attribute <PlayerTag.worldguard.test_flag[<name>].at[<location>]>
                // @returns ObjectTag
                // @plugin Depenizen, WorldGuard
                // @deprecated Use 'PlayerTag.worldguard_flag'
                // @description
                // Deprecated in favor of <@link tag PlayerTag.worldguard_flag>
                // -->
                if (attribute.startsWith("at", 2) && attribute.hasContext(2)) {
                    loc = attribute.contextAsType(2, LocationTag.class);
                    attribute.fulfill(1);
                    if (loc == null) {
                        return null;
                    }
                }
                WorldGuardPlugin worldGuard = (WorldGuardPlugin) WorldGuardBridge.instance.plugin;
                RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
                if (flag instanceof StateFlag stateFlag) {
                    return new ElementTag(query.testState(BukkitAdapter.adapt(loc), worldGuard.wrapPlayer(player.getPlayerEntity()), stateFlag));
                }
                else {
                    return CoreUtilities.objectToTagForm(query.queryValue(BukkitAdapter.adapt(loc), worldGuard.wrapPlayer(player.getPlayerEntity()), flag), attribute.context).asElement();
                }
            }

            return null;
        };

        PlayerTag.tagProcessor.registerTag(ObjectTag.class, "worldguard", runnable);
        PlayerTag.tagProcessor.registerTag(ObjectTag.class, "wg", runnable);

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
        // @attribute <PlayerTag.worldguard_flag[flag=<flag>(;location=<at>)]>
        // @returns ObjectTag
        // @plugin Depenizen, WorldGuard
        // @description
        // Returns the boolean state of a flag for that player at the specified location.
        // For non-state tags, returns the current value of the flag.
        // @example
        // # Returns 'true' if the player can be attacked (according to WG) or 'false' if not.
        // - narrate <player.worldguard_flag[flag=pvp]>
        // -->
        PlayerTag.tagProcessor.registerTag(ObjectTag.class, MapTag.class, "worldguard_flag", (attribute, player, map) -> {
            ElementTag name = map.getRequiredObjectAs("flag", ElementTag.class, attribute);
            Flag<?> flag = Flags.fuzzyMatchFlag(WorldGuard.getInstance().getFlagRegistry(), name.asString());
            if (flag == null) {
                Debug.echoError("The tag PlayerTag.worldguard_flag[...] has an invalid flag: " + name.asString());
                return null;
            }
            LocationTag loc = map.getObjectAs("location", LocationTag.class, attribute.context);
            if (loc == null) {
                loc = player.getLocation();
            }
            WorldGuardPlugin worldGuard = (WorldGuardPlugin) WorldGuardBridge.instance.plugin;
            RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
            if (flag instanceof StateFlag stateFlag) {
                return new ElementTag(query.testState(BukkitAdapter.adapt(loc), worldGuard.wrapPlayer(player.getPlayerEntity()), stateFlag));
            }
            else {
                return CoreUtilities.objectToTagForm(query.queryValue(BukkitAdapter.adapt(loc), worldGuard.wrapPlayer(player.getPlayerEntity()), flag), attribute.context);
            }
        });
    }
}
