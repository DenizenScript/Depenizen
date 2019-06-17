package com.denizenscript.depenizen.bukkit.properties.heroes;

import net.aufdemrand.denizencore.objects.properties.Property;
import net.aufdemrand.denizencore.objects.Mechanism;
import com.denizenscript.depenizen.bukkit.objects.heroes.HeroesHero;
import com.denizenscript.depenizen.bukkit.bridges.HeroesBridge;
import com.herocraftonline.heroes.Heroes;
import net.aufdemrand.denizen.objects.dNPC;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;
import org.bukkit.entity.Player;

public class HeroesPlayerNPCProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "HeroesPlayerNPC";
    }

    @Override
    public void adjust(Mechanism mechanism) {
        // None
    }

    // TODO: Refactor this into two separate extension classes you psychotic idiots.

    public static boolean describes(dObject object) {
        return (object instanceof dPlayer && ((dPlayer) object).isOnline())
                || (object instanceof dNPC && ((dNPC) object).isSpawned()
                && ((dNPC) object).getEntity() instanceof Player);
    }

    public static HeroesPlayerNPCProperties getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new HeroesPlayerNPCProperties(object);
        }
    }

    public static final String[] handledTags = new String[] {
            "heroes"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    private HeroesPlayerNPCProperties(dObject object) {
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
        // @Plugin Depenizen, Heroes
        // -->
        // <--[tag]
        // @attribute <n@npc.heroes>
        // @returns HeroesHero
        // @description
        // Returns a Player-type NPC as a HeroesHero. This is designed
        // to allow <n@npc.heroes.some.extensions> for ease of use, and
        // usually shouldn't be used alone.
        // @Plugin Depenizen, Heroes
        // -->
        if (attribute.startsWith("heroes")) {
            Heroes heroes = (Heroes) HeroesBridge.instance.plugin;
            return new HeroesHero(heroes.getCharacterManager().getHero(player))
                    .getAttribute(attribute.fulfill(1));
        }

        return null;

    }
}
