package com.denizenscript.depenizen.bukkit.events.sentinel;

import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.NPCTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
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
    // @Plugin Depenizen, Sentinel
    //
    // -->

    public SentinelAttackScriptEvent() {
        instance = this;
    }

    public static SentinelAttackScriptEvent instance;
    public SentinelAttackEvent event;
    public ObjectTag entity;
    public NPCTag npc;

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("sentinel npc attacks");
    }

    @Override
    public String getName() {
        return "SentinelAttack";
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(entity instanceof PlayerTag ? (PlayerTag) entity : null, npc);
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.startsWith("entity")) {
            return entity;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onSentinelAttack(SentinelAttackEvent event) {
        npc = new NPCTag(event.getNPC());
        entity = new EntityTag(event.getNPC().getTrait(SentinelTrait.class).chasing).getDenizenObject();
        this.event = event;
        fire(event);
    }
}
