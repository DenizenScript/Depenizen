package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.events.mobarena.MobArenaStartsScriptEvent;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.garbagemule.MobArena.MobArena;
import com.garbagemule.MobArena.framework.Arena;
import com.denizenscript.depenizen.bukkit.commands.mobarena.MobArenaCommand;
import com.denizenscript.depenizen.bukkit.events.mobarena.MobArenaEndsScriptEvent;
import com.denizenscript.depenizen.bukkit.properties.mobarena.MobArenaPlayerProperties;
import com.denizenscript.depenizen.bukkit.objects.mobarena.MobArenaArena;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizencore.events.ScriptEvent;
import net.aufdemrand.denizencore.objects.ObjectFetcher;
import net.aufdemrand.denizencore.objects.TagRunnable;
import net.aufdemrand.denizencore.objects.properties.PropertyParser;
import net.aufdemrand.denizencore.tags.ReplaceableTagEvent;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.tags.Attribute;
import com.denizenscript.depenizen.bukkit.events.mobarena.MobArenaWaveChangesScriptEvent;
import net.aufdemrand.denizencore.tags.TagManager;

public class MobArenaBridge extends Bridge {

    public static MobArenaBridge instance;

    @Override
    public void init() {
        instance = this;
        ObjectFetcher.registerWithObjectFetcher(MobArenaArena.class);
        TagManager.registerTagHandler(new TagRunnable.RootForm() {
            @Override
            public void run(ReplaceableTagEvent event) {
                tagEvent(event);
            }
        }, "mobarena");
        new MobArenaCommand().activate().as("mobarena").withOptions("See Documentation.", 1);
        ScriptEvent.registerScriptEvent(new MobArenaStartsScriptEvent());
        ScriptEvent.registerScriptEvent(new MobArenaEndsScriptEvent());
        ScriptEvent.registerScriptEvent(new MobArenaWaveChangesScriptEvent());
        PropertyParser.registerProperty(MobArenaPlayerProperties.class, dPlayer.class);
    }

    public void tagEvent(ReplaceableTagEvent event) {
        Attribute attribute = event.getAttributes();

        // <--[tag]
        // @attribute <mobarena[<name>]>
        // @returns MobArena
        // @description
        // Returns the mob arena for the input name.
        // @Plugin Depenizen, MobArena
        // -->
        if (attribute.hasContext(1)) {
            MobArenaArena arena = MobArenaArena.valueOf(attribute.getContext(1));
            if (arena != null) {
                event.setReplacedObject(arena.getObjectAttribute(attribute.fulfill(1)));
            }
            else if (!event.hasAlternative()) {
                dB.echoError("Unknown mob arena '" + attribute.getContext(1) + "' for mobarena[] tag.");
            }
            return;
        }

        attribute = attribute.fulfill(1);

        // <--[tag]
        // @attribute <mobarena.list_arenas>
        // @returns dList(MobArena)
        // @description
        // Returns a list of all MobArenas.
        // @Plugin Depenizen, MobArena
        // -->
        if (attribute.startsWith("list_arenas")) {
            dList arenas = new dList();
            for (Arena a : ((MobArena) plugin).getArenaMaster().getArenas()) {
                if (((MobArena) plugin).getArenaMaster().getArenaWithName(a.configName()) == null) {
                    continue;
                }
                arenas.add(new MobArenaArena(a).identify());
            }
            event.setReplacedObject(arenas.getObjectAttribute(attribute.fulfill(1)));
        }
    }
}
