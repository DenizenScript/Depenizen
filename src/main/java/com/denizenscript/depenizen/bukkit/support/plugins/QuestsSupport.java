package com.denizenscript.depenizen.bukkit.support.plugins;

import com.denizenscript.depenizen.bukkit.extensions.quests.QuestsPlayerExtension;
import com.denizenscript.depenizen.bukkit.support.Support;
import net.aufdemrand.denizen.objects.dPlayer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class QuestsSupport extends Support {

    public static Plugin questsPlugin;

    public QuestsSupport() {
        questsPlugin = Bukkit.getPluginManager().getPlugin("Quests");
        registerProperty(QuestsPlayerExtension.class, dPlayer.class);
    }

}
