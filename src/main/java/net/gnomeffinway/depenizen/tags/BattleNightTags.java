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

        // Build a new attribute out of the raw_tag supplied in the script to be fulfilled
        Attribute attribute = new Attribute(event.raw_tag, event.getScriptEntry());

        // Set a string to use for replacing the tag
        String replaced = null;

        if (event.matches("player, pl")) {

            // PlayerTags require a... dPlayer!
            dPlayer p = event.getPlayer();

            // Player tag may specify a new player in the <player[context]...> portion of the tag.
            if (attribute.hasContext(1))
                // Check if this is a valid player and update the dPlayer object reference.
                if (dPlayer.matches(attribute.getContext(1)))
                    p = dPlayer.valueOf(attribute.getContext(1));
                else {
                    dB.echoDebug("Could not match '"
                            + attribute.getContext(1) + "' to a valid player!");
                    return;
                }

            if (p == null || !p.isValid()) {
                dB.echoDebug("Invalid or missing player for tag <" + event.raw_tag + ">!");
                event.setReplaced("null");
                return;
            }

            Player player = p.getPlayerEntity();
            attribute = attribute.fulfill(1);

            if (attribute.startsWith("bn")) {

                attribute = attribute.fulfill(1);

                // <--[tag]
                // @attribute <p@player.bn.class>
                // @returns Element
                // @description
                // Returns the player's class.
                // @plugin BattleNight
                // -->
                if (attribute.startsWith("class")) {
                    if (api.getPlayerClass(player) != null)
                        replaced = new Element(api.getPlayerClass(player).getName())
                                .getAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <p@player.bn.in_battle>
                // @returns Element(Boolean)
                // @description
                // Returns true if the player is in battle.
                // -->
                else if (attribute.startsWith("inbattle") || attribute.startsWith("in_battle")) {
                    replaced = new Element(api.getBattle().containsPlayer(player))
                            .getAttribute(attribute.fulfill(1));
                }

                // Nothing to do here...
                else if (attribute.startsWith("team")) {
                    replaced = null;
                }

            }
        }

        else if (event.matches("battle")) {

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