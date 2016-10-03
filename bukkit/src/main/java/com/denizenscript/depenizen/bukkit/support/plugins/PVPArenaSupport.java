package com.denizenscript.depenizen.bukkit.support.plugins;

import com.denizenscript.depenizen.bukkit.events.pvparena.PVPArenaStartsScriptEvent;
import com.denizenscript.depenizen.bukkit.extensions.pvparena.PVPArenaPlayerExtension;
import com.denizenscript.depenizen.bukkit.objects.pvparena.PVPArenaArena;
import com.denizenscript.depenizen.bukkit.support.Support;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.tags.TagContext;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.tags.Attribute;
import net.slipcor.pvparena.arena.Arena;
import net.slipcor.pvparena.managers.ArenaManager;

public class PVPArenaSupport extends Support {

    public PVPArenaSupport() {
        registerScriptEvents(new PVPArenaStartsScriptEvent());
        registerProperty(PVPArenaPlayerExtension.class, dPlayer.class);
        registerObjects(PVPArenaArena.class);
        registerAdditionalTags("pvparena");
    }

    @Override
    public String additionalTags(Attribute attribute, TagContext tagContext) {

        if (attribute.startsWith("pvparena") && attribute.hasContext(1)) {
            PVPArenaArena arena = PVPArenaArena.valueOf(attribute.getContext(1));
            if (arena == null) {
                return null;
            }
            return arena.getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <pvparena.list_arenas>
        // @returns dList(PVPArena)
        // @description
        // Returns a list of all PVPArenas.
        // @plugin Depenizen, PVPArena
        // -->
        attribute = attribute.fulfill(1);
        if (attribute.startsWith("list_arenas")) {
            dList arenas = new dList();
            for (Arena a : ArenaManager.getArenas()) {
                arenas.add(new PVPArenaArena(a).identify());
            }
            return arenas.getAttribute(attribute.fulfill(1));
        }

        return null;

    }
}
