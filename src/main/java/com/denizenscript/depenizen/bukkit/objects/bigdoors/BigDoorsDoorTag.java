package com.denizenscript.depenizen.bukkit.objects.bigdoors;

import com.denizenscript.denizen.objects.CuboidTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.debugging.Debug;
import com.denizenscript.denizencore.objects.*;
import com.denizenscript.denizencore.objects.core.DurationTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.tags.ObjectTagProcessor;
import com.denizenscript.denizencore.tags.TagContext;
import com.denizenscript.denizencore.tags.TagRunnable;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.denizenscript.depenizen.bukkit.bridges.BigDoorsBridge;
import nl.pim16aap2.bigDoors.Door;
import nl.pim16aap2.bigDoors.util.DoorOwner;

public class BigDoorsDoorTag implements ObjectTag, Adjustable {

    // <--[language]
    // @name BigDoorsDoor Objects
    // @group Depenizen Object Types
    // @plugin Depenizen, Big Doors
    // @description
    // A BigDoorsDoorTag represents a Big Doors door.
    //
    // These use the object notation "bigdoor@".
    // The identity format for a door is it's Door ID as a number. NOT the name of the door you have set.
    // For example, 'bigdoor@1'.
    //
    // -->

    /////////////////////
    //   OBJECT FETCHER
    /////////////////
    @Fetchable("bigdoor")
    public static BigDoorsDoorTag valueOf(String string, TagContext context) {
        if (string == null || string.length() == 0) {
            return null;
        }

        string = string.substring("bigdoor@".length());

        if (!ArgumentHelper.matchesInteger(string)) {
            return null;
        }
        return new BigDoorsDoorTag(BigDoorsBridge.commander.getDoor(null, Integer.parseInt(string)));
    }

    public static boolean matches(String arg) {
        if (arg.startsWith("bigdoor@")) {
            return true;
        }
        return valueOf(arg, CoreUtilities.noDebugContext) != null;
    }

    /////////////////////
    //   STATIC CONSTRUCTORS
    /////////////////
    Door door;

    public BigDoorsDoorTag(Door door) {
        this.door = door;
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
        return "bigdoor@" + door.getDoorUID();
    }

    @Override
    public String identifySimple() {
        return identify();
    }

    @Override
    public String toString() {
        return identify();
    }

    public static void registerTag(String name, TagRunnable.ObjectInterface<BigDoorsDoorTag> runnable, String... variants) {
        tagProcessor.registerTag(name, runnable, variants);
    }

    public static ObjectTagProcessor<BigDoorsDoorTag> tagProcessor = new ObjectTagProcessor<>();

    public static void registerTags() {
        // <--[tag]
        // @attribute <BigDoorsDoorTag.is_open>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Big Doors
        // @description
        // Returns whether the door is open.
        // -->
        registerTag("is_open", (attribute, object) -> {
            return new ElementTag(object.door.isOpen());
        });

        // <--[tag]
        // @attribute <BigDoorsDoorTag.is_locked>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Big Doors
        // @description
        // Returns whether the door is locked.
        // -->
        registerTag("is_locked", (attribute, object) -> {
            return new ElementTag(object.door.isLocked());
        });

        // <--[tag]
        // @attribute <BigDoorsDoorTag.is_busy>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Big Doors
        // @description
        // Returns whether the door is busy (currently animated/opening/closing).
        // -->
        registerTag("is_busy", (attribute, object) -> {
            return new ElementTag(BigDoorsBridge.commander.isDoorBusy(object.door.getDoorUID()));
        });

        // <--[tag]
        // @attribute <BigDoorsDoorTag.is_loaded>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Big Doors
        // @description
        // Returns whether the door is currently loaded (the chunks it sits in are loaded).
        // If this returns false, the door cannot open or close.
        // -->
        registerTag("is_loaded", (attribute, object) -> {
            return new ElementTag(BigDoorsBridge.bigDoors.areChunksLoadedForDoor(object.door));
        });

        // <--[tag]
        // @attribute <BigDoorsDoorTag.cuboid>
        // @returns CuboidTag
        // @plugin Depenizen, Big Doors
        // @description
        // Returns the cuboid of the door.
        // -->
        registerTag("cuboid", (attribute, object) -> {
            return new CuboidTag(object.door.getMinimum(), object.door.getMaximum());
        });

        // <--[tag]
        // @attribute <BigDoorsDoorTag.auto_close>
        // @returns ElementTag(Number)
        // @plugin Depenizen, Big Doors
        // @description
        // Returns the auto close time for the door.
        // Returns 0 if the door does not auto close.
        // -->
        registerTag("auto_close", (attribute, object) -> {
            return new ElementTag(object.door.getAutoClose());
        }, "autoclose");

        // <--[tag]
        // @attribute <BigDoorsDoorTag.power_block>
        // @returns LocationTag
        // @plugin Depenizen, Big Doors
        // @description
        // Returns the location of the power block for the door.
        // -->
        registerTag("power_block", (attribute, object) -> {
            return new LocationTag(object.door.getPowerBlockLoc());
        });

        // <--[tag]
        // @attribute <BigDoorsDoorTag.owners>
        // @returns ListTag(PlayerTag)
        // @plugin Depenizen, Big Doors
        // @description
        // Returns the list of owners of the door.
        // -->
        registerTag("owners", (attribute, object) -> {
            ListTag owners = new ListTag();
            for (DoorOwner owner : BigDoorsBridge.commander.getDoorOwners(object.door.getDoorUID(), null)) {
                owners.addObject(PlayerTag.valueOf(owner.getPlayerUUID().toString(), CoreUtilities.noDebugContext));
            }
            return owners;
        });

        // <--[tag]
        // @attribute <BigDoorsDoorTag.door_type>
        // @returns ElementTag
        // @plugin Depenizen, Big Doors
        // @description
        // Returns the type of door.
        // -->
        registerTag("door_type", (attribute, object) -> {
            return new ElementTag(object.door.getType().toString());
        });

        // <--[tag]
        // @attribute <BigDoorsDoorTag.name>
        // @returns ElementTag
        // @plugin Depenizen, Big Doors
        // @description
        // Returns the name of the door (this is not always unique).
        // -->
        registerTag("", (attribute, object) -> {
            return new ElementTag(object.door.getName());
        });
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
            if (door.isOpen() == mechanism.getValue().asBoolean() || BigDoorsBridge.commander.isDoorBusy(door.getDoorUID())) {
                return;
            }
            if (!BigDoorsBridge.bigDoors.areChunksLoadedForDoor(door)) {
                Debug.echoError("Door chunks are not loaded. cannot open door.");
                return;
            }
            BigDoorsBridge.bigDoors.toggleDoor(door.getDoorUID());
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
            BigDoorsBridge.commander.setLock(door.getDoorUID(), mechanism.getValue().asBoolean());
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
            BigDoorsBridge.commander.updateDoorAutoClose(door.getDoorUID(), mechanism.valueAsType(DurationTag.class).getSecondsAsInt());
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
