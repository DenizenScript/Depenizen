package com.denizenscript.depenizen.bukkit.properties.quests;

import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.depenizen.bukkit.bridges.QuestsBridge;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;
import me.pikamug.quests.BukkitQuestsPlugin;
import me.pikamug.quests.player.Quester;
import me.pikamug.quests.quests.Quest;

public class QuestsPlayerProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "QuestsPlayer";
    }

    public static boolean describes(ObjectTag object) {
        return object instanceof PlayerTag;
    }

    public static QuestsPlayerProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new QuestsPlayerProperties((PlayerTag) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "quests"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    public QuestsPlayerProperties(PlayerTag player) {
        this.player = player;
    }

    public Quester getQuester() {
        BukkitQuestsPlugin quests = (BukkitQuestsPlugin) QuestsBridge.instance.plugin;
        // This would be Quests.getInstance() but the developers of Quests did a stupid and broke that method.
        return quests.getQuester(player.getUUID());
    }

    PlayerTag player;

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        if (attribute.startsWith("quests")) {
            attribute = attribute.fulfill(1);

            // <--[tag]
            // @attribute <PlayerTag.quests.points>
            // @returns ElementTag(Number)
            // @plugin Depenizen, Quests
            // @description
            // Returns the number of quest points the player has.
            // -->
            if (attribute.startsWith("points")) {
                if (getQuester().getBaseData().contains("quest-points")) {
                    return new ElementTag(getQuester().getBaseData().getInt("quest-points")).getObjectAttribute(attribute.fulfill(1));
                }
                return new ElementTag("0").getObjectAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <PlayerTag.quests.completed_ids>
            // @returns ListTag
            // @plugin Depenizen, Quests
            // @description
            // Returns the IDs of quests the player has completed.
            // -->
            if (attribute.startsWith("completed_ids")) {
                ListTag list = new ListTag(getQuester().getCompletedQuests().stream().map(Quest::getId));
                return list.getObjectAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <PlayerTag.quests.completed_names>
            // @returns ListTag
            // @plugin Depenizen, Quests
            // @description
            // Returns the names of quests the player has completed.
            // -->
            if (attribute.startsWith("completed_names")) {
                ListTag list = new ListTag(getQuester().getCompletedQuests().stream().map(Quest::getName));
                return list.getObjectAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <PlayerTag.quests.active_ids>
            // @returns ListTag
            // @plugin Depenizen, Quests
            // @description
            // Returns the IDs of quests the player has active.
            // -->
            if (attribute.startsWith("active_ids")) {
                ListTag list = new ListTag(getQuester().getCurrentQuests().keySet().stream().map(Quest::getId));
                return list.getObjectAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <PlayerTag.quests.active_names>
            // @returns ListTag
            // @plugin Depenizen, Quests
            // @description
            // Returns the names of quests the player has active.
            // -->
            if (attribute.startsWith("active_names")) {
                ListTag list = new ListTag(getQuester().getCurrentQuests().keySet().stream().map(Quest::getName));
                return list.getObjectAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <PlayerTag.quests.completed>
            // @returns ElementTag(Number)
            // @plugin Depenizen, Quests
            // @description
            // Returns the number of quests the player has completed.
            // -->
            else if (attribute.startsWith("completed")) {
                return new ElementTag(getQuester().getCompletedQuests().size()).getObjectAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <PlayerTag.quests.active>
            // @returns ElementTag(Number)
            // @plugin Depenizen, Quests
            // @description
            // Returns the number of quests the player has active.
            // -->
            else if (attribute.startsWith("active")) {
                return new ElementTag(getQuester().getCurrentQuests().size()).getObjectAttribute(attribute.fulfill(1));
            }
            return null;
        }
        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {
    }
}
