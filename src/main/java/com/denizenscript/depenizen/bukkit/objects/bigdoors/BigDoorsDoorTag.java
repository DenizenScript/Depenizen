package com.denizenscript.depenizen.bukkit.objects.bigdoors;

import com.comphenix.protocol.PacketType;
import com.denizenscript.denizen.objects.CuboidTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.debugging.Debug;
import com.denizenscript.denizencore.objects.Adjustable;
import com.denizenscript.denizencore.objects.Fetchable;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.DurationTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.TagContext;
import com.denizenscript.depenizen.bukkit.bridges.BigDoorsBridge;
import nl.pim16aap2.bigDoors.BigDoors;
import nl.pim16aap2.bigDoors.Commander;
import nl.pim16aap2.bigDoors.Door;
import nl.pim16aap2.bigDoors.util.DoorOwner;
import org.bukkit.entity.Player;

import javax.xml.bind.Element;

public class BigDoorsDoorTag implements ObjectTag, Adjustable {

    // <--[language]
    // @name BigDoorsDoor Objects
    // @group Depenizen Object Types
    // @plugin Depenizen, Big Doors
    // @description
    // A BigDoorsDoorTag represents a Big Doors door.
    //
    // These use the object notation "bigdoor@".
    // The identity format for a door is <door_id>
    // For example, 'bigdoor@door_id'.
    //
    // -->

    /////////////////////
    //   OBJECT FETCHER
    /////////////////

    static Commander commander = BigDoorsBridge.getCommander();
    static BigDoors bigDoors = BigDoorsBridge.getBigDoors();

    public static BigDoorsDoorTag valueOf(String string) {
        return valueOf(string, null);
    }

    @Fetchable("bigdoor")
    public static BigDoorsDoorTag valueOf(String string, TagContext context) {
        if (string == null || string.length() == 0) {
            return null;
        }

        string = string.replace("bigdoor@", "");

        try {
            long id = Long.parseLong(string);
            return new BigDoorsDoorTag(commander.getDoor(null, id));
        }
        catch (NumberFormatException e) {
            Debug.echoError("Invalid door id specified.");
        }
        return null;
    }

    public static boolean matches(String arg) {
        if (arg.startsWith("bigdoor@")) {
            return true;
        }
        return valueOf(arg) != null;
    }

    /////////////////////
    //   STATIC CONSTRUCTORS
    /////////////////

    Door door;

    public BigDoorsDoorTag(Door door) {
        this.door = door;
    }

    /////////////////////
    //   INSTANCE FIELDS/METHODS
    /////////////////

    public Door getDoor() {
        return door;
    }

    public boolean isBusy() {
        return commander.isDoorBusy(door.getDoorUID());
    }

    public boolean isOpen() {
        return door.isOpen();
    }

    public boolean isLocked() {
        return door.isLocked();
    }

    public boolean isLoaded() {
        return bigDoors.areChunksLoadedForDoor(door);
    }

    /////////////////////
    //   ObjectTag Methods
    /////////////////

    private String prefix = "BigDoorsDoor";

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public ObjectTag setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    @Override
    public String debug() {
        return (prefix + "='<A>" + identify() + "<G>' ");
    }

    @Override
    public boolean isUnique() {
        return true;
    }

    @Override
    public String getObjectType() {
        return "BigDoorsDoor";
    }

    @Override
    public String identify() {
        return "bigdoor@" + door.getDoorUID() + PropertyParser.getPropertiesString(this);
    }

    @Override
    public String identifySimple() {
        return "bigdoor@" + door.getDoorUID();
    }

