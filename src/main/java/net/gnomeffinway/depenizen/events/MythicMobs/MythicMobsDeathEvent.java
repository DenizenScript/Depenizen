package net.gnomeffinway.depenizen.events.MythicMobs;

import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizen.utilities.DenizenAPI;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.ScriptEntryData;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent;
import net.gnomeffinway.depenizen.objects.mythicmobs.MythicMobsMob;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

// <--[event]
// @Events
// on mythicmob dies
// on mythicmob death
//
// @Regex ^on mythicmob (dies|death)$
//
// @Cancellable false
//
// @Triggers when a MythicMob dies.
//
// @Context
// <context.mob> Returns the MythicMob that has been killed.
// <context.entity> Returns the dEntity for the MythicMob.
// <context.level> Returns the level of the MythicMob.
//
// @Plugin Depenizen, MythicMobs
//
// -->

public class MythicMobsDeathEvent extends BukkitScriptEvent implements Listener {

    public MythicMobsDeathEvent() {
        instance = this;
    }

    MythicMobsDeathEvent instance;
    MythicMobDeathEvent event;
    MythicMobsMob mob;
    dEntity entity;
    dEntity killer;
    Element level;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        return lower.startsWith("mythicmob dies") || lower.startsWith("mythicmob death");
    }

    @Override
    public boolean matches(ScriptContainer scriptContainer, String s) {
        return true;
    }

    @Override
    public String getName() {
        return "MythicMobsDeath";
    }

    @Override
    public void init() {
        Bukkit.getServer().getPluginManager().registerEvents(this, DenizenAPI.getCurrentInstance());
    }

    @Override
    public void destroy() {
        MythicMobDeathEvent.getHandlerList().unregister(this);
    }

    @Override
    public boolean applyDetermination(ScriptContainer container, String determination) {
        return super.applyDetermination(container, determination);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(null, null);
    }

    @Override
    public dObject getContext(String name) {
        if (name.equals("mob")) {
            return mob;
        }
        else if (name.equals("killer")) {
            return killer;
        }
        else if (name.equals("entity")) {
            return entity;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onMythicMobDeath(MythicMobDeathEvent event) {
        mob = new MythicMobsMob(event.getMobInstance());
        entity = new dEntity(event.getEntity());
        killer = new dEntity(event.getKiller());
        level = new Element(event.getMobLevel());
        this.event = event;
        fire();
    }
}
