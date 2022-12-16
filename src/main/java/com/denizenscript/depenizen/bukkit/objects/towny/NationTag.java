package com.denizenscript.depenizen.bukkit.objects.towny;

import com.denizenscript.denizencore.DenizenCore;
import com.denizenscript.denizencore.flags.AbstractFlagTracker;
import com.denizenscript.denizencore.flags.FlaggableObject;
import com.denizenscript.denizencore.flags.RedirectionFlagTracker;
import com.denizenscript.denizencore.tags.ObjectTagProcessor;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.Fetchable;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.TagContext;

import java.util.UUID;

public class NationTag implements ObjectTag, FlaggableObject {

    // <--[ObjectType]
    // @name NationTag
    // @prefix nation
    // @base ElementTag
    // @implements FlaggableObject
    // @format
    // The identity format for nations is <nation_uuid>
    // For example, 'nation@123-abc'.
    //
    // @plugin Depenizen, Towny
    // @description
    // A NationTag represents a Towny nation.
    //
    // This object type is flaggable.
    // Flags on this object type will be stored in the server saves file, under special sub-key "__depenizen_towny_nations_uuid"
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
        string = string.replace("nation@", "");
        if (string.length() == 36 && string.indexOf('-') >= 0) {
            try {
                UUID uuid = UUID.fromString(string);
                if (uuid != null) {
                    Nation nation = TownyUniverse.getInstance().getNation(uuid);
                    if (nation != null) {
                        return new NationTag(nation);
                    }
                }
            }
            catch (IllegalArgumentException e) {
                // Nothing
            }
        }
        Nation nation = TownyUniverse.getInstance().getNation(string);
        if (nation == null) {
            return null;
        }
        return new NationTag(nation);
    }

    public static boolean matches(String arg) {
        if (arg.startsWith("nation@")) {
            return true;
        }
        return valueOf(arg) != null;
    }

    /////////////////////
    //   STATIC CONSTRUCTORS
    /////////////////

    public Nation nation = null;

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
    public boolean isUnique() {
        return true;
    }

    @Override
    public String identify() {
        return "nation@" + nation.getUUID();
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
    public AbstractFlagTracker getFlagTracker() {
        if (DenizenCore.serverFlagMap.hasFlag("__depenizen_towny_nations." + nation.getName())
                && !DenizenCore.serverFlagMap.hasFlag("__depenizen_towny_nations_uuid." + nation.getUUID())) {
            ObjectTag legacyValue = DenizenCore.serverFlagMap.getFlagValue("__depenizen_towny_nations." + nation.getName());
            DenizenCore.serverFlagMap.setFlag("__depenizen_towny_nations_uuid." + nation.getUUID(), legacyValue, null);
            DenizenCore.serverFlagMap.setFlag("__depenizen_towny_nations." + nation.getName(), null, null);
        }
        return new RedirectionFlagTracker(DenizenCore.serverFlagMap, "__depenizen_towny_nations_uuid." + nation.getUUID());
    }

    @Override
    public void reapplyTracker(AbstractFlagTracker tracker) {
        // Nothing to do.
    }

    public static void register() {

        AbstractFlagTracker.registerFlagHandlers(tagProcessor);

        // <--[tag]
        // @attribute <NationTag.allies>
        // @returns ListTag(NationTag)
        // @plugin Depenizen, Towny
        // @description
        // Returns a list of the nation's allies.
        // -->
        tagProcessor.registerTag(ListTag.class, "allies", (attribute, object) -> {
            ListTag list = new ListTag();
            for (Nation ally : object.nation.getAllies()) {
                list.addObject(new NationTag(ally));
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
        tagProcessor.registerTag(ListTag.class, "assistants", (attribute, object) -> {
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
        tagProcessor.registerTag(ElementTag.class, "balance", (attribute, object) -> {
            return new ElementTag(object.nation.getAccount().getHoldingBalance());
        });

        // <--[tag]
        // @attribute <NationTag.capital>
        // @returns TownTag
        // @plugin Depenizen, Towny
        // @description
        // Returns the capital city of the nation as a TownTag.
        // -->
        tagProcessor.registerTag(TownTag.class, "capital", (attribute, object) -> {
            if (object.nation.hasCapital()) {
                return new TownTag(object.nation.getCapital());
            }
            return null;
        });

        // <--[tag]
        // @attribute <NationTag.enemies>
        // @returns ListTag(NationTag)
        // @plugin Depenizen, Towny
        // @description
        // Returns a list of the nation's enemies.
        // -->
        tagProcessor.registerTag(ListTag.class, "enemies", (attribute, object) -> {
            ListTag list = new ListTag();
            for (Nation enemy : object.nation.getEnemies()) {
                list.addObject(new NationTag(enemy));
            }
            return list;
        });

        // <--[tag]
        // @attribute <NationTag.is_neutral>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Towny
        // @description
        // Returns true if the nation is neutral.
        // -->
        tagProcessor.registerTag(ElementTag.class, "is_neutral", (attribute, object) -> {
            return new ElementTag(object.nation.isNeutral());
        });

        // <--[tag]
        // @attribute <NationTag.king>
        // @returns PlayerTag
        // @plugin Depenizen, Towny
        // @description
        // Returns the king of the nation.
        // -->
        tagProcessor.registerTag(PlayerTag.class, "king", (attribute, object) -> {
            return new PlayerTag(object.nation.getCapital().getMayor().getUUID());
        });

        // <--[tag]
        // @attribute <NationTag.name>
        // @returns ElementTag
        // @plugin Depenizen, Towny
        // @description
        // Returns the nation's name.
        // -->
        tagProcessor.registerTag(ElementTag.class, "name", (attribute, object) -> {
            return new ElementTag(object.nation.getName());
        });

        // <--[tag]
        // @attribute <NationTag.player_count>
        // @returns ElementTag(Number)
        // @plugin Depenizen, Towny
        // @description
        // Returns the amount of players in the nation.
        // -->
        tagProcessor.registerTag(ElementTag.class, "player_count", (attribute, object) -> {
            return new ElementTag(object.nation.getNumResidents());
        });

        // <--[tag]
        // @attribute <NationTag.relation[<nation>]>
        // @returns ElementTag
        // @plugin Depenizen, Towny
        // @description
        // Returns the nation's current relation with another nation.
        // -->
        tagProcessor.registerTag(ElementTag.class, "relation", (attribute, object) -> {

            try {
                NationTag to = valueOf(attribute.getParam());

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
            catch (Exception e) {
            }
            return null;
        });

        // <--[tag]
        // @attribute <NationTag.residents>
        // @returns ListTag(PlayerTag)
        // @plugin Depenizen, Towny
        // @description
        // Returns a list of the nation's residents.
        // -->
        tagProcessor.registerTag(ListTag.class, "residents", (attribute, object) -> {
            ListTag list = new ListTag();
            for (Resident resident : object.nation.getResidents()) {
                list.addObject(new PlayerTag(resident.getUUID()));
            }
            return list;
        });

        // <--[tag]
        // @attribute <NationTag.tag>
        // @returns ElementTag
        // @plugin Depenizen, Towny
        // @description
        // Returns the nation's tag.
        // -->
        tagProcessor.registerTag(ElementTag.class, "tag", (attribute, object) -> {
            if (object.nation.hasTag()) {
                return new ElementTag(object.nation.getTag());
            }
            return null;
        });

        // <--[tag]
        // @attribute <NationTag.taxes>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, Towny
        // @description
        // Returns the nation's current taxes.
        // -->
        tagProcessor.registerTag(ElementTag.class, "taxes", (attribute, object) -> {
            return new ElementTag(object.nation.getTaxes());
        });

        // <--[tag]
        // @attribute <NationTag.town_count>
        // @returns ElementTag(Number)
        // @plugin Depenizen, Towny
        // @description
        // Returns the number of towns in the nation.
        // -->
        tagProcessor.registerTag(ElementTag.class, "town_count", (attribute, object) -> {
            return new ElementTag(object.nation.getNumTowns());
        });
    }

    public static ObjectTagProcessor<NationTag> tagProcessor = new ObjectTagProcessor<>();

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {
        return tagProcessor.getObjectAttribute(this, attribute);
    }
}
