package com.denizenscript.depenizen.bukkit.bridges;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.hooks.WorldsProvider;
import com.bgsoftware.superiorskyblock.api.island.IslandFlag;
import com.bgsoftware.superiorskyblock.api.world.Dimension;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.ObjectFetcher;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.tags.PseudoObjectTagBase;
import com.denizenscript.denizencore.tags.TagManager;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.events.superiorskyblock.*;
import com.denizenscript.depenizen.bukkit.objects.superiorskyblock.SuperiorSkyblockIslandTag;
import com.denizenscript.depenizen.bukkit.properties.superiorskyblock.*;
import org.bukkit.World;

public class SuperiorSkyblockBridge extends Bridge {

    static class SuperiorSkyblockTagBase extends PseudoObjectTagBase<SuperiorSkyblockTagBase> {

        public static SuperiorSkyblockTagBase instance;

        public SuperiorSkyblockTagBase() {
            instance = this;
            TagManager.registerStaticTagBaseHandler(SuperiorSkyblockTagBase.class, "superiorskyblock", (t) -> instance);
        }

        public void register() {

            // <--[tag]
            // @attribute <superiorskyblock.island_settings>
            // @returns ListTag
            // @plugin Depenizen, SuperiorSkyblock
            // @description
            // Returns the possible settings for use in <@link tag SuperiorSkyblockTag.settings> and <@link mechanism SuperiorSkyblock.settings>.
            // -->
            tagProcessor.registerTag(ListTag.class, "island_settings", (attribute, object) -> {
                return new ListTag(IslandFlag.values(), flag -> new ElementTag(flag.getName()));
            });

            // <--[tag]
            // @attribute <superiorskyblock.list_islands>
            // @returns ListTag(SuperiorSkyblockIslandTag)
            // @plugin Depenizen, SuperiorSkyblock
            // @description
            // Returns a list of all islands, excluding the spawn island.
            // -->
            tagProcessor.registerTag(ListTag.class, "list_islands", (attribute, object) -> {
                return new ListTag(SuperiorSkyblockAPI.getGrid().getIslands(), SuperiorSkyblockIslandTag::new);
            });

            // <--[tag]
            // @attribute <superiorskyblock.spawn_island>
            // @returns SuperiorSkyblockIslandTag
            // @plugin Depenizen, SuperiorSkyblock
            // @description
            // Returns the spawn island.
            // -->
            tagProcessor.registerTag(SuperiorSkyblockIslandTag.class, "spawn_island", (attribute, object) -> {
                return new SuperiorSkyblockIslandTag(SuperiorSkyblockAPI.getSpawnIsland());
            });
        }
    }

    @Override
    public void init() {
        ScriptEvent.registerScriptEvent(SuperiorSkyblockIslandCreatedScriptEvent.class);
        ScriptEvent.registerScriptEvent(SuperiorSkyblockIslandDisbandedScriptEvent.class);
        ScriptEvent.registerScriptEvent(SuperiorSkyblockIslandInviteScriptEvent.class);
        ScriptEvent.registerScriptEvent(SuperiorSkyblockIslandJoinScriptEvent.class);
        ScriptEvent.registerScriptEvent(SuperiorSkyblockIslandKickScriptEvent.class);
        ScriptEvent.registerScriptEvent(SuperiorSkyblockPlayerLeavesIslandScriptEvent.class);
        SuperiorSkyblockLocationExtensions.register();
        SuperiorSkyblockPlayerExtensions.register();
        ObjectFetcher.registerWithObjectFetcher(SuperiorSkyblockIslandTag.class, SuperiorSkyblockIslandTag.tagProcessor);
        new SuperiorSkyblockTagBase();

        // <--[tag]
        // @attribute <superiorskyblock_island[<uuid>]>
        // @returns SuperiorSkyblockIslandTag
        // @plugin Depenizen, SuperiorSkyblock
        // @description
        // Returns the superiorskyblock island tag with the given uuid.
        // Refer to <@link objecttype SuperiorSkyblockIslandTag> for more information.
        // -->
        TagManager.registerTagHandler(SuperiorSkyblockIslandTag.class, SuperiorSkyblockIslandTag.class, "superiorskyblock_island", (attribute, param) -> {
            return param;
        });
    }

    public static Dimension getIslandsWorldDimension(World world) {
        return getWorldsProvider().getIslandsWorldDimension(world);
    }

    public static SuperiorPlayer getSuperiorPlayer(PlayerTag player) {
        return SuperiorSkyblockAPI.getPlayer(player.getUUID());
    }

    public static WorldsProvider getWorldsProvider() {
        return SuperiorSkyblockAPI.getProviders().getWorldsProvider();
    }

    public static boolean isIslandsWorld(World world) {
        return getWorldsProvider().isIslandsWorld(world);
    }
}
