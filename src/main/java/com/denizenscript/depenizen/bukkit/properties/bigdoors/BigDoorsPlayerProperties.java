package com.denizenscript.depenizen.bukkit.properties.bigdoors;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.depenizen.bukkit.bridges.BigDoorsBridge;
import com.denizenscript.depenizen.bukkit.objects.bigdoors.BigDoorsDoorTag;
import nl.pim16aap2.bigDoors.Door;
import nl.pim16aap2.bigDoors.GUI.GUI;

public class BigDoorsPlayerProperties implements Property {

    PlayerTag player;

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "BigDoorsPlayer";
    }

    public static boolean describes(ObjectTag object) {
        return object instanceof PlayerTag;
    }

    public static BigDoorsPlayerProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new BigDoorsPlayerProperties((PlayerTag) object);
        }
    }

    public static final String[] handledMechs = new String[] {
            "display_bigdoors_manager"
    };

    public BigDoorsPlayerProperties(PlayerTag player) {
        this.player = player;
    }

    public static void registerTags() {
        // <--[tag]
        // @attribute <PlayerTag.bigdoors>
        // @returns ListTag(BigDoorsDoorTag)
        // @plugin Depenizen, Big Doors
        // @description
        // Returns a list of Big Doors the player is an owner of.
        // -->
        PropertyParser.<BigDoorsPlayerProperties>registerTag("bigdoors", (attribute, property) -> {
            ListTag doors = new ListTag();
            for (Door door : BigDoorsBridge.commander.getDoors(property.player.getOfflinePlayer().getUniqueId().toString(), null)) {
                doors.addObject(new BigDoorsDoorTag(door));
            }
            return doors;
        });
    }

    @Override
    public void adjust(Mechanism mechanism) {

        // <--[mechanism]
        // @object PlayerTag
        // @name display_bigdoors_manager
        // @input none
        // @description
        // Opens the Big Doors manager (bdm).
        // -->
        if (mechanism.matches("display_bigdoors_manager")) {
            BigDoorsBridge.bigDoors.addGUIUser(new GUI(BigDoorsBridge.bigDoors, player.getPlayerEntity()));
        }
    }
}
