package com.denizenscript.depenizen.bukkit.properties.bigdoors;

import com.denizenscript.denizen.objects.WorldTag;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.depenizen.bukkit.bridges.BigDoorsBridge;
import com.denizenscript.depenizen.bukkit.objects.bigdoors.BigDoorsDoorTag;
import nl.pim16aap2.bigDoors.Door;

public class BigDoorsWorldProperties implements Property {

    public static boolean describes(ObjectTag object) {
        return object instanceof WorldTag;
    }

    public static BigDoorsWorldProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new BigDoorsWorldProperties((WorldTag) object);
        }
    }

    public static final String[] handledMechs = new String[] {
    }; // None

    public BigDoorsWorldProperties(WorldTag world) {
        this.world = world;
    }

    WorldTag world;

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "BigDoorsWorld";
    }

    public static void register() {
        // <--[tag]
        // @attribute <WorldTag.bigdoors>
        // @returns ListTag(BigDoorsDoorTag)
        // @plugin Depenizen, Big Doors
        // @description
        // Returns a list of Big Doors doors in the world.
        // -->
        PropertyParser.registerTag(BigDoorsWorldProperties.class, ListTag.class, "bigdoors", (attribute, property) -> {
            ListTag doors = new ListTag();
            for (Door door : BigDoorsBridge.commander.getDoorsInWorld(property.world.getWorld())) {
                doors.addObject(new BigDoorsDoorTag(door));
            }
            return doors;
        });
    }

    @Override
    public void adjust(Mechanism mechanism) {
        // None
    }
}
