package com.denizenscript.depenizen.bukkit.events.sentinel;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.NPCTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.mcmonkey.sentinel.events.SentinelNoMoreTargetsEvent;

public class SentinelNoMoreTargetsScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // sentinel npc has no more targets
    //
    // @Regex ^on sentinel npc has no more targets$
    //
    // @Triggers when a Sentinel-powered NPC has no more targets to fight and can return to normal idle behavior.
    //
    // @Plugin Depenizen, Sentinel
    //
    // -->

    public SentinelNoMoreTargetsScriptEvent() {
        instance = this;
    }

    public static SentinelNoMoreTargetsScriptEvent instance;
    public SentinelNoMoreTargetsEvent event;
    public NPCTag npc;

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("sentinel npc has no more targets");
    }

    @Override
    public String getName() {
        return "SentinelNoMoreTargets";
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(null, npc);
    }

    @EventHandler
    public void onSentinelAttack(SentinelNoMoreTargetsEvent event) {
        npc = new NPCTag(event.getNPC());
        this.event = event;
        fire(event);
    }
}
