package com.denizenscript.depenizen.bukkit.objects.superiorskyblock;

import com.bgsoftware.superiorskyblock.SuperiorSkyblockPlugin;
import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.enums.MemberRemoveReason;
import com.bgsoftware.superiorskyblock.api.enums.Rating;
import com.bgsoftware.superiorskyblock.api.hooks.WorldsProvider;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.island.IslandFlag;
import com.bgsoftware.superiorskyblock.api.world.Dimension;
import com.bgsoftware.superiorskyblock.api.wrappers.WorldPosition;
import com.denizenscript.denizen.objects.*;
import com.denizenscript.denizencore.objects.*;
import com.denizenscript.denizencore.objects.core.*;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.ObjectTagProcessor;
import com.denizenscript.denizencore.tags.TagContext;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.denizencore.utilities.text.StringHolder;
import com.denizenscript.depenizen.bukkit.bridges.SuperiorSkyblockBridge;
import org.bukkit.Registry;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.UUID;

public class SuperiorSkyblockIslandTag implements ObjectTag, Adjustable {

    // <--[ObjectType]
    // @name SuperiorSkyblockIslandTag
    // @prefix superiorskyblock_island
    // @base ElementTag
    // @format
    // The identity format is <island_uuid>.
    // For example, 'superiorskyblock_island@460e96b9-7a0e-416d-b2c3-4508164b8b1b'.
    //
    // @plugin Depenizen, SuperiorSkyblock
    // @description
    // A SuperiorSkyblockIslandTag represents a SuperiorSkyblock island.
    // -->

    @Fetchable("superiorskyblock_island")
    public static SuperiorSkyblockIslandTag valueOf(String string, TagContext context) {
        if (string.startsWith("superiorskyblock_island@")) {
            string = string.substring("superiorskyblock_island@".length());
        }
        try {
            UUID uuid = UUID.fromString(string);
            Island island = uuid.equals(spawnIsland) ? SuperiorSkyblockAPI.getSpawnIsland() : SuperiorSkyblockAPI.getIslandByUUID(uuid);
            if (island != null) {
                return new SuperiorSkyblockIslandTag(island);
            }
            if (context == null || context.showErrors()) {
                Debug.echoError("SuperiorSkyblockIslandTag returning null: UUID '" + string + "' is valid, but doesn't match any island.");
            }
            return null;
        }
        catch (IllegalArgumentException e) {
            if (context == null || context.showErrors()) {
                Debug.echoError("SuperiorSkyblockIslandTag returning null: Invalid UUID '" + string + "' specified.");
            }
            return null;
        }
    }

    // Yes, this is the actual UUID that is associated with the spawn island.
    public static final UUID spawnIsland = UUID.fromString("00000000-0000-0000-0000-000000000000");

    public static boolean isSpawnIsland(SuperiorSkyblockIslandTag island) {
        return island.getIsland().isSpawn();
    }

    public static boolean matches(String string) {
        if (string.startsWith("superiorskyblock_island@")) {
            return true;
        }
        return valueOf(string, CoreUtilities.noDebugContext) != null;
    }

    public SuperiorSkyblockIslandTag(Island island) {
        this.island = island;
    }

    Island island;

    String prefix = "SuperiorSkyblockIsland";

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public ObjectTag setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    @Override
    public boolean isUnique() {
        return true;
    }

    @Override
    public String identify() {
        return "superiorskyblock_island@" + island.getUniqueId();
    }

    @Override
    public String identifySimple() {
        return identify();
    }

    @Override
    public String toString() {
        return identify();
    }

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {
        return tagProcessor.getObjectAttribute(this, attribute);
    }

    @Override
    public void adjust(Mechanism mechanism) {
        tagProcessor.processMechanism(this, mechanism);
    }

    @Override
    public void applyProperty(Mechanism mechanism) {
        mechanism.echoError("Cannot apply Properties to a Superior Skyblock Island!");
    }

    public Island getIsland() {
        return island;
    }

