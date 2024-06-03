package com.denizenscript.depenizen.bukkit.properties.essentials;

import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.utilities.debugging.SlowWarning;
import com.denizenscript.depenizen.bukkit.bridges.EssentialsBridge;

import java.math.BigDecimal;

public class EssentialsItemExtensions {

    public static SlowWarning oldWorthTag = new SlowWarning("essentialsItemWorth", "The tag 'ItemTag.worth' from Depenizen/Essentials is deprecated: use 'ItemTag.essentials_worth'.");

    public static void register() {

        // <--[tag]
        // @attribute <ItemTag.essentials_worth>
        // @returns ElementTag(Decimal)
        // @mechanism ItemTag.essentials_worth
        // @plugin Depenizen, Essentials
        // @description
        // Returns the amount of money one of this item is worth in Essentials.
        // -->
        ItemTag.tagProcessor.registerTag(ElementTag.class, "essentials_worth", (attribute, item) -> {
            BigDecimal price = EssentialsBridge.essentials.getWorth().getPrice(EssentialsBridge.essentials, item.getItemStack());
            if (price == null) {
                attribute.echoError("Item does not have a worth value: " + item.identify());
                return null;
            }
            return new ElementTag(price);
        });

        // <--[tag]
        // @attribute <ItemTag.worth>
        // @returns ElementTag(Decimal)
        // @mechanism ItemTag.essentials_worth
        // @plugin Depenizen, Essentials
        // @deprecated Use 'ItemTag.essentials_worth'
        // @description
        // Deprecated in favor of <@link tag ItemTag.essentials_worth>.
        // -->
        ItemTag.tagProcessor.registerTag(ElementTag.class, "worth", (attribute, item) -> {
            oldWorthTag.warn();
            BigDecimal price = EssentialsBridge.essentials.getWorth().getPrice(EssentialsBridge.essentials, item.getItemStack());
            if (price == null) {
                attribute.echoError("Item does not have a worth value: " + item.identify());
                return null;
            }

            // <--[tag]
            // @attribute <ItemTag.worth.quantity[<#>]>
            // @returns ElementTag(Decimal)
            // @plugin Depenizen, Essentials
            // @deprecated Use 'ItemTag.essentials_worth.mul[<#>]'
            // @description
            // Deprecated in favor of <@link tag ItemTag.essentials_worth>.
            // -->
            if (attribute.startsWith("quantity", 2)) {
                attribute.fulfill(1);
                return new ElementTag(price.multiply(BigDecimal.valueOf(attribute.hasParam() ? attribute.getIntParam() : 1)));
            }
            return new ElementTag(price);
        });

        // <--[mechanism]
        // @object ItemTag
        // @name essentials_worth
        // @input ElementTag(Decimal)
        // @plugin Depenizen, Essentials
        // @description
        // Sets the worth of this item in Essentials.
        // @tags
        // <ItemTag.essentials_worth>
        // -->
        ItemTag.tagProcessor.registerMechanism("essentials_worth", false, ElementTag.class, (object, mechanism, input) -> {
            if (mechanism.requireDouble()) {
                EssentialsBridge.essentials.getWorth().setPrice(EssentialsBridge.essentials, object.getItemStack(), input.asDouble());
            }
        }, "worth");
    }
}
