package net.gnomeffinway.depenizen.extensions.prism;

import me.botsko.prism.Prism;
import me.botsko.prism.actionlibs.ActionsQuery;
import me.botsko.prism.actionlibs.QueryParameters;
import me.botsko.prism.actions.Handler;
import me.botsko.prism.appliers.PrismProcessType;
import net.aufdemrand.denizen.objects.dList;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.objects.dObject;
import net.aufdemrand.denizen.tags.Attribute;
import net.gnomeffinway.depenizen.extensions.dObjectExtension;
import net.gnomeffinway.depenizen.objects.prism.PrismAction;
import net.gnomeffinway.depenizen.support.Supported;

import java.util.List;

public class PrismLocationExtension extends dObjectExtension {

    public static boolean describes(dObject loc) {
        return loc instanceof dLocation;
    }

    public static PrismLocationExtension getFrom(dObject loc) {
        if (!describes(loc)) return null;
        else return new PrismLocationExtension((dLocation) loc);
    }

    private PrismLocationExtension(dLocation loc) {
        location = loc;
    }

    dLocation location = null;

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <l@location.prism_logs>
        // @returns dList(PrismAction)
        // @description
        // Returns a list of prism logs for this location.
        // @plugin Prism
        // -->
        if (attribute.startsWith("prism_logs")) {
            attribute = attribute.fulfill(1);
            QueryParameters params = new QueryParameters();
            params.setProcessType(PrismProcessType.LOOKUP);

            // <--[tag]
            // @attribute <l@location.prism_logs.radius[<#>]>
            // @returns dList(PrismAction)
            // @description
            // Returns a list of prism logs for a specified radius of blocks around this location.
            // @plugin Prism
            // -->
            if (attribute.startsWith("radius")) {
                params.setRadius(attribute.getIntContext(1));
                attribute = attribute.fulfill(1);

                // <--[tag]
                // @attribute <l@location.prism_logs.radius[<#>].types[<action>|...]>
                // @returns dList(PrismAction)
                // @description
                // Returns a list of prism logs for a specified radius of blocks around this location
                // with a search for action types.
                // For example, <player.location.prism_logs.radius[10].types[block-break|block-place]>
                // @plugin Prism
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
            // @plugin Prism
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
                // @plugin Prism
                // -->
                if (attribute.startsWith("radius")) {
                    params.setRadius(attribute.getIntContext(1));
                    attribute = attribute.fulfill(1);
                }
            }


            params.setWorld(location.getWorld().getName());
            params.setMinMaxVectorsFromPlayerLocation(location);

            ActionsQuery query = new ActionsQuery((Prism) Supported.get("PRISM").getPlugin());
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
