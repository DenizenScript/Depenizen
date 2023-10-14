package com.denizenscript.depenizen.bukkit.commands.mythicmobs;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.utilities.Utilities;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.depenizen.bukkit.objects.mythicmobs.MythicMobsMobTag;
import io.lumine.mythic.bukkit.BukkitAdapter;

import java.util.Collections;
import java.util.List;

public class MythicThreatCommand  extends AbstractCommand {

    public MythicThreatCommand() {
        setName("mythicthreat");
        setSyntax("mythicthreat [<mythicmob>] [add/subtract/set] [<#.#>] (for:<entity>|...)");
        setRequiredArguments(3, 4);
    }

    // <--[command]
    // @Name MythicThreat
    // @Syntax mythicthreat [<mythicmob>] [add/subtract/set] [<#.#>] (for:<entity>|...)
    // @Group Depenizen
    // @Plugin Depenizen, MythicMobs
    // @Required 3
    // @Maximum 4
    // @Short Modifies the threat table of a Mythic Mob.
    //
    // @Tags
    // None
    //
    // @Description
    // This allows you to modify a threat table for an active MythicMob.
    // Optionally, set a list of entities to apply the threat modification to.
    //
    // @Usage
    // Used to add 50 threat to the attached player on the target mob's threat table.
    // - mythicthreat <player.target.mythicmob> add 50
    //
    // -->

    private enum Operation {ADD, SUBTRACT, SET}

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {
        for (Argument arg : scriptEntry) {
            if (!scriptEntry.hasObject("mythicmob")
                    && arg.matchesArgumentType(MythicMobsMobTag.class)) {
                scriptEntry.addObject("mythicmob", arg.asType(MythicMobsMobTag.class));
            }
            else if (!scriptEntry.hasObject("operation")
                    && arg.matchesEnum(Operation.class)) {
                scriptEntry.addObject("operation", arg.asElement());
            }
            else if (!scriptEntry.hasObject("threat")
                    && arg.matchesFloat()) {
                scriptEntry.addObject("threat", arg.asElement());
            }
            else if (!scriptEntry.hasObject("targets")
                    && arg.matchesPrefix("for")
                    && arg.matchesArgumentList(EntityTag.class)) {
                scriptEntry.addObject("targets", arg.asType(ListTag.class).filter(EntityTag.class, scriptEntry));
            }
            else {
                arg.reportUnhandled();
            }
        }
        if (!scriptEntry.hasObject("mythicmob")) {
            throw new InvalidArgumentsException("Must specify a MythicMob!");
        }
        if (!scriptEntry.hasObject("operation")) {
            throw new InvalidArgumentsException("Must specify an Operation!");
        }
        if (!scriptEntry.hasObject("threat")) {
            throw new InvalidArgumentsException("Must specify a threat value!");
        }
        if (!scriptEntry.hasObject("targets")) {
            scriptEntry.defaultObject("targets", (Utilities.entryHasPlayer(scriptEntry) ? Collections.singletonList(Utilities.getEntryPlayer(scriptEntry).getDenizenEntity()) : null));
        }
    }

    @Override
    public void execute(ScriptEntry scriptEntry) {
        MythicMobsMobTag mythicmob = scriptEntry.getObjectTag("mythicmob");
        ElementTag operation = scriptEntry.getObjectTag("operation");
        ElementTag threat = scriptEntry.getElement("threat");
        List<EntityTag> targets = (List<EntityTag>) scriptEntry.getObject("targets");
        if (scriptEntry.dbCallShouldDebug()) {
            Debug.report(scriptEntry, getName(), mythicmob, operation, threat, db("targets", targets));
        }
        if (!mythicmob.getMob().hasThreatTable()) {
            Debug.echoError("MythicMob does not have a threat table: " + mythicmob);
            return;
        }
        Operation op = Operation.valueOf(operation.toString().toUpperCase());
        switch (op) {
            case ADD:
                for (EntityTag target : targets) {
                    mythicmob.getMob().getThreatTable().threatGain(BukkitAdapter.adapt(target.getBukkitEntity()), threat.asDouble());
                }
                break;
            case SUBTRACT:
                for (EntityTag target : targets) {
                    mythicmob.getMob().getThreatTable().threatLoss(BukkitAdapter.adapt(target.getBukkitEntity()), threat.asDouble());
                }
                break;
            case SET:
                for (EntityTag target : targets) {
                    mythicmob.getMob().getThreatTable().threatSet(BukkitAdapter.adapt(target.getBukkitEntity()), threat.asDouble());
                }
                break;
        }
    }
}
