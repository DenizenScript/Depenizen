package com.denizenscript.depenizen.bukkit.support.plugins;

import com.denizenscript.depenizen.bukkit.support.Support;
import com.denizenscript.depenizen.bukkit.commands.mythicmobs.MythicSpawnCommand;
import com.denizenscript.depenizen.bukkit.extensions.mythicmobs.MythicMobsEntityExtension;
import com.denizenscript.depenizen.bukkit.objects.mythicmobs.MythicMobsMob;
import net.aufdemrand.denizen.objects.dEntity;
import net.elseland.xikage.MythicMobs.Adapters.Bukkit.BukkitAdapter;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;
import net.elseland.xikage.MythicMobs.Mobs.MobManager;
import net.elseland.xikage.MythicMobs.Mobs.MythicMob;
import com.denizenscript.depenizen.bukkit.events.mythicmobs.MythicMobsDeathEvent;
import net.elseland.xikage.MythicMobs.MythicMobs;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.UUID;

public class MythicMobsSupport extends Support {

    public MythicMobsSupport() {
        registerObjects(MythicMobsMob.class);
        registerProperty(MythicMobsEntityExtension.class, dEntity.class);
        registerScriptEvents(new MythicMobsDeathEvent());
        new MythicSpawnCommand().activate().as("mythicspawn").withOptions("See Documentation", 2);
    }

    public static boolean isMythicMob(Entity entity) {
        return MythicMobs.plugin.getMobManager().isActiveMob(BukkitAdapter.adapt(entity));
    }

    public static boolean isMythicMob(UUID uuid) {
        return MythicMobs.plugin.getMobManager().isActiveMob(uuid);
    }

    public static ActiveMob getActiveMob(Entity entity) {
        return MythicMobs.plugin.getMobManager().getActiveMob(entity.getUniqueId());
    }

    public static MythicMob getMythicMob(String name) {
        return MythicMobs.plugin.getMobManager().getMythicMob(name);
    }

    public static Entity spawnMythicMob(MythicMob mythicMob, Location location, int level) {
        return mythicMob.spawn(BukkitAdapter.adapt(location), level).getLivingEntity();
    }
}
