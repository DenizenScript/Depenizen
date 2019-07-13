package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.events.askyblock.PlayerExitsSkyBlockScriptEvent;
import com.denizenscript.depenizen.bukkit.events.askyblock.PlayerCompletesSkyBlockChallengeScriptEvent;
import com.denizenscript.depenizen.bukkit.events.askyblock.PlayerEntersSkyBlockScriptEvent;
import com.denizenscript.depenizen.bukkit.events.askyblock.SkyBlockCreatedScriptEvent;
import com.denizenscript.depenizen.bukkit.events.askyblock.SkyBlockResetScriptEvent;
import com.wasteofplastic.askyblock.ASkyBlockAPI;
import com.denizenscript.denizen.objects.dLocation;
import com.denizenscript.denizen.objects.dPlayer;
import com.denizenscript.denizen.objects.dWorld;
import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.TagRunnable;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.denizencore.tags.ReplaceableTagEvent;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.depenizen.bukkit.properties.askyblock.ASkyBlockLocationProperties;
import com.denizenscript.depenizen.bukkit.properties.askyblock.ASkyBlockPlayerProperties;
import com.denizenscript.depenizen.bukkit.properties.askyblock.ASkyBlockWorldProperties;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.denizencore.tags.TagManager;

public class ASkyBlockBridge extends Bridge {

    @Override
    public void init() {
        api = ASkyBlockAPI.getInstance();
        PropertyParser.registerProperty(ASkyBlockPlayerProperties.class, dPlayer.class);
        PropertyParser.registerProperty(ASkyBlockLocationProperties.class, dLocation.class);
        PropertyParser.registerProperty(ASkyBlockWorldProperties.class, dWorld.class);
        ScriptEvent.registerScriptEvent(new SkyBlockCreatedScriptEvent());
        ScriptEvent.registerScriptEvent(new SkyBlockResetScriptEvent());
        ScriptEvent.registerScriptEvent(new PlayerEntersSkyBlockScriptEvent());
        ScriptEvent.registerScriptEvent(new PlayerExitsSkyBlockScriptEvent());
        ScriptEvent.registerScriptEvent(new PlayerCompletesSkyBlockChallengeScriptEvent());
        TagManager.registerTagHandler(new TagRunnable.RootForm() {
            @Override
            public void run(ReplaceableTagEvent event) {
                tagEvent(event);
            }
        }, "skyblock");
        // TODO: Skyblock Command
    }

    public ASkyBlockAPI api;

    public void tagEvent(ReplaceableTagEvent event) {
        Attribute attribute = event.getAttributes().fulfill(1);

        // <--[tag]
        // @attribute <skyblock.island_world>
        // @returns dWorld
        // @description
        // Returns the world that A Skyblock uses for islands.
        // @Plugin Depenizen, A SkyBlock
        // -->
        if (attribute.startsWith("island_world")) {
            event.setReplacedObject(new dWorld(api.getIslandWorld()).getObjectAttribute(attribute.fulfill(1)));
        }

        // <--[tag]
        // @attribute <skyblock.nether_world>
        // @returns dWorld
        // @description
        // Returns the world that A Skyblock uses for the nether.
        // @Plugin Depenizen, A SkyBlock
        // -->
        else if (attribute.startsWith("nether_world")) {
            event.setReplacedObject(new dWorld(api.getNetherWorld()).getObjectAttribute(attribute.fulfill(1)));
        }

        // <--[tag]
        // @attribute <skyblock.island_count>
        // @returns ElementTag(Number)
        // @description
        // Returns the number of Skyblock Islands that exist.
        // @Plugin Depenizen, A SkyBlock
        // -->
        else if (attribute.startsWith("island_count")) {
            event.setReplacedObject(new ElementTag(api.getIslandCount()).getObjectAttribute(attribute.fulfill(1)));
        }
    }
}
