package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.DenizenCore;
import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.ObjectFetcher;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.core.MapTag;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.denizencore.tags.PseudoObjectTagBase;
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
import com.denizenscript.depenizen.bukkit.properties.mythicmobs.MythicMobsPlayerProperties;
import com.denizenscript.depenizen.bukkit.utilities.mythicmobs.MythicMobsLoaders;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.mobs.MobManager;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.api.packs.Pack;
import io.lumine.mythic.api.skills.placeholders.PlaceholderString;
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

public class MythicMobsBridge extends Bridge {

    static class MythicMobsBridgeTags extends PseudoObjectTagBase<MythicMobsBridgeTags> {

        public static MythicMobsBridgeTags instance;

        public static void register() {
            instance = new MythicMobsBridgeTags();
            TagManager.registerStaticTagBaseHandler(MythicMobsBridgeTags.class, "mythicmobs", (t) -> instance);
        }

        @Override
        public void registerTags() {

            // <--[tag]
            // @attribute <mythicmobs.item_ids>
            // @returns ListTag
            // @plugin Depenizen, MythicMobs
            // @description
            // Returns a ListTag of valid MythicItem IDs. See also <@link tag mythic_item>.
            // -->
            tagProcessor.registerTag(ListTag.class, "item_ids", (attribute, object) -> {
                ListTag list = new ListTag();
                for (String item : getItemNames()) {
                    list.addObject(new ElementTag(item, true));
                }
                return list;
            });

            // <--[tag]
            // @attribute <mythicmobs.skills>
            // @returns ListTag
            // @plugin Depenizen, MythicMobs
            // @description
            // Returns a ListTag of valid MythicSkill IDs.
            // -->
            tagProcessor.registerTag(ListTag.class, "skills", (attribute, object) -> {
                ListTag list = new ListTag();
                for (String item : getSkillNames()) {
                    list.addObject(new ElementTag(item, true));
                }
                return list;
            });

            // <--[tag]
            // @attribute <mythicmobs.mob_ids>
            // @returns ListTag
            // @plugin Depenizen, MythicMobs
            // @description
            // Returns a ListTag of valid MythicMob IDs.
            // -->
            tagProcessor.registerTag(ListTag.class, "mob_ids", (attribute, object) -> {
                ListTag list = new ListTag();
                for (String item : getMobNames()) {
                    list.addObject(new ElementTag(item, true));
                }
                return list;
            });

            // <--[tag]
            // @attribute <mythicmobs.active_mobs>
            // @returns ListTag(MythicMobsMobTag)
            // @plugin Depenizen, MythicMobs
            // @description
            // Returns a ListTag of all active MythicMobs on the server.
            // -->
            tagProcessor.registerTag(ListTag.class, "active_mobs", (attribute, object) -> {
                ListTag list = new ListTag();
                for (ActiveMob entity : MythicMobsBridge.getMobManager().getActiveMobs()) {
                    list.addObject(new MythicMobsMobTag(entity));
                }
                return list;
            });

            // <--[tag]
            // @attribute <mythicmobs.spawners>
            // @returns ListTag(MythicSpawnerTag)
            // @plugin Depenizen, MythicMobs
            // @description
            // Returns a ListTag of all MythicSpawners.
            // -->
            tagProcessor.registerTag(ListTag.class, "spawners", (attribute, object) -> {
                ListTag list = new ListTag();
                for (MythicSpawner spawner : MythicMobsBridge.getSpawnerManager().getSpawners()) {
                    list.addObject(new MythicSpawnerTag(spawner));
                }
                return list;
            });

            // <--[tag]
            // @attribute <mythicmobs.damage_modifiers[<mob_id>]>
            // @returns MapTag
            // @plugin Depenizen, MythicMobs
            // @description
            // Returns a map of the damage modifiers for a MythicMob from a Mob ID.
            // -->
            tagProcessor.registerTag(MapTag.class, ElementTag.class, "damage_modifiers", (attribute, object, id) -> {
                Optional<MythicMob> optionalMythicMob = MythicBukkit.inst().getMobManager().getMythicMob(id.asString());
                if (!optionalMythicMob.isPresent()) return null;
                MythicMob mob = optionalMythicMob.get();
                MapTag result = new MapTag();
                for (Map.Entry<String, Double> entry : MythicMobsBridge.getDamageModifiers(mob).entrySet()) {
                    result.putObject(entry.getKey(), new ElementTag(entry.getValue().toString()));
                }
                return result;
            });

            // <--[tag]
            // @attribute <mythicmobs.mob_path[<mob_id>]>
            // @returns ElementTag
            // @plugin Depenizen, MythicMobs
            // @description
            // Returns the path to the configuration for the MythicMob from a Mob ID.
            // -->
            tagProcessor.registerTag(ElementTag.class, ElementTag.class, "mob_path", (attribute, object, id) -> {
                MythicMob mob = getMythicMob(id.asString());
                if (mob == null) return null;
                return new ElementTag(mob.getConfig().getFile().getPath(), true);
            });

            // <--[tag]
            // @attribute <mythicmobs.item_path[<item_id>]>
            // @returns ElementTag
            // @plugin Depenizen, MythicMobs
            // @description
            // Returns the path to the configuration for the MythicItem from an Item ID.
            // -->
            tagProcessor.registerTag(ElementTag.class, ElementTag.class, "item_path", (attribute, object, id) -> {
                MythicItem item = getMythicItem(id.asString());
                if (item == null) return null;
                return new ElementTag(item.getConfig().getFile().getPath(), true);
            });

            // <--[tag]
            // @attribute <mythicmobs.packs>
            // @returns ListTag
            // @plugin Depenizen, MythicMobs
            // @description
            // Returns a list of all Mythic pack IDs.
            // -->
            tagProcessor.registerTag(ListTag.class, "packs", (attribute, object) -> {
                ListTag list = new ListTag();
                for (Pack pack : MythicBukkit.inst().getPackManager().getPacks()) {
                    list.addObject(new ElementTag(pack.getName(), true));
                }
                return list;
            });
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

        MythicMobsBridgeTags.register();

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

    public static Map<String, Double> getDamageModifiers(MythicMob mob) {
        return mob.getDamageModifiers();
    }

    public static Collection<String> getMobNames() {
        return getMobManager().getMobNames();
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

    public static Collection<String> getItemNames() {
        return getItemManager().getItemNames();
    }

    public static MythicItem getMythicItem(String name) {
        return MythicBukkit.inst().getItemManager().getItem(name).orElse(null);
    }

    public static Collection<String> getSkillNames() {
        return MythicBukkit.inst().getSkillManager().getSkillNames();
    }

    public static void setFaction(ActiveMob mob, String faction) {
        mob.setFaction(faction);
    }

    public static String getFaction(ActiveMob mob) {
        return mob.getFaction();
    }

    public static String parseMythic(AbstractEntity entity, String string) {
        PlaceholderString placeholderString = PlaceholderString.of(string);
        return placeholderString.get(entity);
    }

    public static boolean skillExists(String name) {
        return getSkillNames().contains(name);
    }
}
