package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.depends.Depends;
import com.denizenscript.denizencore.DenizenCore;
import com.denizenscript.denizencore.objects.ObjectFetcher;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.denizencore.tags.TagManager;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.commands.worldguard.RegionCommand;
import com.denizenscript.depenizen.bukkit.objects.worldguard.WorldGuardRegionTag;
import com.denizenscript.depenizen.bukkit.properties.worldguard.*;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import org.bukkit.World;

public class WorldGuardBridge extends Bridge {

    public static WorldGuardBridge instance;

    @Override
    public void init() {
        instance = this;
        ObjectFetcher.registerWithObjectFetcher(WorldGuardRegionTag.class, WorldGuardRegionTag.tagProcessor);
        WorldGuardLocationExtensions.register();
        WorldGuardPlayerExtensions.register();
        PropertyParser.registerProperty(WorldGuardPlayerProperties.class, PlayerTag.class);
        WorldGuardCuboidExtensions.register();
        WorldGuardWorldExtensions.register();
        if (Depends.citizens != null) {
            WorldGuardNPCExtensions.register();
        }
        DenizenCore.commandRegistry.registerCommand(RegionCommand.class);

        // <--[tag]
        // @attribute <region[<region>]>
        // @returns WorldGuardRegionTag
        // @plugin Depenizen, WorldGuard
        // @description
        // Returns a WorldGuard region object constructed from the input value.
        // Refer to <@link objecttype WorldGuardRegionTag>.
        // -->
        TagManager.registerTagHandler(WorldGuardRegionTag.class, WorldGuardRegionTag.class, "region", (attribute, region) -> {
            return region;
        });
    }

    public static RegionManager getManager(World world) {
        return WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world));
    }
}
