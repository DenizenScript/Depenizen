package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.commands.bossshop.BossShopCommand;
import com.denizenscript.depenizen.bukkit.extensions.bossshop.BossShopInventoryExtension;
import com.denizenscript.depenizen.bukkit.support.Support;
import net.aufdemrand.denizen.objects.dInventory;

public class BossShopSupport extends Support {

    public BossShopSupport() {
        new BossShopCommand().activate().as("bossshop").withOptions("See Documentation.", 1);
        registerProperty(BossShopInventoryExtension.class, dInventory.class);
    }
}