    public static void register() {

        // <--[tag]
        // @attribute <SuperiorSkyblockIslandTag.average_rating>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, SuperiorSkyblock
        // @description
        // Returns the current average rating of an island.
        // -->
        tagProcessor.registerTag(ElementTag.class, "average_rating", (attribute, object) -> {
            return new ElementTag(object.getIsland().getTotalRating());
        });

        // <--[tag]
        // @attribute <SuperiorSkyblockIslandTag.balance>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, SuperiorSkyblock
        // @mechanism SuperiorSkyblockIslandTag.balance
        // @description
        // Returns the bank balance of an island.
        // -->
        tagProcessor.registerTag(ElementTag.class, "balance", (attribute, object) -> {
            if (isSpawnIsland(object)) {
                attribute.echoError("Spawn islands do not have a balance.");
                return null;
            }
            return new ElementTag(object.getIsland().getIslandBank().getBalance());
        });

        // <--[tag]
        // @attribute <SuperiorSkyblockIslandTag.banned_players>
        // @returns ListTag(PlayerTag)
        // @plugin Depenizen, SuperiorSkyblock
        // @mechanism SuperiorSkyblockIslandTag.ban_player
        // @mechanism SuperiorSkyblockIslandTag.unban_player
        // @description
        // Returns the players banned on an island.
        // -->
        tagProcessor.registerTag(ListTag.class, "banned_players", (attribute, object) -> {
            return new ListTag(object.getIsland().getBannedPlayers(), player -> new PlayerTag(player.asPlayer()));
        });

        // <--[tag]
        // @attribute <SuperiorSkyblockIslandTag.biome>
        // @returns BiomeTag
        // @plugin Depenizen, SuperiorSkyblock
        // @mechanism SuperiorSkyblockIslandTag.biome
        // @description
        // Returns the current biome of an island.
        // -->
        tagProcessor.registerTag(BiomeTag.class, "biome", (attribute, object) -> {
            return new BiomeTag(object.getIsland().getBiome(SuperiorSkyblockAPI.getSettings().getWorlds().getDefaultWorldDimension()));
        });

        // <--[tag]
        // @attribute <SuperiorSkyblockIslandTag.bonus_level>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, SuperiorSkyblock
        // @mechanism SuperiorSkyblockIslandTag.bonus_level
        // @description
        // Returns the bonus level of an island.
        // -->
        tagProcessor.registerTag(ElementTag.class, "bonus_level", (attribute, object) -> {
            return new ElementTag(object.getIsland().getBonusLevel());
        });

        // <--[tag]
        // @attribute <SuperiorSkyblockIslandTag.bonus_worth>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, SuperiorSkyblock
        // @mechanism SuperiorSkyblockIslandTag.bonus_worth
        // @description
        // Returns the bonus worth of an island.
        // -->
        tagProcessor.registerTag(ElementTag.class, "bonus_worth", (attribute, object) -> {
            return new ElementTag(object.getIsland().getBonusWorth());
        });

        // <--[tag]
        // @attribute <SuperiorSkyblockIslandTag.center[<world>]>
        // @returns LocationTag
        // @plugin Depenizen, SuperiorSkyblock
        // @description
        // Returns the center of an island in the provided world.
        // -->
        tagProcessor.registerTag(LocationTag.class, WorldTag.class, "center", (attribute, object, value) -> {
            if (!SuperiorSkyblockBridge.isIslandsWorld(value.getWorld())) {
                attribute.echoError("The provided world does not contain islands.");
                return null;
            }
            return new LocationTag(object.getIsland().getCenter(SuperiorSkyblockBridge.getIslandsWorldDimension(value.getWorld())));
        });

        // <--[tag]
        // @attribute <SuperiorSkyblockIslandTag.coop_members>
        // @returns ListTag(PlayerTag)
        // @plugin Depenizen, SuperiorSkyblock
        // @mechanism SuperiorSkyblockIslandTag.add_coop_member
        // @mechanism SuperiorSkyblockIslandTag.kick_coop_member
        // @description
        // Returns the coop members of an island.
        // -->
        tagProcessor.registerTag(ListTag.class, "coop_members", (attribute, object) -> {
            return new ListTag(object.getIsland().getCoopPlayers(), player -> new PlayerTag(player.asPlayer()));
        });

        // <--[tag]
        // @attribute <SuperiorSkyblockIslandTag.creation_time>
        // @returns TimeTag
        // @plugin Depenizen, SuperiorSkyblock
        // @description
        // Returns a TimeTag of when an island was created.
        // The amount of detail shown depends on the 'Date Format' setting in SuperiorSkyblock's configuration file.
        // See <@link objecttype TimeTag> to see what is required.
        // -->
        tagProcessor.registerTag(TimeTag.class, "creation_time", (attribute, object) -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(SuperiorSkyblockPlugin.getPlugin().getSettings().getDateFormat());
            String creation = object.getIsland().getCreationTimeDate();
            try {
                return new TimeTag(LocalDateTime.parse(creation, formatter).atZone(ZoneOffset.UTC));
            }
            catch (DateTimeParseException e) {
                try {
                    LocalDate date = LocalDate.parse(creation, formatter);
                    return new TimeTag(date.getYear(), date.getMonthValue(), date.getDayOfMonth(), 0, 0, 0, 0, ZoneOffset.UTC);
                }
                catch (DateTimeParseException ex) {
                    attribute.echoError("SuperiorSkyblock's 'Date Format' setting is missing something necessary to form a TimeTag.");
                    return null;
                }
            }
            catch (IllegalArgumentException e) {
                attribute.echoError("SuperiorSkyblock's 'Date Format' setting has characters that do not correspond to a period of time.");
                return null;
            }
        });

