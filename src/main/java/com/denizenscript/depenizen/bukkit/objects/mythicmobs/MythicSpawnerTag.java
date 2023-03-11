package com.denizenscript.depenizen.bukkit.objects.mythicmobs;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.objects.Adjustable;
import com.denizenscript.denizencore.objects.Fetchable;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.ObjectTagProcessor;
import com.denizenscript.denizencore.tags.TagContext;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.depenizen.bukkit.bridges.MythicMobsBridge;
import com.denizenscript.denizencore.objects.core.DurationTag;
import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.core.spawning.spawners.MythicSpawner;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.UUID;

public class MythicSpawnerTag implements ObjectTag, Adjustable {

    // <--[ObjectType]
    // @name MythicSpawnerTag
    // @prefix mythicspawner
    // @base ElementTag
    // @format
    // The identity format for a MythicSpawner is its name.
    // For example, 'mythicspawner@AngrySludgeSpawner1'.
    //
    // @plugin Depenizen, MythicMobs
    // @description
    // A MythicSpawnerTag represents a MythicMobs spanwer in the world.
    //
    // -->

    public static MythicSpawnerTag valueOf(String name) {
        return valueOf(name, null);
    }

    @Fetchable("mythicspawner")
    public static MythicSpawnerTag valueOf(String string, TagContext context) {
        if (string == null) {
            return null;
        }
        try {
            string = string.replace("mythicspawner@", "");
            if (!MythicMobsBridge.isMythicSpawner(string)) {
                return null;
            }
            return new MythicSpawnerTag(MythicMobsBridge.getMythicSpawner(string));
        }
        catch (Exception e) {
            return null;
        }
    }

    public static boolean matches(String string) {
        return valueOf(string) != null;
    }

    public MythicSpawnerTag(MythicSpawner spawner) {
            this.spawner = spawner;
    }

    public MythicSpawner getSpawner() {
        return spawner;
    }

    private String prefix;
    MythicSpawner spawner;

