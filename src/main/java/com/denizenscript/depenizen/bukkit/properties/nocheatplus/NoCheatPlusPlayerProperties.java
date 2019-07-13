package com.denizenscript.depenizen.bukkit.properties.nocheatplus;

import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import fr.neatmonster.nocheatplus.checks.ViolationHistory;
import com.denizenscript.denizen.objects.dPlayer;
import com.denizenscript.denizencore.objects.Element;
import com.denizenscript.denizencore.objects.dObject;
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

    public static boolean describes(dObject object) {
        return object instanceof dPlayer;
    }

    public static NoCheatPlusPlayerProperties getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new NoCheatPlusPlayerProperties((dPlayer) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "ncp"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    private NoCheatPlusPlayerProperties(dPlayer player) {
        this.player = player;
    }

    dPlayer player;

    @Override
    public String getAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        if (attribute.startsWith("ncp")) {
            attribute = attribute.fulfill(1);

            // <--[tag]
            // @attribute <p@player.ncp.infractions>
            // @returns Element(Number)
            // @description
            // Returns the number of infractions this player has with NoCheatPlus
            // NOTE: Cannot guarantee the accuracy of this tag, due to lack of API in NoCheatPlus.
            // @Plugin Depenizen, NoCheatPlus.
            // -->
            if (attribute.startsWith("infractions")) {
                ViolationHistory history = ViolationHistory.getHistory(player.getName(), false);
                return new Element(history != null ? history.getViolationLevels().length : 0).getAttribute(attribute.fulfill(1));
            }
        }

        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {
    }
}
