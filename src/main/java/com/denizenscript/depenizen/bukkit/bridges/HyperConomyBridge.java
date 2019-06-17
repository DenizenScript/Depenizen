package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.Depenizen;
import com.denizenscript.depenizen.bukkit.events.hyperconomy.HyperConomyEvents;
import com.denizenscript.depenizen.bukkit.Bridge;
import org.bukkit.scheduler.BukkitRunnable;
import regalowl.hyperconomy.bukkit.BukkitConnector;
import regalowl.hyperconomy.event.HyperEventHandler;

public class HyperConomyBridge extends Bridge {

    @Override
    public void init() {
        new BukkitRunnable() {
            @Override
            public void run() {
                BukkitConnector hyperConomy = (BukkitConnector) plugin;
                HyperEventHandler eventHandler = hyperConomy.getHC().getHyperEventHandler();
                eventHandler.registerListener(new HyperConomyEvents());
            }
        }.runTaskLaterAsynchronously(Depenizen.instance, 1);
    }
}
