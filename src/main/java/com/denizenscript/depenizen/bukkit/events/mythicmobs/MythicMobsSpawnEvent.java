package com.denizenscript.depenizen.bukkit.events.mythicmobs;

import com.denizenscript.denizencore.objects.*;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.depenizen.bukkit.objects.mythicmobs.MythicMobsMobTag;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.utilities.CoreUtilities;
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
    // @Triggers when a MythicMob spawns.
    //
    // @Context
    // <context.mob> Returns the MythicMob that is spawning.
    // <context.entity> Returns the EntityTag for the MythicMob.
    // <context.from_spawner> Returns true if the mob was from a spawner.
    // <context.spawner_location> Returns a LocationTag of the spawner that spawned the mob.
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
    public MythicMobsMobTag mob;
    public EntityTag entity;
    public LocationTag location;

    @Override
    public boolean couldMatch(ScriptPath path) {
        String cmd = path.eventArgLowerAt(2);
        return path.eventLower.startsWith("mythicmob")
                && (cmd.equals("death") || cmd.equals("dies") || cmd.equals("killed"));
    }

    @Override
    public boolean matches(ScriptPath path) {
        String mob = path.eventArgLowerAt(1);

        if (!mob.equals("mob")
                && !mob.equals(CoreUtilities.toLowerCase(this.mob.getMobType().getInternalName()))) {
            return false;
        }

        if (!runInCheck(path, entity.getLocation())) {
            return false;
        }

        return super.matches(path);
    }

    @Override
    public String getName() {
        return "MythicMobsDeath";
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("mob")) {
            return mob;
        }
        else if (name.equals("entity")) {
            return entity;
        }
        else if (name.equals("from_spawner")) {
            return new ElementTag(event.isFromMythicSpawner());
        }
        else if (name.equals("spawner_location")) {
            AbstractLocation loc = event.getMythicSpawner().getLocation();
            return new LocationTag(loc.getX(), loc.getY(), loc.getZ(),loc.getWorld().getName());
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onMythicMobSpawns(MythicMobSpawnEvent event) {
        mob = new MythicMobsMobTag(event.getMob());
        entity = new EntityTag(event.getEntity());
        EntityTag.rememberEntity(entity.getBukkitEntity());
        this.event = event;
        fire(event);
        EntityTag.forgetEntity(entity.getBukkitEntity());
    }
}
