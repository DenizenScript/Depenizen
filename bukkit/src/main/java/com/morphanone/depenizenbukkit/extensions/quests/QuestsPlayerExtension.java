package com.morphanone.depenizenbukkit.extensions.quests;

import com.morphanone.depenizenbukkit.extensions.dObjectExtension;
import me.blackvein.quests.Quester;
import me.blackvein.quests.Quests;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.Mechanism;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;

public class QuestsPlayerExtension extends dObjectExtension {

    public static boolean describes(dObject object) {
        return object instanceof dPlayer;
    }

    public static QuestsPlayerExtension getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new QuestsPlayerExtension((dPlayer) object);
        }
    }

    private QuestsPlayerExtension(dPlayer player) {
        this.player = player;
        this.quester = Quests.getInstance().getQuester(player.getOfflinePlayer().getUniqueId());
    }

    dPlayer player = null;
    Quester quester;

    @Override
    public String getAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }


        if (attribute.startsWith("quests")) {
            attribute = attribute.fulfill(1);
            // <--[tag]
            // @attribute <p@player.quests.points>
            // @returns Element(Number)
            // @description
            // Returns the number of quest points the player has.
            // @plugin Depenizen, Quests
            // -->
            if (attribute.startsWith("points")) {
                if (quester.getBaseData().contains("quest-points")) {
                    return new Element(quester.getBaseData().getInt("quest-points")).getAttribute(attribute.fulfill(1));
                }
                return new Element("0").getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <p@player.quests.completed>
            // @returns Element(Number)
            // @description
            // Returns the number of quests the player has completed.
            // @plugin Depenizen, Quests
            // -->
            else if (attribute.startsWith("completed")) {
                return new Element(quester.completedQuests.size()).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <p@player.quests.active>
            // @returns Element(Number)
            // @description
            // Returns the number of quests the player has active.
            // @plugin Depenizen, Quests
            // -->
            else if (attribute.startsWith("active")) {
                return new Element(quester.currentQuests.size()).getAttribute(attribute.fulfill(1));
            }
            return null;
        }
        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {
        Element value = mechanism.getValue();
    }
}
