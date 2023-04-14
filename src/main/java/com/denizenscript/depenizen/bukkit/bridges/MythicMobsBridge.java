package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizen.objects.PlayerTag;
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
import com.denizenscript.depenizen.bukkit.events.mythicmobs.MythicMobsDespawnEvent;
import com.denizenscript.depenizen.bukkit.events.mythicmobs.MythicMobsSpawnEvent;
import com.denizenscript.depenizen.bukkit.objects.mythicmobs.MythicMobsMobTag;
import com.denizenscript.depenizen.bukkit.objects.mythicmobs.MythicSpawnerTag;
import com.denizenscript.depenizen.bukkit.properties.mythicmobs.MythicMobsEntityProperties;
import com.denizenscript.depenizen.bukkit.properties.mythicmobs.MythicMobsPlayerProperties;
import com.denizenscript.depenizen.bukkit.utilities.mythicmobs.MythicMobsLoaders;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.BukkitAPIHelper;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.items.MythicItem;
import io.lumine.mythic.core.mobs.ActiveMob;
import io.lumine.mythic.core.mobs.MobExecutor;
import io.lumine.mythic.core.skills.variables.*;
import io.lumine.mythic.core.spawning.spawners.MythicSpawner;
import io.lumine.mythic.core.spawning.spawners.SpawnerManager;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class MythicMobsBridge extends Bridge {

    @Override
    public void init() {
        ObjectFetcher.registerWithObjectFetcher(MythicMobsMobTag.class, MythicMobsMobTag.tagProcessor);
        ObjectFetcher.registerWithObjectFetcher(MythicSpawnerTag.class, MythicSpawnerTag.tagProcessor);
        PropertyParser.registerProperty(MythicMobsEntityProperties.class, EntityTag.class);
        PropertyParser.registerProperty(MythicMobsPlayerProperties.class, PlayerTag.class);
        ScriptEvent.registerScriptEvent(MythicMobsDeathEvent.class);
        ScriptEvent.registerScriptEvent(MythicMobsSpawnEvent.class);
        ScriptEvent.registerScriptEvent(MythicMobsDespawnEvent.class);
        DenizenCore.commandRegistry.registerCommand(MythicSpawnCommand.class);
        DenizenCore.commandRegistry.registerCommand(MythicThreatCommand.class);
        DenizenCore.commandRegistry.registerCommand(MythicSignalCommand.class);
        DenizenCore.commandRegistry.registerCommand(MythicSkillCommand.class);
        new MythicMobsLoaders().RegisterEvents();

        EntityTag.tagProcessor.custommatchers.add((entityTag, matcher) -> {
            if (matcher.equals("mythic_mob")) {
                return entityTag.getUUID() != null && getMobManager().isActiveMob(entityTag.getUUID());
            }
            if (matcher.startsWith("mythic_mob:")) {
                Entity entity = entityTag.getBukkitEntity();
                ActiveMob activeMob = entity != null ? getActiveMob(entity) : null;
                return activeMob != null && ScriptEvent.runGenericCheck(matcher.substring("mythic_mob:".length()), activeMob.getType().getInternalName());
            }
            return null;
        });
        ItemTag.tagProcessor.custommatchers.add((itemTag, matcher) -> {
            if (matcher.equals("mythic_item")) {
                return MythicBukkit.inst().getItemManager().isMythicItem(itemTag.getItemStack());
            }
            if (matcher.startsWith("mythic_item:")) {
                String mythicID = MythicBukkit.inst().getItemManager().getMythicTypeFromItem(itemTag.getItemStack());
                return mythicID != null && ScriptEvent.runGenericCheck(matcher.substring("mythic_item:".length()), mythicID);
            }
            return null;
        });

        // <--[data]
        // @name not_switches
        // @values mythic_mob, mythic_item
        // -->
        ScriptEvent.ScriptPath.notSwitches.add("mythic_mob");
        EntityTag.specialEntityMatchables.add("mythic_mob");
        BukkitScriptEvent.entityCouldMatchPrefixes.add("mythic_mob");
        ScriptEvent.ScriptPath.notSwitches.add("mythic_item");
        BukkitScriptEvent.itemCouldMatchableText.add("mythic_item");
        BukkitScriptEvent.itemCouldMatchPrefixes.add("mythic_item");

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
            Optional<MythicItem> itemOpt = MythicBukkit.inst().getItemManager().getItem(name);
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
        return MythicBukkit.inst().getMobManager().isActiveMob(BukkitAdapter.adapt(entity));
    }

    public static boolean isMythicMob(UUID uuid) {
        return MythicBukkit.inst().getMobManager().isActiveMob(uuid);
    }

    public static ActiveMob getActiveMob(Entity entity) {
        return MythicBukkit.inst().getMobManager().getMythicMobInstance(entity);
    }

    public static MythicMob getMythicMob(String name) {
        return MythicBukkit.inst().getMobManager().getMythicMob(name).orElse(null);
    }

    public static MobExecutor getMobManager() {
        return MythicBukkit.inst().getMobManager();
    }

    public static SpawnerManager getSpawnerManager() {
        return MythicBukkit.inst().getSpawnerManager();
    }

    public static boolean isMythicSpawner(String name) {
        return MythicBukkit.inst().getSpawnerManager().getSpawnerByName(name) != null;
    }

    public static MythicSpawner getMythicSpawner(String name) {
        return MythicBukkit.inst().getSpawnerManager().getSpawnerByName(name);
    }

    public static BukkitAPIHelper getAPI() {
        return MythicBukkit.inst().getAPIHelper();
    }

    public static String getMythicVariable(Entity entity, String key) {
        return getMythicVariableMap(entity).get(key).toString();
    }

    public static Map<String, Variable> getMythicVariableMap(Entity entity) {
        VariableManager variables = MythicBukkit.inst().getVariableManager();
        VariableRegistry registry = variables.getRegistry(VariableScope.TARGET, BukkitAdapter.adapt(entity));
        return registry.asMap();
    }

    public static void setMythicVariable(Entity entity, String variable, String value, String type) {
        VariableManager variables = MythicBukkit.inst().getVariableManager();
        VariableRegistry registry = variables.getRegistry(VariableScope.TARGET, BukkitAdapter.adapt(entity));
        VariableType varType = VariableType.valueOf(type);
        registry.put(variable, Variable.ofType(varType, value));
    }
    public static void setMythicVariableMap(Entity entity, Map<String, Variable> map) {
        VariableManager variables = MythicBukkit.inst().getVariableManager();
        VariableRegistry registry = variables.getRegistry(VariableScope.TARGET, BukkitAdapter.adapt(entity));
        for (String key : registry.asMap().keySet()) {
            registry.remove(key);
        }
        registry.putAll(map);
    }

    public static boolean skillExists(String name) {
        return MythicBukkit.inst().getSkillManager().getSkillNames().contains(name);
    }
}
