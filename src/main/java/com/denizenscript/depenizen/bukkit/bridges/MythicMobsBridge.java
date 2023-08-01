package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.DenizenCore;
import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.ObjectFetcher;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.core.MapTag;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.denizencore.tags.PseudoObjectTagBase;
import com.denizenscript.denizencore.tags.TagManager;
import com.denizenscript.denizencore.utilities.text.StringHolder;
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
import com.denizenscript.depenizen.bukkit.properties.mythicmobs.MythicMobsPlayerProperties;
import com.denizenscript.depenizen.bukkit.utilities.mythicmobs.MythicMobsLoaders;
import io.lumine.mythic.api.mobs.MobManager;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.BukkitAPIHelper;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.items.ItemExecutor;
import io.lumine.mythic.core.items.MythicItem;
import io.lumine.mythic.core.mobs.ActiveMob;
import io.lumine.mythic.core.skills.variables.*;
import io.lumine.mythic.core.spawning.spawners.MythicSpawner;
import io.lumine.mythic.core.spawning.spawners.SpawnerManager;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class MythicMobsBridge extends Bridge {

    static class MythicMobsBridgeTags extends PseudoObjectTagBase<MythicMobsBridgeTags> {

        public static MythicMobsBridgeTags instance;

        public MythicMobsBridgeTags() {
            instance = this;
            TagManager.registerStaticTagBaseHandler(MythicMobsBridgeTags.class, "mythicmobs", (t) -> instance);
        }

        @Override
        public void register() {

            // <--[tag]
            // @attribute <mythicmobs.item_ids>
            // @returns ListTag
            // @plugin Depenizen, MythicMobs
            // @description
            // Returns a ListTag of valid MythicItem IDs. See also <@link tag mythic_item>.
            // -->
            tagProcessor.registerTag(ListTag.class, "item_ids", (attribute, object) -> {
                return new ListTag(getItemManager().getItemNames(), true);
            });

            // <--[tag]
            // @attribute <mythicmobs.skills>
            // @returns ListTag
            // @plugin Depenizen, MythicMobs
            // @description
            // Returns a ListTag of valid MythicSkill IDs.
            // -->
            tagProcessor.registerTag(ListTag.class, "skills", (attribute, object) -> {
                return new ListTag(getSkillNames(), true);
            });

            // <--[tag]
            // @attribute <mythicmobs.mob_ids>
            // @returns ListTag
            // @plugin Depenizen, MythicMobs
            // @description
            // Returns a ListTag of valid MythicMob IDs.
            // -->
            tagProcessor.registerTag(ListTag.class, "mob_ids", (attribute, object) -> {
                return new ListTag(getMobManager().getMobNames(), true);
            });

            // <--[tag]
            // @attribute <mythicmobs.active_mobs>
            // @returns ListTag(MythicMobsMobTag)
            // @plugin Depenizen, MythicMobs
            // @description
            // Returns a ListTag of all active MythicMobs on the server.
            // -->
            tagProcessor.registerTag(ListTag.class, "active_mobs", (attribute, object) -> {
                return new ListTag(getMobManager().getActiveMobs(), MythicMobsMobTag::new);
            });

            // <--[tag]
            // @attribute <mythicmobs.spawners>
            // @returns ListTag(MythicSpawnerTag)
            // @plugin Depenizen, MythicMobs
            // @description
            // Returns a ListTag of all MythicSpawners.
            // -->
            tagProcessor.registerTag(ListTag.class, "spawners", (attribute, object) -> {
                return new ListTag(getSpawnerManager().getSpawners(), MythicSpawnerTag::new);
            });

            // <--[tag]
            // @attribute <mythicmobs.damage_modifiers[<mob_id>]>
            // @returns MapTag
            // @plugin Depenizen, MythicMobs
            // @description
            // Returns a map of the damage modifiers for a MythicMob from a Mob ID.
            // -->
            tagProcessor.registerTag(MapTag.class, ElementTag.class, "damage_modifiers", (attribute, object, id) -> {
                MythicMob mob = getMythicMob(id.asString());
                if (mob == null) {
                    return null;
                }
                return new MapTag(mob.getDamageModifiers().entrySet().stream().map((e) -> Map.entry(new StringHolder(e.getKey()), new ElementTag(e.getValue()))).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
            });

            // <--[tag]
            // @attribute <mythicmobs.mob_path[<mob_id>]>
            // @returns ElementTag
            // @plugin Depenizen, MythicMobs
            // @description
            // Returns the path to the configuration file for the MythicMob from a Mob ID.
            // -->
            tagProcessor.registerTag(ElementTag.class, ElementTag.class, "mob_path", (attribute, object, id) -> {
                MythicMob mob = getMythicMob(id.asString());
                if (mob == null) {
                    return null;
                }
                return new ElementTag(mob.getConfig().getFile().getPath(), true);
            });

            // <--[tag]
            // @attribute <mythicmobs.item_path[<item_id>]>
            // @returns ElementTag
            // @plugin Depenizen, MythicMobs
            // @description
            // Returns the path to the configuration file for the MythicItem from an Item ID.
            // -->
            tagProcessor.registerTag(ElementTag.class, ElementTag.class, "item_path", (attribute, object, id) -> {
                Optional<MythicItem> item = getItemManager().getItem(id.asString());
                return item.map(mythicItem -> new ElementTag(mythicItem.getConfig().getFile().getPath(), true)).orElse(null);
            });

            // <--[tag]
            // @attribute <mythicmobs.packs>
            // @returns ListTag
            // @plugin Depenizen, MythicMobs
            // @description
            // Returns a list of all Mythic pack IDs.
            // -->
            tagProcessor.registerTag(ListTag.class, "packs", (attribute, object) -> new ListTag(MythicBukkit.inst().getPackManager().getPacks(), (pack) -> new ElementTag(pack.getName())));
        }
    }

    @Override
    public void init() {
        ObjectFetcher.registerWithObjectFetcher(MythicMobsMobTag.class, MythicMobsMobTag.tagProcessor);
        ObjectFetcher.registerWithObjectFetcher(MythicSpawnerTag.class, MythicSpawnerTag.tagProcessor);
        PropertyParser.registerProperty(MythicMobsEntityProperties.class, EntityTag.class);
        PropertyParser.registerProperty(MythicMobsPlayerProperties.class, PlayerTag.class);
        ScriptEvent.registerScriptEvent(MythicMobsDeathEvent.class);
        ScriptEvent.registerScriptEvent(MythicMobsSpawnEvent.class);
        DenizenCore.commandRegistry.registerCommand(MythicSpawnCommand.class);
        DenizenCore.commandRegistry.registerCommand(MythicThreatCommand.class);
        DenizenCore.commandRegistry.registerCommand(MythicSignalCommand.class);
        DenizenCore.commandRegistry.registerCommand(MythicSkillCommand.class);
        new MythicMobsLoaders().RegisterEvents();
        new MythicMobsBridgeTags();

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
            if (itemOpt.isEmpty()) {
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

    public static MobManager getMobManager() {
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
        Object value = getMythicVariableMap(entity).get(key);
        return value == null ? null : value.toString();
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

    public static ItemExecutor getItemManager() {
        return MythicBukkit.inst().getItemManager();
    }

    public static Collection<String> getSkillNames() {
        return MythicBukkit.inst().getSkillManager().getSkillNames();
    }
}
