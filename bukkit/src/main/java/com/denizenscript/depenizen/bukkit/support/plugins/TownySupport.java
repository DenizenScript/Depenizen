package com.denizenscript.depenizen.bukkit.support.plugins;

import com.denizenscript.depenizen.bukkit.events.towny.PlayerEntersTownScriptEvent;
import com.denizenscript.depenizen.bukkit.events.towny.PlayerExitsTownScriptEvent;
import com.denizenscript.depenizen.bukkit.extensions.towny.TownyCuboidExtension;
import com.denizenscript.depenizen.bukkit.extensions.towny.TownyLocationExtension;
import com.denizenscript.depenizen.bukkit.objects.dNation;
import com.denizenscript.depenizen.bukkit.objects.dTown;
import com.denizenscript.depenizen.bukkit.support.Support;
import com.denizenscript.depenizen.bukkit.extensions.towny.TownyPlayerExtension;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import net.aufdemrand.denizen.objects.dCuboid;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.tags.TagContext;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizencore.tags.Attribute;

public class TownySupport extends Support {

    public TownySupport() {
        registerObjects(dTown.class, dNation.class);
        registerProperty(TownyPlayerExtension.class, dPlayer.class);
        registerProperty(TownyLocationExtension.class, dLocation.class);
        registerProperty(TownyCuboidExtension.class, dCuboid.class);
        registerScriptEvents(new PlayerEntersTownScriptEvent());
        registerScriptEvents(new PlayerExitsTownScriptEvent());
        registerAdditionalTags("towny", "town", "nation");
    }

    @Override
    public String additionalTags(Attribute attribute, TagContext tagContext) {

        if (attribute.startsWith("towny")) {

            attribute = attribute.fulfill(1);

            // <--[tag]
            // @attribute <towny.list_towns[<world name>]>
            // @returns dList(Element)
            // @description
            // Returns a list of all towns. Optionally specify a world name.
            // @Plugin DepenizenBukkit, Towny
            // -->
            if (attribute.startsWith("list_towns")) {
                dList towns = new dList();
                if (attribute.hasContext(1)) {
                    try {
                        for(Town town : TownyUniverse.getDataSource().getWorld(attribute.getContext(1)).getTowns()) {
                            towns.add(new dTown(town).identify());
                        }
                    } catch (NotRegisteredException e) {
                        dB.echoError("World specified is not a registered towny world!");
                        return null;
                    }
                }
                else {
                    for(Town town : TownyUniverse.getDataSource().getTowns()) {
                        towns.add(new dTown(town).identify());
                    }
                }
                return towns.getAttribute(attribute.fulfill(1));
            }
        }

        else if (attribute.startsWith("town")) {

            dTown town = null;

            if (attribute.hasContext(1)) {
                if (dTown.matches(attribute.getContext(1))) {
                    town = dTown.valueOf(attribute.getContext(1));
                }
                else {
                    dB.echoError("Could not match '" + attribute.getContext(1) + "' to a valid town!");
                    return null;
                }
            }

            if (town == null) {
                dB.echoError("Invalid or missing town!");
                return null;
            }

            return town.getAttribute(attribute.fulfill(1));

        }

        else if (attribute.startsWith("nation")) {

            dNation nation = null;

            if (attribute.hasContext(1)) {
                if (dNation.matches(attribute.getContext(1))) {
                    nation = dNation.valueOf(attribute.getContext(1));
                }
                else {
                    dB.echoError("Could not match '" + attribute.getContext(1) + "' to a valid nation!");
                    return null;
                }
            }

            if (nation == null) {
                dB.echoError("Invalid or missing nation!");
                return null;
            }

            return nation.getAttribute(attribute.fulfill(1));

        }

        return null;

    }
}
