package net.gnomeffinway.depenizen.extensions.NoCheatPlus;

import fr.neatmonster.nocheatplus.checks.ViolationHistory;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.Mechanism;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;
import net.gnomeffinway.depenizen.extensions.dObjectExtension;

public class NoCheatPlusPlayerExtension extends dObjectExtension {

    public static boolean describes(dObject object) {
        return object instanceof dPlayer;
    }

    public static NoCheatPlusPlayerExtension getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new NoCheatPlusPlayerExtension((dPlayer) object);
        }
    }

    private NoCheatPlusPlayerExtension(dPlayer player) {
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
            // @plugin Depenizen, NoCheatPlus.
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
