package com.denizenscript.depenizen.bukkit.commands.libsdisguises;

import com.denizenscript.denizencore.objects.Argument;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.*;
import com.denizenscript.denizen.objects.dEntity;
import com.denizenscript.denizen.utilities.Utilities;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.Element;
import com.denizenscript.denizencore.objects.ArgumentHelper;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.denizenscript.denizencore.utilities.debugging.Debug;

public class DisguiseCommand extends AbstractCommand {
    // <--[command]
    // @Name disguise
    // @Syntax disguise [remove/player/mob/misc] (type:<entity type>) (target:<entity>) (name:<text>) (baby:true/false) (id:<number>) (data:<number>) (self:true/false)
    // @Group Depenizen
    // @Plugin Depenizen, LibsDisguises
    // @Required 2
    // @Short Disguises an entity as a different entity.

    // @Description
    // Disguises an entity using Lib's Disguises.
    // This hides the true entity and replaces it with a fake entity as a disguise.
    // The entity mimics the same actions and movement as the entity in a disguise.
    //
    // The required arguement depends on the first arguement:
    // If the disguise is a mob, a type is required.
    // If the disguise is a misc, a type is required.
    // If the disguise is a player, a name is required instead of a type.
    //
    // Specify if mob is a baby or not, otherwise its default adult.
    // Specify id and/or data for misc disguises. Default is 1 and 0 respectively.
    // Removing a disgsuise shows the true entity again for all players.
    // Only one disguise can be allowed, if another one is set, the preious disguise is removed.
    // Specify self arguement, if it is set to true, the disguise will be hidden from the player who is disguised.
    // Otherwise it is default set to false, showing the disguised character the player is disguising as.

    // @Tags
    // <e@entity.is_disguised>
    // <e@entity.disguise>

    // @Usage
    // Use to disguise the attached player in the queue as a Player.
    // - disguise player name:Bob

    // @Usage
    // Use to disguise the attached player in the queue as a Zombie.
    // - disguise mob type:ZOMBIE baby:true self:true

    // @Usage
    // Use to disguise the attached player in the queue as a Boat.
    // - disguise misc type:Boat

    // @Usage
    // Use to disguise the attached player in the queue as a Sponge Block.
    // - disguise misc type:Falling_Block id:19 data:0

    // @Usage
    // Use to remove the disguise from the attached player in the queue.
    // - disguise remove

    // @Usage
    // Use to diguise a entity.
    // - disguise player target:<player.target> name:Bob

    // @Usage
    // Use to remove a diguise from a entity.
    // - disguise remove target:<player.target>

    // -->

    private enum Action {REMOVE, MOB, PLAYER, MISC}

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {

        for (Argument arg : ArgumentHelper.interpret(scriptEntry.getArguments())) {

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

            else if (!scriptEntry.hasObject("self")
                    && arg.matchesPrefix("self")) {
                scriptEntry.addObject("self", arg.asElement());
            }

            else if (!scriptEntry.hasObject("action")
                    && arg.matchesEnum(Action.values())) {
                scriptEntry.addObject("action", arg.asElement());
            }

            else {
                arg.reportUnhandled();
            }

        }
        if (!scriptEntry.hasObject("action")) {
            throw new InvalidArgumentsException("Action not specified! (remove/mob/player/misc)");
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

        if (!scriptEntry.hasObject("self")) {
            scriptEntry.addObject("self", new Element(false));
        }

        if (!scriptEntry.hasObject("target")) {
            if (Utilities.entryHasPlayer(scriptEntry)) {
                scriptEntry.addObject("target", Utilities.getEntryPlayer(scriptEntry).getDenizenEntity());
            }
            else {
                throw new InvalidArgumentsException("This command does not have a player attached!");
            }
        }

    }

    @Override
    public void execute(ScriptEntry scriptEntry) {

        dEntity target = scriptEntry.getdObject("target");
        Element type = scriptEntry.getdObject("type");
        Element name = scriptEntry.getdObject("name");
        Element action = scriptEntry.getdObject("action");
        Element id = scriptEntry.getdObject("id");
        Element data = scriptEntry.getdObject("data");
        Element baby = scriptEntry.getdObject("baby");
        Element self = scriptEntry.getdObject("self");

        // Report to dB
        Debug.report(scriptEntry, getName(), action.debug()
                + (target != null ? target.debug() : "")
                + (type != null ? type.debug() : "")
                + (name != null ? name.debug() : "")
                + (id != null ? id.debug() : "")
                + (data != null ? data.debug() : "")
                + (baby != null ? baby.debug() : ""));

        if (target == null) {
            Debug.echoError(scriptEntry.getResidingQueue(), "Target not found!");
            return;
        }

        if (baby == null) {
            Debug.echoError(scriptEntry.getResidingQueue(), "Baby not specified!");
            return;
        }

        if (action.asString().equalsIgnoreCase("remove")) {
            DisguiseAPI.undisguiseToAll(target.getBukkitEntity());
        }

        else if (action.asString().equalsIgnoreCase("mob")) {
            if (type == null) {
                Debug.echoError(scriptEntry.getResidingQueue(), "Entity not specified!");
                return;
            }
            MobDisguise mobDisguise = new MobDisguise(DisguiseType.valueOf(type.toString().toUpperCase()), !baby.asBoolean());
            FlagWatcher watcher = mobDisguise.getWatcher();
            if (name != null) {
                watcher.setCustomNameVisible(true);
                watcher.setCustomName(name.toString());
            }
            if (target.isPlayer() && self.asBoolean()) {
                DisguiseAPI.disguiseIgnorePlayers(target.getBukkitEntity(), mobDisguise, target.getPlayer());
            }
            else {
                DisguiseAPI.disguiseToAll(target.getBukkitEntity(), mobDisguise);
            }
        }

        else if (action.asString().equalsIgnoreCase("player")) {
            if (name == null) {
                Debug.echoError(scriptEntry.getResidingQueue(), "Name not specified!");
                return;
            }
            PlayerDisguise playerDisguise = new PlayerDisguise(name.toString());
            if (target.isPlayer() && self.asBoolean()) {
                DisguiseAPI.disguiseIgnorePlayers(target.getBukkitEntity(), playerDisguise, target.getPlayer());
            }
            else {
                DisguiseAPI.disguiseToAll(target.getBukkitEntity(), playerDisguise);
            }
        }

        else if (action.asString().equalsIgnoreCase("misc")) {
            if (type == null) {
                Debug.echoError(scriptEntry.getResidingQueue(), "Entity not specified!");
                return;
            }
            if (id == null) {
                Debug.echoError(scriptEntry.getResidingQueue(), "ID not specified!");
                return;
            }
            if (data == null) {
                Debug.echoError(scriptEntry.getResidingQueue(), "Data not specified!");
                return;
            }
            MiscDisguise miscDisguise = new MiscDisguise(DisguiseType.valueOf(type.toString().toUpperCase()), id.asInt(), data.asInt());
            FlagWatcher watcher = miscDisguise.getWatcher();
            if (name != null) {
                watcher.setCustomNameVisible(true);
                watcher.setCustomName(name.toString());
            }
            if (target.isPlayer() && self.asBoolean()) {
                DisguiseAPI.disguiseIgnorePlayers(target.getBukkitEntity(), miscDisguise, target.getPlayer());
            }
            else {
                DisguiseAPI.disguiseToAll(target.getBukkitEntity(), miscDisguise);
            }
        }
    }
}