        // <--[tag]
        // @attribute <SuperiorSkyblockIslandTag.cuboid>
        // @returns CuboidTag
        // @plugin Depenizen, SuperiorSkyblock
        // @description
        // Returns the boundaries of an island.
        // -->
        tagProcessor.registerTag(CuboidTag.class, "cuboid", (attribute, object) -> {
            return new CuboidTag(object.getIsland().getMinimum(), object.getIsland().getMaximum());
        });

        // <--[tag]
        // @attribute <SuperiorSkyblockIslandTag.current_visitors>
        // @returns ListTag(PlayerTag)
        // @plugin Depenizen, SuperiorSkyblock
        // @description
        // Returns the players currently within the bounds of an island.
        // -->
        tagProcessor.registerTag(ListTag.class, "current_visitors", (attribute, object) -> {
            return new ListTag(object.getIsland().getAllPlayersInside(), player -> new PlayerTag(player.asPlayer()));
        });

        // <--[tag]
        // @attribute <SuperiorSkyblockIslandTag.description>
        // @returns ElementTag
        // @plugin Depenizen, SuperiorSkyblock
        // @description
        // Returns the description of an island. This may be empty if an island does not have one set.
        // -->
        tagProcessor.registerTag(ElementTag.class, "description", (attribute, object) -> {
            return new ElementTag(object.getIsland().getDescription(), true);
        });

        // <--[tag]
        // @attribute <SuperiorSkyblockIslandTag.homes>
        // @returns ListTag(LocationTag)
        // @plugin Depenizen, SuperiorSkyblock
        // @description
        // Returns the home locations of an island.
        // -->
        tagProcessor.registerTag(ListTag.class, "homes", (attribute, object) -> {
            ListTag values = new ListTag();
            WorldsProvider provider = SuperiorSkyblockBridge.getWorldsProvider();
            for (Map.Entry<Dimension, WorldPosition> map : object.getIsland().getIslandHomes().entrySet()) {
                WorldPosition pos = map.getValue();
                values.addObject(new LocationTag(pos.getX(), pos.getY(), pos.getZ(), provider.getIslandsWorld(object.getIsland(), map.getKey()).getName()));
            }
            return values;
        });

        // <--[tag]
        // @attribute <SuperiorSkyblockIslandTag.is_spawn_island>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, SuperiorSkyblock
        // @description
        // Returns whether this is the spawn island.
        // -->
        tagProcessor.registerTag(ElementTag.class, "is_spawn_island", (attribute, object) -> {
            return new ElementTag(isSpawnIsland(object));
        });

