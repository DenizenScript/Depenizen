package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizen.utilities.DenizenAPI;
import com.denizenscript.denizen.utilities.debugging.Debug;
import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.ObjectFetcher;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.denizencore.tags.ReplaceableTagEvent;
import com.denizenscript.denizencore.tags.TagManager;
import com.denizenscript.denizencore.tags.TagRunnable;
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
import com.denizenscript.depenizen.bukkit.tags.mythicmobs.MythicMobTagBase;
import com.denizenscript.depenizen.bukkit.tags.mythicmobs.MythicMobsTagBase;
import com.denizenscript.depenizen.bukkit.tags.mythicmobs.MythicSpawnerTagBase;
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
        ScriptEvent.registerScriptEvent(new MythicMobsDeathEvent());
        ScriptEvent.registerScriptEvent(new MythicMobsSpawnEvent());
        DenizenAPI.getCurrentInstance().getCommandRegistry().registerCommand(MythicSpawnCommand.class);
        DenizenAPI.getCurrentInstance().getCommandRegistry().registerCommand(MythicThreatCommand.class);
        DenizenAPI.getCurrentInstance().getCommandRegistry().registerCommand(MythicSignalCommand.class);
        DenizenAPI.getCurrentInstance().getCommandRegistry().registerCommand(MythicSkillCommand.class);
        new MythicMobsLoaders().RegisterEvents();
        new MythicMobsTagBase();
        new MythicSpawnerTagBase();
        new MythicMobTagBase();

        // <--[tag]
        // @attribute <mythic_item[<name>]>
        // @returns ItemTag
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns an ItemTag of the named mythic item.
        // -->
        TagManager.registerTagHandler(new TagRunnable.RootForm() {
            @Override
            public void run(ReplaceableTagEvent event) {
                if (!event.hasNameContext()) {
                    return;
                }
                String name = event.getNameContext();
                Optional<MythicItem> itemOpt = MythicMobs.inst().getItemManager().getItem(name);
                if (!itemOpt.isPresent()) {
                    if (!event.hasAlternative()) {
                        Debug.echoError("'" + name + "' is not a valid Mythic item.");
                    }
                    return;
                }
                ItemStack item = BukkitAdapter.adapt(itemOpt.get().generateItemStack(1));
                event.setReplacedObject(new ItemTag(item).getObjectAttribute(event.getAttributes().fulfill(1)));
            }
        }, "mythic_item");
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
