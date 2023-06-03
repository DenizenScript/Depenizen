package com.denizenscript.depenizen.bukkit.properties.essentials;

import com.earth2me.essentials.Essentials;
import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.Mechanism;
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

    public EssentialsItemProperties(ItemTag item) {
        this.item = item;
    }

    ItemTag item;

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        // <--[tag]
        // @attribute <ItemTag.worth>
        // @returns ElementTag(Decimal)
        // @mechanism ItemTag.worth
        // @plugin Depenizen, Essentials
        // @description
        // Returns the amount of money one of this item is worth in Essentials.
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
            attribute = attribute.fulfill(1);

            // <--[tag]
            // @attribute <ItemTag.worth.quantity[<#>]>
            // @returns ElementTag(Decimal)
            // @plugin Depenizen, Essentials
            // @description
            // Returns the amount of money the quantity specified of this item is worth in Essentials.
            // -->
            if (attribute.startsWith("quantity") && attribute.hasParam()) {
                return new ElementTag(priceBD.multiply(BigDecimal.valueOf(attribute.getIntParam())))
                        .getObjectAttribute(attribute.fulfill(1));
            }
            return new ElementTag(priceBD).getObjectAttribute(attribute);
        }

        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {

        // <--[mechanism]
        // @object ItemTag
        // @name worth
        // @input ElementTag(Decimal)
        // @plugin Depenizen, Essentials
        // @description
        // Sets the worth of this item in Essentials.
        // @tags
        // <ItemTag.worth>
        // <ItemTag.worth.quantity[<#>]>
        // -->
        if (mechanism.matches("worth") && mechanism.requireDouble()) {
            Essentials ess = (Essentials) EssentialsBridge.instance.plugin;
            ess.getWorth().setPrice(ess, item.getItemStack(), mechanism.getValue().asDouble());
        }

    }
}
