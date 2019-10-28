package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.depenizen.bukkit.events.quests.PlayerCompletesQuestScriptEvent;
import com.denizenscript.depenizen.bukkit.events.quests.PlayerFailsQuestScriptEvent;
import com.denizenscript.depenizen.bukkit.events.quests.PlayerStartsQuestScriptEvent;
import com.denizenscript.depenizen.bukkit.properties.quests.QuestsPlayerProperties;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.properties.PropertyParser;

public class QuestsBridge extends Bridge {

    public static QuestsBridge instance;

    @Override
    public void init() {
        instance = this;
        PropertyParser.registerProperty(QuestsPlayerProperties.class, PlayerTag.class);
        ScriptEvent.registerScriptEvent(new PlayerCompletesQuestScriptEvent());
        ScriptEvent.registerScriptEvent(new PlayerFailsQuestScriptEvent());
        ScriptEvent.registerScriptEvent(new PlayerStartsQuestScriptEvent());
    }
}