        // <--[tag]
        // @attribute <SuperiorSkyblockIslandTag.leader>
        // @returns PlayerTag
        // @plugin Depenizen, SuperiorSkyblock
        // @mechanism <SuperiorSkyblockIslandTag.leader>
        // @description
        // Returns the leader of an island.
        // -->
        tagProcessor.registerTag(PlayerTag.class, "leader", (attribute, object) -> {
            if (isSpawnIsland(object)) {
                attribute.echoError("Spawn islands do not have a leader.");
                return null;
            }
            return new PlayerTag(object.getIsland().getOwner().asPlayer());
        });

        // <--[tag]
        // @attribute <SuperiorSkyblockIslandTag.level>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, SuperiorSkyblock
        // @description
        // Returns the level of an island.
        // -->
        tagProcessor.registerTag(ElementTag.class, "level", (attribute, object) -> {
            return new ElementTag(object.getIsland().getIslandLevel());
        });

        // <--[tag]
        // @attribute <SuperiorSkyblockIslandTag.locked>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, SuperiorSkyblock
        // @mechanism SuperiorSkyblockIslandTag.locked
        // @description
        // Returns whether an island is locked to visitors.
        // -->
        tagProcessor.registerTag(ElementTag.class, "locked", (attribute, object) -> {
            return new ElementTag(object.getIsland().isLocked());
        });

        // <--[tag]
        // @attribute <SuperiorSkyblockIslandTag.members>
        // @returns ListTag(PlayerTag)
        // @plugin Depenizen, SuperiorSkyblock
        // @mechanism <SuperiorSkyblockIslandTag.add_member>
        // @mechanism <SuperiorSkyblockIslandTag.kick_member>
        // @description
        // Returns the members of an island, including the leader.
        // -->
        tagProcessor.registerTag(ListTag.class, "members", (attribute, object) -> {
            return new ListTag(object.getIsland().getIslandMembers(true), player -> new PlayerTag(player.asPlayer()));
        });

        // <--[tag]
        // @attribute <SuperiorSkyblockIslandTag.name>
        // @returns ElementTag
        // @plugin Depenizen, SuperiorSkyblock
        // @mechanism SuperiorSkyblockIslandTag.name
        // @description
        // Returns the name of an island.
        // -->
        tagProcessor.registerTag(ElementTag.class, "name", (attribute, object) -> {
            return new ElementTag(object.getIsland().getName(), true);
        });

        // <--[tag]
        // @attribute <SuperiorSkyblockIslandTag.net_worth>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, SuperiorSkyblock
        // @mechanism SuperiorSkyblockIslandTag.recalculate_worth
        // @description
        // Returns the net worth of an island. Includes money in the bank.
        // -->
        tagProcessor.registerTag(ElementTag.class, "net_worth", (attribute, object) -> {
            return new ElementTag(object.getIsland().getWorth());
        });

        // <--[tag]
        // @attribute <SuperiorSkyblockIslandTag.rating[<player>]>
        // @returns ElementTag(Number)
        // @plugin Depenizen, SuperiorSkyblock
        // @description
        // Returns the rating a player gave an island, or null if the player hasn't given one.
        // -->
        tagProcessor.registerTag(ElementTag.class, PlayerTag.class, "rating", (attribute, object, player) -> {
            Rating rating = object.getIsland().getRating(SuperiorSkyblockBridge.getSuperiorPlayer(player));
            return rating.getValue() > -1 ? new ElementTag(rating.getValue()) : null;
        });

        // <--[tag]
        // @attribute <SuperiorSkyblockIslandTag.schematic>
        // @returns ElementTag
        // @plugin Depenizen, SuperiorSkyblock
        // @description
        // Returns the schematic used to create an island.
        // -->
        tagProcessor.registerTag(ElementTag.class, "schematic", (attribute, object) -> {
            return new ElementTag(object.getIsland().getSchematicName(), true);
        });

