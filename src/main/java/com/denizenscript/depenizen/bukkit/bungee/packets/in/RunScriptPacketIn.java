package com.denizenscript.depenizen.bukkit.bungee.packets.in;

import com.denizenscript.depenizen.bukkit.Depenizen;
import com.denizenscript.depenizen.bukkit.bungee.BungeeBridge;
import com.denizenscript.depenizen.bukkit.bungee.PacketIn;
import io.netty.buffer.ByteBuf;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.objects.dScript;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.queues.ScriptQueue;
import net.aufdemrand.denizencore.scripts.queues.core.InstantQueue;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.List;
import java.util.UUID;

public class RunScriptPacketIn extends PacketIn {

    @Override
    public String getName() {
        return "RunScript";
    }

    @Override
    public void process(ByteBuf data) {
        if (data.readableBytes() < 8 + 8 + 4 + 4) {
            BungeeBridge.instance.handler.fail("Invalid RunScriptPacket (bytes available: " + data.readableBytes() + ")");
            return;
        }
        int nameLength = data.readInt();
        if (data.readableBytes() < nameLength || nameLength < 0) {
            BungeeBridge.instance.handler.fail("Invalid RunScriptPacket (address bytes requested: " + nameLength + ")");
            return;
        }
        String scriptName = readString(data, nameLength);
        int defsLength = data.readInt();
        if (data.readableBytes() < defsLength || defsLength < 0) {
            BungeeBridge.instance.handler.fail("Invalid RunScriptPacket (address bytes requested: " + defsLength + ")");
            return;
        }
        String defs = readString(data, defsLength);
        long uuidMost = data.readLong();
        long uuidLeast = data.readLong();
        Bukkit.getScheduler().scheduleSyncDelayedTask(Depenizen.instance, new Runnable() {
            @Override
            public void run() {
                dPlayer linkedPlayer = null;
                if (uuidMost != 0 || uuidLeast != 0) {
                    UUID uuid = new UUID(uuidMost, uuidLeast);
                    try {
                        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
                        if (player != null) {
                            linkedPlayer = new dPlayer(player);
                        }
                    }
                    catch (Exception ex) {
                        // Ignore
                    }
                }
                dScript script = dScript.valueOf(scriptName);
                List<ScriptEntry> entries = script.getContainer().getBaseEntries(new BukkitScriptEntryData(linkedPlayer, null));
                if (entries.size() == 0) {
                    return;
                }
                ScriptQueue queue = new InstantQueue("BUNGEERUN_" + scriptName).addEntries(entries);
                int x = 1;
                dList definitions = dList.valueOf(defs);
                String[] definition_names = null;
                try {
                    if (script != null && script.getContainer() != null) {
                        String str = script.getContainer().getString("definitions");
                        if (str != null) {
                            definition_names = str.split("\\|");
                        }
                    }
                }
                catch (Exception e) {
                    // Ignored
                }
                for (String definition : definitions) {
                    String name = definition_names != null && definition_names.length >= x ?
                            definition_names[x - 1].trim() : String.valueOf(x);
                    queue.addDefinition(name, definition);
                    dB.echoDebug(entries.get(0), "Adding definition '" + name + "' as " + definition);
                    x++;
                }
                queue.addDefinition("raw_context", defs);
                queue.start();
            }
        });
    }
}
