package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.extensions.battlenight.BNPlayerExtension;
import com.denizenscript.depenizen.bukkit.Bridge;
import me.limebyte.battlenight.api.BattleNightAPI;
import me.limebyte.battlenight.core.BattleNight;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.TagRunnable;
import net.aufdemrand.denizencore.objects.properties.PropertyParser;
import net.aufdemrand.denizencore.tags.ReplaceableTagEvent;
import net.aufdemrand.denizencore.objects.Duration;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.tags.Attribute;
import com.denizenscript.depenizen.bukkit.commands.BattleNightCommands;
import net.aufdemrand.denizencore.tags.TagManager;

public class BattleNightBridge extends Bridge {

    @Override
    public void init() {
        PropertyParser.registerProperty(BNPlayerExtension.class, dPlayer.class);
        TagManager.registerTagHandler(new TagRunnable.RootForm() {
            @Override
            public void run(ReplaceableTagEvent event) {
                tagEvent(event);
            }
        }, "battle");
        new BattleNightCommands().activate().as("BN").withOptions("See Documentation.", 1);
    }

    public void tagEvent(ReplaceableTagEvent event) {
        Attribute attribute = event.getAttributes().fulfill(1);

        BattleNightAPI api = BattleNight.instance.getAPI();

        if (api.getBattle().isInProgress()) {

            // <--[tag]
            // @attribute <battle.arena>
            // @returns Element
            // @description
            // Returns the battle's arena name.
            // @Plugin Depenizen, BattleNight
            // -->
            if (attribute.startsWith("arena")) {
                event.setReplacedObject(new Element(api.getBattle().getArena().getName())
                        .getObjectAttribute(attribute.fulfill(1)));
            }

            // <--[tag]
            // @attribute <battle.time_remaining>
            // @returns Duration
            // @description
            // Returns the amount of time the battle has left.
            // @Plugin Depenizen, BattleNight
            // -->
            else if (attribute.startsWith("timeremaining") || attribute.startsWith("time_remaining")) {
                event.setReplacedObject(new Duration(BattleNight.instance.getAPI().getBattle().getTimer().getTimeRemaining())
                        .getObjectAttribute(attribute.fulfill(1)));
            }
        }

        // <--[tag]
        // @attribute <battle.in_progress>
        // @returns Element
        // @description
        // Returns true if a battle is in progress.
        // @Plugin Depenizen, BattleNight
        // -->
        else if (attribute.startsWith("inprogress") || attribute.startsWith("in_progress")) {
            event.setReplacedObject(new Element(api.getBattle().isInProgress())
                    .getObjectAttribute(attribute.fulfill(1)));
        }

    }
}
