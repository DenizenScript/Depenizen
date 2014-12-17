package net.gnomeffinway.depenizen.extensions.heroes;

import com.herocraftonline.heroes.Heroes;
import net.aufdemrand.denizen.objects.dNPC;
import net.aufdemrand.denizen.objects.dObject;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.tags.Attribute;
import net.gnomeffinway.depenizen.extensions.dObjectExtension;
import net.gnomeffinway.depenizen.objects.heroes.HeroesHero;
import net.gnomeffinway.depenizen.support.Supported;
import net.gnomeffinway.depenizen.support.plugins.HeroesSupport;
import org.bukkit.entity.Player;

public class HeroesPlayerNPCExtension extends dObjectExtension {

    public static boolean describes(dObject obj) {
        return (obj instanceof dPlayer && ((dPlayer) obj).isOnline())
                || (obj instanceof dNPC && ((dNPC) obj).isSpawned()
                && ((dNPC) obj).getEntity() instanceof Player);
    }

    public static HeroesPlayerNPCExtension getFrom(dObject obj) {
        if (!describes(obj)) return null;
        else return new HeroesPlayerNPCExtension(obj);
    }

    private HeroesPlayerNPCExtension(dObject obj) {
        if (obj instanceof dPlayer)
            player = ((dPlayer) obj).getPlayerEntity();
        else if (obj instanceof dNPC)
            player = (Player) ((dNPC) obj).getEntity();
    }

    Player player = null;

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <p@player.heroes>
        // @returns HeroesHero
        // @description
        // Returns this player as a HeroesHero. This is designed
        // to allow <p@player.heroes.some.extensions> for ease of use, and
        // usually should not be used alone.
        // @plugin Depenizen, Heroes
        // -->
        // <--[tag]
        // @attribute <n@npc.heroes>
        // @returns HeroesHero
        // @description
        // Returns a Player-type NPC as a HeroesHero. This is designed
        // to allow <n@npc.heroes.some.extensions> for ease of use, and
        // usually shouldn't be used alone.
        // @plugin Depenizen, Heroes
        // -->
        if (attribute.startsWith("heroes")) {
            Heroes heroes = HeroesSupport.getPlugin();
            return new HeroesHero(heroes.getCharacterManager().getHero(player))
                    .getAttribute(attribute.fulfill(1));
        }

        return null;

    }

}
