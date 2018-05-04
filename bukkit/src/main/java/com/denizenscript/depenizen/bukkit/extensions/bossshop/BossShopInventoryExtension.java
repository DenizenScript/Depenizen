package com.denizenscript.depenizen.bukkit.extensions.bossshop;

import com.denizenscript.depenizen.bukkit.extensions.dObjectExtension;
import com.denizenscript.depenizen.bukkit.support.Support;
import com.denizenscript.depenizen.bukkit.support.plugins.BossShopSupport;
import net.aufdemrand.denizen.objects.dInventory;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;
import org.black_ixx.bossshop.BossShop;

public class BossShopInventoryExtension extends dObjectExtension {
    public static boolean describes(dObject object) {
        return object instanceof dInventory;
    }

    public static BossShopInventoryExtension getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new BossShopInventoryExtension((dInventory) object);
        }
    }

    public BossShopInventoryExtension(dInventory inventory) {
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
        // @Plugin DepenizenBukkit, BossShop
        // -->
        if (attribute.startsWith("is_bossshop")) {
            BossShop bs = Support.getPlugin(BossShopSupport.class);


            return new Element(bs.getAPI().isValidShop(inv.getInventory())).getAttribute(attribute.fulfill(1));
        }

        return null;
    }
}
