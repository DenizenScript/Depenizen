package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.properties.coreprotect.CoreProtectLocationProperties;
import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;

public class CoreProtectBridge extends Bridge {

    public static CoreProtectAPI apiInstance;

    @Override
    public void init() {
        apiInstance = ((CoreProtect) plugin).getAPI();
        PropertyParser.registerProperty(CoreProtectLocationProperties.class, LocationTag.class);
    }
}
