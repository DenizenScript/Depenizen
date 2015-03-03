package net.gnomeffinway.depenizen.commands;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizencore.exceptions.CommandExecutionException;
import net.aufdemrand.denizencore.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.commands.AbstractCommand;
import net.aufdemrand.denizencore.utilities.debugging.dB;
import net.gnomeffinway.depenizen.Depenizen;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class BungeeCommand extends AbstractCommand {

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {

        for (aH.Argument arg : aH.interpret(scriptEntry.getArguments())) {

            // No arguments

        }

    }

    @Override
    public void execute(ScriptEntry scriptEntry) throws CommandExecutionException {

        dB.report(scriptEntry, getName(), "Sending message");

        ByteArrayDataOutput data = ByteStreams.newDataOutput();
        data.writeUTF("Depenizen");
        data.writeUTF("dummy data");

        ((BukkitScriptEntryData) scriptEntry.entryData).getPlayer().getPlayerEntity()
                .sendPluginMessage(Depenizen.getCurrentInstance(), "BungeeCord", data.toByteArray());

        /*
        for (Entity entity : Bukkit.getServer().getWorld("cooal").getEntities()) {
            if (entity instanceof Player) {
                ((Player) entity).sendPluginMessage(Depenizen.getCurrentInstance(), "BungeeCord", data.toByteArray());
                break;
            }
        }
        */
    }
}
