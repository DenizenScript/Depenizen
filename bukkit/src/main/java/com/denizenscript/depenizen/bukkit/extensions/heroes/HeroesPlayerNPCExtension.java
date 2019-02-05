package com.denizenscript.depenizen.bukkit.extensions.heroes;

import com.denizenscript.depenizen.bukkit.extensions.dObjectExtension;
import com.denizenscript.depenizen.bukkit.objects.heroes.HeroesHero;
import com.denizenscript.depenizen.bukkit.support.Support;
import com.denizenscript.depenizen.bukkit.support.plugins.HeroesSupport;
import com.herocraftonline.heroes.Heroes;
import net.aufdemrand.denizen.objects.dNPC;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;
import org.bukkit.entity.Player;

public class HeroesPlayerNPCExtension extends dObjectExtension {

    // TODO: Refactor this into two separate extension classes you psychotic idiots.

    public static boolean describes(dObject object) {
        return (object instanceof dPlayer && ((dPlayer) object).isOnline())
                || (object instanceof dNPC && ((dNPC) object).isSpawned()
                && ((dNPC) object).getEntity() instanceof Player);
    }

    public static HeroesPlayerNPCExtension getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new HeroesPlayerNPCExtension(object);
        }
    }

    public static final String[] handledTags = new String[]{
            "heroes"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    private HeroesPlayerNPCExtension(dObject object) {
        if (object instanceof dPlayer) {
            player = ((dPlayer) object).getPlayerEntity();
        }
        else if (object instanceof dNPC) {
            player = (Player) ((dNPC) object).getEntity();
        }
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
        // @Plugin DepenizenBukkit, Heroes
        // -->
        // <--[tag]
        // @attribute <n@npc.heroes>
        // @returns HeroesHero
        // @description
        // Returns a Player-type NPC as a HeroesHero. This is designed
        // to allow <n@npc.heroes.some.extensions> for ease of use, and
        // usually shouldn't be used alone.
        // @Plugin DepenizenBukkit, Heroes
        // -->
        if (attribute.startsWith("heroes")) {
            Heroes heroes = Support.getPlugin(HeroesSupport.class);
            return new HeroesHero(heroes.getCharacterManager().getHero(player))
                    .getAttribute(attribute.fulfill(1));
        }

        return null;

    }

}
