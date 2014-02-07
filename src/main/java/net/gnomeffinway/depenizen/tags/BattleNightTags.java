package net.gnomeffinway.depenizen.tags;

import me.limebyte.battlenight.api.BattleNightAPI;
import me.limebyte.battlenight.core.BattleNight;
import net.aufdemrand.denizen.events.bukkit.ReplaceableTagEvent;
import net.aufdemrand.denizen.objects.Duration;
import net.aufdemrand.denizen.objects.Element;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.tags.Attribute;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.gnomeffinway.depenizen.Depenizen;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class BattleNightTags implements Listener {

    public BattleNightTags(Depenizen depenizen) {
        depenizen.getServer().getPluginManager().registerEvents(this, depenizen);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void battlenightTags(ReplaceableTagEvent event) {

        BattleNightAPI api = BattleNight.instance.getAPI();

        Attribute attribute = event.getAttributes();

        // Set a string to use for replacing the tag
        String replaced = null;

        if (event.matches("battle")) {

            attribute = attribute.fulfill(1);

            // <--[tag]
            // @attribute <battle.arena>
            // @returns Element
            // @description
            // Returns the battle's arena name.
            // @plugin BattleNight
            // -->
            if (attribute.startsWith("arena")) {
                if (BattleNight.instance.getAPI().getBattle().isInProgress())
                    replaced = new Element(api.getBattle().getArena().getName())
                            .getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <battle.in_progress>
            // @returns Element
            // @description
            // Returns true if a battle is in progress.
            // @plugin BattleNight
            // -->
            else if (attribute.startsWith("inprogress") || attribute.startsWith("in_progress")) {
                replaced = new Element(api.getBattle().isInProgress())
                        .getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <battle.time_remaining>
            // @returns Duration
            // @description
            // Returns the amount of time the battle has left.
            // @plugin BattleNight
            // -->
            else if (attribute.startsWith("timeremaining") || attribute.startsWith("time_remaining")) {
                if (api.getBattle().isInProgress())
                    replaced = new Duration(BattleNight.instance.getAPI().getBattle().getTimer().getTimeRemaining())
                            .getAttribute(attribute.fulfill(1));
            }

        }

        if (replaced != null)
            event.setReplaced(replaced);
    }
}
