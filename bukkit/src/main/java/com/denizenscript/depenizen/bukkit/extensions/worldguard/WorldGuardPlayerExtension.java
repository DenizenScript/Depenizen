package com.denizenscript.depenizen.bukkit.extensions.worldguard;

import com.denizenscript.depenizen.bukkit.extensions.dObjectExtension;
import com.denizenscript.depenizen.bukkit.support.Support;
import com.denizenscript.depenizen.bukkit.support.plugins.WorldGuardSupport;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;

import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;

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

    private ApplicableRegionSet getApplicableRegions() {
        return wgp.getRegionManager(player.getWorld()).getApplicableRegions(player.getLocation());
    }

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <p@player.can_build[<l@location>]>
        // @returns Element(Boolean)
        // @description
        // Whether WorldGuard allows to build at a location.
        // @Plugin DepenizenBukkit, WorldGuard
        // -->
        if (attribute.startsWith("can_build") && attribute.hasContext(1)) {
            dLocation location = dLocation.valueOf(attribute.getContext(1));
            if (location == null ) {
                return null;
            }
            if (wgp.canBuild(player, location)) {
                return Element.TRUE.getAttribute(attribute.fulfill(1));
            }
            return Element.FALSE.getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <p@player.can_pvp>
        // @returns Element(Boolean)
        // @description
        // Whether WorldGuard allows to pvp at the players location.
        // @Plugin DepenizenBukkit, WorldGuard
        // -->
        if (attribute.startsWith("can_pvp")) {
            ApplicableRegionSet set = getApplicableRegions();
            if (set.testState(wgp.wrapPlayer(player), DefaultFlag.PVP)) {
                return Element.TRUE.getAttribute(attribute.fulfill(1));
            }
            return Element.FALSE.getAttribute(attribute.fulfill(1));
        }

        return null;

    }

}
