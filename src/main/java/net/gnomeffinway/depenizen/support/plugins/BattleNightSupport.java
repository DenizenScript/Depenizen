package net.gnomeffinway.depenizen.support.plugins;

import me.limebyte.battlenight.api.BattleNightAPI;
import me.limebyte.battlenight.core.BattleNight;
import net.aufdemrand.denizen.objects.*;
import net.aufdemrand.denizen.tags.Attribute;
import net.gnomeffinway.depenizen.commands.BattleNightCommands;
import net.gnomeffinway.depenizen.support.Support;
import net.gnomeffinway.depenizen.extensions.battlenight.BNPlayerTags;

public class BattleNightSupport extends Support {

    public BattleNightSupport() {
        registerProperty(BNPlayerTags.class, dPlayer.class);
        registerAdditionalTags("battle");
        new BattleNightCommands().activate().as("BN").withOptions("see documentation", 1);
    }

    @Override
    public String additionalTags(Attribute attribute) {

        BattleNightAPI api = BattleNight.instance.getAPI();

        if (attribute.startsWith("battle")) {

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
                    return new Element(api.getBattle().getArena().getName())
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
                return new Element(api.getBattle().isInProgress())
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
                    return new Duration(BattleNight.instance.getAPI().getBattle().getTimer().getTimeRemaining())
                            .getAttribute(attribute.fulfill(1));
            }

        }

        return null;

    }
}
