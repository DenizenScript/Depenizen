package com.denizenscript.depenizen.bukkit.extensions.vampire;

import com.denizenscript.depenizen.bukkit.extensions.dObjectExtension;
import com.massivecraft.vampire.entity.UPlayer;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;

public class VampirePlayerExtension extends dObjectExtension {

    public static boolean describes(dObject object) {
        return object instanceof dPlayer;
    }

    public static VampirePlayerExtension getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new VampirePlayerExtension((dPlayer) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "is_vampire", "is_infected", "combat_infect", "combat_damage"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    public VampirePlayerExtension(dPlayer player) {
        this.uPlayer = UPlayer.get(player.getPlayerEntity());
    }

    UPlayer uPlayer = null;

    @Override
    public String getAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        // <--[tag]
        // @attribute <p@player.is_vampire>
        // @returns Element(Boolean)
        // @description
        // Returns true if the player is a vampire.
        // @Plugin DepenizenBukkit, Vampire
        // -->
        if (attribute.startsWith("is_vampire")) {
            return new Element(uPlayer.isVampire()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <p@player.is_ínfected>
        // @returns Element(Boolean)
        // @description
        // Returns true if the player is infected.
        // @Plugin DepenizenBukkit, Vampire
        // -->
        if (attribute.startsWith("is_ínfected")) {
            return new Element(uPlayer.isInfected()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <p@player.combat_infect>
        // @returns Element(Double)
        // @description
        // Returns the value of the combat infection risk.
        // @Plugin DepenizenBukkit, Vampire
        // -->
        if (attribute.startsWith("combat_infect")) {
            return new Element(uPlayer.combatInfectRisk()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <p@player.combat_damage>
        // @returns Element(Double)
        // @description
        // Returns the value of the combat damage factor.
        // @Plugin DepenizenBukkit, Vampire
        // -->
        if (attribute.startsWith("combat_damage")) {
            return new Element(uPlayer.combatDamageFactor()).getAttribute(attribute.fulfill(1));
        }

        return null;
    }
}
