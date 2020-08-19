package com.denizenscript.depenizen.bukkit.objects.towny;

import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.debugging.Debug;
import com.denizenscript.denizencore.objects.Fetchable;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.tags.ObjectTagProcessor;
import com.denizenscript.denizencore.tags.TagContext;
import com.denizenscript.denizencore.tags.TagRunnable;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.palmergames.bukkit.towny.exceptions.EconomyException;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import com.palmergames.bukkit.towny.object.WorldCoord;
import org.bukkit.Location;

public class TownTag implements ObjectTag {

    // <--[language]
    // @name TownTag Objects
    // @group Depenizen Object Types
    // @plugin Depenizen, Towny
    // @description
    // A TownTag represents a Towny town in the world.
    //
    // These use the object notation "town@".
    // The identity format for towns is <town_name>
    // For example, 'town@mytown'.
    //
    // -->

    /////////////////////
    //   OBJECT FETCHER
    /////////////////

    public static TownTag valueOf(String string) {
        return valueOf(string, null);
    }

    @Fetchable("town")
    public static TownTag valueOf(String string, TagContext context) {
        if (string == null) {
            return null;
        }

        ////////
        // Match town name

        string = string.replace("town@", "");
        try {
            return new TownTag(TownyUniverse.getDataSource().getTown(string));
        }
        catch (NotRegisteredException e) {
            return null;
        }
    }

    public static boolean matches(String arg) {
        arg = arg.replace("town@", "");
        return TownyUniverse.getDataSource().hasTown(arg);
    }

    /////////////////////
    //   STATIC CONSTRUCTORS
    /////////////////

    Town town;

    public TownTag(Town town) {
        this.town = town;
    }

    public static TownTag fromWorldCoord(WorldCoord coord) {
        if (coord == null) {
            return null;
        }
        try {
            return new TownTag(coord.getTownBlock().getTown());
        }
        catch (NotRegisteredException e) {
            return null;
        }
    }

    /////////////////////
    //   ObjectTag Methods
    /////////////////

    private String prefix = "Town";

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public TownTag setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    @Override
    public String debug() {
        return (prefix + "='<A>" + identify() + "<G>' ");
    }

    @Override
    public boolean isUnique() {
        return true;
    }

    @Override
    public String getObjectType() {
        return "Town";
    }

    @Override
    public String identify() {
        return "town@" + town.getName();
    }

    @Override
    public String identifySimple() {
        // TODO: Properties?
        return identify();
    }

    public Town getTown() {
        return town;
    }

    public boolean equals(TownTag town) {
        return CoreUtilities.equalsIgnoreCase(town.getTown().getName(), this.getTown().getName());
    }

    @Override
    public String toString() {
        return identify();
    }

    public static void registerTag(String name, TagRunnable.ObjectInterface<TownTag> runnable, String... variants) {
        tagProcessor.registerTag(name, runnable, variants);
    }

    public static ObjectTagProcessor<TownTag> tagProcessor = new ObjectTagProcessor<>();

