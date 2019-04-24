package com.denizenscript.depenizen.bukkit.events.sentinel;

import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizen.objects.dNPC;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.ScriptEntryData;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.mcmonkey.sentinel.SentinelTrait;
import org.mcmonkey.sentinel.events.SentinelAttackEvent;

public class SentinelAttackScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // sentinel npc attacks
    //
    // @Regex ^on sentinel npc attacks$
    //
    // @Cancellable true
    //
    // @Triggers when a Sentinel-powered NPC attacks a target.
    //
    // @Context
    // <context.entity> returns the entity that the NPC is attacking.
    //
    // @Plugin DepenizenBukkit, Sentinel
    //
    // -->

    public SentinelAttackScriptEvent() {
        instance = this;
    }

    public static SentinelAttackScriptEvent instance;
    public SentinelAttackEvent event;
    public dObject entity;
    public dNPC npc;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        return CoreUtilities.toLowerCase(s).startsWith("sentinel npc attacks");
    }

    @Override
    public boolean matches(ScriptPath path) {
        return true;
    }

    @Override
    public String getName() {
        return "SentinelAttack";
    }

    @Override
    public boolean applyDetermination(ScriptContainer container, String determination) {
        return super.applyDetermination(container, determination);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(entity instanceof dPlayer ? (dPlayer) entity : null, npc);
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
        npc = new dNPC(event.getNPC());
        entity = new dEntity(event.getNPC().getTrait(SentinelTrait.class).chasing).getDenizenObject();
        this.event = event;
        fire(event);
    }
}
