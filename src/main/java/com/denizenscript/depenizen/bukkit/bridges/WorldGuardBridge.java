package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizen.Denizen;
import com.denizenscript.denizencore.DenizenCore;
import com.denizenscript.depenizen.bukkit.commands.worldguard.RegionCommand;
import com.denizenscript.depenizen.bukkit.properties.worldguard.WorldGuardCuboidProperties;
import com.denizenscript.depenizen.bukkit.properties.worldguard.WorldGuardWorldProperties;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.denizen.objects.CuboidTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.objects.WorldTag;
import com.denizenscript.denizencore.objects.ObjectFetcher;
import com.denizenscript.denizencore.tags.TagRunnable;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.denizencore.tags.ReplaceableTagEvent;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.depenizen.bukkit.properties.worldguard.WorldGuardLocationProperties;
import com.denizenscript.depenizen.bukkit.properties.worldguard.WorldGuardPlayerProperties;
import com.denizenscript.depenizen.bukkit.objects.worldguard.WorldGuardRegionTag;
import com.denizenscript.denizencore.tags.TagManager;

public class WorldGuardBridge extends Bridge {

    public static WorldGuardBridge instance;

    @Override
    public void init() {
        instance = this;
        ObjectFetcher.registerWithObjectFetcher(WorldGuardRegionTag.class);
        PropertyParser.registerProperty(WorldGuardLocationProperties.class, LocationTag.class);
        PropertyParser.registerProperty(WorldGuardPlayerProperties.class, PlayerTag.class);
        PropertyParser.registerProperty(WorldGuardCuboidProperties.class, CuboidTag.class);
        PropertyParser.registerProperty(WorldGuardWorldProperties.class, WorldTag.class);
        DenizenCore.commandRegistry.registerCommand(RegionCommand.class);
        TagManager.registerTagHandler(new TagRunnable.RootForm() {
            @Override
            public void run(ReplaceableTagEvent event) {
                tagEvent(event);
            }
        }, "region");
    }

    public void tagEvent(ReplaceableTagEvent event) {
        Attribute attribute = event.getAttributes();

        // <--[tag]
        // @attribute <region[<region>]>
        // @returns WorldGuardRegionTag
        // @plugin Depenizen, WorldGuard
        // @description
        // Returns a WorldGuard region object constructed from the input value.
        // Refer to <@link objecttype WorldGuardRegionTag>.
        // -->
        if (attribute.startsWith("region") && attribute.hasContext(1)) {
            WorldGuardRegionTag region =  attribute.contextAsType(1, WorldGuardRegionTag.class);
            if (region != null) {
                event.setReplacedObject(region.getObjectAttribute(attribute.fulfill(1)));
            }
            else {
                attribute.echoError("Unknown WorldGuard region '" + attribute.getContext(1) + "' for region[] tag.");
            }
        }
    }
}
