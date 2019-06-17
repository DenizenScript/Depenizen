package com.denizenscript.depenizen.bukkit.extensions.magicspells;

import com.denizenscript.depenizen.bukkit.extensions.dObjectExtension;
import com.nisovin.magicspells.MagicSpells;
import com.nisovin.magicspells.mana.ManaHandler;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;

public class MagicSpellsPlayerExtension extends dObjectExtension {

    public static boolean describes(dObject object) {
        return object instanceof dPlayer;
    }

    public static MagicSpellsPlayerExtension getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new MagicSpellsPlayerExtension((dPlayer) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "magicspells"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    private MagicSpellsPlayerExtension(dPlayer player) {
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
