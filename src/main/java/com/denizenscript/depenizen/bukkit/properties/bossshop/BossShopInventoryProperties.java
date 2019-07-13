package com.denizenscript.depenizen.bukkit.properties.bossshop;

import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.depenizen.bukkit.bridges.BossShopBridge;
import net.aufdemrand.denizen.objects.dInventory;
import com.denizenscript.denizencore.objects.Element;
import com.denizenscript.denizencore.objects.dObject;
import com.denizenscript.denizencore.tags.Attribute;
import org.black_ixx.bossshop.BossShop;

public class BossShopInventoryProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "BossShopInventory";
    }

    @Override
    public void adjust(Mechanism mechanism) {
        // None
    }
    public static boolean describes(dObject object) {
        return object instanceof dInventory;
    }

    public static BossShopInventoryProperties getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new BossShopInventoryProperties((dInventory) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "is_bossshop"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    public BossShopInventoryProperties(dInventory inventory) {
        this.inv = inventory;
    }

    dInventory inv;

    @Override
    public String getAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        // <--[tag]
        // @attribute <in@inventory.is_bossshop>
        // @returns Element(Boolean)
        // @description
        // Returns whether the inventory is a BossShop.
        // @Plugin Depenizen, BossShop
        // -->
        if (attribute.startsWith("is_bossshop")) {
            BossShop bs = (BossShop) BossShopBridge.instance.plugin;

            return new Element(bs.getAPI().isValidShop(inv.getInventory())).getAttribute(attribute.fulfill(1));
        }

        return null;
    }
}
