package com.denizenscript.depenizen.bukkit.properties.bigdoors;

import com.denizenscript.denizen.objects.WorldTag;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.depenizen.bukkit.bridges.BigDoorsBridge;
import com.denizenscript.depenizen.bukkit.objects.bigdoors.BigDoorsDoorTag;
import nl.pim16aap2.bigDoors.Commander;
import nl.pim16aap2.bigDoors.Door;

public class BigDoorsWorldProperties implements Property {

    Commander commander = BigDoorsBridge.getCommander();
    WorldTag world;

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "BigDoorsWorld";
    }

    @Override
    public void adjust(Mechanism mechanism) {
        // None
    }

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

    public static final String[] handledTags = new String[] {
            "bigdoors"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    public BigDoorsWorldProperties(WorldTag world) {
        this.world = world;
    }


    @Override
    public String getAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        // <--[tag]
        // @attribute <WorldTag.bigdoors>
        // @returns ListTag(BigDoorsDoorTag)
        // @plugin Depenizen, Big Doors
        // @description
        // Returns a list of Big Doors doors in the world.
        // -->
        if (attribute.startsWith("bigdoors")) {
            ListTag doors = new ListTag();
            for (Door door : commander.getDoorsInWorld(world.getWorld())) {
                doors.addObject(new BigDoorsDoorTag(door));
            }
            return doors.getAttribute(attribute.fulfill(1));
        }

        return null;
    }
}
