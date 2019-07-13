package com.denizenscript.depenizen.bukkit.properties.essentials;

import com.earth2me.essentials.Essentials;
import com.denizenscript.denizen.objects.dItem;
import com.denizenscript.denizen.utilities.debugging.dB;
import com.denizenscript.denizencore.objects.Element;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.aH;
import com.denizenscript.denizencore.objects.dObject;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.depenizen.bukkit.bridges.EssentialsBridge;

import java.math.BigDecimal;

public class EssentialsItemProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "EssentialsItem";
    }

    public static boolean describes(dObject object) {
        return object instanceof dItem;
    }

    public static EssentialsItemProperties getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new EssentialsItemProperties((dItem) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "worth"
    };

    public static final String[] handledMechs = new String[] {
            "worth"
    };

    private EssentialsItemProperties(dItem item) {
        this.item = item;
    }

    dItem item;

    @Override
    public String getAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        // <--[tag]
        // @attribute <i@item.worth>
        // @returns Element(Decimal)
        // @description
        // Returns the amount of money one of this item is worth in Essentials.
        // @Plugin Depenizen, Essentials
        // -->
        if (attribute.startsWith("worth")) {
            Essentials ess = (Essentials) EssentialsBridge.instance.plugin;
            BigDecimal priceBD = ess.getWorth().getPrice(ess, item.getItemStack());
            if (priceBD == null) {
                if (!attribute.hasAlternative()) {
                    dB.echoError("Item does not have a worth value: " + item.identify());
                }
                return null;
            }
            double price = priceBD.doubleValue();
            // <--[tag]
            // @attribute <i@item.worth.quantity[<#>]>
            // @returns Element(Decimal)
            // @description
            // Returns the amount of money the quantity specified of this item is worth in Essentials.
            // @Plugin Depenizen, Essentials
            // -->
            if (attribute.getAttribute(2).startsWith("quantity") &&
                    attribute.hasContext(2) && aH.matchesInteger(attribute.getContext(2))) {
                return new Element(price * attribute.getIntContext(2)).getAttribute(attribute.fulfill(2));
            }
            return new Element(price).getAttribute(attribute.fulfill(1));
        }

        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {
        // <--[mechanism]
        // @object dItem
        // @name worth
        // @input Element(Decimal)
        // @description
        // Sets the worth of this item in Essentials.
        // @tags
        // <i@item.worth>
        // <i@item.worth.quantity[<Element>]>
        // @Plugin Depenizen, Essentials
        // -->
        if (mechanism.matches("worth") && mechanism.getValue().isDouble()) {
            Essentials ess = (Essentials) EssentialsBridge.instance.plugin;
            ess.getWorth().setPrice(ess, item.getItemStack(), mechanism.getValue().asDouble());
        }

    }
}
