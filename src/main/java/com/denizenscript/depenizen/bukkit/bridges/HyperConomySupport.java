package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.DepenizenPlugin;
import com.denizenscript.depenizen.bukkit.events.hyperconomy.HyperConomyEvents;
import com.denizenscript.depenizen.bukkit.support.Support;
import org.bukkit.scheduler.BukkitRunnable;
import regalowl.hyperconomy.bukkit.BukkitConnector;
import regalowl.hyperconomy.event.HyperEventHandler;

public class HyperConomySupport extends Support {

    public HyperConomySupport() {
        new BukkitRunnable() {
            @Override
            public void run() {
                BukkitConnector hyperConomy = Support.getPlugin(HyperConomySupport.class);
                HyperEventHandler eventHandler = hyperConomy.getHC().getHyperEventHandler();
                eventHandler.registerListener(new HyperConomyEvents());
            }
        }.runTaskLaterAsynchronously(DepenizenPlugin.getCurrentInstance(), 1);
    }
}
