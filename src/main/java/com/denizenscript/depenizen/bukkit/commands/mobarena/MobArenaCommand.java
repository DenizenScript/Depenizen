package com.denizenscript.depenizen.bukkit.commands.mobarena;

import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.depenizen.bukkit.objects.mobarena.MobArenaArena;
import com.garbagemule.MobArena.framework.Arena;
import com.denizenscript.denizen.objects.dPlayer;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.ArgumentHelper;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.denizenscript.denizencore.utilities.debugging.Debug;

import java.util.List;

public class MobArenaCommand extends AbstractCommand {

    // <--[command]
    // @Name MobArena
    // @Syntax mobarena [<mobarena>] (add:<player>|...) (remove:<player>|...) (spectate:<player>|...)
    // @Group Depenizen
    // @Plugin Depenizen, MobArena
    // @Required 1
    // @Short Make a player join, remove a player from or make a player spectate a MobArena.

    // @Description
    // This command allows you to make a player join an arena, make them leave an arena or make them spectate an arena.
    // Follows normal MobArena functionality, so acts as if the player has typed '/mobarena join'.
    // NOTE: You can use all 3: ("add", "remove", and "spectate") as once, however avoid conflicts.

    // @Tags
    // <player.mobarena.*>

    // @Usage
    // Use to force the player to join an arena.
    // - mobarena mobarena@Default add:<player>

    // @Usage
    // Use to force the player to leave an arena.
    // - mobarena mobarena@Default remove:<player>

    // @Usage
    // Use to force a player to join an arena and another to leave..
    // - mobarena mobarena@Default add:<player> remove:p@mcmonkey4eva

    // @Usage
    // Use to cause all players who aren't in an arena to spectate.
    // - mobarena mobarena@Default spectate:<server.list_online_players.filter[mobarena.in_arena.not]>

    // -->

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {
        for (Argument arg : ArgumentHelper.interpret(scriptEntry.getArguments())) {
            if (!scriptEntry.hasObject("arena") &&
                    (arg.matchesPrefix("arena") || MobArenaArena.matches(arg.getValue()))) {
                scriptEntry.addObject("arena", MobArenaArena.valueOf(arg.getValue()));
            }
            else if (!scriptEntry.hasObject("add")
                    && arg.matchesPrefix("add", "join")
                    && arg.matchesArgumentList(dPlayer.class)) {
                scriptEntry.addObject("add", arg.asType(ListTag.class).filter(dPlayer.class, scriptEntry));
            }
            else if (!scriptEntry.hasObject("remove")
                    && arg.matchesPrefix("remove", "leave")
                    && arg.matchesArgumentList(dPlayer.class)) {
                scriptEntry.addObject("remove", arg.asType(ListTag.class).filter(dPlayer.class, scriptEntry));
            }
            else if (!scriptEntry.hasObject("spectate")
                    && arg.matchesPrefix("spectate", "spec")
                    && arg.matchesArgumentList(dPlayer.class)) {
                scriptEntry.addObject("spectate", arg.asType(ListTag.class).filter(dPlayer.class, scriptEntry));
            }
            else {
                arg.reportUnhandled();
            }
        }

        if (!scriptEntry.hasObject("arena")) {
            throw new InvalidArgumentsException("Must specify a valid MobArena!");
        }
        else if (!scriptEntry.hasObject("add") && !scriptEntry.hasObject("remove") && !scriptEntry.hasObject("spectate")) {
            throw new InvalidArgumentsException("Must specify players to add, remove or spectate!");
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void execute(ScriptEntry scriptEntry) {
        MobArenaArena arena = scriptEntry.getdObject("arena");
        List<dPlayer> add = (List<dPlayer>) scriptEntry.getObject("add");
        List<dPlayer> remove = (List<dPlayer>) scriptEntry.getObject("remove");
        List<dPlayer> spectate = (List<dPlayer>) scriptEntry.getObject("spectate");

        Debug.report(scriptEntry, getName(), arena.debug()
                + ArgumentHelper.debugList("Add Players", add)
                + ArgumentHelper.debugList("Remove Players", remove)
                + ArgumentHelper.debugList("Spectate Players", spectate));

        Arena mobArena = arena.getArena();

        if (add != null && !add.isEmpty()) {
            for (dPlayer p : add) {
                if (mobArena.canJoin(p.getPlayerEntity())) {
                    mobArena.playerJoin(p.getPlayerEntity(), p.getLocation());
                }
            }
        }
        if (remove != null && !remove.isEmpty()) {
            for (dPlayer p : remove) {
                if (mobArena.getAllPlayers().contains(p.getPlayerEntity())) {
                    mobArena.playerLeave(p.getPlayerEntity());
                }
            }
        }
        if (spectate != null && !spectate.isEmpty()) {
            for (dPlayer p : spectate) {
                if (mobArena.canSpec(p.getPlayerEntity())) {
                    mobArena.playerSpec(p.getPlayerEntity(), p.getLocation());
                }
            }
        }
    }
}
