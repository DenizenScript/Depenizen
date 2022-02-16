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
        setSyntax("libsdisguise [remove/player/mob/misc] (type:<entity type>) (target:<entity>) (name:<text>) (baby:true/{false}) (id:<item>) (self:true/{false})");
        setRequiredArguments(1, 6);
    }

    // <--[command]
    // @Name libsdisguise
    // @Syntax libsdisguise [remove/player/mob/misc] (type:<entity type>) (target:<entity>) (name:<text>) (baby:true/{false}) (id:<item>) (self:true/{false})
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
    // Optionally specify 'self:true' to hide the disguise from the target player, so they still see their normal player (defaults to false).
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
            scriptEntry.addObject("baby", new ElementTag(false));
        }
        if (!scriptEntry.hasObject("self")) {
            scriptEntry.addObject("self", new ElementTag(false));
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
        ElementTag action = scriptEntry.getObjectTag("action");
        ElementTag id = scriptEntry.getObjectTag("id");
        ElementTag baby = scriptEntry.getObjectTag("baby");
        ElementTag self = scriptEntry.getObjectTag("self");
        if (scriptEntry.dbCallShouldDebug()) {
            Debug.report(scriptEntry, getName(), action, target, type, name, id, baby);
        }
        if (target == null) {
            Debug.echoError(scriptEntry, "Target not found!");
            return;
        }
        if (baby == null) {
            Debug.echoError(scriptEntry, "Baby not specified!");
            return;
        }
        if (action.asString().equalsIgnoreCase("remove")) {
            DisguiseAPI.undisguiseToAll(target.getBukkitEntity());
        }
        else if (action.asString().equalsIgnoreCase("mob")) {
            if (type == null) {
                Debug.echoError(scriptEntry, "Entity not specified!");
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
                Debug.echoError(scriptEntry, "Name not specified!");
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
            if (target.isPlayer() && self.asBoolean()) {
                DisguiseAPI.disguiseIgnorePlayers(target.getBukkitEntity(), miscDisguise, target.getPlayer());
            }
            else {
                DisguiseAPI.disguiseToAll(target.getBukkitEntity(), miscDisguise);
            }
        }
    }
}
