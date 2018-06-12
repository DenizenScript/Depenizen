package com.denizenscript.depenizen.bukkit.extensions.worldguard;

import com.denizenscript.depenizen.bukkit.extensions.dObjectExtension;
import com.denizenscript.depenizen.bukkit.support.Support;
import com.denizenscript.depenizen.bukkit.support.plugins.WorldGuardSupport;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;

import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;
import net.aufdemrand.denizencore.utilities.debugging.dB;

import org.bukkit.entity.Player;

public class WorldGuardPlayerExtension extends dObjectExtension {

    public static boolean describes(dObject object) {
        return object instanceof dPlayer
                && ((dPlayer) object).isOnline();
    }

    public static WorldGuardPlayerExtension getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        return new WorldGuardPlayerExtension((dPlayer) object);
    }

    private WorldGuardPlayerExtension(dPlayer player) {
        this.player = player.getPlayerEntity();
        this.wgp = Support.getPlugin(WorldGuardSupport.class);
    }

    Player player = null;
    WorldGuardPlugin wgp = null;

    // All regions at the players location
    private ApplicableRegionSet getApplicableRegions() {
        return wgp.getRegionManager(player.getWorld()).getApplicableRegions(player.getLocation());
    }

    // The original flag-reference without fuzziness
    private StateFlag getStateFlag(String s) {
        for (Flag<?> flag : DefaultFlag.getFlags()) {
            if (flag instanceof StateFlag && flag.getName().equalsIgnoreCase(s)) {
                return (StateFlag) flag;
            }
        }
        return null;
    }

    @Override
    public String getAttribute(Attribute attribute) {

        if (!attribute.startsWith("worldguard") && !attribute.startsWith("wg") ) {
            return null;
        }
        attribute = attribute.fulfill(1);

        // <--[tag]
        // @attribute <p@player.worldguard.can_build[<l@location>]>
        // @returns Element(Boolean)
        // @description
        // Whether WorldGuard allows to build at a location.
        // @Plugin DepenizenBukkit, WorldGuard
        // -->
        if (attribute.startsWith("can_build") && attribute.hasContext(1)) {
            dLocation location = dLocation.valueOf(attribute.getContext(1));
            if (location == null) {
                return null;
            }
            if (wgp.canBuild(player, location)) {
                return new Element(true).getAttribute(attribute.fulfill(1));
            }
            return new Element(false).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <p@player.worldguard.test_flag[<name>]>
        // @returns Element(Boolean)
        // @description
        // Returns the state of a flag at the players location.
        // @Plugin DepenizenBukkit, WorldGuard
        // -->
        if (attribute.startsWith("test_flag")) {
            if (!attribute.hasContext(1)) {
                dB.echoError("The tag p@player.worlduard.test_flag[...] must have a value.");
                return null;
            }
            StateFlag flag = getStateFlag(attribute.getContext(1));
            if (flag == null) {
                dB.echoError("The tag p@player.worlduard.test_flag[...] has an invalid value: " + attribute.getContext(1));
                return null;
            }
            ApplicableRegionSet set = getApplicableRegions();
            if (set.testState(wgp.wrapPlayer(player), flag)) {
                return new Element(true).getAttribute(attribute.fulfill(1));
            }
            return new Element(false).getAttribute(attribute.fulfill(1));
        }

        return null;

    }

}
