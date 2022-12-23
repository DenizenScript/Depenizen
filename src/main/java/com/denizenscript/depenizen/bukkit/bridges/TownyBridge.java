package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizen.objects.WorldTag;
import com.denizenscript.depenizen.bukkit.events.towny.*;
import com.denizenscript.depenizen.bukkit.properties.towny.TownyCuboidProperties;
import com.denizenscript.depenizen.bukkit.properties.towny.TownyLocationProperties;
import com.denizenscript.depenizen.bukkit.objects.towny.NationTag;
import com.denizenscript.depenizen.bukkit.objects.towny.TownTag;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.properties.towny.TownyPlayerProperties;
import com.denizenscript.depenizen.bukkit.properties.towny.TownyWorldProperties;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.denizenscript.denizen.objects.CuboidTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.ObjectFetcher;
import com.denizenscript.denizencore.tags.TagRunnable;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.denizencore.tags.ReplaceableTagEvent;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.TagManager;
import com.palmergames.bukkit.towny.object.TownyWorld;

public class TownyBridge extends Bridge {

    @Override
    public void init() {
        ObjectFetcher.registerWithObjectFetcher(TownTag.class, TownTag.tagProcessor);
        ObjectFetcher.registerWithObjectFetcher(NationTag.class, NationTag.tagProcessor);
        PropertyParser.registerProperty(TownyPlayerProperties.class, PlayerTag.class);
        TownyLocationProperties.register();
        PropertyParser.registerProperty(TownyCuboidProperties.class, CuboidTag.class);
        PropertyParser.registerProperty(TownyWorldProperties.class, WorldTag.class);
        ScriptEvent.registerScriptEvent(PlayerClaimsPlotScriptEvent.class);
        ScriptEvent.registerScriptEvent(PlayerCreatesTownScriptEvent.class);
        ScriptEvent.registerScriptEvent(PlayerEntersTownScriptEvent.class);
        ScriptEvent.registerScriptEvent(PlayerExitsTownScriptEvent.class);
        ScriptEvent.registerScriptEvent(TownCreatedScriptEvent.class);
        ScriptEvent.registerScriptEvent(PlayerJoinsTownScriptEvent.class);
        ScriptEvent.registerScriptEvent(PlayerLeavesTownScriptEvent.class);
        TagManager.registerTagHandler(new TagRunnable.RootForm() {
            @Override
            public void run(ReplaceableTagEvent event) {
                townyTagEvent(event);
            }
        }, "towny");
        TagManager.registerTagHandler(new TagRunnable.RootForm() {
            @Override
            public void run(ReplaceableTagEvent event) {
                townTagEvent(event);
            }
        }, "town");
        TagManager.registerTagHandler(new TagRunnable.RootForm() {
            @Override
            public void run(ReplaceableTagEvent event) {
                nationTagEvent(event);
            }
        }, "nation");
    }

    public void townyTagEvent(ReplaceableTagEvent event) {
        Attribute attribute = event.getAttributes().fulfill(1);

        // <--[tag]
        // @attribute <towny.list_towns[(<world>)]>
        // @returns ListTag(TownTag)
        // @plugin Depenizen, Towny
        // @description
        // Returns a list of all towns. Optionally specify a world name.
        // -->
        if (attribute.startsWith("list_towns")) {
            ListTag towns = new ListTag();
            if (attribute.hasParam()) {
                TownyWorld world = TownyAPI.getInstance().getDataSource().getWorld(attribute.getParam().replace("w@", ""));
                if (world == null) {
                    attribute.echoError("World specified is not a registered towny world!");
                    return;
                }
                for (Town town : world.getTowns().values()) {
                    towns.addObject(new TownTag(town));
                }
            }
            else {
                for (Town town : TownyUniverse.getInstance().getTowns()) {
                    towns.addObject(new TownTag(town));
                }
            }
            event.setReplacedObject(towns.getObjectAttribute(attribute.fulfill(1)));
        }

        // <--[tag]
        // @attribute <towny.nations>
        // @returns ListTag(NationTag)
        // @plugin Depenizen, Towny
        // @description
        // Returns a list of all nations.
        // -->
        if (attribute.startsWith("nations")) {
            ListTag nations = new ListTag();
            for (Nation nation : TownyUniverse.getInstance().getNations()) {
                nations.addObject(new NationTag(nation));
            }
            event.setReplacedObject(nations.getObjectAttribute(attribute.fulfill(1)));
        }
    }

    public void townTagEvent(ReplaceableTagEvent event) {
        Attribute attribute = event.getAttributes();

        // <--[tag]
        // @attribute <town[<name>]>
        // @returns TownTag
        // @plugin Depenizen, Towny
        // @description
        // Returns the town by the input name.
        // -->
        if (attribute.hasParam()) {
            TownTag town;
            if (TownTag.matches(attribute.getParam())) {
                town = attribute.paramAsType(TownTag.class);
            }
            else {
                attribute.echoError("Could not match '" + attribute.getParam() + "' to a valid town!");
                return;
            }
            if (town != null) {
                event.setReplacedObject(town.getObjectAttribute(attribute.fulfill(1)));
            }
            else {
                attribute.echoError("Unknown town '" + attribute.getParam() + "' for town[] tag.");
            }
        }
    }

    public void nationTagEvent(ReplaceableTagEvent event) {
        Attribute attribute = event.getAttributes();

        // <--[tag]
        // @attribute <nation[<name>]>
        // @returns NationTag
        // @plugin Depenizen, Towny
        // @description
        // Returns the nation by the input name.
        // -->
        if (attribute.hasParam()) {
            NationTag nation;
            if (NationTag.matches(attribute.getParam())) {
                nation = attribute.paramAsType(NationTag.class);
            }
            else {
                attribute.echoError("Could not match '" + attribute.getParam() + "' to a valid nation!");
                return;
            }
            if (nation != null) {
                event.setReplacedObject(nation.getObjectAttribute(attribute.fulfill(1)));
            }
            else {
                attribute.echoError("Unknown nation '" + attribute.getParam() + "' for nation[] tag.");
            }
        }

    }
}
