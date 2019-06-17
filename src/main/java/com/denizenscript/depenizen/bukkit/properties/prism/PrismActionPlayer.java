package com.denizenscript.depenizen.bukkit.properties.prism;

import com.denizenscript.depenizen.bukkit.objects.prism.PrismAction;
import me.botsko.prism.actions.Handler;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.Mechanism;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.objects.properties.Property;
import net.aufdemrand.denizencore.tags.Attribute;

public class PrismActionPlayer implements Property {

    public static boolean describes(dObject obj) {
        return obj instanceof PrismAction;
    }

    public static PrismActionPlayer getFrom(dObject action) {
        if (!describes(action)) {
            return null;
        }
        else {
            return new PrismActionPlayer((PrismAction) action);
        }
    }

    ///////////////////
    // Instance Fields and Methods
    /////////////

    PrismAction prismAction;
    Handler action;

    private PrismActionPlayer(PrismAction action) {
        this.prismAction = action;
        this.action = action.getAction();
    }

    @Override
    public String getPropertyString() {
        return "p@" + action.getPlayerName();
    }

    @Override
    public String getPropertyId() {
        return "player";
    }

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <prism@action.player>
        // @returns dPlayer
        // @description
        // Returns the player who performed this action.
        // @Plugin Depenizen, Prism
        // -->
        if (attribute.startsWith("player")) {
            return dPlayer.valueOf(getPropertyString()).getAttribute(attribute.fulfill(1));
        }

        return null;

    }

    @Override
    public void adjust(Mechanism mechanism) {

        // No documentation, internal only
        if (mechanism.matches("player") && mechanism.requireObject(dPlayer.class)) {
            prismAction.setPlayer(mechanism.valueAsType(dPlayer.class).getName());
        }

    }
}