    @Override
    public ObjectTag setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {
        return tagProcessor.getObjectAttribute(this, attribute);
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public boolean isUnique() {
        return true;
    }

    @Override
    public String identify() {
        return "mythicspawner@" + spawner.getName();
    }

    @Override
    public String identifySimple() {
        return identify();
    }

    @Override
    public String toString() {
        return identify();
    }
    public static ObjectTagProcessor<MythicSpawnerTag> tagProcessor = new ObjectTagProcessor<>();

    public static void register() {

        // <--[tag]
        // @attribute <MythicSpawnerTag.name>
        // @returns ElementTag
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns the name of the MythicSpawner.
        // -->
        tagProcessor.registerTag(ElementTag.class, "name", (attribute, object) -> {
            return new ElementTag(object.getSpawner().getName());
        });

        // <--[tag]
        // @attribute <MythicSpawnerTag.cooldown>
        // @returns DurationTag
        // @plugin Depenizen, MythicMobs
        // @mechanism MythicSpawnerTag.cooldown
        // @description
        // Returns the MythicSpawner's configured cooldown.
        // -->
        tagProcessor.registerTag(DurationTag.class, "cooldown", (attribute, object) -> {
            return new DurationTag(object.getSpawner().getCooldownSeconds());
        });

        // <--[tag]
        // @attribute <MythicSpawnerTag.mythic_mobs>
        // @returns ListTag(MythicMobsMobTag)
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns a ListTag of all active MythicMobs from this spawner.
        // -->
        tagProcessor.registerTag(ListTag.class, "mythic_mobs", (attribute, object) -> {
            ListTag list = new ListTag();
            for (UUID uuid : object.getSpawner().getAssociatedMobs()) {
                list.addObject(MythicMobsMobTag.valueOf(uuid.toString()));
            }
            return list;
        });

        // <--[tag]
        // @attribute <MythicSpawnerTag.mob_type>
        // @returns ElementTag
        // @plugin Depenizen, MythicMobs
        // @mechanism MythicSpawnerTag.mob_type
        // @description
        // Returns the internal name the MythicMob mob type spawned.
        // -->
        tagProcessor.registerTag(ElementTag.class, "mob_type", (attribute, object) -> {
            return new ElementTag(object.getSpawner().getTypeName());
        });

        // <--[tag]
        // @attribute <MythicSpawnerTag.mobs>
        // @returns ListTag(EntityTag)
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns a ListTag of all active MythicMobs from this spawner.
        // -->
        tagProcessor.registerTag(ListTag.class, "mobs", (attribute, object) -> {
            ArrayList<EntityTag> list = new ArrayList<>();
            for (UUID uuid : object.getSpawner().getAssociatedMobs()) {
                list.add(new EntityTag(MythicMobsMobTag.valueOf(uuid.toString()).getEntity()));
            }
            return new ListTag(list);
        });

        // <--[tag]
        // @attribute <MythicSpawnerTag.location>
        // @returns LocationTag
        // @plugin Depenizen, MythicMobs
        // @mechanism MythicSpawnerTag.location
        // @description
        // Returns a LocationTag of the MythicSpawner's location.
        // -->
        tagProcessor.registerTag(LocationTag.class, "location", (attribute, object) -> {
            AbstractLocation loc = object.getSpawner().getLocation();
            return new LocationTag((World) loc.getWorld(), loc.getX(), loc.getY(), loc.getZ());
        });

        // <--[tag]
        // @attribute <MythicSpawnerTag.group>
        // @returns ElementTag
        // @plugin Depenizen, MythicMobs
        // @mechanism MythicSpawnerTag.group
        // @description
        // Returns the MythicSpawner's group, if applicable.
        // -->
        tagProcessor.registerTag(ElementTag.class, "group", (attribute, object) -> {
            return new ElementTag(object.getSpawner().getGroup());
        });
    }

    @Override
    public void applyProperty(Mechanism mechanism) {
        mechanism.echoError("Cannot apply properties to a MythicSpawner!");
    }

    @Override
    public void adjust(Mechanism mechanism) {

        // <--[mechanism]
        // @object MythicSpawnerTag
        // @name group
        // @input ElementTag
        // @plugin Depenizen, MythicMobs
        // @description
        // Sets the group of the MythicSpawner.
        // @tags
        // <MythicSpawnerTag.group>
        // -->
        if (mechanism.matches("group")) {
            spawner.setGroup(mechanism.getValue().asString());
        }

        // <--[mechanism]
        // @object MythicSpawnerTag
        // @name location
        // @input LocationTag
        // @plugin Depenizen, MythicMobs
        // @description
        // Sets the location of the MythicSpawner.
        // @tags
        // <MythicSpawnerTag.location>
        // -->
        else if (mechanism.matches("location") && mechanism.requireObject(LocationTag.class)) {
            spawner.setLocation(BukkitAdapter.adapt(mechanism.valueAsType(LocationTag.class)));
        }

        // <--[mechanism]
        // @object MythicSpawnerTag
        // @name cooldown
        // @input DurationTag
        // @plugin Depenizen, MythicMobs
        // @description
        // Sets the MythicSpawner's configured cooldown timer, in seconds.
        // @tags
        // <MythicSpawnerTag.cooldown>
        // -->
        else if (mechanism.matches("cooldown") && mechanism.requireObject(DurationTag.class)) {
            spawner.setCooldownSeconds(mechanism.valueAsType(DurationTag.class).getSecondsAsInt());
        }

        // <--[mechanism]
        // @object MythicSpawnerTag
        // @name start_cooldown
        // @input None
        // @plugin Depenizen, MythicMobs
        // @description
        // Starts the MythicSpawner's cooldown timer.
        // @tags
        // <MythicSpawnerTag.cooldown>
        // -->
        else if (mechanism.matches("start_cooldown")) {
            spawner.setOnCooldown();
        }

        // <--[mechanism]
        // @object MythicSpawnerTag
        // @name mob_type
        // @input ElementTag
        // @plugin Depenizen, MythicMobs
        // @description
        // Sets the MythicSpawner's MythicMob Mob type.
        // @tags
        // <MythicSpawnerTag.mob_type>
        // -->
        else if (mechanism.matches("mob_type")) {
            MythicMob mob = MythicMobsBridge.getMythicMob(mechanism.getValue().asString());
            if (mob == null) {
                Debug.echoError("MythicMob type does not exist: " + mechanism.getValue().asString());
                return;
            }
            spawner.setType(mechanism.getValue().asString());
        }

        // <--[mechanism]
        // @object MythicSpawnerTag
        // @name spawn
        // @input None
        // @plugin Depenizen, MythicMobs
        // @description
        // Forces the MythicSpawner to spawn.
        // This spawn method still checks conditions.
        // -->
        else if (mechanism.matches("spawn")) {
            spawner.Spawn();
        }

        // <--[mechanism]
        // @object MythicSpawnerTag
        // @name enable
        // @input None
        // @plugin Depenizen, MythicMobs
        // @description
        // Enables the MythicSpawner.
        // -->
        else if (mechanism.matches("enable")) {
            spawner.Enable();
        }

        // <--[mechanism]
        // @object MythicSpawnerTag
        // @name disable
        // @input None
        // @plugin Depenizen, MythicMobs
        // @description
        // Disables the MythicSpawner.
        // -->
        else if (mechanism.matches("disable")) {
            spawner.Disable();
        }
    }
}
