package com.denizenscript.depenizen.bukkit.events.mythicmobs;

import com.denizenscript.denizencore.objects.*;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.depenizen.bukkit.objects.mythicmobs.MythicMobsMobTag;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.LocationTag;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobSpawnEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;


public class MythicMobsSpawnEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // mythicmob mob spawns
    // mythicmob <mob> spawns
    //
    // @Regex ^on mythicmob [^\s]+ spawns$
    //
    // @Switch in:<area> to only process the event if it occurred within a specified area.
    //
    // @Triggers when a MythicMob spawns.
    //
    // @Context
    // <context.mob> Returns the MythicMob that is spawning.
    // <context.entity> Returns the EntityTag for the MythicMob.
    // <context.location> Returns the LocationTag of where the MythicMob will spawn.
    // <context.from_spawner> Returns true if the mob was from a spawner.
    // <context.spawner_location> Returns the LocationTag of the spawner that spawned the mob, if any.
    //
    // @Plugin Depenizen, MythicMobs
    //
    // @Group Depenizen
    //
    // -->

    public MythicMobsSpawnEvent() {
        instance = this;
    }

    public static MythicMobsSpawnEvent instance;
    public MythicMobSpawnEvent event;
    public MythicMobsMobTag mythicmob;
    public LocationTag location;

    @Override
    public boolean couldMatch(ScriptPath path) {
        String cmd = path.eventArgLowerAt(1);
        return (path.eventLower.startsWith("mythicmob")
                && (cmd.equals("spawns")));
    }

    @Override
    public boolean matches(ScriptPath path) {
        String mob = path.eventArgLowerAt(1);
        if (!mob.equals("mob") && runGenericCheck(mob, mythicmob.getMobType().getInternalName())) {
            return false;
        }
        if (!runInCheck(path, location)) {
            return false;
        }
        return super.matches(path);
    }

    @Override
    public String getName() {
        return "MythicMobsSpawn";
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("mob")) {
            return mythicmob;
        }
        else if (name.equals("entity")) {
            return new EntityTag(event.getEntity());
        }
        else if (name.equals("location")) {
            return location;
        }
        else if (name.equals("from_spawner")) {
            return new ElementTag(event.isFromMythicSpawner());
        }
        else if (event.isFromMythicSpawner() && (name.equals("spawner_location"))) {
                AbstractLocation loc = event.getMythicSpawner().getLocation();
                return new LocationTag(loc.getX(), loc.getY(), loc.getZ(), loc.getWorld().getName());
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onMythicMobSpawns(MythicMobSpawnEvent event) {
        mythicmob = new MythicMobsMobTag(event.getMob());
        location = new LocationTag(event.getLocation());
        this.event = event;
        EntityTag.rememberEntity(event.getEntity());
        fire(event);
        EntityTag.forgetEntity(event.getEntity());
    }
}
