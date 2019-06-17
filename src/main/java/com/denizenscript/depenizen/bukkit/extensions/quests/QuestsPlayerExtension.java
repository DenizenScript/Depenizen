package com.denizenscript.depenizen.bukkit.extensions.quests;

import com.denizenscript.depenizen.bukkit.extensions.dObjectExtension;
import com.denizenscript.depenizen.bukkit.support.plugins.QuestsSupport;
import me.blackvein.quests.Quest;
import me.blackvein.quests.Quester;
import me.blackvein.quests.Quests;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.Mechanism;
import net.aufdemrand.denizencore.objects.dList;
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

    public static final String[] handledTags = new String[] {
            "quests"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    private QuestsPlayerExtension(dPlayer player) {
        this.player = player;
        Quests quests = (Quests) QuestsSupport.questsPlugin;
        // This would be Quests.getInstance() but the developers of Quests did a stupid and broke that method.
        this.quester = quests.getQuester(player.getOfflinePlayer().getUniqueId());
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
            // @Plugin DepenizenBukkit, Quests
            // -->
            if (attribute.startsWith("points")) {
                if (quester.getBaseData().contains("quest-points")) {
                    return new Element(quester.getBaseData().getInt("quest-points")).getAttribute(attribute.fulfill(1));
                }
                return new Element("0").getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <p@player.quests.completed_names>
            // @returns dList
            // @description
            // Returns the names of quests the player has completed.
            // @Plugin DepenizenBukkit, Quests
            // -->
            if (attribute.startsWith("completed_names")) {
                dList list = new dList();
                for (String quest : quester.getCompletedQuests()) {
                    list.add(quest);
                }
                return list.getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <p@player.quests.active_names>
            // @returns dList
            // @description
            // Returns the names of quests the player has active.
            // @Plugin DepenizenBukkit, Quests
            // -->
            if (attribute.startsWith("active_names")) {
                dList list = new dList();
                for (Quest quest : quester.getCurrentQuests().keySet()) {
                    list.add(quest.getName());
                }
                return list.getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <p@player.quests.completed>
            // @returns Element(Number)
            // @description
            // Returns the number of quests the player has completed.
            // @Plugin DepenizenBukkit, Quests
            // -->
            else if (attribute.startsWith("completed")) {
                return new Element(quester.getCompletedQuests().size()).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <p@player.quests.active>
            // @returns Element(Number)
            // @description
            // Returns the number of quests the player has active.
            // @Plugin DepenizenBukkit, Quests
            // -->
            else if (attribute.startsWith("active")) {
                return new Element(quester.getCurrentQuests().size()).getAttribute(attribute.fulfill(1));
            }
            return null;
        }
        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {
    }
}
