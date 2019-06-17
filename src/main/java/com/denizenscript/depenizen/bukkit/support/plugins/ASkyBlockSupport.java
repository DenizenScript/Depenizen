package com.denizenscript.depenizen.bukkit.support.plugins;

import com.denizenscript.depenizen.bukkit.events.askyblock.PlayerExitsSkyBlockScriptEvent;
import com.denizenscript.depenizen.bukkit.events.askyblock.PlayerCompletesSkyBlockChallengeScriptEvent;
import com.denizenscript.depenizen.bukkit.events.askyblock.PlayerEntersSkyBlockScriptEvent;
import com.denizenscript.depenizen.bukkit.events.askyblock.SkyBlockCreatedScriptEvent;
import com.denizenscript.depenizen.bukkit.events.askyblock.SkyBlockResetScriptEvent;
import com.wasteofplastic.askyblock.ASkyBlockAPI;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.objects.dWorld;
import net.aufdemrand.denizencore.tags.TagContext;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.tags.Attribute;
import com.denizenscript.depenizen.bukkit.extensions.askyblock.ASkyBlockLocationExtension;
import com.denizenscript.depenizen.bukkit.extensions.askyblock.ASkyBlockPlayerExtension;
import com.denizenscript.depenizen.bukkit.extensions.askyblock.ASkyBlockWorldExtension;
import com.denizenscript.depenizen.bukkit.support.Support;

public class ASkyBlockSupport extends Support {

    public ASkyBlockSupport() {
        registerProperty(ASkyBlockPlayerExtension.class, dPlayer.class);
        registerProperty(ASkyBlockLocationExtension.class, dLocation.class);
        registerProperty(ASkyBlockWorldExtension.class, dWorld.class);
        registerScriptEvents(new SkyBlockCreatedScriptEvent());
        registerScriptEvents(new SkyBlockResetScriptEvent());
        registerScriptEvents(new PlayerEntersSkyBlockScriptEvent());
        registerScriptEvents(new PlayerExitsSkyBlockScriptEvent());
        registerScriptEvents(new PlayerCompletesSkyBlockChallengeScriptEvent());
        // TODO: Skyblock Command
    }

    ASkyBlockAPI api = ASkyBlockAPI.getInstance();

    @Override
    public String additionalTags(Attribute attribute, TagContext tagContext) {
        if (attribute == null) {
            return null;
        }

        if (attribute.startsWith("skyblock")) {
            attribute = attribute.fulfill(1);

            // <--[tag]
            // @attribute <skyblock.island_world>
            // @returns dWorld
            // @description
            // Returns the world that A Skyblock uses for islands.
            // @Plugin DepenizenBukkit, A SkyBlock
            // -->
            if (attribute.startsWith("island_world")) {
                return new dWorld(api.getIslandWorld()).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <skyblock.nether_world>
            // @returns dWorld
            // @description
            // Returns the world that A Skyblock uses for the nether.
            // @Plugin DepenizenBukkit, A SkyBlock
            // -->
            else if (attribute.startsWith("nether_world")) {
                return new dWorld(api.getNetherWorld()).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <skyblock.island_count>
            // @returns Element(Number)
            // @description
            // Returns the number of Skyblock Islands that exist.
            // @Plugin DepenizenBukkit, A SkyBlock
            // -->
            else if (attribute.startsWith("island_count")) {
                return new Element(api.getIslandCount()).getAttribute(attribute.fulfill(1));
            }
        }

        return null;
    }

}
