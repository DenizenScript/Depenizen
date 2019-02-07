package com.denizenscript.depenizen.bukkit.extensions.prism;

import com.denizenscript.depenizen.bukkit.extensions.dObjectExtension;
import com.denizenscript.depenizen.bukkit.objects.prism.PrismAction;
import com.denizenscript.depenizen.bukkit.support.Support;
import com.denizenscript.depenizen.bukkit.support.plugins.PrismSupport;
import me.botsko.prism.Prism;
import me.botsko.prism.actionlibs.ActionsQuery;
import me.botsko.prism.actionlibs.QueryParameters;
import me.botsko.prism.actions.Handler;
import me.botsko.prism.appliers.PrismProcessType;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;

import java.util.List;

public class PrismLocationExtension extends dObjectExtension {

    public static boolean describes(dObject object) {
        return object instanceof dLocation;
    }

    public static PrismLocationExtension getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new PrismLocationExtension((dLocation) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "prism_logs"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    private PrismLocationExtension(dLocation location) {
        this.location = location;
    }

    dLocation location = null;

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <l@location.prism_logs>
        // @returns dList(PrismAction)
        // @description
        // Returns a list of prism logs for this location.
        // @Plugin DepenizenBukkit, Prism
        // -->
        if (attribute.startsWith("prism_logs")) {
            attribute = attribute.fulfill(1);
            QueryParameters params = new QueryParameters();
            params.setProcessType(PrismProcessType.LOOKUP);

            boolean isRadius = false;

            // <--[tag]
            // @attribute <l@location.prism_logs.radius[<#>]>
            // @returns dList(PrismAction)
            // @description
            // Returns a list of prism logs for a specified radius of blocks around this location.
            // @Plugin DepenizenBukkit, Prism
            // -->
            if (attribute.startsWith("radius")) {
                params.setRadius(attribute.getIntContext(1));
                isRadius = true;
                attribute = attribute.fulfill(1);

                // <--[tag]
                // @attribute <l@location.prism_logs.radius[<#>].types[<action>|...]>
                // @returns dList(PrismAction)
                // @description
                // Returns a list of prism logs for a specified radius of blocks around this location
                // with a search for action types.
                // For example, <player.location.prism_logs.radius[10].types[block-break|block-place]>
                // @Plugin DepenizenBukkit, Prism
                // -->
                if (attribute.startsWith("types")) {
                    for (String type : dList.valueOf(attribute.getContext(1))) {
                        params.addActionType(type);
                    }
                    attribute = attribute.fulfill(1);
                }
            }
            // <--[tag]
            // @attribute <l@location.prism_logs.types[<action>|...]>
            // @returns dList(PrismAction)
            // @description
            // Returns a list of prism logs for this location, with a search for action types.
            // For example, <player.location.prism_logs.types[block-break|block-place]>
            // @Plugin DepenizenBukkit, Prism
            // -->
            else if (attribute.startsWith("types")) {
                for (String type : dList.valueOf(attribute.getContext(1))) {
                    params.addActionType(type);
                }
                attribute = attribute.fulfill(1);

                // <--[tag]
                // @attribute <l@location.prism_logs.types[<action>|...].radius[<#>]>
                // @returns dList(PrismAction)
                // @description
                // Returns a list of prism logs, with a search for action types, for a specified radius
                // of blocks around this location.
                // For example, <player.location.prism_logs.types[block-break|block-place].radius[10]>
                // @Plugin DepenizenBukkit, Prism
                // -->
                if (attribute.startsWith("radius")) {
                    params.setRadius(attribute.getIntContext(1));
                    isRadius = true;
                    attribute = attribute.fulfill(1);
                }
            }


            params.setWorld(location.getWorld().getName());
            if (isRadius) {
                params.setMinMaxVectorsFromPlayerLocation(location);
            }
            else {
                params.setSpecificBlockLocation(location);
            }

            ActionsQuery query = new ActionsQuery((Prism) Support.getPlugin(PrismSupport.class));
            List<Handler> results = query.lookup(params).getActionResults();
            dList list = new dList();
            for (Handler action : results) {
                list.add(new PrismAction(action).identify());
            }
            return list.getAttribute(attribute);
        }

        return null;

    }

}
