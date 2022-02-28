package com.denizenscript.depenizen.bukkit.bungee.packets.in;

import com.denizenscript.denizen.tags.BukkitTagContext;
import com.denizenscript.denizencore.scripts.containers.core.TaskScriptContainer;
import com.denizenscript.denizencore.tags.TagContext;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.denizenscript.denizencore.utilities.ScriptUtilities;
import com.denizenscript.depenizen.bukkit.Depenizen;
import com.denizenscript.depenizen.bukkit.bungee.BungeeBridge;
import com.denizenscript.depenizen.bukkit.bungee.PacketIn;
import io.netty.buffer.ByteBuf;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.debugging.Debug;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.core.ScriptTag;
import org.bukkit.Bukkit;

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
        String scriptName = readString(data, "scriptName");
        String defs = readString(data, "defs");
        if (scriptName == null || defs == null) {
            return;
        }
        long uuidMost = data.readLong();
        long uuidLeast = data.readLong();
        Bukkit.getScheduler().scheduleSyncDelayedTask(Depenizen.instance, () -> {
            PlayerTag linkedPlayer = null;
            if (uuidMost != 0 || uuidLeast != 0) {
                UUID uuid = new UUID(uuidMost, uuidLeast);
                linkedPlayer = new PlayerTag(Bukkit.getOfflinePlayer(uuid));
            }
            String scrName = scriptName;
            String path = null;
            int dotIndex = scrName.indexOf('.');
            if (dotIndex != -1) {
                path = scrName.substring(dotIndex + 1);
                scrName = scrName.substring(0, dotIndex);
            }
            ScriptTag script = ScriptTag.valueOf(scrName, CoreUtilities.basicContext);
            if (script == null) {
                Debug.echoError("Invalid Depenizen bungeerun script '" + scrName + "': script does not exist.");
                return;
            }
            if (!(script.getContainer() instanceof TaskScriptContainer)) {
                Debug.echoError("Invalid Depenizen bungeerun script '" + scrName + "': script is not a 'task' script.");
                return;
            }
            TagContext context = new BukkitTagContext(linkedPlayer, null, script);
            ScriptUtilities.createAndStartQueue(script.getContainer(), path, new BukkitScriptEntryData(linkedPlayer, null),
                    null, null, null, "BUNGEERUN_" + scrName, ListTag.valueOf(defs, context), script.getContainer());
        });
    }
}
