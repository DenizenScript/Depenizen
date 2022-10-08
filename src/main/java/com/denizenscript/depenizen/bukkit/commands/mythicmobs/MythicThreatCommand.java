package com.denizenscript.depenizen.bukkit.commands.mythicmobs;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.denizenscript.denizencore.scripts.commands.generator.ArgLinear;
import com.denizenscript.denizencore.scripts.commands.generator.ArgName;
import com.denizenscript.denizencore.scripts.commands.generator.ArgPrefixed;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.depenizen.bukkit.objects.mythicmobs.MythicMobsMobTag;
import io.lumine.mythic.bukkit.BukkitAdapter;

public class MythicThreatCommand  extends AbstractCommand {

    public MythicThreatCommand() {
        setName("mythicthreat");
        setSyntax("mythicthreat [<mythicmob>] [add/subtract/set] [<#.#>] (for:<entity>|...)");
        setRequiredArguments(3, 4);
        autoCompile();
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

    public static void autoExecute(ScriptEntry scriptEntry,
                                   @ArgLinear @ArgName("mythicmob") MythicMobsMobTag mythicmob,
                                   @ArgLinear @ArgName("operation") Operation operation,
                                   @ArgLinear @ArgName("threat") ElementTag threat,
                                   @ArgPrefixed @ArgName("for") ListTag targets) {
        if (!mythicmob.getMob().hasThreatTable()) {
            Debug.echoError("MythicMob does not have a threat table: " + mythicmob);
            return;
        }
        switch (operation) {
            case ADD:
                for (EntityTag target : targets.filter(EntityTag.class, scriptEntry)) {
                    mythicmob.getMob().getThreatTable().threatGain(BukkitAdapter.adapt(target.getBukkitEntity()), threat.asDouble());
                }
                break;
            case SUBTRACT:
                for (EntityTag target : targets.filter(EntityTag.class, scriptEntry)) {
                    mythicmob.getMob().getThreatTable().threatLoss(BukkitAdapter.adapt(target.getBukkitEntity()), threat.asDouble());
                }
                break;
            case SET:
                for (EntityTag target : targets.filter(EntityTag.class, scriptEntry)) {
                    mythicmob.getMob().getThreatTable().threatSet(BukkitAdapter.adapt(target.getBukkitEntity()), threat.asDouble());
                }
                break;
        }
    }
}
