package com.denizenscript.depenizen.bukkit.objects.superiorskyblock;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.enums.Rating;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.Adjustable;
import com.denizenscript.denizencore.objects.Fetchable;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.ObjectTagProcessor;
import com.denizenscript.denizencore.tags.TagContext;

public class SuperiorSkyblockIslandTag implements ObjectTag, Adjustable {

    // <--[ObjectType]
    // @name SuperiorSkyblockIslandTag
    // @prefix island
    // @base ElementTag
    // @format
    // The identity format for group is <island_name>
    // For example, 'island@denizen_scripters'.
    //
    // @plugin Depenizen, SuperiorSkyblock
    // @description
    // A SuperiorSkyblockIslandTag represents a SuperiorSkyblock island.
    //
    // -->

    @Fetchable("island")
    public static SuperiorSkyblockIslandTag valueOf(String string, TagContext context) {
        if (string.startsWith("island@")) {
            string = string.substring(7);
        }
        Island island = SuperiorSkyblockAPI.getIsland(string);
        if (island == null) {
            return null;
        }
        return new SuperiorSkyblockIslandTag(island);
    }

    public static boolean matches(String string) {
        string = string.replace("island@", "");
        return SuperiorSkyblockAPI.getIsland(string) != null;
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
        return "island@" + island.getName();
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
        // @attribute <SuperiorSkyblockIslandTag.balance>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, SuperiorSkyblock
        // @mechanism SuperiorSkyblockIslandTag.balance
        // @description
        // Returns the balance of the specified island.
        // -->
        tagProcessor.registerTag(ElementTag.class, "balance", (attribute, object) -> {
            return new ElementTag(object.getIsland().getIslandBank().getBalance());
        });

        // <--[tag]
        // @attribute <SuperiorSkyblockIslandTag.banned_players>
        // @returns ListTag(PlayerTag)
        // @plugin Depenizen, SuperiorSkyblock
        // @description
        // Returns a list of players banned on the specified island.
        // -->
        tagProcessor.registerTag(ListTag.class, "banned_players", (attribute, object) -> {
            return new ListTag(object.getIsland().getBannedPlayers(), players -> new PlayerTag(players.asPlayer()));
        });

        // <--[tag]
        // @attribute <SuperiorSkyblockIslandTag.bonus_level>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, SuperiorSkyblock
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
        // @description
        // Returns the bonus worth of an island.
        // -->
        tagProcessor.registerTag(ElementTag.class, "bonus_worth", (attribute, object) -> {
            return new ElementTag(object.getIsland().getBonusWorth());
        });

        // <--[tag]
        // @attribute <SuperiorSkyblockIslandTag.coop_members>
        // @returns ListTag(PlayerTag)
        // @plugin Depenizen, SuperiorSkyblock
        // @description
        // Returns the coop members of the specified island, if any.
        // -->
        tagProcessor.registerTag(ListTag.class, "coop_members", (attribute, object) -> {
            return new ListTag(object.getIsland().getCoopPlayers(), players -> new PlayerTag(players.asPlayer()));
        });

        // <--[tag]
        // @attribute <SuperiorSkyblockIslandTag.current_visitors>
        // @returns ListTag(PlayerTag)
        // @plugin Depenizen, SuperiorSkyblock
        // @description
        // Returns the players currently within the bounds of the specified island.
        // -->
        tagProcessor.registerTag(ListTag.class, "current_visitors", (attribute, object) -> {
            return new ListTag(object.getIsland().getAllPlayersInside(), players -> new PlayerTag(players.asPlayer()));
        });

        // <--[tag]
        // @attribute <SuperiorSkyblockIslandTag.description>
        // @returns ElementTag
        // @plugin Depenizen, SuperiorSkyblock
        // @description
        // Returns the description of the specified island, if any.
        // -->
        tagProcessor.registerTag(ElementTag.class, "description", (attribute, object) -> {
            return object.getIsland().getDescription().isBlank() ? null : new ElementTag(object.getIsland().getDescription(), true);
        });

        // <--[tag]
        // @attribute <SuperiorSkyblockIslandTag.level>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, SuperiorSkyblock
        // @description
        // Returns the level of the specified island.
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
        // @description
        // Returns the members of the specified island, including the leader.
        // -->
        tagProcessor.registerTag(ListTag.class, "members", (attribute, object) -> {
            return new ListTag(object.getIsland().getIslandMembers(true), players -> new PlayerTag(players.asPlayer()));
        });

        // <--[tag]
        // @attribute <SuperiorSkyblockIslandTag.name>
        // @returns ElementTag
        // @plugin Depenizen, SuperiorSkyblock
        // @mechanism SuperiorSkyblockIslandTag.name
        // @description
        // Returns the name of the specified island.
        // -->
        tagProcessor.registerTag(ElementTag.class, "name", (attribute, object) -> {
            return new ElementTag(object.getIsland().getName(), true);
        });

        // <--[tag]
        // @attribute <SuperiorSkyblockIslandTag.net_worth>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, SuperiorSkyblock
        // @description
        // Returns the net worth of the specified island. Includes money in the bank.
        // -->
        tagProcessor.registerTag(ElementTag.class, "net_worth", (attribute, object) -> {
            return new ElementTag(object.getIsland().getWorth());
        });

        // <--[tag]
        // @attribute <SuperiorSkyblockIslandTag.owner>
        // @returns PlayerTag
        // @plugin Depenizen, SuperiorSkyblock
        // @description
        // Returns the owner of the specified island.
        // -->
        tagProcessor.registerTag(PlayerTag.class, "owner", (attribute, object) -> {
            return new PlayerTag(object.getIsland().getOwner().asPlayer());
        });

        // <--[tag]
        // @attribute <SuperiorSkyblockIslandTag.rating[<player>]>
        // @returns ElementTag(Number)
        // @plugin Depenizen, SuperiorSkyblock
        // @description
        // Returns the rating a player gave the specified island, if any.
        // -->
        tagProcessor.registerTag(ElementTag.class, PlayerTag.class, "rating", (attribute, object, player) -> {
            Rating rating = object.getIsland().getRating(SuperiorSkyblockAPI.getPlayer(player.getUUID()));
            if (rating.getValue() > -1) {
                return new ElementTag(rating.getValue());
            }
            return null;
        });

        // <--[tag]
        // @attribute <SuperiorSkyblockIslandTag.schematic>
        // @returns ElementTag
        // @plugin Depenizen, SuperiorSkyblock
        // @description
        // Returns the schematic used to create the specified island.
        // -->
        tagProcessor.registerTag(ElementTag.class, "schematic", (attribute, object) -> {
            return new ElementTag(object.getIsland().getSchematicName(), true);
        });

        // <--[tag]
        // @attribute <SuperiorSkyblockIslandTag.size>
        // @returns ElementTag(Number)
        // @plugin Depenizen, SuperiorSkyblock
        // @mechanism SuperiorSkyblockIslandTag.size
        // @description
        // Returns the size of the specified island.
        // -->
        tagProcessor.registerTag(ElementTag.class, "size", (attribute, object) -> {
            return new ElementTag(object.getIsland().getIslandSize());
        });

        // <--[tag]
        // @attribute <SuperiorSkyblockIslandTag.total_rating>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, SuperiorSkyblock
        // @description
        // Returns the current average rating of the specified island.
        // -->
        tagProcessor.registerTag(ElementTag.class, "total_rating", (attribute, object) -> {
            return new ElementTag(object.getIsland().getTotalRating());
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
            if (mechanism.requireDouble()) {
                object.getIsland().getIslandBank().setBalance(mechanism.getValue().asBigDecimal());
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
            object.getIsland().setDescription(mechanism.getValue().asString());
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
            if (mechanism.requireBoolean()) {
                object.getIsland().setLocked(mechanism.getValue().asBoolean());
            }
        });

        // <--[mechanism]
        // @object SuperiorSkyblockIslandTag
        // @name name
        // @input ElementTag
        // @plugin Depenizen, SuperiorSkyblock
        // @description
        // Changes an island's name.
        // @tags
        // <SuperiorSkyblockIslandTag.name>
        // -->
        tagProcessor.registerMechanism("name", false, ElementTag.class, (object, mechanism, value) -> {
            if (!mechanism.getValue().asString().isEmpty()) {
                object.getIsland().setName(mechanism.getValue().toString());
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
            if (mechanism.requireInteger() && mechanism.getValue().asInt() >= 1) {
                object.getIsland().setIslandSize(mechanism.getValue().asInt());
            }
        });
    }

    public static final ObjectTagProcessor<SuperiorSkyblockIslandTag> tagProcessor = new ObjectTagProcessor<>();

}
