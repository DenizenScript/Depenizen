package com.denizenscript.depenizen.bukkit.objects.towny;

import com.denizenscript.denizen.objects.ChunkTag;
import com.denizenscript.denizen.objects.WorldTag;
import com.denizenscript.denizencore.objects.*;
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

public class TownTag implements ObjectTag, Adjustable {

    // <--[ObjectType]
    // @name TownTag
    // @prefix town
    // @base ElementTag
    // @format
    // The identity format for towns is <town_name>
    // For example, 'town@mytown'.
    //
    // @plugin Depenizen, Towny
    // @description
    // A TownTag represents a Towny town in the world.
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
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <TownTag.assistants>
        // @returns ListTag
        // @plugin Depenizen, Towny
        // @description
        // Returns a list of the town's assistants. Players will be valid PlayerTag instances, non-players will be plaintext of the name.
        // -->
        if (attribute.startsWith("assistants")) {
            ListTag list = new ListTag();
            for (Resident resident : town.getAssistants()) {
                PlayerTag player = PlayerTag.valueOf(resident.getName(), attribute.context);
                if (player != null) {
                    list.addObject(player);
                }
                else {
                    list.add(resident.getName());
                }
            }
            return list.getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <TownTag.balance>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, Towny
        // @mechanism TownTag.balance
        // @description
        // Returns the current money balance of the town.
        // -->
        else if (attribute.startsWith("balance")) {
            try {
                return new ElementTag(town.getAccount().getHoldingBalance()).getAttribute(attribute.fulfill(1));
            }
            catch (EconomyException e) {
                if (!attribute.hasAlternative()) {
                    Debug.echoError("Invalid economy response!");
                }
            }
        }

        // <--[tag]
        // @attribute <TownTag.board>
        // @returns ElementTag
        // @plugin Depenizen, Towny
        // @description
        // Returns the town's current board.
        // -->
        else if (attribute.startsWith("board")) {
            return new ElementTag(town.getTownBoard())
                    .getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <TownTag.is_open>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Towny
        // @description
        // Returns true if the town is currently open.
        // -->
        else if (attribute.startsWith("isopen") || attribute.startsWith("is_open")) {
            return new ElementTag(town.isOpen())
                    .getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <TownTag.is_public>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Towny
        // @description
        // Returns true if the town is currently public.
        // -->
        else if (attribute.startsWith("ispublic") || attribute.startsWith("is_public")) {
            return new ElementTag(town.isPublic())
                    .getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <TownTag.mayor>
        // @returns PlayerTag
        // @plugin Depenizen, Towny
        // @description
        // Returns the mayor of the town.
        // -->
        else if (attribute.startsWith("mayor")) {
            return PlayerTag.valueOf(town.getMayor().getName(), attribute.context)
                    .getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <TownTag.name>
        // @returns ElementTag
        // @plugin Depenizen, Towny
        // @description
        // Returns the town's name.
        // -->
        else if (attribute.startsWith("name")) {
            return new ElementTag(town.getName())
                    .getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <TownTag.nation>
        // @returns NationTag
        // @plugin Depenizen, Towny
        // @description
        // Returns the nation that the town belongs to.
        // -->
        else if (attribute.startsWith("nation")) {
            try {
                return new NationTag(town.getNation())
                        .getAttribute(attribute.fulfill(1));
            }
            catch (NotRegisteredException e) {
            }
        }

        // <--[tag]
        // @attribute <TownTag.player_count>
        // @returns ElementTag(Number)
        // @plugin Depenizen, Towny
        // @description
        // Returns the number of players in the town.
        // -->
        else if (attribute.startsWith("playercount") || attribute.startsWith("player_count")) {
            return new ElementTag(town.getNumResidents())
                    .getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <TownTag.residents>
        // @returns ListTag
        // @plugin Depenizen, Towny
        // @description
        // Returns a list of the town's residents. Players will be valid PlayerTag instances, non-players will be plaintext of the name.
        // -->
        else if (attribute.startsWith("residents")) {
            ListTag list = new ListTag();
            for (Resident resident : town.getResidents()) {
                PlayerTag player = PlayerTag.valueOf(resident.getName(), attribute.context);
                if (player != null) {
                    list.addObject(player);
                }
                else {
                    list.add(resident.getName());
                }
            }
            return list.getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <TownTag.size>
        // @returns ElementTag(Number)
        // @plugin Depenizen, Towny
        // @description
        // Returns the number of blocks the town owns.
        // -->
        else if (attribute.startsWith("size")) {
            return new ElementTag(town.getPurchasedBlocks())
                    .getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <TownTag.spawn>
        // @returns LocationTag
        // @plugin Depenizen, Towny
        // @description
        // Returns the spawn point of the town.
        // -->
        else if (attribute.startsWith("spawn")) {
            try {
                return new LocationTag(town.getSpawn()).getBlockLocation()
                        .getAttribute(attribute.fulfill(1));
            }
            catch (TownyException e) {
            }
        }

        // <--[tag]
        // @attribute <TownTag.tag>
        // @returns ElementTag
        // @plugin Depenizen, Towny
        // @description
        // Returns the town's tag.
        // -->
        else if (attribute.startsWith("tag")) {
            return new ElementTag(town.getTag())
                    .getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <TownTag.taxes>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, Towny
        // @description
        // Returns the town's current taxes.
        // -->
        else if (attribute.startsWith("taxes")) {
            return new ElementTag(town.getTaxes())
                    .getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <TownTag.outposts>
        // @returns ListTag(LocationTag)
        // @plugin Depenizen, Towny
        // @description
        // Returns a list of the town's outpost locations.
        // -->
        else if (attribute.startsWith("outposts")) {
            ListTag posts = new ListTag();
            for (Location p : town.getAllOutpostSpawns()) {
                posts.addObject(new LocationTag(p));
            }
            return posts.getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <TownTag.has_explosions>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Towny
        // @description
        // Returns if the town has explosions turned on.
        // -->
        else if (attribute.startsWith("has_explosions")) {
            return new ElementTag(town.isBANG()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <TownTag.has_mobs>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Towny
        // @description
        // Returns if the town has mobs turned on.
        // -->
        else if (attribute.startsWith("has_mobs")) {
            return new ElementTag(town.hasMobs()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <TownTag.has_pvp>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Towny
        // @description
        // Returns if the town has PvP turned on.
        // -->
        else if (attribute.startsWith("has_pvp")) {
            return new ElementTag(town.isPVP()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <TownTag.has_firespread>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Towny
        // @description
        // Returns if the town has firespread turned on.
        // -->
        else if (attribute.startsWith("has_firespread")) {
            return new ElementTag(town.isFire()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <TownTag.has_taxpercent>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Towny
        // @description
        // Returns if the town has taxes in percentage.
        // -->
        else if (attribute.startsWith("has_taxpercent")) {
            return new ElementTag(town.isTaxPercentage()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <TownTag.plot_object_group_names>
        // @returns ListTag
        // @plugin Depenizen, Towny
        // @description
        // Returns a list of the names of town plot object groups.
        // -->
        else if (attribute.startsWith("plot_object_group_names")) {
            ListTag output = new ListTag();
            if (!town.hasPlotGroups()) {
                return null;
            }
            for (PlotGroup group : town.getPlotObjectGroups()) {
                output.add(group.getName());
            }
            return output.getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <TownTag.plots>
        // @returns ListTag(LocationTag)
        // @plugin Depenizen, Towny
        // @description
        // Returns a list of the names of town plots.
        // -->
        else if (attribute.startsWith("plots")) {
            ListTag output = new ListTag();
            for (TownBlock block : town.getTownBlocks()) {
                output.addObject(new ChunkTag(new WorldTag(block.getWorld().getName()), block.getX(), block.getZ()));
            }
            return output.getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <TownTag.plottax>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, Towny
        // @description
        // Returns the amount of taxes collected from plots.
        // -->
        else if (attribute.startsWith("plottax")) {
            return new ElementTag(town.getPlotTax()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <TownTag.plotprice>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, Towny
        // @description
        // Returns the price of a plot.
        // -->
        else if (attribute.startsWith("plotprice")) {
            return new ElementTag(town.getPlotPrice()).getAttribute(attribute.fulfill(1));
        }

        return new ElementTag(identify()).getAttribute(attribute);

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
