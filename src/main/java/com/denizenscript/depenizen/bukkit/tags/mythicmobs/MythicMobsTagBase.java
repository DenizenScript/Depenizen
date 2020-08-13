package com.denizenscript.depenizen.bukkit.tags.mythicmobs;

import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.TagManager;
import com.denizenscript.depenizen.bukkit.bridges.MythicMobsBridge;
import com.denizenscript.depenizen.bukkit.objects.mythicmobs.MythicMobsMobTag;
import com.denizenscript.depenizen.bukkit.objects.mythicmobs.MythicSpawnerTag;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.spawning.spawners.MythicSpawner;

public class MythicMobsTagBase {

    public MythicMobsTagBase() {
        TagManager.registerTagHandler("mythicmobs", (attribute) -> {
            return MythicMobsTag(attribute);
        });
    }

    public ObjectTag MythicMobsTag(Attribute attribute) {
        attribute.fulfill(1);

        // <--[tag]
        // @attribute <mythicmobs.active_mobs>
        // @returns ListTag(MythicMobsMobTag)
        // @description
        // Returns a ListTag of all active MythicMobs on the server.
        // -->
        if (attribute.startsWith("active_mobs")) {
            ListTag list = new ListTag();
            for (ActiveMob entity : MythicMobsBridge.getMobManager().getActiveMobs()) {
                list.addObject(new MythicMobsMobTag(entity));
            }
            return list;
        }

        // <--[tag]
        // @attribute <mythicmobs.spawners>
        // @returns ListTag(MythicSpawnerTag)
        // @description
        // Returns a ListTag of all MythicSpawners.
        // -->
        else if (attribute.startsWith("spawners")) {
            ListTag list = new ListTag();
            for (MythicSpawner spawner : MythicMobsBridge.getSpawnerManager().getSpawners()) {
                list.addObject(new MythicSpawnerTag(spawner));
            }
            return list;
        }
        return null;
    }
}