    @Override
    public String toString() {
        return identify();
    }

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <BigDoorsDoorTag.is_open>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Big Doors
        // @description
        // Returns whether the door is open.
        // -->
        if (attribute.startsWith("is_open")) {
            return new ElementTag(door.isOpen()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <BigDoorsDoorTag.is_locked>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Big Doors
        // @description
        // Returns whether the door is locked.
        // -->
        if (attribute.startsWith("is_locked")) {
            return new ElementTag(door.isLocked()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <BigDoorsDoorTag.is_busy>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Big Doors
        // @description
        // Returns whether the door is busy (currently animated/opening/closing).
        // -->
        else if (attribute.startsWith("is_busy")) {
            return new ElementTag(isBusy()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <BigDoorsDoorTag.is_loaded>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Big Doors
        // @description
        // Returns whether the door is currently loaded (the chunks it sits in are loaded).
        // If this returns false, the door cannot open or close.
        // -->
        else if (attribute.startsWith("is_loaded")) {
            return new ElementTag(isLoaded()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <BigDoorsDoorTag.cuboid>
        // @returns CuboidTag
        // @plugin Depenizen, Big Doors
        // @description
        // Returns the cuboid of the door.
        // -->
        else if (attribute.startsWith("cuboid")) {
            return new CuboidTag(door.getMinimum(), door.getMaximum()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <BigDoorsDoorTag.auto_close>
        // @returns ElementTag(Number)
        // @plugin Depenizen, Big Doors
        // @description
        // Returns the auto close time for the door.
        // Returns 0 if the door does not auto close.
        // -->
        else if (attribute.startsWith("auto_close")) {
            return new ElementTag(door.getAutoClose()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <BigDoorsDoorTag.power_block>
        // @returns LocationTag
        // @plugin Depenizen, Big Doors
        // @description
        // Returns the location of the power block for the door.
        // -->
        else if (attribute.startsWith("power_block")) {
            return new LocationTag(door.getPowerBlockLoc()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <BigDoorsDoorTag.owners>
        // @returns ListTag(PlayerTag)
        // @plugin Depenizen, Big Doors
        // @description
        // Returns the list of owners of the door.
        // -->
        else if (attribute.startsWith("owners")) {
            ListTag owners = new ListTag();
            for (DoorOwner owner : commander.getDoorOwners(door.getDoorUID(), null)) {
                owners.addObject(PlayerTag.valueOf(owner.getPlayerUUID().toString(), attribute.context));
            }
            return owners.getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <BigDoorsDoorTag.door_type>
        // @returns ElementTag
        // @plugin Depenizen, Big Doors
        // @description
        // Returns the type of door.
        // -->
        else if (attribute.startsWith("door_type")) {
            return new ElementTag(door.getType().toString()).getAttribute(attribute.fulfill(1));
        }

        return new ElementTag(identify()).getAttribute(attribute);

    }

    @Override
    public void adjust(Mechanism mechanism) {

        // <--[mechanism]
        // @object BigDoorsDoorTag
        // @name is_open
        // @input ElementTag(Boolean)
        // @description
        // Sets opened state of a door. (This activates the animations).
        // Doors cannot be opened or closed if they are busy (currently opening or closing).
        // This ignores whether a door is locked.
        // @tags
        // <BigDoorsDoorTag.is_open>
        // <BigDoorsDoorTag.is_busy>
        // -->
        if (mechanism.matches("is_open") && mechanism.requireBoolean("Doors can only be open or closed.")) {
            if (isOpen() == mechanism.getValue().asBoolean() || isBusy()) {
                return;
            }
            if (!isLoaded()) {
                Debug.echoError("Door chunks are not loaded. cannot open door.");
                return;
            }
            bigDoors.toggleDoor(door.getDoorUID());
            return;
        }

        // <--[mechanism]
        // @object BigDoorsDoorTag
        // @name is_locked
        // @input ElementTag(Boolean)
        // @description
        // Sets the locked state of the door.
        // @tags
        // <BigDoorsDoorTag.is_locked>
        // -->
        if (mechanism.matches("is_locked") && mechanism.requireBoolean("Doors can only be locked or unlocked.")) {
            commander.setLock(door.getDoorUID(), mechanism.getValue().asBoolean());
            return;
        }

        // <--[mechanism]
        // @object BigDoorsDoorTag
        // @name auto_close
        // @input ElementTag(Number)
        // @description
        // Sets the auto_close time of the door.
        // Set this to 0 to disable auto close.
        // @tags
        // <BigDoorsDoorTag.auto_close>
        // -->
        if (mechanism.matches("auto_close") && mechanism.requireObject(DurationTag.class)) {
            commander.updateDoorAutoClose(door.getDoorUID(), mechanism.valueAsType(DurationTag.class).getSecondsAsInt());
        }

        if (!mechanism.fulfilled()) {
            mechanism.reportInvalid();
        }
    }

    @Override
    public void applyProperty(Mechanism mechanism) {
        Debug.echoError("Cannot apply Properties to a BigDoors Door!");
    }
}
