package net.gnomeffinway.depenizen.extensions.battlenight;

import me.limebyte.battlenight.api.battle.Battle;
import me.limebyte.battlenight.api.battle.TeamedBattle;
import net.aufdemrand.denizen.objects.*;
import net.aufdemrand.denizen.objects.properties.Property;
import net.aufdemrand.denizen.tags.Attribute;
import me.limebyte.battlenight.api.BattleNightAPI;
import me.limebyte.battlenight.core.BattleNight;
import net.aufdemrand.denizen.objects.Element;
import net.aufdemrand.denizen.objects.dPlayer;
import org.bukkit.entity.Player;

public class BNPlayerTags implements Property {

    public static boolean describes(dObject pl) {
        return pl instanceof dPlayer
                && ((dPlayer) pl).isOnline();
    }

    public static BNPlayerTags getFrom(dObject pl) {
        if (!describes(pl)) return null;
        else return new BNPlayerTags((dPlayer) pl);
    }


    ///////////////////
    // Instance Fields and Methods
    /////////////

    private BNPlayerTags(dPlayer pl) {
        player = pl.getPlayerEntity();
    }

    Player player = null;

    /////////
    // Property Methods
    ///////

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "BNPlayerTags";
    }

    @Override
    public String getAttribute(Attribute attribute) {
        if (attribute.startsWith("bn")) {

            BattleNightAPI api = BattleNight.instance.getAPI();
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
                    return new Element(api.getPlayerClass(player).getName())
                            .getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <p@player.bn.in_battle>
            // @returns Element(Boolean)
            // @description
            // Returns true if the player is in battle.
            // @plugin BattleNight
            // -->
            else if (attribute.startsWith("inbattle") || attribute.startsWith("in_battle")) {
                Battle battle = api.getBattle();
                return new Element(battle != null && battle.containsPlayer(player))
                        .getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <p@player.bn.team>
            // @returns Element
            // @decription
            // Returns the team the player is currenly on.
            // @plugin BattleNight
            // -->
            else if (attribute.startsWith("team")) {
                Battle battle = api.getBattle();
                if (battle != null && battle instanceof TeamedBattle) {
                    return new Element(((TeamedBattle) battle).getTeam(player).getName())
                            .getAttribute(attribute.fulfill(1));
                }
            }
        }

        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {
    }
}
