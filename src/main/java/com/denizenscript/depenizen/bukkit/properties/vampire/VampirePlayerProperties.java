package com.denizenscript.depenizen.bukkit.properties.vampire;

import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.massivecraft.vampire.entity.UPlayer;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;

public class VampirePlayerProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "VampirePlayer";
    }

    @Override
    public void adjust(Mechanism mechanism) {
        // None
    }

    public static boolean describes(ObjectTag object) {
        return object instanceof PlayerTag;
    }

    public static VampirePlayerProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new VampirePlayerProperties((PlayerTag) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "is_vampire", "is_infected", "combat_infect", "combat_damage"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    public VampirePlayerProperties(PlayerTag player) {
        this.uPlayer = UPlayer.get(player.getPlayerEntity());
    }

    UPlayer uPlayer;

    @Override
    public String getAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        // <--[tag]
        // @attribute <PlayerTag.is_vampire>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Vampire
        // @description
        // Returns true if the player is a vampire.
        // -->
        if (attribute.startsWith("is_vampire")) {
            return new ElementTag(uPlayer.isVampire()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <PlayerTag.is_infected>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Vampire
        // @description
        // Returns true if the player is infected.
        // -->
        if (attribute.startsWith("is_infected")) {
            return new ElementTag(uPlayer.isInfected()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <PlayerTag.combat_infect>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, Vampire
        // @description
        // Returns the value of the combat infection risk.
        // -->
        if (attribute.startsWith("combat_infect")) {
            return new ElementTag(uPlayer.combatInfectRisk()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <PlayerTag.combat_damage>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, Vampire
        // @description
        // Returns the value of the combat damage factor.
        // -->
        if (attribute.startsWith("combat_damage")) {
            return new ElementTag(uPlayer.combatDamageFactor()).getAttribute(attribute.fulfill(1));
        }

        return null;
    }
}