        // <--[tag]
        // @attribute <SuperiorSkyblockIslandTag.settings>
        // @returns MapTag
        // @plugin Depenizen, SuperiorSkyblock
        // @mechanism <SuperiorSkyblockIslandTag.settings>
        // @description
        // Returns the settings of an island in 'SETTING:VALUE' format.
        // See <@link tag SuperiorSkyblock.island_settings> for a list of possible settings.
        // -->
        tagProcessor.registerTag(MapTag.class, "settings", (attribute, object) -> {
            Island island = object.getIsland();
            MapTag values = new MapTag();
            for (IslandFlag flag : IslandFlag.values()) {
                values.putObject(flag.getName(), new ElementTag(island.hasSettingsEnabled(flag)));
            }
            return values;
        });

        // <--[tag]
        // @attribute <SuperiorSkyblockIslandTag.size>
        // @returns ElementTag(Number)
        // @plugin Depenizen, SuperiorSkyblock
        // @mechanism SuperiorSkyblockIslandTag.size
        // @description
        // Returns the world border width of an island.
        // -->
        tagProcessor.registerTag(ElementTag.class, "size", (attribute, object) -> {
            return new ElementTag(object.getIsland().getIslandSize());
        });

        // <--[tag]
        // @attribute <SuperiorSkyblockIslandTag.team_limit>
        // @returns ElementTag(Number)
        // @plugin Depenizen, SuperiorSkyblock
        // @mechanism SuperiorSkyblockIslandTag.team_limit
        // @description
        // Returns the maximum amount of players on an island's team.
        // -->
        tagProcessor.registerTag(ElementTag.class, "team_limit", (attribute, object) -> {
            return new ElementTag(object.getIsland().getTeamLimit());
        });

        // <--[tag]
        // @attribute <SuperiorSkyblockIslandTag.uuid>
        // @returns ElementTag
        // @plugin Depenizen, SuperiorSkyblock
        // @description
        // Returns the uuid of an island.
        // -->
        tagProcessor.registerTag(ElementTag.class, "uuid", (attribute, object) -> {
            return new ElementTag(object.getIsland().getUniqueId().toString(), true);
        });

        // <--[mechanism]
        // @object SuperiorSkyblockIslandTag
        // @name add_coop_member
        // @input PlayerTag
        // @plugin Depenizen, SuperiorSkyblock
        // @description
        // Adds a coop member to an island's team.
        // @tags
        // <SuperiorSkyblockIslandTag.coop_members>
        // -->
        tagProcessor.registerMechanism("add_coop_member", false, PlayerTag.class, (object, mechanism, value) -> {
            if (isSpawnIsland(object)) {
                mechanism.echoError("Spawn islands cannot have a team of players.");
            }
            else if (object.getIsland().getCoopPlayers().contains(SuperiorSkyblockBridge.getSuperiorPlayer(value))) {
                mechanism.echoError("This player is already part of the island.");
            }
            else {
                object.getIsland().addCoop(SuperiorSkyblockBridge.getSuperiorPlayer(value));
            }
        });

        // <--[mechanism]
        // @object SuperiorSkyblockIslandTag
        // @name add_member
        // @input PlayerTag
        // @plugin Depenizen, SuperiorSkyblock
        // @description
        // Adds a member to an island's team.
        // @tags
        // <SuperiorSkyblockIslandTag.members>
        // -->
        tagProcessor.registerMechanism("add_member", false, PlayerTag.class, (object, mechanism, value) -> {
            if (isSpawnIsland(object)) {
                mechanism.echoError("Spawn islands cannot have a team of players.");
            }
            else if (object.getIsland().getIslandMembers(true).contains(SuperiorSkyblockBridge.getSuperiorPlayer(value))) {
                mechanism.echoError("This player is already part of the island.");
            }
            else {
                object.getIsland().addMember(SuperiorSkyblockBridge.getSuperiorPlayer(value), SuperiorSkyblockPlugin.getPlugin().getRoles().getDefaultRole());
            }
        });

