package com.denizenscript.depenizen.bukkit.bungee.packets.in;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.tags.BukkitTagContext;
import com.denizenscript.denizencore.tags.TagContext;
import com.denizenscript.denizencore.tags.TagManager;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.denizenscript.depenizen.bukkit.Depenizen;
import com.denizenscript.depenizen.bukkit.bungee.BungeeBridge;
import com.denizenscript.depenizen.bukkit.bungee.BungeePacketIn;
import com.denizenscript.depenizen.bukkit.bungee.packets.out.RedirectPacketOut;
import com.denizenscript.depenizen.bukkit.bungee.packets.out.redirectable.TagResponsePacketOut;
import io.netty.buffer.ByteBuf;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.List;
import java.util.UUID;

public class ReadTagPacketIn extends BungeePacketIn {

    @Override
    public String getName() {
        return "ReadTag";
    }

    @Override
    public void process(ByteBuf data) {
        if (data.readableBytes() < 12 + 16 + 4) {
            BungeeBridge.instance.handler.fail("Invalid ReadTagPacket (bytes available: " + data.readableBytes() + ")");
            return;
        }
        String responseServer = readString(data, "serverName");
        String tag = readString(data, "tag");
        String defs = readString(data, "defs");
        if (tag == null || defs == null) {
            return;
        }
        long uuidMost = data.readLong();
        long uuidLeast = data.readLong();
        int responseId = data.readInt();
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
            TagContext context = new BukkitTagContext(linkedPlayer, null, null, false, null);
            List<String> defSets = CoreUtilities.split(defs, '\r');
            List<String> defNames = CoreUtilities.split(defSets.get(0), '\n');
            List<String> defValues = CoreUtilities.split(defSets.get(1), '\n');
            for (int i = 0; i < defNames.size(); i++) {
                String name = RunCommandsPacketIn.unescape(defNames.get(i));
                if (name.length() > 0) {
                    String value = RunCommandsPacketIn.unescape(defValues.get(i));
                    context.definitionProvider.addDefinition(name, value);
                }
            }
            String result = TagManager.tag(tag, context);
            TagResponsePacketOut packet = new TagResponsePacketOut();
            packet.id = responseId;
            packet.result = result;
            RedirectPacketOut redirectPacket = new RedirectPacketOut(responseServer, packet);
            BungeeBridge.instance.sendPacket(redirectPacket);
        });
    }
}
