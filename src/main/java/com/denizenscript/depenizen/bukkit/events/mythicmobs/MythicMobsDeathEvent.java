package com.denizenscript.depenizen.bukkit.events.mythicmobs;

import com.denizenscript.denizencore.objects.*;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.depenizen.bukkit.objects.mythicmobs.MythicMobsMobTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MythicMobsDeathEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // mythicmob <'mob'> dies|death|killed
    //
    // @Location true
    //
    // @Switch by:<entity> to only process the event if the killer matches a specified entity type.
    //
    // @Triggers when a MythicMob dies.
    //
    // @Context
    // <context.mob> Returns the MythicMob that has been killed.
    // <context.entity> Returns the EntityTag for the MythicMob.
    // <context.killer> returns the EntityTag that killed the MythicMob (if available).
    // <context.level> Returns the level of the MythicMob.
    // <context.drops> Returns a list of items dropped.
    //
    // @Determine
    // ListTag(ItemTag) to specify new items to be dropped.
    //
    // @Plugin Depenizen, MythicMobs
    //
    // @Group Depenizen
    //
    // -->

    public MythicMobsDeathEvent() {
        registerCouldMatcher("mythicmob <'mob'> dies|death|killed");
        registerSwitches("by");
    }

    public static MythicMobsDeathEvent instance;
    public MythicMobDeathEvent event;
    public MythicMobsMobTag mythicmob;
    public EntityTag entity;
    public EntityTag killer;

    @Override
    public boolean matches(ScriptPath path) {
        String mob = path.eventArgLowerAt(1);
        if (!mob.equals("mob") && !runGenericCheck(mob, mythicmob.getMobType().getInternalName())) {
            return false;
        }
        if ((path.eventArgLowerAt(3).equals("by")) && (killer == null || !path.tryArgObject(4, killer))) {
            return false;
        }
        if (!path.tryObjectSwitch("by", killer)) {
            return false;
        }
        if (!runInCheck(path, entity.getLocation()) && (killer == null || !runInCheck(path, killer.getLocation()))) {
            return false;
        }
        return super.matches(path);
    }

    @Override
    public boolean applyDetermination(ScriptPath path, ObjectTag determinationObj) {
        String determination = determinationObj.toString();
        if (Argument.valueOf(determination).matchesArgumentList(ItemTag.class)) {
            List<ItemStack> newDrops = new ArrayList<>();
            for (ItemTag item : ListTag.valueOf(determination, getTagContext(path)).filter(ItemTag.class, path.container, true)) {
                if (item != null) {
                    newDrops.add(item.getItemStack());
                }
            }
            event.setDrops(newDrops);
            return true;
        }
        return super.applyDetermination(path, determinationObj);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(killer);
    }

    @Override
    public ObjectTag getContext(String name) {
        switch (name) {
            case "mob":
                return mythicmob;
            case "killer":
                return killer;
            case "entity":
                return entity;
            case "drops":
                ListTag drops = new ListTag();
                for (ItemStack i : event.getDrops()) {
                    drops.addObject(new ItemTag(i));
                }
                return drops;
            case "level":
                return new ElementTag(event.getMobLevel());
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onMythicMobDeath(MythicMobDeathEvent event) {
        mythicmob = new MythicMobsMobTag(event.getMob());
        entity = event.getEntity() == null ? null : new EntityTag(event.getEntity());
        if (entity != null) {
            EntityTag.rememberEntity(entity.getBukkitEntity());
        }
        killer = event.getKiller() == null ? null : new EntityTag(event.getKiller());
        this.event = event;
        fire(event);
        if (entity != null) {
            EntityTag.forgetEntity(entity.getBukkitEntity());
        }
    }
}
