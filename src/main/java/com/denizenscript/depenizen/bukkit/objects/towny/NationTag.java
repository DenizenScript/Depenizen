package com.denizenscript.depenizen.bukkit.objects.towny;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.debugging.Debug;
import com.denizenscript.denizencore.objects.Fetchable;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.tags.ObjectTagProcessor;
import com.denizenscript.denizencore.tags.TagContext;
import com.denizenscript.denizencore.tags.TagRunnable;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.EconomyException;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;

public class NationTag implements ObjectTag {

    // <--[language]
    // @name NationTag Objects
    // @group Depenizen Object Types
    // @plugin Depenizen, Factions
    // @description
    // A NationTag represents a Factions nation.
    //
    // These use the object notation "nation@".
    // The identity format for nations is <nation_name>
    // For example, 'nation@my_nation'.
    //
    // -->

    /////////////////////
    //   OBJECT FETCHER
    /////////////////

    public static NationTag valueOf(String string) {
        return valueOf(string, null);
    }

    @Fetchable("nation")
    public static NationTag valueOf(String string, TagContext context) {
        if (string == null) {
            return null;
        }

        ////////
        // Match nation name

        string = string.replace("nation@", "");
        try {
            return new NationTag(TownyAPI.getInstance().getDataSource().getNation(string));
        }
        catch (NotRegisteredException e) {
            return null;
        }
    }

    public static boolean matches(String arg) {
        arg = arg.replace("nation@", "");
        return TownyAPI.getInstance().getDataSource().hasNation(arg);
    }

    /////////////////////
    //   STATIC CONSTRUCTORS
    /////////////////

    Nation nation = null;

    public NationTag(Nation nation) {
        if (nation != null) {
            this.nation = nation;
        }
        else {
            Debug.echoError("Nation referenced is null!");
        }
    }

    /////////////////////
    //   ObjectTag Methods
    /////////////////

    private String prefix = "Nation";

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public NationTag setPrefix(String prefix) {
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
        return "Nation";
    }

