package net.gnomeffinway.depenizen.tags.essentials;

import net.aufdemrand.denizen.objects.Element;
import net.aufdemrand.denizen.objects.Mechanism;
import net.aufdemrand.denizen.objects.dObject;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.objects.properties.Property;
import net.aufdemrand.denizen.tags.Attribute;
import net.gnomeffinway.depenizen.Depenizen;

public class EssentialsPlayerTags implements Property {

    public static boolean describes(dObject pl) { return pl instanceof dPlayer; }

    public static EssentialsPlayerTags getFrom(dObject pl) {
        if (!describes(pl)) return null;
        else return new EssentialsPlayerTags((dPlayer) pl);
    }


    ///////////////////
    // Instance Fields and Methods
    /////////////

    private EssentialsPlayerTags(dPlayer pl) { this.player = pl; }

    dPlayer player = null;

    @Override
    public String getPropertyString() { return null; }

    @Override
    public String getPropertyId() {
        return "EssentialsPlayerTags";
    }

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <p@player.is_afk>
        // @returns Element(Boolean)
        // @description
        // Returns whether the player is AFK.
        // @plugin Essentials
        // -->
        if (attribute.startsWith("is_afk")) {
            return new Element(Depenizen.essentials.getUser(player.getName()).isAfk())
                    .getAttribute(attribute.fulfill(1));
        }

        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {

    }
}
