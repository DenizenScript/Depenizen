package com.denizenscript.depenizen.bukkit.objects.prism.properties;

import com.denizenscript.depenizen.bukkit.objects.prism.PrismAction;
import me.botsko.prism.actions.Handler;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.Mechanism;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.objects.properties.Property;
import net.aufdemrand.denizencore.tags.Attribute;

public class PrismActionAggregate implements Property {

    public static boolean describes(dObject obj) {
        return obj instanceof PrismAction;
    }

    public static PrismActionAggregate getFrom(dObject action) {
        if (!describes(action)) {
            return null;
        }
        else {
            return new PrismActionAggregate((PrismAction) action);
        }
    }

    ///////////////////
    // Instance Fields and Methods
    /////////////

    PrismAction prismAction;
    Handler action;

    private PrismActionAggregate(PrismAction action) {
        this.prismAction = action;
        this.action = action.getAction();
    }

    @Override
    public String getPropertyString() {
        return String.valueOf(action.getAggregateCount());
    }

    @Override
    public String getPropertyId() {
        return "aggregate";
    }

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <prism@action.aggregate>
        // @returns dMaterial
        // @description
        // Returns the new material for this action.
        // @Plugin Depenizen, Prism
        // -->
        if (attribute.startsWith("aggregate")) {
            return new Element(action.getAggregateCount()).getAttribute(attribute.fulfill(1));
        }

        return null;

    }

    @Override
    public void adjust(Mechanism mechanism) {

        // No documentation, internal only
        if (mechanism.matches("aggregate") && mechanism.requireObject(Element.class)) {
            prismAction.setAggregateCount(mechanism.getValue().asInt());
        }

    }

}
