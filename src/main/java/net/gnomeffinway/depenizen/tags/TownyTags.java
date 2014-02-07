package net.gnomeffinway.depenizen.tags;

import net.aufdemrand.denizen.events.bukkit.ReplaceableTagEvent;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.tags.Attribute;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.gnomeffinway.depenizen.Depenizen;
import net.gnomeffinway.depenizen.objects.dNation;
import net.gnomeffinway.depenizen.objects.dTown;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class TownyTags implements Listener {

    public TownyTags(Depenizen depenizen) {
        depenizen.getServer().getPluginManager().registerEvents(this, depenizen);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void townyTags(ReplaceableTagEvent event) {

        // Build a new attribute out of the raw_tag supplied in the script to be fulfilled
        Attribute attribute = new Attribute(event.raw_tag, event.getScriptEntry());

        /////////////////////
        //   TOWN TAGS
        /////////////////

        if (event.matches("town")) {

            dTown town = null;

            if (attribute.hasContext(1)) {
                if (dTown.matches(attribute.getContext(1))) {
                    town = dTown.valueOf(attribute.getContext(1));
                } else {
                    dB.echoDebug("Could not match '" + attribute.getContext(1) + "' to a valid town!");
                    return;
                }
            }

            if (town == null) {
                dB.echoDebug("Invalid or missing town for tag <" + event.raw_tag + ">!");
                event.setReplaced("null");
                return;
            }

            event.setReplaced(town.getAttribute(attribute.fulfill(1)));

        }

        /////////////////////
        //   NATION TAGS
        /////////////////

        else if (event.matches("nation")) {

            dNation nation = null;

            if (attribute.hasContext(1)) {
                if (dNation.matches(attribute.getContext(1))) {
                    nation = dNation.valueOf(attribute.getContext(1));
                } else {
                    dB.echoDebug("Could not match '" + attribute.getContext(1) + "' to a valid nation!");
                    return;
                }
            }

            if (nation == null) {
                dB.echoDebug("Invalid or missing nation for tag <" + event.raw_tag + ">!");
                event.setReplaced("null");
                return;
            }

            event.setReplaced(nation.getAttribute(attribute.fulfill(1)));

        }

    }
}
