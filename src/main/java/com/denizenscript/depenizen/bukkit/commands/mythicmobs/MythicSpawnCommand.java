package com.denizenscript.depenizen.bukkit.commands.mythicmobs;

import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.depenizen.bukkit.bridges.MythicMobsBridge;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.depenizen.bukkit.objects.mythicmobs.MythicMobsMobTag;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.BukkitAdapter;
import org.bukkit.entity.Entity;

public class MythicSpawnCommand extends AbstractCommand {

    public MythicSpawnCommand() {
        setName("mythicspawn");
        setSyntax("mythicspawn [<name>] [<location>] (level:<#>) (reason:<reason>)");
        setRequiredArguments(2, 3);
    }

    // <--[command]
    // @Name MythicSpawn
    // @Syntax mythicspawn [<name>] [<location>] (level:<#>) (reason:<reason>)
    // @Group Depenizen
    // @Plugin Depenizen, MythicMobs
    // @Required 2
    // @Maximum 3
    // @Short Spawns a MythicMob at a location.
    //
    // @Description
    // This allows you to spawn a MythicMob at a location using the mob's internal name.
    //
    // Optionally specify 'reason:<reason>' (Paper only) to specify the reason an entity is spawning for the 'entity spawns' event,
    // using any reason from https://hub.spigotmc.org/javadocs/spigot/org/bukkit/event/entity/CreatureSpawnEvent.SpawnReason.html
    //
    // @Tags
    // <entry[saveName].spawned_mythicmob> returns the spawned MythicMobsMob.
    //
    // @Usage
    // Use to spawn a BarbarianMinion at a player's location.
    // - mythicspawn BarbarianMinion <player.location>
    // -->

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {
        for (Argument arg : scriptEntry) {
            if (!scriptEntry.hasObject("location")
                    && arg.matchesArgumentType(LocationTag.class)) {
                scriptEntry.addObject("location", arg.asType(LocationTag.class));
            }
            else if (!scriptEntry.hasObject("level")
                    && arg.matchesPrefix("level", "l")
                    && arg.matchesInteger()) {
                scriptEntry.addObject("level", arg.asElement());
            }
            else if (!scriptEntry.hasObject("reason") && args.matchesEnum()) {
                scriptEntry.addObject("reason", arg.asElement());
            }
            else if (!scriptEntry.hasObject("name")) {
                scriptEntry.addObject("name", arg.asElement());
            }
            else {
                arg.reportUnhandled();
            }
        }
        if (!scriptEntry.hasObject("location") || !scriptEntry.hasObject("name")) {
            throw new InvalidArgumentsException("Must specify a name and location.");
        }
        scriptEntry.defaultObject("level", new ElementTag(1));
    }

    @Override
    public void execute(ScriptEntry scriptEntry) {
        ElementTag name = scriptEntry.getElement("name");
        LocationTag location = scriptEntry.getObjectTag("location");
        ElementTag level = scriptEntry.getElement("level");
        ElementTag reason = scriptEntry.getElement("reason");
        if (scriptEntry.dbCallShouldDebug()) {
            Debug.report(scriptEntry, getName(), name, location, level, reason);
        }
        try {
            MythicMob mob = MythicMobsBridge.getMythicMob(name.asString());
            if (mob == null) {
                Debug.echoError("MythicMob does not exist: " + name.asString());
                return;
            }
            Entity entity = mob.spawn(BukkitAdapter.adapt(location), level.asDouble(), reason.asEnum(SpawnReason.class)).getEntity().getBukkitEntity();
            scriptEntry.addObject("spawned_mythicmob", new MythicMobsMobTag(MythicMobsBridge.getActiveMob(entity)));
        }
        catch (Exception e) {
            Debug.echoError(e);
        }
    }
}
