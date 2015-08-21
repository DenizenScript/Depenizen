package net.gnomeffinway.depenizen.events;

import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizencore.events.OldEventManager;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dObject;
import net.elseland.xikage.MythicMobs.API.Events.MythicMobSpawnEvent;
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
        context.put("level", new Element(event.getMobLevel()));


        List<String> determinations = OldEventManager.doEvents(Arrays.asList(
                        "mythicmob spawn",
                        "mythicmob " + mobType + " spawn"),
                null, context);

        for (String determination : determinations) {
            determination = determination.toUpperCase();
            if (determination.startsWith("CANCELLED"))
                event.setCancelled();
        }
    }
}
