package com.denizenscript.depenizen.bukkit.properties.essentials;

import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.denizencore.utilities.debugging.SlowWarning;
import com.denizenscript.depenizen.bukkit.bridges.EssentialsBridge;

import java.math.BigDecimal;

public class EssentialsItemExtensions {

    public static SlowWarning oldWorthQuantityTag = new SlowWarning("essentialsItemWorthQuantity", "The tag 'ItemTag.worth.quantity[<#>]' from Depenizen/Essentials is deprecated: use 'ItemTag.worth.mul[<#>]'.");

    public static void register() {

        // <--[tag]
        // @attribute <ItemTag.worth>
        // @returns ElementTag(Decimal)
        // @mechanism ItemTag.worth
        // @plugin Depenizen, Essentials
        // @description
        // Returns the amount of money one of this item is worth in Essentials.
        // -->
        ItemTag.tagProcessor.registerTag(ElementTag.class, "worth", (attribute, item) -> {
            BigDecimal price = EssentialsBridge.essentials.getWorth().getPrice(EssentialsBridge.essentials, item.getItemStack());
            if (price == null) {
                if (!attribute.hasAlternative()) {
                    Debug.echoError("Item does not have a worth value: " + item.identify());
                }
                return null;
            }

            // <--[tag]
            // @attribute <ItemTag.worth.quantity[<#>]>
            // @returns ElementTag(Decimal)
            // @plugin Depenizen, Essentials
            // @deprecated Use 'ElementTag.mul[<#>]'
            // @description
            // Returns the amount of money the quantity specified of this item is worth in Essentials.
            // -->
            if (attribute.startsWith("quantity", 2)) {
                attribute.fulfill(1);
                oldWorthQuantityTag.warn();
                return new ElementTag(price.multiply(BigDecimal.valueOf(attribute.hasParam() ? attribute.getIntParam() : 1)));
            }
            return new ElementTag(price);
        });

        // <--[mechanism]
        // @object ItemTag
        // @name worth
        // @input ElementTag(Decimal)
        // @plugin Depenizen, Essentials
        // @description
        // Sets the worth of this item in Essentials.
        // @tags
        // <ItemTag.worth>
        // -->
        ItemTag.tagProcessor.registerMechanism("worth", false, ElementTag.class, (object, mechanism, input) -> {
            if (mechanism.requireDouble()) {
                EssentialsBridge.essentials.getWorth().setPrice(EssentialsBridge.essentials, object.getItemStack(), input.asDouble());;
            }
        });

    }

}
