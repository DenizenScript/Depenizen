package com.denizenscript.depenizen.bukkit.support.plugins;

import com.denizenscript.depenizen.bukkit.extensions.battlenight.BNPlayerExtension;
import com.denizenscript.depenizen.bukkit.support.Support;
import me.limebyte.battlenight.api.BattleNightAPI;
import me.limebyte.battlenight.core.BattleNight;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.tags.TagContext;
import net.aufdemrand.denizencore.objects.Duration;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.tags.Attribute;
import com.denizenscript.depenizen.bukkit.commands.BattleNightCommands;

public class BattleNightSupport extends Support {

    public BattleNightSupport() {
        registerProperty(BNPlayerExtension.class, dPlayer.class);
        registerAdditionalTags("battle");
        new BattleNightCommands().activate().as("BN").withOptions("See Documentation.", 1);
    }

    @Override
    public String additionalTags(Attribute attribute, TagContext tagContext) {

        BattleNightAPI api = BattleNight.instance.getAPI();

        if (attribute.startsWith("battle")) {

            attribute = attribute.fulfill(1);

            if (api.getBattle().isInProgress()) {

                // <--[tag]
                // @attribute <battle.arena>
                // @returns Element
                // @description
                // Returns the battle's arena name.
                // @Plugin DepenizenBukkit, BattleNight
                // -->
                if (attribute.startsWith("arena")) {
                    return new Element(api.getBattle().getArena().getName())
                            .getAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <battle.time_remaining>
                // @returns Duration
                // @description
                // Returns the amount of time the battle has left.
                // @Plugin DepenizenBukkit, BattleNight
                // -->
                else if (attribute.startsWith("timeremaining") || attribute.startsWith("time_remaining")) {
                    return new Duration(BattleNight.instance.getAPI().getBattle().getTimer().getTimeRemaining())
                            .getAttribute(attribute.fulfill(1));
                }
            }

            // <--[tag]
            // @attribute <battle.in_progress>
            // @returns Element
            // @description
            // Returns true if a battle is in progress.
            // @Plugin DepenizenBukkit, BattleNight
            // -->
            else if (attribute.startsWith("inprogress") || attribute.startsWith("in_progress")) {
                return new Element(api.getBattle().isInProgress())
                        .getAttribute(attribute.fulfill(1));
            }

        }

        return null;

    }
}
