package net.gnomeffinway.depenizen.events;

import net.gnomeffinway.depenizen.Depenizen;

import org.bukkit.event.Listener;

public class DynmapEvents implements Listener {
    
    public DynmapEvents(Depenizen depenizen) {
        depenizen.getServer().getPluginManager().registerEvents(this, depenizen);
    }

    
}
