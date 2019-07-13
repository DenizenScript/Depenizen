package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.commands.mythicmobs.MythicSpawnCommand;
import com.denizenscript.depenizen.bukkit.properties.mythicmobs.MythicMobsEntityProperties;
import com.denizenscript.depenizen.bukkit.objects.mythicmobs.MythicMobsMob;
import com.denizenscript.denizen.objects.EntityTag;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import com.denizenscript.depenizen.bukkit.events.mythicmobs.MythicMobsDeathEvent;
import io.lumine.xikage.mythicmobs.MythicMobs;
import com.denizenscript.denizen.utilities.DenizenAPI;
import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.ObjectFetcher;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.UUID;

public class MythicMobsBridge extends Bridge {

    @Override
    public void init() {
        ObjectFetcher.registerWithObjectFetcher(MythicMobsMob.class);
        PropertyParser.registerProperty(MythicMobsEntityProperties.class, EntityTag.class);
        ScriptEvent.registerScriptEvent(new MythicMobsDeathEvent());
        DenizenAPI.getCurrentInstance().getCommandRegistry().registerCoreMember(MythicSpawnCommand.class,
                "MYTHICSPAWN", "mythicspawn [<name>] [<location>] (level:<#>)", 2);
    }

    public static boolean isMythicMob(Entity entity) {
        return MythicMobs.inst().getMobManager().isActiveMob(BukkitAdapter.adapt(entity));
    }

    public static boolean isMythicMob(UUID uuid) {
        return MythicMobs.inst().getMobManager().isActiveMob(uuid);
    }

    public static ActiveMob getActiveMob(Entity entity) {
        return MythicMobs.inst().getMobManager().getMythicMobInstance(entity);
    }

    public static MythicMob getMythicMob(String name) {
        return MythicMobs.inst().getMobManager().getMythicMob(name);
    }

    public static Entity spawnMythicMob(MythicMob mythicMob, Location location, int level) {
        return mythicMob.spawn(BukkitAdapter.adapt(location), level).getLivingEntity();
    }
}
