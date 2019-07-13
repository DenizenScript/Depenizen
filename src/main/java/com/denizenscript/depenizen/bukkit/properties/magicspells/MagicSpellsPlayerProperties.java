package com.denizenscript.depenizen.bukkit.properties.magicspells;

import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.nisovin.magicspells.MagicSpells;
import com.nisovin.magicspells.mana.ManaHandler;
import com.denizenscript.denizen.objects.dPlayer;
import com.denizenscript.denizencore.objects.Element;
import com.denizenscript.denizencore.objects.dObject;
import com.denizenscript.denizencore.tags.Attribute;

public class MagicSpellsPlayerProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "MagicSpellsPlayer";
    }

    @Override
    public void adjust(Mechanism mechanism) {
        // None
    }

    public static boolean describes(dObject object) {
        return object instanceof dPlayer;
    }

    public static MagicSpellsPlayerProperties getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new MagicSpellsPlayerProperties((dPlayer) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "magicspells"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    private MagicSpellsPlayerProperties(dPlayer player) {
        this.player = player;
    }

    private dPlayer player;

    @Override
    public String getAttribute(Attribute attribute) {

        if (attribute.startsWith("magicspells")) {

            attribute = attribute.fulfill(1);

            // <--[tag]
            // @attribute <p@player.magicspells.mana>
            // @returns Element(Number)
            // @description
            // Returns the mana of the player.
            // @Plugin Depenizen, MagicSpells
            // -->
            if (attribute.startsWith("mana")) {
                ManaHandler mH = MagicSpells.getManaHandler();
                if (mH == null) {
                    return null;
                }
                return new Element(mH.getMana(player.getPlayerEntity()))
                        .getAttribute(attribute.fulfill(1));
            }

        }

        return null;

    }
}