        // <--[mechanism]
        // @object SuperiorSkyblockIslandTag
        // @name balance
        // @input ElementTag(Decimal)
        // @plugin Depenizen, SuperiorSkyblock
        // @description
        // Changes an island's bank balance.
        // @tags
        // <SuperiorSkyblockIslandTag.balance>
        // -->
        tagProcessor.registerMechanism("balance", false, ElementTag.class, (object, mechanism, value) -> {
            if (isSpawnIsland(object)) {
                mechanism.echoError("Spawn islands cannot have a balance.");
            }
            else if (mechanism.requireDouble()) {
                object.getIsland().getIslandBank().setBalance(value.asBigDecimal());
            }
        });

        // <--[mechanism]
        // @object SuperiorSkyblockIslandTag
        // @name ban_player
        // @input PlayerTag
        // @plugin Depenizen, SuperiorSkyblock
        // @description
        // Bans a player from an island.
        // @tags
        // <SuperiorSkyblockIslandTag.banned_players>
        // -->
        tagProcessor.registerMechanism("ban_player", false, PlayerTag.class, (object, mechanism, value) -> {
            if (isSpawnIsland(object)) {
                mechanism.echoError("Spawn islands cannot have banned players.");
            }
            else if (object.getIsland().getBannedPlayers().contains(SuperiorSkyblockBridge.getSuperiorPlayer(value))) {
                mechanism.echoError("This player is already banned from the island.");
            }
            else {
                object.getIsland().banMember(SuperiorSkyblockBridge.getSuperiorPlayer(value));
            }
        });

        // <--[mechanism]
        // @object SuperiorSkyblockIslandTag
        // @name biome
        // @input BiomeTag
        // @plugin Depenizen, SuperiorSkyblock
        // @description
        // Sets the biome of an island.
        // @tags
        // <SuperiorSkyblockIslandTag.biome>
        // -->
        tagProcessor.registerMechanism("biome", false, BiomeTag.class, (object, mechanism, value) -> {
            object.getIsland().setBiome(SuperiorSkyblockAPI.getSettings().getWorlds().getDefaultWorldDimension(), Registry.BIOME.get(value.getBiome().getKey()));
        });

        // <--[mechanism]
        // @object SuperiorSkyblockIslandTag
        // @name bonus_level
        // @input ElementTag(Decimal)
        // @plugin Depenizen, SuperiorSkyblock
        // @description
        // Sets the bonus level given to an island.
        // @tags
        // <SuperiorSkyblockIslandTag.bonus_level>
        // -->
        tagProcessor.registerMechanism("bonus_level", false, ElementTag.class, (object, mechanism, value) -> {
            if (mechanism.requireDouble()) {
                object.getIsland().setBonusLevel(value.asBigDecimal());
            }
        });

        // <--[mechanism]
        // @object SuperiorSkyblockIslandTag
        // @name bonus_worth
        // @input ElementTag(Decimal)
        // @plugin Depenizen, SuperiorSkyblock
        // @description
        // Controls the bonus worth given to an island.
        // @tags
        // <SuperiorSkyblockIslandTag.bonus_worth>
        // -->
        tagProcessor.registerMechanism("bonus_worth", false, ElementTag.class, (object, mechanism, value) -> {
            if (mechanism.requireDouble()) {
                object.getIsland().setBonusWorth(value.asBigDecimal());
            }
        });

        // <--[mechanism]
        // @object SuperiorSkyblockIslandTag
        // @name description
        // @input ElementTag
        // @plugin Depenizen, SuperiorSkyblock
        // @description
        // Changes an island's description. Leave blank to remove.
        // @tags
        // <SuperiorSkyblockIslandTag.description>
        // -->
        tagProcessor.registerMechanism("description", false, ElementTag.class, (object, mechanism, value) -> {
            if (isSpawnIsland(object)) {
                mechanism.echoError("Spawn islands cannot have a description.");
            }
            else {
                object.getIsland().setDescription(value.asString());
            }
        });

