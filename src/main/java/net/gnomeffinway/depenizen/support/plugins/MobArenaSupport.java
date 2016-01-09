package net.gnomeffinway.depenizen.support.plugins;

import com.garbagemule.MobArena.MobArena;
import com.garbagemule.MobArena.framework.Arena;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.tags.Attribute;
import net.gnomeffinway.depenizen.events.MobArena.MobArenaEndsScriptEvent;
import net.gnomeffinway.depenizen.events.MobArena.MobArenaStartsScriptEvent;
import net.gnomeffinway.depenizen.events.MobArena.MobArenaWaveChangesScriptEvent;
import net.gnomeffinway.depenizen.extensions.mobarena.MobArenaPlayerExtension;
import net.gnomeffinway.depenizen.objects.mobarena.mobarena;
import net.gnomeffinway.depenizen.support.Support;
import org.bukkit.Bukkit;

public class MobArenaSupport extends Support {

    public static MobArena plugin;

    public MobArenaSupport() {
        plugin = (MobArena) Bukkit.getPluginManager().getPlugin("MobArena");
        registerObjects(mobarena.class);
        registerAdditionalTags("mobarena");
        registerEvents(MobArenaStartsScriptEvent.class);
        registerEvents(MobArenaEndsScriptEvent.class);
        registerEvents(MobArenaWaveChangesScriptEvent.class);
        registerProperty(MobArenaPlayerExtension.class, dPlayer.class);
    }

    public static MobArena getPlugin() {
        return plugin;
    }

    @Override
    public String additionalTags(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        if (attribute.startsWith("mobarena")) {
            // <mobarena[<arena name>]>
            if (attribute.hasContext(1)) {
                mobarena arena = mobarena.valueOf(attribute.getContext(1));
                if (arena == null) {
                    return null;
                }
                return arena.getAttribute(attribute.fulfill(1));
            }

            attribute = attribute.fulfill(1);

            if (attribute.startsWith("list_arenas")) {
                dList arenas = new dList();
                for (Arena a : plugin.getArenaMaster().getArenas()) {
                    arenas.add(new mobarena(a).identify());
                }
                return arenas.getAttribute(attribute.fulfill(1));
            }
        }

        return null;
    }
}
