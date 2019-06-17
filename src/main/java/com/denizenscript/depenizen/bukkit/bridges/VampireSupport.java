package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.extensions.vampire.VampirePlayerExtension;
import com.denizenscript.depenizen.bukkit.support.Support;
import net.aufdemrand.denizen.objects.dPlayer;

public class VampireSupport extends Support {
    public VampireSupport() {
        registerProperty(VampirePlayerExtension.class, dPlayer.class);
    }
}