        // <--[mechanism]
        // @object SuperiorSkyblockIslandTag
        // @name kick_coop_member
        // @input PlayerTag
        // @plugin Depenizen, SuperiorSkyblock
        // @description
        // Kick a coop member from an island's team.
        // @tags
        // <SuperiorSkyblockIslandTag.coop_members>
        // -->
        tagProcessor.registerMechanism("kick_coop_member", false, PlayerTag.class, (object, mechanism, value) -> {
            if (!(object.getIsland().getCoopPlayers().contains(SuperiorSkyblockBridge.getSuperiorPlayer(value)))) {
                mechanism.echoError("This player is already not a part of this island.");
            }
            else {
                object.getIsland().removeCoop(SuperiorSkyblockBridge.getSuperiorPlayer(value));
            }
        });

        // <--[mechanism]
        // @object SuperiorSkyblockIslandTag
        // @name kick_member
        // @input PlayerTag
        // @plugin Depenizen, SuperiorSkyblock
        // @description
        // Kick a member from an island's team.
        // @tags
        // <SuperiorSkyblockIslandTag.members>
        // -->
        tagProcessor.registerMechanism("kick_member", false, PlayerTag.class, (object, mechanism, value) -> {
            if (!(object.getIsland().getIslandMembers(true).contains(SuperiorSkyblockBridge.getSuperiorPlayer(value)))) {
                mechanism.echoError("This player is already not a part of this island.");
            }
            else {
                object.getIsland().removeMember(SuperiorSkyblockBridge.getSuperiorPlayer(value), MemberRemoveReason.KICK);
            }
        });

        // <--[mechanism]
        // @object SuperiorSkyblockIslandTag
        // @name leader
        // @input PlayerTag
        // @plugin Depenizen, SuperiorSkyblock
        // @description
        // Transfers island leadership to the specified player.
        // Since only one player can have the 'Leader' role at a time, the current leader will be shifted to the next highest role.
        // @tags
        // <SuperiorSkyblockIslandTag.leader>
        // -->
        tagProcessor.registerMechanism("leader", false, PlayerTag.class, (object, mechanism, value) -> {
            object.getIsland().transferIsland(SuperiorSkyblockBridge.getSuperiorPlayer(value));
        });

        // <--[mechanism]
        // @object SuperiorSkyblockIslandTag
        // @name locked
        // @input ElementTag(Boolean)
        // @plugin Depenizen, SuperiorSkyblock
        // @description
        // Changes whether an island is locked to visitors.
        // @tags
        // <SuperiorSkyblockIslandTag.locked>
        // -->
        tagProcessor.registerMechanism("locked", false, ElementTag.class, (object, mechanism, value) -> {
            if (isSpawnIsland(object)) {
                mechanism.echoError("Spawn islands cannot be locked.");
            }
            else if (mechanism.requireBoolean()) {
                object.getIsland().setLocked(value.asBoolean());
            }
        });

        // <--[mechanism]
        // @object SuperiorSkyblockIslandTag
        // @name name
        // @input ElementTag
        // @plugin Depenizen, SuperiorSkyblock
        // @description
        // Changes an island's name. Multiple islands cannot have the same name.
        // @tags
        // <SuperiorSkyblockIslandTag.name>
        // -->
        tagProcessor.registerMechanism("name", false, ElementTag.class, (object, mechanism, value) -> {
            if (isSpawnIsland(object)) {
                mechanism.echoError("Spawn islands cannot have a name.");
            }
            else if (value.asString().isEmpty()) {
                mechanism.echoError("You cannot have an island with no name.");
            }
            else if (SuperiorSkyblockAPI.getIsland(value.asString()) != null) {
                mechanism.echoError("There is already an island with the name '" + value + "'.");
            }
            else {
                object.getIsland().setName(value.toString());
            }
        });

        // <--[mechanism]
        // @object SuperiorSkyblockIslandTag
        // @name recalculate_worth
        // @input None
        // @plugin Depenizen, SuperiorSkyblock
        // @description
        // Recalculates an island's net worth.
        // @tags
        // <SuperiorSkyblockIslandTag.net_worth>
        // -->
        tagProcessor.registerMechanism("recalculate_worth", false, (object, mechanism) -> {
            object.getIsland().calcIslandWorth(null);
        });

