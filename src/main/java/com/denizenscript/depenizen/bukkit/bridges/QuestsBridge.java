package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.ReplaceableTagEvent;
import com.denizenscript.denizencore.tags.TagManager;
import com.denizenscript.denizencore.tags.TagRunnable;
import com.denizenscript.depenizen.bukkit.events.quests.PlayerCompletesQuestScriptEvent;
import com.denizenscript.depenizen.bukkit.events.quests.PlayerFailsQuestScriptEvent;
import com.denizenscript.depenizen.bukkit.events.quests.PlayerStartsQuestScriptEvent;
import com.denizenscript.depenizen.bukkit.properties.quests.QuestsPlayerProperties;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.commands.quests.QuestsCommand;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.DenizenAPI;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import me.blackvein.quests.Quest;
import me.blackvein.quests.Quests;

public class QuestsBridge extends Bridge {

    public static QuestsBridge instance;

    @Override
    public void init() {
        instance = this;
        PropertyParser.registerProperty(QuestsPlayerProperties.class, PlayerTag.class);
        DenizenAPI.getCurrentInstance().getCommandRegistry().registerCommand(QuestsCommand.class);
        ScriptEvent.registerScriptEvent(new PlayerCompletesQuestScriptEvent());
        ScriptEvent.registerScriptEvent(new PlayerFailsQuestScriptEvent());
        ScriptEvent.registerScriptEvent(new PlayerStartsQuestScriptEvent());
        TagManager.registerTagHandler(new TagRunnable.RootForm() {
            @Override
            public void run(ReplaceableTagEvent event) {
                tagEvent(event);
            }
        }, "quests");
    }

    public void tagEvent(ReplaceableTagEvent event) {
        Attribute attribute = event.getAttributes().fulfill(1);

        // <--[tag]
        // @attribute <quests.list_quests>
        // @returns ListTag
        // @plugin Depenizen, Quests
        // @description
        // Returns a list of all quest IDs from the Quests plugin.
        // -->
        if (attribute.startsWith("list_quests")) {
            ListTag list = new ListTag();
            for (Quest quest : ((Quests) plugin).getQuests()) {
                list.add(quest.getId());
            }
            event.setReplacedObject(list.getObjectAttribute(attribute.fulfill(1)));
        }
    }
}
