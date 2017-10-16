package com.denizenscript.depenizen.bukkit.commands;

import com.sun.scenario.effect.Effect;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.*;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizencore.exceptions.CommandExecutionException;
import net.aufdemrand.denizencore.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.commands.AbstractCommand;
import net.aufdemrand.denizencore.utilities.debugging.dB;

public class DisguiseCommands extends AbstractCommand {
    // <--[command]
    // @Name disguise
    // @Syntax disguise [REMOVE/PLAYER/MOB/MISC] [target:<entity>] [type:<entity type>] [baby:true/false] [id:<number>] [data:<number>]
    // @Group Depenizen
    // @Plugin DepenizenBukkit, LibsDisguises
    // @Required 2
    // @Stable Stable
    // @Short Disguises an entity as a different entity.
    // @Author Mwthorn

    // @Description
    // Disguises an entity using Lib's Disguises.
    // This hides the true entity and replaces it with a fake entity as a disguise.
    // The entity mimics the same actions and movement as the entity in a disguise.
    // Specify for MOB if baby or adult, otherwise its default adult.
    // Specify id and/or data for MISC disguises. Default is 1 and 0 respectively.
    // Removing a digsuise shows the true entity again for all players.
    // Only one disguise can be allowed, if another one is set, the preious disguise is removed.

    // @Tags <e@entity.is_disguised> <e@entity.disguise>

    // @Usage
    // Use to disguise the attached player in the queue as a Player.
    // - disguise PLAYER name:Bob

    // @Usage
    // Use to disguise the attached player in the queue as a Zombie.
    // - disguise MOB entity:ZOMBIE baby:true

    // @Usage
    // Use to disguise the attached player in the queue as a Boat.
    // - disguise MISC entity:Boat

    // @Usage
    // Use to disguise the attached player in the queue as a Sponge Block.
    // - disguise MISC entity:Falling_Block id:19 data:0

    // @Usage
    // Use to remove the disguise from the attached player in the queue.
    // - disguise REMOVE

    // @Usage
    // Use to diguise a entity.
    // - disguise PLAYER target:<player.target> name:Bob

    // @Usage
    // Use to remove a diguise from a entity.
    // - disguise REMOVE target:<player.target>

    // -->

    private enum Action {REMOVE, MOB, PLAYER, MISC}

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {

        for (aH.Argument arg : aH.interpret(scriptEntry.getArguments())) {


            if (!scriptEntry.hasObject("target")
                    && arg.matchesPrefix("target")) {
                scriptEntry.addObject("target", arg.asType(dEntity.class));
            }

            else if (!scriptEntry.hasObject("name")
                    && arg.matchesPrefix("name")) {
                scriptEntry.addObject("name", arg.asElement());
            }

            else if (!scriptEntry.hasObject("type")
                    && arg.matchesPrefix("type")) {
                scriptEntry.addObject("type", arg.asElement());
            }

            else if (!scriptEntry.hasObject("id")
                    && arg.matchesPrefix("id")) {
                scriptEntry.addObject("id", arg.asElement());
            }

            else if (!scriptEntry.hasObject("data")
                    && arg.matchesPrefix("data")) {
                scriptEntry.addObject("data", arg.asElement());
            }

            else if (!scriptEntry.hasObject("baby")
                    && arg.matchesPrefix("baby")) {
                scriptEntry.addObject("baby", arg.asElement());
            }

            else if (!scriptEntry.hasObject("action")) {
                scriptEntry.addObject("action", arg.asElement());
            }

            else {
                arg.reportUnhandled();
            }

        }
        if (!scriptEntry.hasObject("action")) {
            throw new InvalidArgumentsException("Action not specified! (ADD/REMOVE)");
        }

        if (!scriptEntry.hasObject("baby")) {
            scriptEntry.addObject("baby", new Element(false));
        }

        if (!scriptEntry.hasObject("id")) {
            scriptEntry.addObject("id", new Element(1));
        }

        if (!scriptEntry.hasObject("data")) {
            scriptEntry.addObject("data", new Element(0));
        }

        if (!scriptEntry.hasObject("target")) {
            if (((BukkitScriptEntryData) scriptEntry.entryData).hasPlayer()) {
                scriptEntry.addObject("target", ((BukkitScriptEntryData) scriptEntry.entryData).getPlayer().getDenizenEntity());
            } else {
                throw new InvalidArgumentsException("This command does not have a player attached!");
            }
        }

    }

    @Override
    public void execute(ScriptEntry scriptEntry) throws CommandExecutionException {

        dEntity target = scriptEntry.getdObject("target");
        Element type = scriptEntry.getdObject("type");
        Element name = scriptEntry.getdObject("name");
        Element action = scriptEntry.getdObject("action");
        Element id = scriptEntry.getdObject("id");
        Element data = scriptEntry.getdObject("data");
        Element baby = scriptEntry.getdObject("baby");

        if (action == null) {
            dB.echoError(scriptEntry.getResidingQueue(), "Type not specified! (REMOVE/MOB/PLAYER/MISC)");
            return;
        }

        if (action.asString().equalsIgnoreCase("REMOVE")) {
            DisguiseAPI.undisguiseToAll(target.getBukkitEntity());
        }

        else if (action.asString().equalsIgnoreCase("MOB")) {
            if (type == null) {
                dB.echoError(scriptEntry.getResidingQueue(), "Entity not specified!");
                return;
            }
            MobDisguise mobDisguise = new MobDisguise(DisguiseType.valueOf(type.toString().toUpperCase()),!baby.asBoolean());
            FlagWatcher watcher = mobDisguise.getWatcher();
            if (name != null) {
                watcher.setCustomNameVisible(true);
                watcher.setCustomName(name.toString());
            }
            DisguiseAPI.disguiseToAll(target.getBukkitEntity(),mobDisguise);
        }

        else if (action.asString().equalsIgnoreCase("PLAYER")) {
            if (name == null) {
                dB.echoError(scriptEntry.getResidingQueue(), "Name not specified!");
                return;
            }
            PlayerDisguise playerDisguise = new PlayerDisguise(name.toString());
            DisguiseAPI.disguiseToAll(target.getBukkitEntity(),playerDisguise);
        }

        else if (action.asString().equalsIgnoreCase("MISC")) {
            if (type == null) {
                dB.echoError(scriptEntry.getResidingQueue(), "Entity not specified!");
                return;
            }
            MiscDisguise miscDisguise = new MiscDisguise(DisguiseType.valueOf(type.toString().toUpperCase()),id.asInt(),data.asInt());
            FlagWatcher watcher = miscDisguise.getWatcher();
            if (name != null) {
                watcher.setCustomNameVisible(true);
                watcher.setCustomName(name.toString());
            }
            DisguiseAPI.disguiseToAll(target.getBukkitEntity(),miscDisguise);
        }
    }
}
