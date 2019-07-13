package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.commands.bossshop.BossShopCommand;
import com.denizenscript.depenizen.bukkit.properties.bossshop.BossShopInventoryProperties;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.denizen.objects.dInventory;
import com.denizenscript.denizen.utilities.DenizenAPI;
import com.denizenscript.denizencore.objects.properties.PropertyParser;

public class BossShopBridge extends Bridge {

    public static BossShopBridge instance;

    @Override
    public void init() {
        instance = this;
        DenizenAPI.getCurrentInstance().getCommandRegistry().registerCoreMember(BossShopCommand.class, "BOSSSHOP", "bosshop [<shop name>] (target:<player>)", 1);
        PropertyParser.registerProperty(BossShopInventoryProperties.class, dInventory.class);
    }
}
