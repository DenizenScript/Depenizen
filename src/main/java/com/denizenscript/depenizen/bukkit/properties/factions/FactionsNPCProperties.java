package com.denizenscript.depenizen.bukkit.properties.factions;

import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.depenizen.bukkit.objects.factions.FactionTag;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.entity.MPlayerColl;
import com.massivecraft.massivecore.util.IdUtil;
import com.denizenscript.denizen.objects.NPCTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;

public class FactionsNPCProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "FactionsNPC";
    }

    @Override
    public void adjust(Mechanism mechanism) {
        // None
    }

    public static boolean describes(ObjectTag object) {
        return object instanceof NPCTag;
    }

    public static FactionsNPCProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new FactionsNPCProperties((NPCTag) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "factions", "faction"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    public FactionsNPCProperties(NPCTag object) {
        npc = object;
    }

    public MPlayer getMPlayer() {
        MPlayer player = MPlayer.get(IdUtil.getId(npc.getCitizen().getUniqueId()));
        if (player == null) {
            // Attempt to force NPCs into Factions
            player = MPlayerColl.get().create(IdUtil.getId(npc.getCitizen().getUniqueId()));
        }
        return player;
    }

    NPCTag npc;

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {

        if (attribute.startsWith("factions")) {

            attribute = attribute.fulfill(1);

            // <--[tag]
            // @attribute <NPCTag.factions.power>
            // @returns ElementTag(Decimal)
            // @plugin Depenizen, Factions
            // @description
            // Returns the NPC's power level.
            // -->
            if (attribute.startsWith("power")) {
                return new ElementTag(getMPlayer().getPower()).getObjectAttribute(attribute.fulfill(1));
            }

            else if (getMPlayer().hasFaction()) {

                // <--[tag]
                // @attribute <NPCTag.factions.role>
                // @returns ElementTag
                // @plugin Depenizen, Factions
                // @description
                // Returns the NPC's role in their faction.
                // Note: In modern Factions these are called ranks instead of roles.
                // -->
                if (attribute.startsWith("role")) {
                    if (getMPlayer().getRank() != null) {
                        return new ElementTag(getMPlayer().getRank().toString()).getObjectAttribute(attribute.fulfill(1));
                    }
                }

                // <--[tag]
                // @attribute <NPCTag.factions.title>
                // @returns ElementTag
                // @plugin Depenizen, Factions
                // @description
                // Returns the NPC's title.
                // -->
                else if (attribute.startsWith("title")) {
                    if (getMPlayer().hasTitle()) {
                        return new ElementTag(getMPlayer().getTitle()).getObjectAttribute(attribute.fulfill(1));
                    }
                }
            }

        }

        // <--[tag]
        // @attribute <NPCTag.faction>
        // @returns FactionTag
        // @plugin Depenizen, Factions
        // @description
        // Returns the NPC's faction.
        // -->
        else if (attribute.startsWith("faction")) {
            return new FactionTag(getMPlayer().getFaction()).getObjectAttribute(attribute.fulfill(1));
        }

        return null;

    }
}
