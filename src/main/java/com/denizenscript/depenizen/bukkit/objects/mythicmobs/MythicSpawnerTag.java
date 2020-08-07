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
import com.denizenscript.denizencore.tags.TagRunnable;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.depenizen.bukkit.bridges.MythicMobsBridge;
import com.denizenscript.denizencore.objects.core.DurationTag;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.spawning.spawners.MythicSpawner;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.UUID;

public class MythicSpawnerTag implements ObjectTag, Adjustable {

    // <--[language]
    // @name MythicSpawnerTag Objects
    // @group Depenizen Object Types
    // @plugin Depenizen, MythicMobs
    // @description
    // A MythicSpawnerTag represents a MythicMobs spanwer in the world.
    //
    // These use the object notation "mythicspawner@".
    // The identity format for a mythicspawner is it's name.
    // For example, 'mythicspawner@AngrySludgeSpawner1'.
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
        if (spawner != null) {
            this.spawner = spawner;
        }
        else {
            Debug.echoError("Mythic Spawner referenced is null!");
        }
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
    public String debug() {
        return prefix + "='<A>" + identify() + "<G>' ";
    }

    @Override
    public boolean isUnique() {
        return true;
    }

    @Override
    public String getObjectType() {
        return "MythicSpawner";
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

    public static void registerTag(String name, TagRunnable.ObjectInterface<MythicSpawnerTag> runnable, String... variants) {
        tagProcessor.registerTag(name, runnable, variants);
    }
    public static ObjectTagProcessor<MythicSpawnerTag> tagProcessor = new ObjectTagProcessor<>();

    public static void registerTags() {

        // <--[tag]
        // @attribute <MythicSpawnerTag.name>
        // @returns ElementTag
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns the name of the MythicSpawner.
        // -->
        registerTag("name", (attribute, object) -> {
            return new ElementTag(object.getSpawner().getName());
        });

        // <--[tag]
        // @attribute <MythicSpawnerTag.cooldown>
        // @returns ElementTag
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns an ElementTag(Number) of the MythicSpawner's cooldown.
        // -->
        registerTag("mythic_mobs", (attribute, object) -> {
            return new DurationTag(object.getSpawner().getCooldownSeconds());
        });

        // <--[tag]
        // @attribute <MythicSpawnerTag.mythic_mobs>
        // @returns ElementTag
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns a ListTag(MythicMobsMobTag) of all active MythicMobs from this spawner.
        // -->
        registerTag("mythic_mobs", (attribute, object) -> {
            ArrayList<MythicMobsMobTag> list = new ArrayList<>();
            for (UUID uuid : object.getSpawner().getAssociatedMobs()){
                list.add(MythicMobsMobTag.valueOf(uuid.toString()));
            }
            return new ListTag(list);
        });

        // <--[tag]
        // @attribute <MythicSpawnerTag.mob_type>
        // @returns ElementTag
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns an ElementTag for internal name the MythicMob mob type spawned.
        // -->
        registerTag("mob_type", (attribute, object) -> {
            ArrayList<MythicMobsMobTag> list = new ArrayList<>();
            for (UUID uuid : object.getSpawner().getAssociatedMobs()){
                list.add(MythicMobsMobTag.valueOf(uuid.toString()));
            }
            return new ListTag(list);
        });

        // <--[tag]
        // @attribute <MythicSpawnerTag.mobs>
        // @returns ElementTag
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns a ListTag(EntityTag) of all active MythicMobs from this spawner.
        // -->
        registerTag("mobs", (attribute, object) -> {
            ArrayList<EntityTag> list = new ArrayList<>();
            for (UUID uuid : object.getSpawner().getAssociatedMobs()){
                list.add(new EntityTag(MythicMobsMobTag.valueOf(uuid.toString()).getEntity()));
            }
            return new ListTag(list);
        });

        // <--[tag]
        // @attribute <MythicSpawnerTag.location>
        // @returns ElementTag
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns a LocationTag of the MythicSpawner.
        // -->
        registerTag("location", (attribute, object) -> {
            AbstractLocation loc = object.getSpawner().getLocation();
            return new LocationTag((World) loc.getWorld(), loc.getX(), loc.getY(), loc.getZ());
        });

        // <--[tag]
        // @attribute <MythicSpawnerTag.group>
        // @returns ElementTag
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns an ElementTag of a MythicSpawn's group, if applicable.
        // -->
        registerTag("group", (attribute, object) -> {
            return new ElementTag(object.getSpawner().getGroup());
        });
    }


    @Override
    public void adjust(Mechanism mechanism) {

        // <--[mechanism]
        // @object MythicSpawnerTag
        // @name group
        // @input ElementTag
        // @description
        // Sets group of the MythicSpawner.
        // @tags
        // <MythicMobsMobTag.group>
        // -->
        if (mechanism.matches("global_cooldown")) {
            spawner.setGroup(mechanism.getValue().asString());
        }

        // <--[mechanism]
        // @object MythicSpawnerTag
        // @name location
        // @input LocationTag
        // @description
        // Sets the location of the MythicSpawner.
        // @tags
        // <MythicMobsMobTag.location>
        // -->
        else if (mechanism.matches("location") && mechanism.requireObject(LocationTag.class)) {
            spawner.setLocation(BukkitAdapter.adapt(mechanism.valueAsType(LocationTag.class)));
        }

        // <--[mechanism]
        // @object MythicSpawnerTag
        // @name cooldown
        // @input ElementTag(Number)
        // @description
        // Sets the MythicSpawner's cooldown timer.
        // @tags
        // <MythicMobsMobTag.cooldown>
        // -->
        else if (mechanism.matches("cooldown") && mechanism.requireInteger()) {
            spawner.setCooldownSeconds(mechanism.getValue().asInt());
        }

        // <--[mechanism]
        // @object MythicSpawnerTag
        // @name start_cooldown
        // @input None
        // @description
        // Starts the MythicSpawner's cooldown timer.
        // @tags
        // <MythicMobsMobTag.cooldown>
        // -->
        else if (mechanism.matches("start_cooldown")) {
            spawner.setOnCooldown();
        }

        // <--[mechanism]
        // @object MythicSpawnerTag
        // @name set_mob_type
        // @input ElementTag
        // @description
        // Sets the MythicSpawner's MythicMob Mob type.
        // @tags
        // <MythicMobsMobTag.mob_type>
        // -->
        else if (mechanism.matches("set_mob_type")) {
            spawner.setOnCooldown();
        }

        // <--[mechanism]
        // @object MythicSpawnerTag
        // @name spawn
        // @input None
        // @description
        // Forces the MythicSpawner to spawn.
        // @tags
        // <MythicMobsMobTag.mob_type>
        // -->
        else if (mechanism.matches("spawn")) {
            spawner.Spawn();
        }

        // <--[mechanism]
        // @object MythicSpawnerTag
        // @name enable
        // @input None
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
        // @description
        // Disables the MythicSpawner.
        // -->
        else if (mechanism.matches("disable")) {
            spawner.Disable();
        }

    }
    @Override
    public void applyProperty(Mechanism mechanism) {
        Debug.echoError("Cannot apply properties to a MythicSpawner!");
    }

}
