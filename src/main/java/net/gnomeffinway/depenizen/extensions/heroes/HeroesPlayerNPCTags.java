package net.gnomeffinway.depenizen.extensions.heroes;

import com.herocraftonline.heroes.Heroes;
import net.aufdemrand.denizen.objects.*;
import net.aufdemrand.denizen.objects.properties.Property;
import net.aufdemrand.denizen.tags.Attribute;
import net.gnomeffinway.depenizen.objects.heroes.HeroesHero;
import net.gnomeffinway.depenizen.support.Supported;
import org.bukkit.entity.Player;

public class HeroesPlayerNPCTags implements Property {

    public static boolean describes(dObject obj) {
        return (obj instanceof dPlayer && ((dPlayer) obj).isOnline())
                || (obj instanceof dNPC && ((dNPC) obj).isSpawned()
                && ((dNPC) obj).getEntity() instanceof Player);
    }

    public static HeroesPlayerNPCTags getFrom(dObject obj) {
        if (!describes(obj)) return null;
        else return new HeroesPlayerNPCTags(obj);
    }


    ///////////////////
    // Instance Fields and Methods
    /////////////

    private HeroesPlayerNPCTags(dObject obj) {
        if (obj instanceof dPlayer)
            player = ((dPlayer) obj).getPlayerEntity();
        else if (obj instanceof dNPC)
            player = (Player) ((dNPC) obj).getEntity();
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
        return "HeroesPlayerNPCTags";
    }

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <p@player.heroes>
        // @returns HeroesHero
        // @description
        // Returns this player as a HeroesHero. This is designed
        // to allow <p@player.heroes.some.extensions> for ease of use, and
        // usually should not be used alone.
        // @plugin Heroes
        // -->
        // <--[tag]
        // @attribute <n@npc.heroes>
        // @returns HeroesHero
        // @description
        // Returns a Player-type NPC as a HeroesHero. This is designed
        // to allow <n@npc.heroes.some.extensions> for ease of use, and
        // usually shouldn't be used alone.
        // @plugin Heroes
        // -->
        if (attribute.startsWith("heroes")) {
            Heroes heroes = Supported.get("HEROES").getPlugin();
            return new HeroesHero(heroes.getCharacterManager().getHero(player))
                    .getAttribute(attribute.fulfill(1));
        }

        return null;

    }

    @Override
    public void adjust(Mechanism mechanism) {
    }

}
