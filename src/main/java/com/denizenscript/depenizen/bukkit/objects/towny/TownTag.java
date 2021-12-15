package com.denizenscript.depenizen.bukkit.objects.towny;

import com.denizenscript.denizen.objects.ChunkTag;
import com.denizenscript.denizen.objects.WorldTag;
import com.denizenscript.denizencore.DenizenCore;
import com.denizenscript.denizencore.flags.AbstractFlagTracker;
import com.denizenscript.denizencore.flags.FlaggableObject;
import com.denizenscript.denizencore.flags.RedirectionFlagTracker;
import com.denizenscript.denizencore.objects.*;
import com.denizenscript.denizencore.tags.ObjectTagProcessor;
import com.palmergames.bukkit.towny.exceptions.EconomyException;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.*;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.debugging.Debug;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.TagContext;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import org.bukkit.Location;

public class TownTag implements ObjectTag, Adjustable, FlaggableObject {

    // <--[ObjectType]
    // @name TownTag
    // @prefix town
    // @base ElementTag
    // @implements FlaggableObject
    // @format
    // The identity format for towns is <town_name>
    // For example, 'town@mytown'.
    //
    // @plugin Depenizen, Towny
    // @description
    // A TownTag represents a Towny town in the world.
    //
    // This object type is flaggable.
    // Flags on this object type will be stored in the server saves file, under special sub-key "__depenizen_towny_towns"
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
        string = string.replace("town@", "");
        Town town = TownyUniverse.getInstance().getTown(string);
        if (town == null) {
            return null;
        }
        return new TownTag(town);
    }

    public static boolean matches(String arg) {
        arg = arg.replace("town@", "");
        return TownyUniverse.getInstance().hasTown(arg);
    }

    /////////////////////
    //   STATIC CONSTRUCTORS
    /////////////////

    public Town town;

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
    @Override
    public AbstractFlagTracker getFlagTracker() {
        return new RedirectionFlagTracker(DenizenCore.serverFlagMap, "__depenizen_towny_nations." + town.getName());
    }

    @Override
    public void reapplyTracker(AbstractFlagTracker tracker) {
        // Nothing to do.
    }

    public static void registerTags() {

        AbstractFlagTracker.registerFlagHandlers(tagProcessor);

        // <--[tag]
        // @attribute <TownTag.assistants>
        // @returns ListTag
        // @plugin Depenizen, Towny
        // @description
        // Returns a list of the town's assistants. Players will be valid PlayerTag instances, non-players will be plaintext of the name.
        // -->
        tagProcessor.registerTag(ListTag.class, "assistants", (attribute, object) -> {
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
        // @mechanism TownTag.balance
        // @description
        // Returns the current money balance of the object.town.
        // -->
        tagProcessor.registerTag(ElementTag.class, "balance", (attribute, object) -> {
            try {
                return new ElementTag(object.town.getAccount().getHoldingBalance());
            }
            catch (EconomyException e) {
                if (!attribute.hasAlternative()) {
                    Debug.echoError("Invalid economy response!");
                }
            }
            return null;
        });

        // <--[tag]
        // @attribute <TownTag.board>
        // @returns ElementTag
        // @plugin Depenizen, Towny
        // @description
        // Returns the town's current board.
        // -->
        tagProcessor.registerTag(ElementTag.class, "board", (attribute, object) -> {
            return new ElementTag(object.town.getTownBoard());
        });

        // <--[tag]
        // @attribute <TownTag.is_open>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Towny
        // @description
        // Returns true if the town is currently open.
        // -->
        tagProcessor.registerTag(ElementTag.class, "is_open", (attribute, object) -> {
            return new ElementTag(object.town.isOpen());
        });

        // <--[tag]
        // @attribute <TownTag.is_public>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Towny
        // @description
        // Returns true if the town is currently public.
        // -->
        tagProcessor.registerTag(ElementTag.class, "is_public", (attribute, object) -> {
            return new ElementTag(object.town.isPublic())
                    ;
        });

        // <--[tag]
        // @attribute <TownTag.mayor>
        // @returns PlayerTag
        // @plugin Depenizen, Towny
        // @description
        // Returns the mayor of the object.town.
        // -->
        tagProcessor.registerTag(PlayerTag.class, "mayor", (attribute, object) -> {
            return PlayerTag.valueOf(object.town.getMayor().getName(), attribute.context);
        });

        // <--[tag]
        // @attribute <TownTag.name>
        // @returns ElementTag
        // @plugin Depenizen, Towny
        // @description
        // Returns the town's name.
        // -->
        tagProcessor.registerTag(ElementTag.class, "name", (attribute, object) -> {
            return new ElementTag(object.town.getName());
        });

        // <--[tag]
        // @attribute <TownTag.nation>
        // @returns NationTag
        // @plugin Depenizen, Towny
        // @description
        // Returns the nation that the town belongs to.
        // -->
        tagProcessor.registerTag(NationTag.class, "nation", (attribute, object) -> {
            try {
                return new NationTag(object.town.getNation());
            }
            catch (NotRegisteredException e) {
            }
            return null;
        });

        // <--[tag]
        // @attribute <TownTag.player_count>
        // @returns ElementTag(Number)
        // @plugin Depenizen, Towny
        // @description
        // Returns the number of players in the object.town.
        // -->
        tagProcessor.registerTag(ElementTag.class, "player_count", (attribute, object) -> {
            return new ElementTag(object.town.getNumResidents());
        });

        // <--[tag]
        // @attribute <TownTag.residents>
        // @returns ListTag
        // @plugin Depenizen, Towny
        // @description
        // Returns a list of the town's residents. Players will be valid PlayerTag instances, non-players will be plaintext of the name.
        // -->
        tagProcessor.registerTag(ListTag.class, "residents", (attribute, object) -> {
            ListTag list = new ListTag();
            for (Resident resident : object.town.getResidents()) {
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
        // @attribute <TownTag.size>
        // @returns ElementTag(Number)
        // @plugin Depenizen, Towny
        // @description
        // Returns the number of blocks the town owns.
        // -->
        tagProcessor.registerTag(ElementTag.class, "size", (attribute, object) -> {
            return new ElementTag(object.town.getPurchasedBlocks());
        });

        // <--[tag]
        // @attribute <TownTag.spawn>
        // @returns LocationTag
        // @plugin Depenizen, Towny
        // @description
        // Returns the spawn point of the object.town.
        // -->
        tagProcessor.registerTag(LocationTag.class, "spawn", (attribute, object) -> {
            try {
                return new LocationTag(object.town.getSpawn()).getBlockLocation();
            }
            catch (TownyException e) {
            }
            return null;
        });

        // <--[tag]
        // @attribute <TownTag.tag>
        // @returns ElementTag
        // @plugin Depenizen, Towny
        // @description
        // Returns the town's tag.
        // -->
        tagProcessor.registerTag(ElementTag.class, "tag", (attribute, object) -> {
            return new ElementTag(object.town.getTag());
        });

        // <--[tag]
        // @attribute <TownTag.taxes>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, Towny
        // @description
        // Returns the town's current taxes.
        // -->
        tagProcessor.registerTag(ElementTag.class, "taxes", (attribute, object) -> {
            return new ElementTag(object.town.getTaxes());
        });

        // <--[tag]
        // @attribute <TownTag.outposts>
        // @returns ListTag(LocationTag)
        // @plugin Depenizen, Towny
        // @description
        // Returns a list of the town's outpost locations.
        // -->
        tagProcessor.registerTag(ListTag.class, "outposts", (attribute, object) -> {
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
        tagProcessor.registerTag(ElementTag.class, "has_explosions", (attribute, object) -> {
            return new ElementTag(object.town.isBANG());
        });

        // <--[tag]
        // @attribute <TownTag.has_mobs>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Towny
        // @description
        // Returns if the town has mobs turned on.
        // -->
        tagProcessor.registerTag(ElementTag.class, "has_mobs", (attribute, object) -> {
            return new ElementTag(object.town.hasMobs());
        });

        // <--[tag]
        // @attribute <TownTag.has_pvp>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Towny
        // @description
        // Returns if the town has PvP turned on.
        // -->
        tagProcessor.registerTag(ElementTag.class, "has_pvp", (attribute, object) -> {
            return new ElementTag(object.town.isPVP());
        });

        // <--[tag]
        // @attribute <TownTag.has_firespread>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Towny
        // @description
        // Returns if the town has firespread turned on.
        // -->
        tagProcessor.registerTag(ElementTag.class, "has_firespread", (attribute, object) -> {
            return new ElementTag(object.town.isFire());
        });

        // <--[tag]
        // @attribute <TownTag.has_taxpercent>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Towny
        // @description
        // Returns if the town has taxes in percentage.
        // -->
        tagProcessor.registerTag(ElementTag.class, "has_taxpercent", (attribute, object) -> {
            return new ElementTag(object.town.isTaxPercentage());
        });

        // <--[tag]
        // @attribute <TownTag.plot_object_group_names>
        // @returns ListTag
        // @plugin Depenizen, Towny
        // @description
        // Returns a list of the names of town plot object groups.
        // -->
        tagProcessor.registerTag(ListTag.class, "plot_object_group_names", (attribute, object) -> {
            ListTag output = new ListTag();
            if (!object.town.hasPlotGroups()) {
                return null;
            }
            for (PlotGroup group : object.town.getPlotObjectGroups()) {
                output.add(group.getName());
            }
            return output;
        });

        // <--[tag]
        // @attribute <TownTag.plots>
        // @returns ListTag(LocationTag)
        // @plugin Depenizen, Towny
        // @description
        // Returns a list of the names of town plots.
        // -->
        tagProcessor.registerTag(ListTag.class, "plots", (attribute, object) -> {
            ListTag output = new ListTag();
            for (TownBlock block : object.town.getTownBlocks()) {
                output.addObject(new ChunkTag(new WorldTag(block.getWorld().getName()), block.getX(), block.getZ()));
            }
            return output;
        });

        // <--[tag]
        // @attribute <TownTag.plottax>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, Towny
        // @description
        // Returns the amount of taxes collected from plots.
        // -->
        tagProcessor.registerTag(ElementTag.class, "plottax", (attribute, object) -> {
            return new ElementTag(object.town.getPlotTax());
        });

        // <--[tag]
        // @attribute <TownTag.plotprice>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, Towny
        // @description
        // Returns the price of a plot.
        // -->
        tagProcessor.registerTag(ElementTag.class, "plotprice", (attribute, object) -> {
            return new ElementTag(object.town.getPlotPrice());
        });

    }

    public static ObjectTagProcessor<TownTag> tagProcessor = new ObjectTagProcessor<>();

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {
        return tagProcessor.getObjectAttribute(this, attribute);
    }

    @Override
    public void adjust(Mechanism mechanism) {

        // <--[mechanism]
        // @object TownTag
        // @name balance
        // @input ElementTag(Decimal)|ElementTag
        // @plugin Depenizen, Towny
        // @description
        // Sets the money balance of a town, with a reason for the change.
        // @tags
        // <TownTag.balance>
        // -->
        if (mechanism.matches("balance")) {
            ListTag input = mechanism.valueAsType(ListTag.class);
            if (input.size() != 2 || !ArgumentHelper.matchesDouble(input.get(0))) {
                mechanism.echoError("Invalid balance mech input.");
                return;
            }
            try {
                town.getAccount().setBalance(new ElementTag(input.get(0)).asDouble(), input.get(1));
            }
            catch (EconomyException ex) {
                Debug.echoError(ex);
            }
        }
    }

    @Override
    public void applyProperty(Mechanism mechanism) {
        Debug.echoError("Cannot apply properties to a Towny town!");
    }
}
