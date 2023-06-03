package com.denizenscript.depenizen.bukkit.objects.factions;

import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.massivecraft.factions.entity.*;
import com.massivecraft.massivecore.money.Money;
import com.massivecraft.massivecore.ps.PS;
import com.denizenscript.denizen.objects.ChunkTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.Fetchable;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.TagContext;

import java.util.Set;

public class FactionTag implements ObjectTag {

    // <--[ObjectType]
    // @name FactionTag
    // @prefix faction
    // @base ElementTag
    // @format
    // The identity format for factions is <faction_name>
    // For example, 'faction@my_faction'.
    //
    // @plugin Depenizen, Factions
    // @description
    // A FactionTag represents a Factions faction.
    //
    // -->

    /////////////////////
    //   OBJECT FETCHER
    /////////////////

    @Fetchable("faction")
    public static FactionTag valueOf(String string, TagContext context) {
        if (string == null) {
            return null;
        }

        ////////
        // Match faction name

        string = string.replace("faction@", "");
        Faction faction = FactionColl.get().getByName(string);
        if (faction != null) {
            return new FactionTag(faction);
        }

        return null;
    }

    public static boolean matches(String arg) {
        if (valueOf(arg, CoreUtilities.noDebugContext) != null) {
            return true;
        }

        return false;
    }

    /////////////////////
    //   STATIC CONSTRUCTORS
    /////////////////

    Faction faction = null;

    public FactionTag(Faction faction) {
        if (faction != null) {
            this.faction = faction;
        }
        else {
            Debug.echoError("Faction referenced is null!");
        }
    }

    public Faction getFaction() {
        return faction;
    }

    /////////////////////
    //   ObjectTag Methods
    /////////////////

    private String prefix = "Faction";

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
        return "faction@" + faction.getName();
    }

    @Override
    public String identifySimple() {
        // TODO: Properties?
        return identify();
    }

    @Override
    public String toString() {
        return identify();
    }

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <FactionTag.balance>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, Factions
        // @description
        // Returns the amount of money the faction currently has.
        // -->
        if (attribute.startsWith("balance")) {
            return new ElementTag(Money.get(faction))
                    .getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <FactionTag.warp[<name>]>
        // @returns LocationTag
        // @plugin Depenizen, Factions
        // @description
        // Returns the location of the faction's warp by name, if any.
        // Note that this was previously named "home" instead of "warp".
        // -->
        else if (attribute.startsWith("warp") && attribute.hasParam()) {
            Warp warp = faction.getWarp(attribute.getParam());
            if (warp != null) {
                return new LocationTag(warp.getLocation().asBukkitLocation())
                        .getObjectAttribute(attribute.fulfill(1));
            }
        }
        else if (attribute.startsWith("home")) { // Legacy sorta-compat
            Warp warp = faction.getWarp("home");
            if (warp != null) {
                return new LocationTag(warp.getLocation().asBukkitLocation())
                        .getObjectAttribute(attribute.fulfill(1));
            }
        }

        // <--[tag]
        // @attribute <FactionTag.id>
        // @returns ElementTag
        // @plugin Depenizen, Factions
        // @description
        // Returns the unique ID for this faction.
        // -->
        else if (attribute.startsWith("id")) {
            return new ElementTag(faction.getId()).getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <FactionTag.is_open>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Factions
        // @description
        // Returns true if the faction is open.
        // -->
        else if (attribute.startsWith("isopen") || attribute.startsWith("is_open")) {
            return new ElementTag(faction.isOpen())
                    .getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <FactionTag.is_peaceful>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Factions
        // @description
        // Returns true if the faction is peaceful.
        // -->
        else if (attribute.startsWith("ispeaceful") || attribute.startsWith("is_peaceful")) {
            return new ElementTag(faction.getFlag(MFlag.getFlagPeaceful()))
                    .getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <FactionTag.is_permanent>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Factions
        // @description
        // Returns true if the faction is permanent.
        // -->
        else if (attribute.startsWith("ispermanent") || attribute.startsWith("is_permanent")) {
            return new ElementTag(faction.getFlag(MFlag.getFlagPermanent()))
                    .getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <FactionTag.leader>
        // @returns PlayerTag
        // @plugin Depenizen, Factions
        // @description
        // Returns the faction's leader as a PlayerTag.
        // -->
        else if (attribute.startsWith("leader")) {
            if (faction.getLeader() != null) {
                return new PlayerTag(faction.getLeader().getUuid())
                        .getObjectAttribute(attribute.fulfill(1));
            }
        }

        // <--[tag]
        // @attribute <FactionTag.name>
        // @returns ElementTag
        // @plugin Depenizen, Factions
        // @description
        // Returns the name of the faction.
        // -->
        else if (attribute.startsWith("name")) {
            return new ElementTag(faction.getName())
                    .getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <FactionTag.player_count>
        // @returns ElementTag(Number)
        // @plugin Depenizen, Factions
        // @description
        // Returns the number of players in the faction.
        // -->
        else if (attribute.startsWith("playercount") || attribute.startsWith("player_count")) {
            return new ElementTag(faction.getMPlayers().size())
                    .getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <FactionTag.power>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, Factions
        // @description
        // Returns the amount of power the faction currently has.
        // -->
        else if (attribute.startsWith("power")) {
            return new ElementTag(faction.getPower())
                    .getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <FactionTag.relation[<faction>]>
        // @returns ElementTag
        // @plugin Depenizen, Factions
        // @description
        // Returns the current relation between the faction and another faction.
        // -->
        else if (attribute.startsWith("relation")) {
            FactionTag to = valueOf(attribute.getParam(), attribute.context);
            if (to != null) {
                return new ElementTag(faction.getRelationTo(to.getFaction()).toString())
                        .getObjectAttribute(attribute.fulfill(1));
            }
        }

        // <--[tag]
        // @attribute <FactionTag.size>
        // @returns ElementTag(Number)
        // @plugin Depenizen, Factions
        // @description
        // Returns the amount of land the faction has.
        // -->
        else if (attribute.startsWith("size")) {
            return new ElementTag(faction.getLandCount())
                    .getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <FactionTag.claimed_chunks>
        // @returns ListTag(ChunkTag)
        // @plugin Depenizen, Factions
        // @description
        // Returns a list of all chunks claimed in the faction.
        // -->
        if (attribute.startsWith("claimed_chunks")) {
            Set<PS> chunks = BoardColl.get().getChunks(faction);
            ListTag dchunks = new ListTag();
            for (PS ps : chunks) {
                dchunks.addObject(new ChunkTag(ps.asBukkitChunk()));
            }
            return dchunks.getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <FactionTag.list_players>
        // @returns ListTag(PlayerTag)
        // @plugin Depenizen, Factions
        // @description
        // Returns a list of all players in the faction.
        // -->
        if (attribute.startsWith("list_players")) {
            ListTag players = new ListTag();
            for (MPlayer ps : faction.getMPlayers()) {
                players.addObject(new PlayerTag(ps.getUuid()));
            }
            return players.getObjectAttribute(attribute.fulfill(1));
        }

        return new ElementTag(identify()).getObjectAttribute(attribute);

    }
}
