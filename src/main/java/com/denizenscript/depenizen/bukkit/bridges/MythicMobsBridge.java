package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizencore.DenizenCore;
import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.ObjectFetcher;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.denizencore.tags.TagManager;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.commands.mythicmobs.MythicSignalCommand;
import com.denizenscript.depenizen.bukkit.commands.mythicmobs.MythicSkillCommand;
import com.denizenscript.depenizen.bukkit.commands.mythicmobs.MythicSpawnCommand;
import com.denizenscript.depenizen.bukkit.commands.mythicmobs.MythicThreatCommand;
import com.denizenscript.depenizen.bukkit.events.mythicmobs.MythicMobsDeathEvent;
import com.denizenscript.depenizen.bukkit.events.mythicmobs.MythicMobsSpawnEvent;
import com.denizenscript.depenizen.bukkit.objects.mythicmobs.MythicMobsMobTag;
import com.denizenscript.depenizen.bukkit.objects.mythicmobs.MythicSpawnerTag;
import com.denizenscript.depenizen.bukkit.properties.mythicmobs.MythicMobsEntityProperties;
import com.denizenscript.depenizen.bukkit.utilities.mythicmobs.MythicMobsLoaders;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper;
import io.lumine.xikage.mythicmobs.items.MythicItem;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MobManager;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import io.lumine.xikage.mythicmobs.spawning.spawners.MythicSpawner;
import io.lumine.xikage.mythicmobs.spawning.spawners.SpawnerManager;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;
import java.util.UUID;

public class MythicMobsBridge extends Bridge {

    @Override
    public void init() {
        ObjectFetcher.registerWithObjectFetcher(MythicMobsMobTag.class, MythicMobsMobTag.tagProcessor);
        ObjectFetcher.registerWithObjectFetcher(MythicSpawnerTag.class, MythicSpawnerTag.tagProcessor);
        PropertyParser.registerProperty(MythicMobsEntityProperties.class, EntityTag.class);
        ScriptEvent.registerScriptEvent(MythicMobsDeathEvent.class);
        ScriptEvent.registerScriptEvent(MythicMobsSpawnEvent.class);
        DenizenCore.commandRegistry.registerCommand(MythicSpawnCommand.class);
        DenizenCore.commandRegistry.registerCommand(MythicThreatCommand.class);
        DenizenCore.commandRegistry.registerCommand(MythicSignalCommand.class);
        DenizenCore.commandRegistry.registerCommand(MythicSkillCommand.class);
        new MythicMobsLoaders().RegisterEvents();

        // <--[tag]
        // @attribute <mythic_item[<name>]>
        // @returns ItemTag
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns an ItemTag of the named mythic item.
        // -->
        TagManager.registerTagHandler(ItemTag.class, "mythic_item", (attribute) -> {
            if (!attribute.hasParam()) {
                attribute.echoError("The mythic_item tag must have input.");
                return null;
            }
            String name = attribute.getParam();
            Optional<MythicItem> itemOpt = MythicMobs.inst().getItemManager().getItem(name);
            if (!itemOpt.isPresent()) {
                attribute.echoError("'" + name + "' is not a valid Mythic item.");
                return null;
            }
            ItemStack item = BukkitAdapter.adapt(itemOpt.get().generateItemStack(1));
            return new ItemTag(item);
        });

        // <--[tag]
        // @attribute <mythicmob[<name>]>
        // @returns MythicMobsMobTag
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns a MythicMobsMobTag based on the name input.
        // Refer to <@link objecttype MythicMobsMobTag>.
        // -->
        TagManager.registerTagHandler(MythicMobsMobTag.class, "mythicmob", (attribute) -> {
            if (!attribute.hasParam()) {
                attribute.echoError("MythicMob tag base must have input.");
                return null;
            }
            return MythicMobsMobTag.valueOf(attribute.getParam(), attribute.context);
        });

        // <--[tag]
        // @attribute <mythicspawner[<name>]>
        // @returns MythicSpawnerTag
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns a MythicSpawnerTag based on the name input.
        // Refer to <@link objecttype MythicSpawnerTag>.
        // -->
        TagManager.registerTagHandler(MythicSpawnerTag.class, "mythicspawner", (attribute) -> {
            if (!attribute.hasParam()) {
                attribute.echoError("MythicSpawner tag base must have input.");
                return null;
            }
            return MythicSpawnerTag.valueOf(attribute.getParam(), attribute.context);
        });

        TagManager.registerTagHandler(ObjectTag.class, "mythicmobs", (attribute) -> {
            attribute.fulfill(1);

            // <--[tag]
            // @attribute <mythicmobs.active_mobs>
            // @returns ListTag(MythicMobsMobTag)
            // @plugin Depenizen, MythicMobs
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
            // @plugin Depenizen, MythicMobs
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
        });
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

    public static MobManager getMobManager() {
        return MythicMobs.inst().getMobManager();
    }

    public static SpawnerManager getSpawnerManager() {
        return MythicMobs.inst().getSpawnerManager();
    }

    public static boolean isMythicSpawner(String name) {
        return MythicMobs.inst().getSpawnerManager().getSpawnerByName(name) != null;
    }

    public static MythicSpawner getMythicSpawner(String name) {
        return MythicMobs.inst().getSpawnerManager().getSpawnerByName(name);
    }

    public static BukkitAPIHelper getAPI() {
        return MythicMobs.inst().getAPIHelper();
    }

    public static boolean skillExists(String name) {
        return MythicMobs.inst().getSkillManager().getSkillNames().contains(name);
    }
}
