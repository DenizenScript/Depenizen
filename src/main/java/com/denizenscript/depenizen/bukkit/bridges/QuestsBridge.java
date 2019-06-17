package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.extensions.quests.QuestsPlayerExtension;
import com.denizenscript.depenizen.bukkit.Bridge;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.properties.PropertyParser;

public class QuestsBridge extends Bridge {

    @Override
    public void init() {
        PropertyParser.registerProperty(QuestsPlayerExtension.class, dPlayer.class);
    }
}
