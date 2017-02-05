package com.denizenscript.depenizen.bukkit.events;


import fr.neatmonster.nocheatplus.checks.CheckType;
import fr.neatmonster.nocheatplus.checks.access.IViolationInfo;
import fr.neatmonster.nocheatplus.hooks.NCPHook;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.objects.dNPC;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.events.OldEventManager;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dObject;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NCPEvents implements NCPHook {

    // <--[event]
    // @Events
    // ncp violation
    //
    // @Triggers when NoCheatPlus detects a violation.
    //
    // @Context
    // <context.type> returns the type of violation.
    //
    // @Determine
    // "CANCELLED" to prevent NoCheatPlus from taking action upon the violation.
    //
    // @Plugin DepenizenBukkit, NoCheatPlus
    // -->
    @Override
    public boolean onCheckFailure(CheckType type, Player bukkitplayer, IViolationInfo info) {

        dPlayer player = new dPlayer(bukkitplayer);

        Map<String, dObject> context = new HashMap<String, dObject>();
        context.put("type", new Element(type.toString()));


        List<String> determinations = OldEventManager.doEvents(Arrays.asList
                        ("ncp violation"),
                new BukkitScriptEntryData(player, null), context);

        for (String determination : determinations) {
            determination = determination.toUpperCase();
            if (determination.equals("CANCELLED")) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String getHookName() {
        return "DEPENIZEN";
    }

    @Override
    public String getHookVersion() {
        return "1";
    }
}
