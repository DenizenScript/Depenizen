package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizen.events.ScriptEventRegistry;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.objects.WorldTag;
import com.denizenscript.denizen.utilities.debugging.Debug;
import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.ObjectFetcher;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.ReplaceableTagEvent;
import com.denizenscript.denizencore.tags.TagManager;
import com.denizenscript.denizencore.tags.TagRunnable;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.events.bigdoors.BigDoorsDoorTogglesScriptEvent;
import com.denizenscript.depenizen.bukkit.objects.bigdoors.BigDoorsDoorTag;
import com.denizenscript.depenizen.bukkit.properties.bigdoors.BigDoorsPlayerProperties;
import com.denizenscript.depenizen.bukkit.properties.bigdoors.BigDoorsWorldProperties;
import nl.pim16aap2.bigDoors.BigDoors;
import nl.pim16aap2.bigDoors.Commander;
import org.bukkit.Bukkit;

public class BigDoorsBridge extends Bridge {

    private static BigDoors bigDoors;
    private static Commander commander;

    public static BigDoors getBigDoors() {
        return bigDoors;
    }

    public static Commander getCommander() {
        return commander;
    }

    @Override
    public void init() {
        bigDoors = (BigDoors) Bukkit.getPluginManager().getPlugin("BigDoors");
        commander = bigDoors.getCommander();
        ObjectFetcher.registerWithObjectFetcher(BigDoorsDoorTag.class);
        TagManager.registerTagHandler(new TagRunnable.RootForm() {
            @Override
            public void run(ReplaceableTagEvent event) {
                bigdoorTagEvent(event);
            }
        }, "bigdoor");
        PropertyParser.registerProperty(BigDoorsWorldProperties.class, WorldTag.class);
        PropertyParser.registerProperty(BigDoorsPlayerProperties.class, PlayerTag.class);
        ScriptEvent.registerScriptEvent(new BigDoorsDoorTogglesScriptEvent());
    }

    public void bigdoorTagEvent(ReplaceableTagEvent event) {
        Attribute attribute = event.getAttributes();

        // <--[tag]
        // @attribute <bigdoor[<door>]>
        // @returns BigDoorsDoorTag
        // @plugin Depenizen, Big Doors
        // @description
        // Returns the door for the value
        // -->
        if (attribute.hasContext(1)) {
             if (BigDoorsDoorTag.matches(attribute.getContext(1))) {
                 event.setReplacedObject(BigDoorsDoorTag.valueOf(attribute.getContext(1)).getObjectAttribute(attribute.fulfill(1)));
             }
             else {
                 Debug.echoError("Could not match: '" + attribute.getContext(1) + "' to a valid door!");
             }
        }
        attribute.fulfill(1);
    }
}
