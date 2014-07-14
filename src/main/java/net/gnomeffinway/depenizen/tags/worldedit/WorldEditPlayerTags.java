package net.gnomeffinway.depenizen.tags.worldedit;

import com.sk89q.worldedit.bukkit.selections.Selection;
import net.aufdemrand.denizen.objects.*;
import net.aufdemrand.denizen.objects.properties.Property;
import net.aufdemrand.denizen.tags.Attribute;
import net.gnomeffinway.depenizen.Depenizen;
import org.bukkit.entity.Player;

public class WorldEditPlayerTags implements Property {

    public static boolean describes(dObject pl) {
        return pl instanceof dPlayer
                && ((dPlayer) pl).isOnline();
    }

    public static WorldEditPlayerTags getFrom(dObject pl) {
        if (!describes(pl)) return null;
        else return new WorldEditPlayerTags((dPlayer) pl);
    }


    ///////////////////
    // Instance Fields and Methods
    /////////////

    private WorldEditPlayerTags(dPlayer pl) {
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
        return "WorldEditPlayerTags";
    }

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <p@player.selected_region>
        // @returns dCuboid
        // @description
        // Returns the player's current region selection, as a dCuboid.
        // @Plugin WorldEdit
        // -->
        if (attribute.startsWith("selected_region")) {
            Selection selection = Depenizen.worldedit.getSelection(player);
            if (selection != null)
                return new dCuboid(selection.getMinimumPoint(), selection.getMaximumPoint()).getAttribute(attribute.fulfill(1));
            else
                return Element.NULL.getAttribute(attribute.fulfill(1));
        }

        return null;

    }

    @Override
    public void adjust(Mechanism mechanism) {}

}
