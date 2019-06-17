package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.events.pvparena.PlayerExitsPVPArenaScriptEvent;
import com.denizenscript.depenizen.bukkit.events.pvparena.PlayerJoinsPVPArenaScriptEvent;
import com.denizenscript.depenizen.bukkit.events.pvparena.PlayerLeavesPVPArenaScriptEvent;
import com.denizenscript.depenizen.bukkit.events.pvparena.PVPArenaStartsScriptEvent;
import com.denizenscript.depenizen.bukkit.extensions.pvparena.PVPArenaPlayerExtension;
import com.denizenscript.depenizen.bukkit.objects.pvparena.PVPArenaArena;
import com.denizenscript.depenizen.bukkit.Bridge;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizencore.events.ScriptEvent;
import net.aufdemrand.denizencore.objects.ObjectFetcher;
import net.aufdemrand.denizencore.objects.TagRunnable;
import net.aufdemrand.denizencore.objects.properties.PropertyParser;
import net.aufdemrand.denizencore.tags.ReplaceableTagEvent;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.tags.Attribute;
import net.aufdemrand.denizencore.tags.TagManager;
import net.slipcor.pvparena.arena.Arena;
import net.slipcor.pvparena.managers.ArenaManager;

public class PVPArenaBridge extends Bridge {

    @Override
    public void init() {
        ScriptEvent.registerScriptEvent(new PVPArenaStartsScriptEvent());
        ScriptEvent.registerScriptEvent(new PlayerJoinsPVPArenaScriptEvent());
        ScriptEvent.registerScriptEvent(new PlayerLeavesPVPArenaScriptEvent());
        ScriptEvent.registerScriptEvent(new PlayerExitsPVPArenaScriptEvent());
        PropertyParser.registerProperty(PVPArenaPlayerExtension.class, dPlayer.class);
        ObjectFetcher.registerWithObjectFetcher(PVPArenaArena.class);
        TagManager.registerTagHandler(new TagRunnable.RootForm() {
            @Override
            public void run(ReplaceableTagEvent event) {
                tagEvent(event);
            }
        }, "pvparena");
    }

    public void tagEvent(ReplaceableTagEvent event) {
        Attribute attribute = event.getAttributes();

        // <--[tag]
        // @attribute <pvparena[<name>]>
        // @returns PVPArena
        // @description
        // Returns the PVPArena by the input name.
        // @Plugin Depenizen, PVPArena
        // -->
        if (attribute.hasContext(1)) {
            PVPArenaArena arena = PVPArenaArena.valueOf(attribute.getContext(1));
            if (arena != null) {
                event.setReplacedObject(arena.getObjectAttribute(attribute.fulfill(1)));
            }
            else if (!event.hasAlternative()) {
                dB.echoError("Unknown arena '" + attribute.getContext(1) + "' for pvparena[] tag.");
            }
            return;
        }

        // <--[tag]
        // @attribute <pvparena.list_arenas>
        // @returns dList(PVPArena)
        // @description
        // Returns a list of all PVPArenas.
        // @Plugin Depenizen, PVPArena
        // -->
        attribute = attribute.fulfill(1);
        if (attribute.startsWith("list_arenas")) {
            dList arenas = new dList();
            for (Arena a : ArenaManager.getArenas()) {
                arenas.add(new PVPArenaArena(a).identify());
            }
            event.setReplacedObject(arenas.getObjectAttribute(attribute.fulfill(1)));
        }

    }
}
