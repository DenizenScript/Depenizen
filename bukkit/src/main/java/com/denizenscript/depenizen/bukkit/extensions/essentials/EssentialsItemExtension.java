package com.denizenscript.depenizen.bukkit.extensions.essentials;

import com.denizenscript.depenizen.bukkit.support.Support;
import com.earth2me.essentials.Essentials;
import net.aufdemrand.denizen.objects.dItem;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.Mechanism;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;
import com.denizenscript.depenizen.bukkit.extensions.dObjectExtension;
import com.denizenscript.depenizen.bukkit.support.plugins.EssentialsSupport;

import java.math.BigDecimal;

public class EssentialsItemExtension extends dObjectExtension {

    public static boolean describes(dObject object) {
        return object instanceof dItem;
    }

    public static EssentialsItemExtension getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new EssentialsItemExtension((dItem) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "worth"
    };

    public static final String[] handledMechs = new String[] {
            "worth"
    };

    private EssentialsItemExtension(dItem item) {
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
        // @Plugin DepenizenBukkit, Essentials
        // -->
        if (attribute.startsWith("worth")) {
            Essentials ess = Support.getPlugin(EssentialsSupport.class);
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
            // @Plugin DepenizenBukkit, Essentials
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
        // @Plugin DepenizenBukkit, Essentials
        // -->
        if (mechanism.matches("worth") && mechanism.getValue().isDouble()) {
            Essentials ess = Support.getPlugin(EssentialsSupport.class);
            ess.getWorth().setPrice(ess, item.getItemStack(), mechanism.getValue().asDouble());
        }

    }
}