        // <--[mechanism]
        // @object SuperiorSkyblockIslandTag
        // @name settings
        // @input MapTag
        // @plugin Depenizen, SuperiorSkyblock
        // @description
        // Controls the settings of an island.
        // See <@link tag SuperiorSkyblock.island_settings> for a list of possible settings.
        // @example
        // # Enables pvp and disables creeper explosions.
        // - adjust <[island]> settings:<map[pvp=true;creeper_explosion=false]>
        // @tags
        // <SuperiorSkyblockIslandTag.settings>
        // -->
        tagProcessor.registerMechanism("settings", false, MapTag.class, (object, mechanism, map) -> {
            for (Map.Entry<StringHolder, ObjectTag> setting : map.entrySet()) {
                IslandFlag key;
                try {
                    key = IslandFlag.getByName(setting.getKey().toString());
                }
                catch (NullPointerException e) {
                    mechanism.echoError("'" + setting.getKey() + "' is not a valid island setting.");
                    continue;
                }
                ElementTag value = setting.getValue().asElement();
                if (!(value.isBoolean())) {
                    mechanism.echoError("The setting for '" + key.getName() + "' is not a boolean.");
                }
                else if (value.asBoolean()) {
                    object.getIsland().enableSettings(key);
                }
                else {
                    object.getIsland().disableSettings(key);
                }
            }
        });

        // <--[mechanism]
        // @object SuperiorSkyblockIslandTag
        // @name size
        // @input ElementTag(Number)
        // @plugin Depenizen, SuperiorSkyblock
        // @description
        // Changes an island's border size.
        // @tags
        // <SuperiorSkyblockIslandTag.size>
        // -->
        tagProcessor.registerMechanism("size", false, ElementTag.class, (object, mechanism, value) -> {
            if (isSpawnIsland(object)) {
                mechanism.echoError("Spawn islands cannot have their size adjusted.");
                return;
            }
            else if (!(mechanism.requireInteger())) {
                return;
            }
            int max = SuperiorSkyblockPlugin.getPlugin().getSettings().getMaxIslandSize();
            if (value.asInt() < 1 || value.asInt() > max) {
                mechanism.echoError("Island size must be between 1 and " + max + ".");
            }
            else {
                object.getIsland().setIslandSize(value.asInt());
            }
        });

        // <--[mechanism]
        // @object SuperiorSkyblockIslandTag
        // @name team_limit
        // @input ElementTag(Number)
        // @plugin Depenizen, SuperiorSkyblock
        // @description
        // Changes the maximum amount of players on an island's team.
        // @tags
        // <SuperiorSkyblockIslandTag.team_limit>
        // -->
        tagProcessor.registerMechanism("team_limit", false, ElementTag.class, (object, mechanism, value) -> {
            if (isSpawnIsland(object)) {
                mechanism.echoError("Spawn islands cannot have a team.");
                return;
            }
            else if (!(mechanism.requireInteger())) {
                return;
            }
            if (value.asInt() < 1) {
                mechanism.echoError("Island team limit must be a positive integer.");
            }
            else {
                object.getIsland().setTeamLimit(value.asInt());
            }
        });

        // <--[mechanism]
        // @object SuperiorSkyblockIslandTag
        // @name unban_player
        // @input PlayerTag
        // @plugin Depenizen, SuperiorSkyblock
        // @description
        // Unbans a player from an island.
        // @tags
        // <SuperiorSkyblockIslandTag.banned_players>
        // -->
        tagProcessor.registerMechanism("unban_player", false, PlayerTag.class, (object, mechanism, value) -> {
            if (!(object.getIsland().getBannedPlayers().contains(SuperiorSkyblockBridge.getSuperiorPlayer(value)))) {
                mechanism.echoError("This player is not banned from the island.");
            }
            else {
                object.getIsland().unbanMember(SuperiorSkyblockBridge.getSuperiorPlayer(value));
            }
        });
    }

    public static ObjectTagProcessor<SuperiorSkyblockIslandTag> tagProcessor = new ObjectTagProcessor<>();

}
