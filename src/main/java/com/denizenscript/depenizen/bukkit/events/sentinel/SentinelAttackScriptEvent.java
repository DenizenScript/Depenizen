package com.denizenscript.depenizen.bukkit.events.sentinel;

import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.NPCTag;
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
    // @Cancellable true
    //
    // @Triggers when a Sentinel-powered NPC attacks a target.
    //
    // @Switch entity:<entity> to only fire the event if the target entity matches the given entity matcher.
    //
    // @Context
    // <context.entity> returns the entity that the NPC is attacking.
    //
    // @Plugin Depenizen, Sentinel
    //
    // @NPC Always.
    //
    // @Player When the attacked entity is a player.
    //
    // @Group Depenizen
    //
    // -->

    public SentinelAttackScriptEvent() {
        instance = this;
        registerCouldMatcher("sentinel npc attacks");
        registerSwitches("entity");
    }

    public static SentinelAttackScriptEvent instance;
    public SentinelAttackEvent event;
    public EntityTag entity;
    public NPCTag npc;

    @Override
    public String getName() {
        return "SentinelAttack";
    }

    @Override
    public boolean matches(ScriptPath path) {
        if (path.switches.containsKey("entity") && !tryEntity(entity, path.switches.get("entity"))) {
            return false;
        }
        return super.matches(path);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(entity.isPlayer() ? entity.getDenizenPlayer() : null, npc);
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.startsWith("entity")) {
            return entity.getDenizenObject();
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onSentinelAttack(SentinelAttackEvent event) {
        npc = new NPCTag(event.getNPC());
        entity = new EntityTag(event.getNPC().getOrAddTrait(SentinelTrait.class).chasing);
        this.event = event;
        fire(event);
    }
}
