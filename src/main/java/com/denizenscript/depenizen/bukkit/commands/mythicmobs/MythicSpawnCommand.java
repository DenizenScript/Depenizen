package com.denizenscript.depenizen.bukkit.commands.mythicmobs;

import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.denizenscript.denizencore.scripts.commands.generator.ArgDefaultText;
import com.denizenscript.denizencore.scripts.commands.generator.ArgLinear;
import com.denizenscript.denizencore.scripts.commands.generator.ArgName;
import com.denizenscript.denizencore.scripts.commands.generator.ArgPrefixed;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.depenizen.bukkit.bridges.MythicMobsBridge;
import com.denizenscript.depenizen.bukkit.objects.mythicmobs.MythicMobsMobTag;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.api.mobs.entities.SpawnReason;
import io.lumine.mythic.bukkit.BukkitAdapter;
import org.bukkit.entity.Entity;

import java.util.Set;

public class MythicSpawnCommand extends AbstractCommand {

    public MythicSpawnCommand() {
        setName("mythicspawn");
        setSyntax("mythicspawn [<name>] [<location>] (level:<#>) (reason:<#>)");
        setRequiredArguments(2, 4);
        autoCompile();
    }

    // <--[command]
    // @Name MythicSpawn
    // @Syntax mythicspawn [<name>] [<location>] (level:<#>) (reason:<reason>)
    // @Group Depenizen
    // @Plugin Depenizen, MythicMobs
    // @Required 2
    // @Maximum 4
    // @Short Spawns a MythicMob at a location.
    //
    // @Description
    // This allows you to spawn a MythicMob at a location using the mob's internal name.
    // (Requires Paper) Additionally, you may specify a spawn reason for this MythicMob, which can be any of these: <@link url https://www.mythicmobs.net/javadocs/io/lumine/xikage/mythicmobs/mobs/entities/SpawnReason.html>
    //
    // @Tags
    // <entry[saveName].spawned_mythicmob> returns the spawned MythicMobsMob.
    //
    // @Usage
    // Use to spawn a BarbarianMinion at a player's location.
    // - mythicspawn BarbarianMinion <player.location>
    //
    // -->

    @Override
    public void addCustomTabCompletions(TabCompletionsBuilder tab) {
        tab.add((Set<String>) MythicMobsBridge.getMobManager().getMobNames());
    }

    public static void autoExecute(ScriptEntry scriptEntry,
                                   @ArgLinear @ArgName("name") ElementTag name,
                                   @ArgLinear @ArgName("location") LocationTag location,
                                   @ArgPrefixed @ArgName("level") @ArgDefaultText("1") ElementTag level,
                                   @ArgPrefixed @ArgName("reason") @ArgDefaultText("command") SpawnReason reason) {
        try {
            MythicMob mob = MythicMobsBridge.getMythicMob(name.asString());
            if (mob == null) {
                Debug.echoError("MythicMob does not exist: " + name.asString());
                return;
            }
            Entity entity = mob.spawn(BukkitAdapter.adapt(location), level.asDouble(), reason).getEntity().getBukkitEntity();
            scriptEntry.addObject("spawned_mythicmob", new MythicMobsMobTag(MythicMobsBridge.getActiveMob(entity)));
        }
        catch (Exception e) {
            Debug.echoError(e);
        }
    }
}
