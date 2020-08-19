package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.objects.WorldTag;
import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.ObjectFetcher;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.denizencore.tags.TagManager;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.events.bigdoors.BigDoorsDoorTogglesScriptEvent;
import com.denizenscript.depenizen.bukkit.objects.bigdoors.BigDoorsDoorTag;
import com.denizenscript.depenizen.bukkit.properties.bigdoors.BigDoorsPlayerProperties;
import com.denizenscript.depenizen.bukkit.properties.bigdoors.BigDoorsWorldProperties;
import nl.pim16aap2.bigDoors.BigDoors;
import nl.pim16aap2.bigDoors.Commander;
import org.bukkit.Bukkit;

public class BigDoorsBridge extends Bridge {

    public static BigDoors bigDoors;
    public static Commander commander;

    @Override
    public void init() {
        bigDoors = (BigDoors) Bukkit.getPluginManager().getPlugin("BigDoors");
        commander = bigDoors.getCommander();
        ObjectFetcher.registerWithObjectFetcher(BigDoorsDoorTag.class, BigDoorsDoorTag.tagProcessor);
        PropertyParser.registerProperty(BigDoorsWorldProperties.class, WorldTag.class);
        PropertyParser.registerProperty(BigDoorsPlayerProperties.class, PlayerTag.class);
        ScriptEvent.registerScriptEvent(new BigDoorsDoorTogglesScriptEvent());

        TagManager.registerTagHandler("bigdoor", (attribute) -> {
            if (attribute.hasContext(1)) {
                return BigDoorsDoorTag.valueOf(attribute.getContext(1), attribute.context);
            }
            return null;
        });

    }
}
