package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.properties.quests.QuestsPlayerProperties;
import com.denizenscript.depenizen.bukkit.Bridge;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.properties.PropertyParser;

public class QuestsBridge extends Bridge {

    public static QuestsBridge instance;

    @Override
    public void init() {
        instance = this;
        PropertyParser.registerProperty(QuestsPlayerProperties.class, dPlayer.class);
    }
}
