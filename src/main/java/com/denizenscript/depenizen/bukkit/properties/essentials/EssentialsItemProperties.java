package com.denizenscript.depenizen.bukkit.properties.essentials;

import com.earth2me.essentials.Essentials;
import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizen.utilities.debugging.Debug;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ArgumentHelper;
import com.denizenscript.denizencore.objects.ObjectTag;
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

    public static boolean describes(ObjectTag object) {
        return object instanceof ItemTag;
    }

    public static EssentialsItemProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new EssentialsItemProperties((ItemTag) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "worth"
    };

    public static final String[] handledMechs = new String[] {
            "worth"
    };

    private EssentialsItemProperties(ItemTag item) {
        this.item = item;
    }

    ItemTag item;

    @Override
    public String getAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        // <--[tag]
        // @attribute <ItemTag.worth>
        // @returns ElementTag(Decimal)
        // @description
        // Returns the amount of money one of this item is worth in Essentials.
        // @Plugin Depenizen, Essentials
        // -->
        if (attribute.startsWith("worth")) {
            Essentials ess = (Essentials) EssentialsBridge.instance.plugin;
            BigDecimal priceBD = ess.getWorth().getPrice(ess, item.getItemStack());
            if (priceBD == null) {
                if (!attribute.hasAlternative()) {
                    Debug.echoError("Item does not have a worth value: " + item.identify());
                }
                return null;
            }
            double price = priceBD.doubleValue();
            // <--[tag]
            // @attribute <ItemTag.worth.quantity[<#>]>
            // @returns ElementTag(Decimal)
            // @description
            // Returns the amount of money the quantity specified of this item is worth in Essentials.
            // @Plugin Depenizen, Essentials
            // -->
            if (attribute.getAttribute(2).startsWith("quantity") &&
                    attribute.hasContext(2) && ArgumentHelper.matchesInteger(attribute.getContext(2))) {
                return new ElementTag(price * attribute.getIntContext(2)).getAttribute(attribute.fulfill(2));
            }
            return new ElementTag(price).getAttribute(attribute.fulfill(1));
        }

        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {
        // <--[mechanism]
        // @object ItemTag
        // @name worth
        // @input Element(Decimal)
        // @description
        // Sets the worth of this item in Essentials.
        // @tags
        // <ItemTag.worth>
        // <ItemTag.worth.quantity[<Element>]>
        // @Plugin Depenizen, Essentials
        // -->
        if (mechanism.matches("worth") && mechanism.getValue().isDouble()) {
            Essentials ess = (Essentials) EssentialsBridge.instance.plugin;
            ess.getWorth().setPrice(ess, item.getItemStack(), mechanism.getValue().asDouble());
        }

    }
}
