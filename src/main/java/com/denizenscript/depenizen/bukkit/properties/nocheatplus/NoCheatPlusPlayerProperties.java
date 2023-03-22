package com.denizenscript.depenizen.bukkit.properties.nocheatplus;

import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import fr.neatmonster.nocheatplus.checks.ViolationHistory;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;

public class NoCheatPlusPlayerProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "NoCheatPlusPlayer";
    }

    public static boolean describes(ObjectTag object) {
        return object instanceof PlayerTag;
    }

    public static NoCheatPlusPlayerProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new NoCheatPlusPlayerProperties((PlayerTag) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "ncp"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    public NoCheatPlusPlayerProperties(PlayerTag player) {
        this.player = player;
    }

    PlayerTag player;

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        if (attribute.startsWith("ncp")) {
            attribute = attribute.fulfill(1);

            // <--[tag]
            // @attribute <PlayerTag.ncp.infractions>
            // @returns ElementTag(Number)
            // @plugin Depenizen, NoCheatPlus
            // @description
            // Returns the number of infractions this player has with NoCheatPlus
            // NOTE: Cannot guarantee the accuracy of this tag, due to lack of API in NoCheatPlus.
            // -->
            if (attribute.startsWith("infractions")) {
                ViolationHistory history = ViolationHistory.getHistory(player.getName(), false);
                return new ElementTag(history != null ? history.getViolationLevels().length : 0).getObjectAttribute(attribute.fulfill(1));
            }
        }

        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {
    }
}
