package com.denizenscript.depenizen.bukkit.properties.magicspells;

import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.nisovin.magicspells.MagicSpells;
import com.nisovin.magicspells.Spell;
import com.nisovin.magicspells.Spellbook;
import com.nisovin.magicspells.mana.ManaHandler;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
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

    public static boolean describes(ObjectTag object) {
        return object instanceof PlayerTag;
    }

    public static MagicSpellsPlayerProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new MagicSpellsPlayerProperties((PlayerTag) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "magicspells"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    public MagicSpellsPlayerProperties(PlayerTag player) {
        this.player = player;
    }

    public PlayerTag player;

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {

        if (attribute.startsWith("magicspells")) {

            attribute = attribute.fulfill(1);

            // <--[tag]
            // @attribute <PlayerTag.magicspells.mana>
            // @returns ElementTag(Number)
            // @plugin Depenizen, MagicSpells
            // @description
            // Returns the mana of the player.
            // -->
            if (attribute.startsWith("mana")) {
                ManaHandler mH = MagicSpells.getManaHandler();
                if (mH == null) {
                    return null;
                }
                return new ElementTag(mH.getMana(player.getPlayerEntity()))
                        .getObjectAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <PlayerTag.magicspells.max_mana>
            // @returns ElementTag(Number)
            // @plugin Depenizen, MagicSpells
            // @description
            // Returns the maximum mana of the player.
            // -->
            if (attribute.startsWith("max_mana")) {
                ManaHandler mH = MagicSpells.getManaHandler();
                if (mH == null) {
                    return null;
                }
                return new ElementTag(mH.getMaxMana(player.getPlayerEntity()))
                        .getObjectAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <PlayerTag.magicspells.known_spells>
            // @returns ListTag
            // @plugin Depenizen, MagicSpells
            // @description
            // Returns a list of spells the player knows, by internal name.
            // -->
            if (attribute.startsWith("known_spells")) {
                Spellbook book = MagicSpells.getSpellbook(player.getPlayerEntity());
                if (book == null) {
                    return null;
                }
                ListTag result = new ListTag();
                for (Spell spell : book.getSpells()) {
                    result.add(spell.getInternalName());
                }
                return result.getObjectAttribute(attribute.fulfill(1));
            }
        }

        return null;

    }
}
