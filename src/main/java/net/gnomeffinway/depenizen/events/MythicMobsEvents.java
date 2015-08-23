package net.gnomeffinway.depenizen.events;

import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizencore.events.OldEventManager;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dObject;
import net.elseland.xikage.MythicMobs.API.Events.MythicMobSpawnEvent;
import net.elseland.xikage.MythicMobs.API.Events.MythicMobDeathEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MythicMobsEvents implements Listener {

    @EventHandler
    public void onMythicMobSpawn(MythicMobSpawnEvent event) {
        String mobType = event.getMobType().MobName;

        Map<String, dObject> context = new HashMap<String, dObject>();
        context.put("entity", new dEntity(event.getEntity()));
        context.put("location", new dLocation(event.getLocation()));
        context.put("mobtype", new Element(mobType));
        context.put("level", new Element(event.getMobLevel()));


        List<String> determinations = OldEventManager.doEvents(Arrays.asList(
                        "mythicmob spawn",
                        "mythicmob spawn " + mobType),
                new BukkitScriptEntryData(null, null), context);

        for (String determination : determinations) {
            determination = determination.toUpperCase();
            if (determination.startsWith("CANCELLED"))
                event.setCancelled();
        }
    }

    @EventHandler
    public void onMythicMobDeath(MythicMobDeathEvent event) {
        String mobType = event.getMobType().MobName;

        Map<String, dObject> context = new HashMap<String, dObject>();
        context.put("entity", new dEntity(event.getEntity()));
        context.put("killer", new dEntity(event.getKiller()));
        context.put("mobtype", new Element(mobType));
        context.put("level", new Element(event.getMobLevel()));
        context.put("mobkillcount", new Element(event.getNumberOfPlayerKills()));


        OldEventManager.doEvents(Arrays.asList(
                        "mythicmob death",
                        "mythicmob death " + mobType),
                new BukkitScriptEntryData(null, null), context);
    }

}