    public static void registerTags() {

        // <--[tag]
        // @attribute <TownTag.assistants>
        // @returns ListTag
        // @plugin Depenizen, Towny
        // @description
        // Returns a list of the town's assistants. Players will be valid PlayerTag instances, non-players will be plaintext of the name.
        // -->
        registerTag("assistants", (attribute, object) -> {
            ListTag list = new ListTag();
            for (Resident resident : object.town.getAssistants()) {
                PlayerTag player = PlayerTag.valueOf(resident.getName(), attribute.context);
                if (player != null) {
                    list.addObject(player);
                }
                else {
                    list.add(resident.getName());
                }
            }
            return list;
        });

        // <--[tag]
        // @attribute <TownTag.balance>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, Towny
        // @description
        // Returns the current money balance of the town.
        // -->
        registerTag("balance", (attribute, object) -> {
            try {
                return new ElementTag(object.town.getAccount().getHoldingBalance());
            }
            catch (EconomyException e) {
                Debug.echoError("Invalid economy response!");
                return null;
            }
        });

        // <--[tag]
        // @attribute <TownTag.board>
        // @returns ElementTag
        // @plugin Depenizen, Towny
        // @description
        // Returns the town's current board.
        // -->
        registerTag("board", (attribute, object) -> {
            return new ElementTag(object.town.getTownBoard());
        });

        // <--[tag]
        // @attribute <TownTag.board>
        // @returns ElementTag
        // @plugin Depenizen, Towny
        // @description
        // Returns the town's current board.
        // -->
        registerTag("is_open", (attribute, object) -> {
            return new ElementTag(object.town.isOpen());
        });

        // <--[tag]
        // @attribute <TownTag.is_public>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Towny
        // @description
        // Returns true if the town is currently public.
        // -->
        registerTag("is_public", (attribute, object) -> {
            return new ElementTag(object.town.isPublic());
        });

        // <--[tag]
        // @attribute <TownTag.mayor>
        // @returns PlayerTag
        // @plugin Depenizen, Towny
        // @description
        // Returns the mayor of the town.
        // -->
        registerTag("mayor", (attribute, object) -> {
            return PlayerTag.valueOf(object.town.getMayor().getUUID().toString(), attribute.context);
        });

        // <--[tag]
        // @attribute <TownTag.name>
        // @returns ElementTag
        // @plugin Depenizen, Towny
        // @description
        // Returns the town's name.
        // -->
        registerTag("name", (attribute, object) -> {
            return new ElementTag(object.town.getName());
        });

        // <--[tag]
        // @attribute <TownTag.nation>
        // @returns NationTag
        // @plugin Depenizen, Towny
        // @description
        // Returns the nation that the town belongs to.
        // -->
        registerTag("nation", (attribute, object) -> {
            try {
                return new NationTag(object.town.getNation());
            }
            catch (NotRegisteredException e) {
                return null;
            }
        });

        // <--[tag]
        // @attribute <TownTag.has_nation>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Towny
        // @description
        // Returns whether the town belongs to a nation.
        // -->
        registerTag("has_nation", (attribute, object) -> {
            return new ElementTag(object.town.hasNation());
        });

        // <--[tag]
        // @attribute <TownTag.player_count>
        // @returns ElementTag(Number)
        // @plugin Depenizen, Towny
        // @description
        // Returns the number of players in the town.
        // -->
        registerTag("player_count", (attribute, object) -> {
            return new ElementTag(object.town.getNumResidents());
        }, "playercount");

        // <--[tag]
        // @attribute <TownTag.residents>
        // @returns ListTag
        // @plugin Depenizen, Towny
        // @description
        // Returns a list of the town's residents. Players will be valid PlayerTag instances, non-players will be plaintext of the name.
        // -->
        registerTag("residents", (attribute, object) -> {
            ListTag residents = new ListTag();
            for (Resident resident : object.town.getResidents()) {
                residents.addObject(PlayerTag.valueOf(resident.getUUID().toString(), attribute.context));
            }
            return residents;
        });

        // <--[tag]
        // @attribute <TownTag.size>
        // @returns ElementTag(Number)
        // @plugin Depenizen, Towny
        // @description
        // Returns the number of blocks the town owns.
        // -->
        registerTag("size", (attribute, object) -> {
            return new ElementTag(object.town.getPurchasedBlocks());
        });

        // <--[tag]
        // @attribute <TownTag.spawn>
        // @returns LocationTag
        // @plugin Depenizen, Towny
        // @description
        // Returns the spawn point of the town.
        // -->
        registerTag("spawn", (attribute, object) -> {
            try {
                return new LocationTag(object.town.getSpawn());
            }
            catch (TownyException e) {
                return null;
            }
        });

        // <--[tag]
        // @attribute <TownTag.has_spawn>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Towny
        // @description
        // Returns whether the town has a spawn point set.
        // -->
        registerTag("has_spawn", (attribute, object) -> {
            return new ElementTag(object.town.hasSpawn());
        });

        // <--[tag]
        // @attribute <TownTag.tag>
        // @returns ElementTag
        // @plugin Depenizen, Towny
        // @description
        // Returns the town's tag.
        // -->
        registerTag("", (attribute, object) -> {
            return new ElementTag(object.town.getTag());
        });

        // <--[tag]
        // @attribute <TownTag.taxes>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, Towny
        // @description
        // Returns the town's current taxes.
        // -->
        registerTag("taxes", (attribute, object) -> {
            return new ElementTag(object.town.getTaxes());
        });
        // <--[tag]
        // @attribute <TownTag.outposts>
        // @returns ListTag(LocationTag)
        // @plugin Depenizen, Towny
        // @description
        // Returns a list of the town's outpost locations.
        // -->
        registerTag("", (attribute, object) -> {
            ListTag posts = new ListTag();
            for (Location p : object.town.getAllOutpostSpawns()) {
                posts.addObject(new LocationTag(p));
            }
            return posts;
        });

        // <--[tag]
        // @attribute <TownTag.has_explosions>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Towny
        // @description
        // Returns if the town has explosions turned on.
        // -->
        registerTag("has_explosions", (attribute, object) -> {
            return new ElementTag(object.town.isBANG());
        });

        // <--[tag]
        // @attribute <TownTag.has_mobs>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Towny
        // @description
        // Returns if the town has mobs turned on.
        // -->
        registerTag("has_mobs", (attribute, object) -> {
            return new ElementTag(object.town.hasMobs());
        });

        // <--[tag]
        // @attribute <TownTag.has_pvp>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Towny
        // @description
        // Returns if the town has PvP turned on.
        // -->
        registerTag("has_pvp", (attribute, object) -> {
            return new ElementTag(object.town.isPVP());
        });

        // <--[tag]
        // @attribute <TownTag.has_firespread>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Towny
        // @description
        // Returns if the town has firespread turned on.
        // -->
        registerTag("has_firespread", (attribute, object) -> {
            return new ElementTag(object.town.isFire());
        });

        // <--[tag]
        // @attribute <TownTag.has_taxpercent>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Towny
        // @description
        // Returns if the town has taxes in percentage.
        // -->
        registerTag("has_taxpercent", (attribute, object) -> {
            return new ElementTag(object.town.isTaxPercentage());
        });

        // <--[tag]
        // @attribute <TownTag.plottax>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, Towny
        // @description
        // Returns the amount of taxes collected from plots.
        // -->
        registerTag("plottax", (attribute, object) -> {
            return new ElementTag(object.town.getPlotTax());
        }, "plot_tax");


        // <--[tag]
        // @attribute <TownTag.plotprice>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, Towny
        // @description
        // Returns the price of a plot.
        // -->
        registerTag("", (attribute, object) -> {
            return new ElementTag(object.town.getPlotPrice());
        }, "plot_price");
    }
}
