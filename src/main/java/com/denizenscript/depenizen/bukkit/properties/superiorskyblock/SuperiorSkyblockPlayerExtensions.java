package com.denizenscript.depenizen.bukkit.properties.superiorskyblock;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.island.PlayerRole;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.depenizen.bukkit.objects.superiorskyblock.SuperiorSkyblockIslandTag;

public class SuperiorSkyblockPlayerExtensions {

    public SuperiorSkyblockPlayerExtensions(PlayerTag player) {
        this.player = player;
    }

    PlayerTag player;

    public static void register() {

        // <--[tag]
        // @attribute <PlayerTag.superiorskyblock_bypass_mode>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, SuperiorSkyblock
        // @mechanism PlayerTag.superiorskyblock_bypass_mode
        // @description
        // Returns whether a player can build on islands that they are not a part of.
        // -->
        PlayerTag.tagProcessor.registerTag(ElementTag.class, "superiorskyblock_bypass_mode", (attribute, player) -> {
            return new ElementTag(SuperiorSkyblockAPI.getPlayer(player.getUUID()).hasBypassModeEnabled());
        });

        // <--[tag]
        // @attribute <PlayerTag.superiorskyblock_disbands>
        // @returns ElementTag(Number)
        // @plugin Depenizen, SuperiorSkyblock
        // @mechanism PlayerTag.superiorskyblock_disbands
        // @description
        // Returns the amount of disbands a player has left.
        // -->
        PlayerTag.tagProcessor.registerTag(ElementTag.class, "superiorskyblock_disbands", (attribute, player) -> {
            return new ElementTag(SuperiorSkyblockAPI.getPlayer(player.getUUID()).getDisbands());
        });

        // <--[tag]
        // @attribute <PlayerTag.superiorskyblock_island>
        // @returns SuperiorSkyblockIslandTag
        // @plugin Depenizen, SuperiorSkyblock
        // @description
        // Returns the island a player belongs to, if any.
        // -->
        PlayerTag.tagProcessor.registerTag(SuperiorSkyblockIslandTag.class, "superiorskyblock_island", (attribute, player) -> {
            Island island = SuperiorSkyblockAPI.getPlayer(player.getUUID()).getIsland();
            if (island != null) {
                return new SuperiorSkyblockIslandTag(island);
            }
            return null;
        });

        // <--[tag]
        // @attribute <PlayerTag.superiorskyblock_island_role>
        // @returns ElementTag
        // @plugin Depenizen, SuperiorSkyblock
        // @description
        // Returns the role a player has on an island, if any.
        // -->
        PlayerTag.tagProcessor.registerTag(ElementTag.class, "superiorskyblock_island_role", (attribute, player) -> {
            PlayerRole role = SuperiorSkyblockAPI.getPlayer(player.getUUID()).getPlayerRole();
            if (role != null) {
                return new ElementTag(role.getName(), true);
            }
            return null;
        });

        // <--[tag]
        // @attribute <PlayerTag.superiorskyblock_spy_mode>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, SuperiorSkyblock
        // @mechanism PlayerTag.superiorskyblock_spy_mode
        // @description
        // Returns whether a player can see team chat from all islands.
        // -->
        PlayerTag.tagProcessor.registerTag(ElementTag.class, "superiorskyblock_spy_mode", (attribute, player) -> {
            return new ElementTag(SuperiorSkyblockAPI.getPlayer(player.getUUID()).hasAdminSpyEnabled());
        });

        // <--[mechanism]
        // @object PlayerTag
        // @name superiorskyblock_bypass_mode
        // @input ElementTag(Boolean)
        // @plugin Depenizen, SuperiorSkyblock
        // @description
        // Changes whether a player can build on islands that are not a part of.
        // @tags
        // <PlayerTag.superiorskyblock_bypass_mode>
        // -->
        PlayerTag.registerOnlineOnlyMechanism("superiorskyblock_bypass_mode", ElementTag.class, (player, mechanism, value) -> {
            if (mechanism.requireBoolean()) {
                SuperiorSkyblockAPI.getPlayer(player.getUUID()).setBypassMode(mechanism.getValue().asBoolean());
            }
        });

        // <--[mechanism]
        // @object PlayerTag
        // @name superiorskyblock_disbands
        // @input ElementTag(Number)
        // @plugin Depenizen, SuperiorSkyblock
        // @description
        // Changes how many disbands a player can use.
        // @tags
        // <PlayerTag.superiorskyblock_disbands>
        // -->
        PlayerTag.registerOnlineOnlyMechanism("superiorskyblock_disbands", ElementTag.class, (player, mechanism, value) -> {
            if (mechanism.requireInteger()) {
                SuperiorSkyblockAPI.getPlayer(player.getUUID()).setDisbands(mechanism.getValue().asInt());
            }
        });

        // <--[mechanism]
        // @object PlayerTag
        // @name superiorskyblock_spy_mode
        // @input ElementTag(Boolean)
        // @plugin Depenizen, SuperiorSkyblock
        // @description
        // Changes whether a player can see team chats from all islands.
        // @tags
        // <PlayerTag.superiorskyblock_spy_mode>
        // -->
        PlayerTag.registerOnlineOnlyMechanism("superiorskyblock_spy_mode", ElementTag.class, (player, mechanism, value) -> {
            if (mechanism.requireBoolean()) {
                SuperiorSkyblockAPI.getPlayer(player.getUUID()).setAdminSpy(mechanism.getValue().asBoolean());
            }
        });
    }
}
