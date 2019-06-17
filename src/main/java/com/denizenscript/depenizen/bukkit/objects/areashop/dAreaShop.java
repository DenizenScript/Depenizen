package com.denizenscript.depenizen.bukkit.objects.areashop;

import me.wiefferink.areashop.AreaShop;
import me.wiefferink.areashop.regions.GeneralRegion;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.Fetchable;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;
import net.aufdemrand.denizencore.tags.TagContext;
import net.aufdemrand.denizencore.utilities.debugging.dB;
import com.denizenscript.depenizen.bukkit.objects.worldguard.WorldGuardRegion;

public class dAreaShop implements dObject {

    public static dAreaShop valueOf(String string) {
        return dAreaShop.valueOf(string, null);
    }

    @Fetchable("areashop")
    public static dAreaShop valueOf(String string, TagContext context) {
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
        return new dAreaShop(areaShop);
    }

    public static boolean matches(String arg) {
        arg = arg.replace("areashop@", "");
        return AreaShop.getInstance().getFileManager().getRegion(arg) != null;
    }

    GeneralRegion areaShop = null;

    public dAreaShop(GeneralRegion areaShop) {
        if (areaShop != null) {
            this.areaShop = areaShop;
        }
        else {
            dB.echoError("AreaShop referenced is null!");
        }
    }

    String prefix = "AreaShop";

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public String debug() {
        return (prefix + "='<A>" + identify() + "<G>' ");
    }

    @Override
    public boolean isUnique() {
        return true;
    }

    @Override
    public String getObjectType() {
        return "AreaShop";
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
    public dObject setPrefix(String prefix) {
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

    public boolean equals(dAreaShop areaShop) {
        return areaShop.getAreaShop().equals(this.getAreaShop());
    }

    @Override
    public String getAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        // <--[tag]
        // @attribute <areashop@areashop.is_bought>
        // @returns Element(Boolean)
        // @description
        // Returns whether this AreaShop has been bought.
        // @Plugin Depenizen, AreaShop
        // -->
        if (attribute.startsWith("is_bought")) {
            return new Element(areaShop.isBuyRegion()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <areashop@areashop.is_rented>
        // @returns Element(Boolean)
        // @description
        // Returns whether this AreaShop is being rented.
        // @Plugin Depenizen, AreaShop
        // -->
        else if (attribute.startsWith("is_rented")) {
            return new Element(areaShop.isRentRegion()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <areashop@areashop.groups>
        // @returns dList
        // @description
        // Returns a list of groups that control this AreaShop.
        // @Plugin Depenizen, AreaShop
        // -->
        else if (attribute.startsWith("groups")) {
            return new dList(areaShop.getGroupNames()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <areashop@areashop.landlord>
        // @returns dPlayer
        // @description
        // Returns the landlord of the AreaShop.
        // @Plugin Depenizen, AreaShop
        // -->
        else if (attribute.startsWith("landlord")) {
            return new dPlayer(areaShop.getLandlord()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <areashop@areashop.name>
        // @returns Element
        // @description
        // Returns the name of the AreaShop.
        // @Plugin Depenizen, AreaShop
        // -->
        else if (attribute.startsWith("name")) {
            return new Element(areaShop.getName()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <areashop@areashop.owner>
        // @returns dPlayer
        // @description
        // Returns the owner of the AreaShop.
        // @Plugin Depenizen, AreaShop
        // -->
        else if (attribute.startsWith("owner")) {
            return new dPlayer(areaShop.getOwner()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <areashop@areashop.worldguard_region>
        // @returns WorldGuardRegion
        // @description
        // Returns the WorldGuardRegion that holds the AreaShop.
        // @Plugin Depenizen, AreaShop
        // -->
        else if (attribute.startsWith("worldguard_region")) {
            return new WorldGuardRegion(areaShop.getRegion(), areaShop.getWorld()).getAttribute(attribute.fulfill(1));
        }

        return new Element(identify()).getAttribute(attribute);
    }
}
