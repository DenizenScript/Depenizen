package com.denizenscript.depenizen.bukkit.bungee.packets.in;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.scripts.ScriptBuilder;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.queues.ScriptQueue;
import com.denizenscript.denizencore.scripts.queues.core.InstantQueue;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.depenizen.bukkit.Depenizen;
import com.denizenscript.depenizen.bukkit.bungee.BungeeBridge;
import com.denizenscript.depenizen.bukkit.bungee.BungeePacketIn;
import io.netty.buffer.ByteBuf;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.*;

public class RunCommandsPacketIn extends BungeePacketIn {

    @Override
    public String getName() {
        return "RunCommands";
    }

    @Override
    public void process(ByteBuf data) {
        if (data.readableBytes() < 12) {
            BungeeBridge.instance.handler.fail("Invalid RunCommandsPacket (bytes available: " + data.readableBytes() + ")");
            return;
        }
        String commands = readString(data, "commands");
        String defs = readString(data, "defs");
        if (commands == null || defs == null) {
            return;
        }
        boolean shouldDebug = data.readByte() != 0;
        long uuidMost = data.readLong();
        long uuidLeast = data.readLong();
        Bukkit.getScheduler().scheduleSyncDelayedTask(Depenizen.instance, () -> {
            PlayerTag linkedPlayer = null;
            if (uuidMost != 0 || uuidLeast != 0) {
                UUID uuid = new UUID(uuidMost, uuidLeast);
                try {
                    OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
                    if (player != null) {
                        linkedPlayer = new PlayerTag(player);
                    }
                }
                catch (Exception ex) {
                    // Ignore
                }
            }
            List<String> commandsSeparated = CoreUtilities.split(commands, '\n');
            List<Object> rawEntries = new ArrayList<>();
            for (String cmd : commandsSeparated) {
                if (cmd.length() > 0) {
                    rawEntries.add(parseCommandsBack(unescape(cmd)));
                }
            }
            List<ScriptEntry> entries = ScriptBuilder.buildScriptEntries(rawEntries, null, new BukkitScriptEntryData(linkedPlayer, null));
            if (entries.isEmpty()) {
                return;
            }
            if (!shouldDebug) {
                for (ScriptEntry entry : entries) {
                    entry.internal.shouldDebugBool = false;
                }
            }
            ScriptQueue queue = new InstantQueue("BUNGEE_");
            queue.addEntries(entries);
            List<String> defSets = CoreUtilities.split(defs, '\r');
            List<String> defNames = CoreUtilities.split(defSets.get(0), '\n');
            List<String> defValues = CoreUtilities.split(defSets.get(1), '\n');
            for (int i = 0; i < defNames.size(); i++) {
                String name = unescape(defNames.get(i));
                if (name.length() > 0) {
                    String value = unescape(defValues.get(i));
                    queue.addDefinition(name, value);
                    Debug.echoDebug(entries.get(0), "Adding definition '" + name + "' as " + value);
                }
            }
            queue.start();
        });
    }

    public static String unescape(String text) {
        return text.replace("\\n", "\n").replace("\\r", "\r").replace("\\\\", "\\");
    }

    public static Object parseCommandsBack(String value) {
        List<String> cmds = CoreUtilities.split(value, '\r');
        if (cmds.size() == 1) {
            return cmds.get(0);
        }
        List<Object> resultSubList = new ArrayList<>();
        for (int i = 1; i < cmds.size(); i++) {
            if (cmds.get(i).length() > 0) {
                resultSubList.add(parseCommandsBack(unescape(cmds.get(i))));
            }
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(cmds.get(0), resultSubList);
        return resultMap;
    }
}
