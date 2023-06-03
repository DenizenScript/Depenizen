package com.denizenscript.depenizen.bukkit.objects.areashop;

import me.wiefferink.areashop.AreaShop;
import me.wiefferink.areashop.regions.BuyRegion;
import me.wiefferink.areashop.regions.GeneralRegion;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.Fetchable;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.TagContext;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.depenizen.bukkit.objects.worldguard.WorldGuardRegionTag;
import me.wiefferink.areashop.regions.RentRegion;

public class AreaShopTag implements ObjectTag {

    // <--[ObjectType]
    // @name AreaShopTag
    // @prefix areashop
    // @base ElementTag
    // @format
    // The identity format for shops is <shop_name>
    // For example, 'areashop@my_shop'.
    //
    // @plugin Depenizen, AreaShop
    // @description
    // An AreaShopTag represents an AreaShop shop.
    //
    // -->

    @Fetchable("areashop")
    public static AreaShopTag valueOf(String string, TagContext context) {
        if (string == null) {
            return null;
        }

        ////////
        // Match areaShop name

        string = string.replace("areashop@", "");
        GeneralRegion areaShop = AreaShop.getInstance().getFileManager().getRegion(string);
        if (areaShop == null) {
            return null;
        }
        return new AreaShopTag(areaShop);
    }

    public static boolean matches(String arg) {
        arg = arg.replace("areashop@", "");
        return AreaShop.getInstance().getFileManager().getRegion(arg) != null;
    }

    GeneralRegion areaShop = null;

    public AreaShopTag(GeneralRegion areaShop) {
        if (areaShop != null) {
            this.areaShop = areaShop;
        }
        else {
            Debug.echoError("AreaShop referenced is null!");
        }
    }

    String prefix = "AreaShop";

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public boolean isUnique() {
        return true;
    }

    @Override
    public String identify() {
        return "areashop@" + areaShop.getName();
    }

    @Override
    public String identifySimple() {
        return identify();
    }

    @Override
    public ObjectTag setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    @Override
    public String toString() {
        return identify();
    }

    public GeneralRegion getAreaShop() {
        return areaShop;
    }

    public boolean equals(AreaShopTag areaShop) {
        return areaShop.getAreaShop().equals(this.getAreaShop());
    }

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        // <--[tag]
        // @attribute <AreaShopTag.is_bought>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, AreaShop
        // @description
        // Returns whether this AreaShop has been bought.
        // -->
        if (attribute.startsWith("is_bought")) {
            return new ElementTag(areaShop.isBuyRegion()).getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <AreaShopTag.is_rented>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, AreaShop
        // @description
        // Returns whether this AreaShop is being rented.
        // -->
        else if (attribute.startsWith("is_rented")) {
            return new ElementTag(areaShop.isRentRegion()).getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <AreaShopTag.groups>
        // @returns ListTag
        // @plugin Depenizen, AreaShop
        // @description
        // Returns a list of groups that control this AreaShop.
        // -->
        else if (attribute.startsWith("groups")) {
            return new ListTag(areaShop.getGroupNames()).getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <AreaShopTag.landlord>
        // @returns PlayerTag
        // @plugin Depenizen, AreaShop
        // @description
        // Returns the landlord of the AreaShop.
        // -->
        else if (attribute.startsWith("landlord")) {
            return new PlayerTag(areaShop.getLandlord()).getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <AreaShopTag.name>
        // @returns ElementTag
        // @plugin Depenizen, AreaShop
        // @description
        // Returns the name of the AreaShop.
        // -->
        else if (attribute.startsWith("name")) {
            return new ElementTag(areaShop.getName()).getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <AreaShopTag.price>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, AreaShop
        // @description
        // Returns the price of the AreaShop.
        // -->
        else if (attribute.startsWith("name")) {
            double price;
            if (areaShop instanceof BuyRegion) {
                price = ((BuyRegion) areaShop).getPrice();
            }
            else if (areaShop instanceof RentRegion) {
                price = ((RentRegion) areaShop).getPrice();
            }
            else {
                return null;
            }
            return new ElementTag(price).getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <AreaShopTag.owner>
        // @returns PlayerTag
        // @plugin Depenizen, AreaShop
        // @description
        // Returns the owner of the AreaShop.
        // -->
        else if (attribute.startsWith("owner")) {
            return new PlayerTag(areaShop.getOwner()).getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <AreaShopTag.worldguard_region>
        // @returns WorldGuardRegionTag
        // @plugin Depenizen, AreaShop
        // @description
        // Returns the WorldGuardRegionTag that holds the AreaShop.
        // -->
        else if (attribute.startsWith("worldguard_region")) {
            return new WorldGuardRegionTag(areaShop.getRegion(), areaShop.getWorld()).getObjectAttribute(attribute.fulfill(1));
        }

        return new ElementTag(identify()).getObjectAttribute(attribute);
    }
}
