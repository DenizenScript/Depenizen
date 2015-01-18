package net.gnomeffinway.depenizen.support.plugins;

import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.tags.Attribute;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.gnomeffinway.depenizen.events.PVPArenaEvents;
import net.gnomeffinway.depenizen.extensions.pvparena.PVPArenaPlayerExtension;
import net.gnomeffinway.depenizen.support.Support;
import net.slipcor.pvparena.arena.Arena;
import net.slipcor.pvparena.managers.ArenaManager;

public class PVPArenaSupport extends Support {

    public PVPArenaSupport() {
        registerEvents(PVPArenaEvents.class);
        registerProperty(PVPArenaPlayerExtension.class, dPlayer.class);
        registerAdditionalTags("pvparena");
    }

    @Override
    public String additionalTags(Attribute attribute) {

        if (attribute.startsWith("pvparena")) {

            // PvPArena tags require a... PvPArena!
            Arena a = null;

            // PvPArena tag may specify a new PvPArena in the <pvparena[context]...> portion of the tag.
            if (attribute.hasContext(1))
                // Check if this is a valid PvPArena
                if (ArenaManager.getArenaByName(attribute.getContext(1)) != null) {
                    a = ArenaManager.getArenaByName(attribute.getContext(1));
                } else {
                    dB.echoError("Could not match '" + attribute.getContext(1) + "' to a valid arena!");
                    return null;
                }

            if (a == null) {
                dB.echoError("Invalid or missing arena!");
                return null;
            }

            attribute = attribute.fulfill(1);

            // <--[tag]
            // @attribute <pvparena[<arena>].player_count>
            // @returns Element(Integer)
            // @description
            // Returns the number of players in the arena.
            // @plugin Depenizen, PvP Arena
            // -->
            if (attribute.startsWith("playercount") || attribute.startsWith("player_count")) {
                return new Element(a.getFighters().size()).getAttribute(attribute.fulfill(1));
            }

        }

        return null;

    }
}