    @Override
    public String identify() {
        return "nation@" + nation.getName();
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

    public static void registerTag(String name, TagRunnable.ObjectInterface<NationTag> runnable, String... variants) {
        tagProcessor.registerTag(name, runnable, variants);
    }

    public static ObjectTagProcessor<NationTag> tagProcessor = new ObjectTagProcessor<>();

    public static void registerTags() {

        // <--[tag]
        // @attribute <NationTag.allies>
        // @returns ListTag(NationTag)
        // @plugin Depenizen, Towny
        // @description
        // Returns a list of the nation's allies.
        // -->
        registerTag("allies", (attribute, object) -> {
            ListTag list = new ListTag();
            for (Nation ally : object.nation.getAllies()) {
                list.addObject(new NationTag(ally));
            }
            return list;
        });

        // <--[tag]
        // @attribute <NationTag.enemies>
        // @returns ListTag(NationTag)
        // @plugin Depenizen, Towny
        // @description
        // Returns a list of the nation's enemies.
        // -->
        registerTag("enemies", (attribute, object) -> {
            ListTag list = new ListTag();
            for (Nation enemy : object.nation.getEnemies()) {
                list.addObject(new NationTag(enemy));
            }
            return list;
        });

        // <--[tag]
        // @attribute <NationTag.assistants>
        // @returns ListTag(PlayerTag)
        // @plugin Depenizen, Towny
        // @description
        // Returns a list of the nation's assistants.
        // -->
        registerTag("assistants", (attribute, object) -> {
            ListTag list = new ListTag();
            for (Resident resident : object.nation.getAssistants()) {
                list.addObject(new PlayerTag(resident.getUUID()));
            }
            return list;
        });

        // <--[tag]
        // @attribute <NationTag.balance>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, Towny
        // @description
        // Returns the current money balance of the nation.
        // -->
        registerTag("balance", (attribute, object) -> {
            try {
                return new ElementTag(object.nation.getAccount().getHoldingBalance());
            }
            catch (EconomyException e) {
                Debug.echoError("Invalid economy response!");
                return null;
            }
        });

        // <--[tag]
        // @attribute <NationTag.has_capital>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Towny
        // @description
        // Returns whether the nation has a capital Town.
        // -->
        registerTag("has_capital", (attribute, object) -> {
            return new ElementTag(object.nation.hasCapital());
        });

        // <--[tag]
        // @attribute <NationTag.capital>
        // @returns TownTag
        // @plugin Depenizen, Towny
        // @description
        // Returns the capital city of the nation as a TownTag.
        // -->
        registerTag("capital", (attribute, object) -> {
            if (object.nation.hasCapital()) {
                return new TownTag(object.nation.getCapital());
            }
            return null;
        });

        // <--[tag]
        // @attribute <NationTag.is_neutral>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Towny
        // @description
        // Returns true if the nation is neutral.
        // -->
        registerTag("is_neutral", (attribute, object) -> {
            return new ElementTag(object.nation.isNeutral());
        }, "isneutral", "neutral");

        // <--[tag]
        // @attribute <NationTag.king>
        // @returns PlayerTag
        // @plugin Depenizen, Towny
        // @description
        // Returns the king of the nation.
        // -->
        registerTag("king", (attribute, object) -> {
            return new PlayerTag(object.nation.getKing().getUUID());
        });

        // <--[tag]
        // @attribute <NationTag.name>
        // @returns ElementTag
        // @plugin Depenizen, Towny
        // @description
        // Returns the nation's name.
        // -->
        registerTag("name", (attribute, object) -> {
            return new ElementTag(object.nation.getName());
        });

        // <--[tag]
        // @attribute <NationTag.player_count>
        // @returns ElementTag(Number)
        // @plugin Depenizen, Towny
        // @description
        // Returns the amount of players in the nation.
        // -->
        registerTag("player_count", (attribute, object) -> {
            return new ElementTag(object.nation.getNumResidents());
        }, "playercount");

        // <--[tag]
        // @attribute <NationTag.relation[<nation>]>
        // @returns ElementTag
        // @plugin Depenizen, Towny
        // @description
        // Returns the nation's current relation with another nation.
        // -->
        registerTag("relation", (attribute, object) -> {
            if (attribute.hasContext(1)) {
                if (matches(attribute.getContext(1))) {
                    NationTag to = valueOf(attribute.getContext(1));
                    if (object.nation.hasAlly(to.nation)) {
                        return new ElementTag("allies");
                    }
                    else if (object.nation.hasEnemy(to.nation)) {
                        return new ElementTag("enemies");
                    }
                    else {
                        return new ElementTag("neutral");
                    }
                }
                else {
                    Debug.echoError("Unable to match nation: '" + attribute.getContext(1) + "' to a nation!");
                    return null;
                }
            }
            else {
                Debug.echoError("Must specify a nation for tag <NationTag.relation[<nation>]>!");
                return null;
            }
        });

        // <--[tag]
        // @attribute <NationTag.residents>
        // @returns ListTag(PlayerTag)
        // @plugin Depenizen, Towny
        // @description
        // Returns a list of the nation's residents.
        // -->
        registerTag("residents", (attribute, object) -> {
            ListTag residents = new ListTag();
            for (Resident resident : object.nation.getResidents()) {
                residents.addObject(new PlayerTag(resident.getUUID()));
            }
            return residents;
        });

        // <--[tag]
        // @attribute <NationTag.tag>
        // @returns ElementTag
        // @plugin Depenizen, Towny
        // @description
        // Returns the nation's tag.
        // -->
        registerTag("tag", (attribute, object) -> {
            return new ElementTag(object.nation.getTag());
        });

        // <--[tag]
        // @attribute <NationTag.taxes>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, Towny
        // @description
        // Returns the nation's current taxes.
        // -->
        registerTag("taxes", (attribute, object) -> {
            return new ElementTag(object.nation.getTaxes());
        });
        // <--[tag]
        // @attribute <NationTag.town_count>
        // @returns ElementTag(Number)
        // @plugin Depenizen, Towny
        // @description
        // Returns the number of towns in the nation.
        // -->
        registerTag("town_count", (attribute, object) -> {
            return new ElementTag(object.nation.getNumTowns());
        }, "towncount");
    }
}
