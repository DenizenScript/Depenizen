package com.denizenscript.depenizen.bukkit.properties.towny;

import com.denizenscript.denizen.objects.WorldTag;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.palmergames.bukkit.towny.TownyAPI;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;

public class TownyWorldProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "TownyWorld";
    }

    @Override
    public void adjust(Mechanism mechanism) {
    }

    public static boolean describes(ObjectTag object) {
        return object instanceof WorldTag;
    }

    public static TownyWorldProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new TownyWorldProperties((WorldTag) object);
        }
    }

    public static final String[] handledMechs = new String[] {
    }; // None

    private TownyWorldProperties(WorldTag world) {
        this.world = world;
    }

    public WorldTag world;

    public static void register() {

        // <--[tag]
        // @attribute <WorldTag.towny_enabled>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Towny
        // @description
        // Returns whether this world has Towny enabled.
        // -->
        PropertyParser.registerTag(TownyWorldProperties.class, ElementTag.class, "towny_enabled", (attribute, property) -> {
            return new ElementTag(TownyAPI.getInstance().isTownyWorld(property.world.getWorld()));
        });
    }
}
