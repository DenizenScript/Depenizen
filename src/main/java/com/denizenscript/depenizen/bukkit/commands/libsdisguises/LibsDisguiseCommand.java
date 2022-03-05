package com.denizenscript.depenizen.bukkit.commands.libsdisguises;

import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizencore.objects.Argument;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.*;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.utilities.Utilities;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.denizenscript.denizencore.utilities.debugging.Debug;

public class LibsDisguiseCommand extends AbstractCommand {

    public LibsDisguiseCommand() {
        setName("libsdisguise");
        setSyntax("libsdisguise [remove/player/mob/misc] (type:<entity type>) (target:<entity>) (name:<text>) (baby) (id:<item>) (self) (hide_name)");
        setRequiredArguments(1, 8);
        setBooleansHandled("self", "baby", "hide_name");
    }

    // <--[command]
    // @Name libsdisguise
    // @Syntax libsdisguise [remove/player/mob/misc] (type:<entity type>) (target:<entity>) (name:<text>) (baby) (id:<item>) (self) (hide_name)
    // @Group Depenizen
    // @Plugin Depenizen, LibsDisguises
    // @Required 1
    // @Maximum 8
    // @Short Disguises an entity as a different entity.
    //
    // @Description
    // Disguises an entity using Lib's Disguises.
    // This hides the true entity and replaces it with a fake entity as a disguise.
    // The entity mimics the same actions and movement as the entity in a disguise.
    //
    // The required argument depends on the first argument:
    // For 'mob':
    //     Specify an entity type.
    //     Optionally, specify if the mob is a baby or not (defaults to false).
    // For 'player':
    //     Specify the player name.
    // For 'misc':
    //     Specify a misc disguise type.
    //     Optionally, specify id as an ItemTag.
    //
    // Removing a disguise shows the true entity again for all players.
    // Only one disguise can be set per target, if another one is set, the previous disguise is removed.
    //
    // Optionally specify 'self' to hide the disguise from the target player, so they still see their normal player.
    //
    // Optionally specify 'baby' to make the disguised mob a baby (where applicable).
    //
    // Optionally specifiy 'hide_name' to hide the nameplate of a player disguise.
    //
    // @Tags
    // <EntityTag.libsdisguise_is_disguised>
    // <EntityTag.libsdisguise_disguise>
    //
    // @Usage
    // Use to disguise the linked player as a different player named Bob.
    // - libsdisguise player name:Bob
    //
    // @Usage
    // Use to disguise the linked player as a baby Zombie, which can only be seen by other players.
    // - libsdisguise mob type:ZOMBIE baby:true self:true
    //
    // @Usage
    // Use to disguise the linked player as a Boat.
    // - libsdisguise misc type:Boat
    //
    // @Usage
    // Use to disguise the linked player as a Sponge Block.
    // - libsdisguise misc type:Falling_Block id:sponge
    //
    // @Usage
    // Use to remove the disguise from the linked player.
    // - libsdisguise remove
    //
    // @Usage
    // Use to disguise an entity as a player named Bob.
    // - libsdisguise player target:<player.target> name:Bob
    //
    // @Usage
    // Use to remove a disguise from an entity.
    // - libsdisguise remove target:<player.target>
    //
    // -->

    private enum Action {REMOVE, MOB, PLAYER, MISC}

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {
        for (Argument arg : scriptEntry) {
            if (!scriptEntry.hasObject("target")
                    && arg.matchesPrefix("target")) {
                scriptEntry.addObject("target", arg.asType(EntityTag.class));
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
            else if (!scriptEntry.hasObject("action")
                    && arg.matchesEnum(Action.class)) {
                scriptEntry.addObject("action", arg.asElement());
            }
            else {
                arg.reportUnhandled();
            }
        }
        if (!scriptEntry.hasObject("action")) {
            throw new InvalidArgumentsException("Action not specified! (remove/mob/player/misc)");
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
        EntityTag target = scriptEntry.getObjectTag("target");
        ElementTag type = scriptEntry.getObjectTag("type");
        ElementTag name = scriptEntry.getObjectTag("name");
        ElementTag actionElement = scriptEntry.getObjectTag("action");
        ElementTag id = scriptEntry.getObjectTag("id");
        boolean baby = scriptEntry.argAsBoolean("baby");
        boolean self = scriptEntry.argAsBoolean("self");
        boolean hideName = scriptEntry.argAsBoolean("hide_name");
        if (scriptEntry.dbCallShouldDebug()) {
            Debug.report(scriptEntry, getName(), actionElement, target, type, name, id, db("baby", baby), db("self", self), db("hide_name", hideName));
        }
        if (target == null) {
            Debug.echoError(scriptEntry, "Target not found!");
            return;
        }
        switch (actionElement.asEnum(Action.class)) {
            case REMOVE: {
                DisguiseAPI.undisguiseToAll(target.getBukkitEntity());
                break;
            }
            case MOB: {
                if (type == null) {
                    Debug.echoError(scriptEntry, "Entity not specified!");
                    return;
                }
                MobDisguise mobDisguise = new MobDisguise(DisguiseType.valueOf(type.toString().toUpperCase()), !baby);
                FlagWatcher watcher = mobDisguise.getWatcher();
                if (name != null) {
                    watcher.setCustomNameVisible(true);
                    watcher.setCustomName(name.toString());
                }
                if (target.isPlayer() && self) {
                    DisguiseAPI.disguiseIgnorePlayers(target.getBukkitEntity(), mobDisguise, target.getPlayer());
                }
                else {
                    DisguiseAPI.disguiseToAll(target.getBukkitEntity(), mobDisguise);
                }
                break;
            }
            case PLAYER: {
                if (name == null) {
                    Debug.echoError(scriptEntry, "Name not specified!");
                    return;
                }
                PlayerDisguise playerDisguise = new PlayerDisguise(name.toString());
                playerDisguise.setNameVisible(!hideName);
                if (target.isPlayer() && self) {
                    DisguiseAPI.disguiseIgnorePlayers(target.getBukkitEntity(), playerDisguise, target.getPlayer());
                }
                else {
                    DisguiseAPI.disguiseToAll(target.getBukkitEntity(), playerDisguise);
                }
                break;
            }
            case MISC: {
                if (type == null) {
                    Debug.echoError(scriptEntry, "Entity not specified!");
                    return;
                }
                DisguiseType disType = DisguiseType.valueOf(type.toString().toUpperCase());
                MiscDisguise miscDisguise;
                if (disType == DisguiseType.FALLING_BLOCK || disType == DisguiseType.DROPPED_ITEM) {
                    if (id == null) {
                        Debug.echoError(scriptEntry, "ID not specified!");
                        return;
                    }
                    miscDisguise = new MiscDisguise(disType, ItemTag.valueOf(id.asString(), scriptEntry.context).getItemStack());
                }
                else {
                    miscDisguise = new MiscDisguise(disType);
                }
                FlagWatcher watcher = miscDisguise.getWatcher();
                if (name != null) {
                    watcher.setCustomNameVisible(true);
                    watcher.setCustomName(name.toString());
                }
                if (target.isPlayer() && self) {
                    DisguiseAPI.disguiseIgnorePlayers(target.getBukkitEntity(), miscDisguise, target.getPlayer());
                }
                else {
                    DisguiseAPI.disguiseToAll(target.getBukkitEntity(), miscDisguise);
                }
                break;
            }
        }
    }
}
