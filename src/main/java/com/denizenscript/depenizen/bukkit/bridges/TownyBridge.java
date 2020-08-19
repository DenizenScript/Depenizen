package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizen.objects.CuboidTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.objects.WorldTag;
import com.denizenscript.denizen.utilities.debugging.Debug;
import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.ObjectFetcher;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.denizencore.tags.TagManager;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.events.towny.PlayerEntersTownScriptEvent;
import com.denizenscript.depenizen.bukkit.events.towny.PlayerExitsTownScriptEvent;
import com.denizenscript.depenizen.bukkit.events.towny.PlayerCreatesTownyTownScriptEvent;
import com.denizenscript.depenizen.bukkit.events.towny.TownyTownCreatedScriptEvent;
import com.denizenscript.depenizen.bukkit.objects.towny.NationTag;
import com.denizenscript.depenizen.bukkit.objects.towny.TownTag;
import com.denizenscript.depenizen.bukkit.properties.towny.TownyCuboidProperties;
import com.denizenscript.depenizen.bukkit.properties.towny.TownyLocationProperties;
import com.denizenscript.depenizen.bukkit.properties.towny.TownyPlayerProperties;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Town;

public class TownyBridge extends Bridge {

    @Override
    public void init() {
        ObjectFetcher.registerWithObjectFetcher(TownTag.class, TownTag.tagProcessor);
        ObjectFetcher.registerWithObjectFetcher(NationTag.class, NationTag.tagProcessor);
        PropertyParser.registerProperty(TownyPlayerProperties.class, PlayerTag.class);
        PropertyParser.registerProperty(TownyLocationProperties.class, LocationTag.class);
        PropertyParser.registerProperty(TownyCuboidProperties.class, CuboidTag.class);
        ScriptEvent.registerScriptEvent(new PlayerEntersTownScriptEvent());
        ScriptEvent.registerScriptEvent(new PlayerExitsTownScriptEvent());
        ScriptEvent.registerScriptEvent(new PlayerCreatesTownyTownScriptEvent());
        ScriptEvent.registerScriptEvent(new TownyTownCreatedScriptEvent());

        TagManager.registerTagHandler("town", (attribute) -> {
            // <--[tag]
            // @attribute <town[<name>]>
            // @returns TownTag
            // @plugin Depenizen, Towny
            // @description
            // Returns the town by the input name.
            // -->
            if (attribute.hasContext(1)) {
                if (TownTag.matches(attribute.getContext(1))) {
                    return attribute.contextAsType(1, TownTag.class);
                }
                else {
                    Debug.echoError("Unable to match: '" + attribute.getContext(1) + "' to a valid town!");
                    return null;
                }
            }
            Debug.echoError("Must specify a town for tag <town[<name>]>!");
            return null;
        });

        // <--[tag]
        // @attribute <towny.list_towns[(<world>)]>
        // @returns ListTag
        // @plugin Depenizen, Towny
        // @description
        // Returns a list of all towns. Optionally specify a world name.
        // -->
        TagManager.registerTagHandler("towny", (attribute) -> {
            attribute = attribute.fulfill(1);
            if (attribute.startsWith("towns") || attribute.startsWith("list_town")) {
                ListTag towns = new ListTag();
                if (attribute.hasContext(1) && WorldTag.matches(attribute.getContext(1))) {
                    try {
                        WorldTag world = attribute.contextAsType(1, WorldTag.class);
                        for (Town town : TownyAPI.getInstance().getDataSource().getWorld(world.getName()).getTowns().values()) {
                            towns.addObject(new TownTag(town));
                        }
                    }
                    catch (NotRegisteredException e) {
                        Debug.echoError("World specified is not a registered towny world!");
                        return null;
                    }
                }
                else {
                    for (Town town : TownyAPI.getInstance().getDataSource().getTowns()) {
                        towns.addObject(new TownTag(town));
                    }
                }
                return towns;
            }
            return null;
        });

        TagManager.registerTagHandler("nation", (attribute) -> {

            // <--[tag]
            // @attribute <nation[<name>]>
            // @returns NationTag
            // @plugin Depenizen, Towny
            // @description
            // Returns the nation by the input name.
            // -->
            if (attribute.hasContext(1)) {
                if (NationTag.matches(attribute.getContext(1))) {
                    return attribute.contextAsType(1, NationTag.class);
                }
                else {
                    Debug.echoError("Unable to match: '" + attribute.getContext(1) + "' to a valid nation!");
                    return null;
                }
            }
            Debug.echoError("Must specify a nation for tag <nation[<name>]>!");
            return null;
        });
    }

}
