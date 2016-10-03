package com.morphanone.depenizenbukkit.support.plugins;

import com.morphanone.depenizenbukkit.events.pvparena.PVPArenaStartsScriptEvent;
import com.morphanone.depenizenbukkit.extensions.pvparena.PVPArenaPlayerExtension;
import com.morphanone.depenizenbukkit.objects.pvparena.PVPArenaArena;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.tags.TagContext;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.tags.Attribute;
import com.morphanone.depenizenbukkit.support.Support;
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
