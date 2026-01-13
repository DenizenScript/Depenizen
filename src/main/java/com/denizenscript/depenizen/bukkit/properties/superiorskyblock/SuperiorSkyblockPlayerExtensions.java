package com.denizenscript.depenizen.bukkit.properties.superiorskyblock;

import com.bgsoftware.superiorskyblock.SuperiorSkyblockPlugin;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.island.PlayerRole;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.depenizen.bukkit.bridges.SuperiorSkyblockBridge;
import com.denizenscript.depenizen.bukkit.objects.superiorskyblock.SuperiorSkyblockIslandTag;

public class SuperiorSkyblockPlayerExtensions {

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
            return new ElementTag(SuperiorSkyblockBridge.getSuperiorPlayer(player).hasBypassModeEnabled());
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
            return new ElementTag(SuperiorSkyblockBridge.getSuperiorPlayer(player).getDisbands());
        });

        // <--[tag]
        // @attribute <PlayerTag.superiorskyblock_island>
        // @returns SuperiorSkyblockIslandTag
        // @plugin Depenizen, SuperiorSkyblock
        // @description
        // Returns the island a player belongs to, if any.
        // -->
        PlayerTag.registerOnlineOnlyTag(SuperiorSkyblockIslandTag.class, "superiorskyblock_island", (attribute, player) -> {
            Island island = SuperiorSkyblockBridge.getSuperiorPlayer(player).getIsland();
            return island != null ? new SuperiorSkyblockIslandTag(island) : null;
        });

        // <--[tag]
        // @attribute <PlayerTag.superiorskyblock_island_role>
        // @returns ElementTag
        // @plugin Depenizen, SuperiorSkyblock
        // @mechanism <PlayerTag.superiorskyblock_island_role>
        // @description
        // Returns the role a player has on their island, if they are part of one.
        // -->
        PlayerTag.registerOnlineOnlyTag(ElementTag.class, "superiorskyblock_island_role", (attribute, player) -> {
            PlayerRole role = SuperiorSkyblockBridge.getSuperiorPlayer(player).getPlayerRole();
            return role != null ? new ElementTag(role.getName(), true) : null;
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
            return new ElementTag(SuperiorSkyblockBridge.getSuperiorPlayer(player).hasAdminSpyEnabled());
        });

        // <--[mechanism]
        // @object PlayerTag
        // @name superiorskyblock_bypass_mode
        // @input ElementTag(Boolean)
        // @plugin Depenizen, SuperiorSkyblock
        // @description
        // Changes whether a player can build on islands they are not a part of.
        // @tags
        // <PlayerTag.superiorskyblock_bypass_mode>
        // -->
        PlayerTag.registerOfflineMechanism("superiorskyblock_bypass_mode", ElementTag.class, (player, mechanism, value) -> {
            if (mechanism.requireBoolean()) {
                SuperiorSkyblockBridge.getSuperiorPlayer(player).setBypassMode(value.asBoolean());
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
        PlayerTag.registerOfflineMechanism("superiorskyblock_disbands", ElementTag.class, (player, mechanism, value) -> {
            if (mechanism.requireInteger()) {
                SuperiorSkyblockBridge.getSuperiorPlayer(player).setDisbands(value.asInt());
            }
        });

        // <--[mechanism]
        // @object PlayerTag
        // @name superiorskyblock_island_role
        // @input ElementTag
        // @plugin Depenizen, SuperiorSkyblock
        // @description
        // Changes what role a player has on their island.
        // Valid roles are Guest, Coop, Member, Moderator, and Admin.
        // See <@link mechanism SuperiorSkyblockIslandTag.leader> to transfer the island's 'Leader' role.
        // @tags
        // <PlayerTag.superiorskyblock_island_role>
        // -->
        PlayerTag.registerOnlineOnlyMechanism("superiorskyblock_island_role", ElementTag.class, (player, mechanism, value) -> {
            SuperiorPlayer supPlayer = SuperiorSkyblockBridge.getSuperiorPlayer(player);
            if (!(supPlayer.hasIsland())) {
                mechanism.echoError("This player is not part of an island.");
                return;
            }
            PlayerRole role = SuperiorSkyblockPlugin.getPlugin().getRoles().getPlayerRole(value.toString());
            if (role == null) {
                mechanism.echoError("'" + value + "' is not a valid player role.");
            }
            else if (supPlayer.getPlayerRole().isLastRole() || role.isLastRole()) {
                mechanism.echoError("Changes involving the 'leader' role cannot be done through the 'PlayerTag.superiorskyblock_island_role' mechanism. Use the 'SuperiorSkyblockIslandTag.leader' mechanism instead.");
            }
            else {
                supPlayer.setPlayerRole(role);
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
        PlayerTag.registerOfflineMechanism("superiorskyblock_spy_mode", ElementTag.class, (player, mechanism, value) -> {
            if (mechanism.requireBoolean()) {
                SuperiorSkyblockBridge.getSuperiorPlayer(player).setAdminSpy(value.asBoolean());
            }
        });
    }
}
