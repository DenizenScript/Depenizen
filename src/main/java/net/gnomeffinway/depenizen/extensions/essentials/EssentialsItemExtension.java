package net.gnomeffinway.depenizen.extensions.essentials;

import com.earth2me.essentials.Essentials;
import net.aufdemrand.denizen.objects.dItem;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.Mechanism;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;
import net.gnomeffinway.depenizen.extensions.dObjectExtension;
import net.gnomeffinway.depenizen.support.Support;
import net.gnomeffinway.depenizen.support.plugins.EssentialsSupport;

public class EssentialsItemExtension extends dObjectExtension {

    public static boolean describes(dObject item) {
        return item instanceof dItem;
    }

    public static EssentialsItemExtension getFrom(dObject item) {
        if (!describes(item)) return null;
        else return new EssentialsItemExtension((dItem) item);
    }

    private EssentialsItemExtension(dItem item) {
        this.item = item;
    }

    dItem item;
    Essentials ess = Support.getPlugin(EssentialsSupport.class);

    @Override
    public String getAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }
        Double price = ess.getWorth().getPrice(item.getItemStack()).doubleValue();

        // <--[tag]
        // @attribute <i@item.worth>
        // @returns Element(Decimal)
        // @description
        // Returns the amount of money one of this item is worth in Essentials.
        // @plugin Depenizen, Essentials
        // -->
        if (attribute.startsWith("worth")) {
            // <--[tag]
            // @attribute <i@item.worth.quantity[<#>]>
            // @returns Element(Decimal)
            // @description
            // Returns the amount of money the quantity specified of this item is worth in Essentials.
            // @plugin Depenizen, Essentials
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

        Element value = mechanism.getValue();

        // <--[mechanism]
        // @object dItem
        // @name worth
        // @input Element(Decimal)
        // @description
        // Sets the worth of this item in Essentials.
        // @tags
        // <i@item.worth>
        // <i@item.worth.quantity[<Element>]>
        // @plugin Depenizen, Essentials
        // -->
        if (mechanism.matches("worth") && value.isDouble()) {
            ess.getWorth().setPrice(item.getItemStack(), value.asDouble());
        }

    }
}
