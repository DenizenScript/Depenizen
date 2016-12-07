package com.denizenscript.depenizen.bukkit.events.sentinel;

import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizen.objects.dNPC;
import net.aufdemrand.denizen.utilities.DenizenAPI;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.ScriptEntryData;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.mcmonkey.sentinel.events.SentinelAttackEvent;

// <--[event]
// @Events
// on sentinel npc attacks
//
// @Regex ^on sentinel npc attacks$
//
// @Cancellable true
//
// @Triggers when a Sentinel-powered NPC attacks a target.
//
// @Context
// <context.entity> returns the NPC that is attacking.
//
// @Plugin DepenizenBukkit, Sentinel
//
// -->

public class SentinelAttackScriptEvent extends BukkitScriptEvent implements Listener {

    public SentinelAttackScriptEvent() {
        instance = this;
    }

    public static SentinelAttackScriptEvent instance;
    public SentinelAttackEvent event;
    public dNPC entity;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        return CoreUtilities.toLowerCase(s).startsWith("sentinel npc attacks");
    }

    @Override
    public boolean matches(ScriptContainer scriptContainer, String s) {
        return true;
    }

    @Override
    public String getName() {
        return "SentinelAttack";
    }

    @Override
    public void init() {
        Bukkit.getServer().getPluginManager().registerEvents(this, DenizenAPI.getCurrentInstance());
    }

    @Override
    public void destroy() {
        SentinelAttackEvent.getHandlerList().unregister(this);
    }

    @Override
    public boolean applyDetermination(ScriptContainer container, String determination) {
        return super.applyDetermination(container, determination);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(null, entity);
    }

    @Override
    public dObject getContext(String name) {
        if (name.startsWith("entity")) {
            return entity;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onSentinelAttack(SentinelAttackEvent event) {
        entity = new dNPC(event.getNPC());
        cancelled = event.isCancelled();
        this.event = event;
        fire();
        event.setCancelled(cancelled);
    }
}
