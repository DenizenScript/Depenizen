package com.denizenscript.depenizen.sponge.support.clientizen;

import com.denizenscript.denizen2core.Denizen2Core;
import com.denizenscript.denizen2core.utilities.CoreUtilities;
import com.denizenscript.denizen2sponge.Denizen2Sponge;
import com.denizenscript.denizen2sponge.spongeevents.Denizen2SpongeReloadEvent;
import com.denizenscript.depenizen.common.socket.DataDeserializer;
import com.denizenscript.depenizen.common.socket.DataSerializer;
import com.denizenscript.depenizen.sponge.Depenizen2Sponge;
import com.denizenscript.depenizen.sponge.commands.clientizen.ClientRunCommand;
import org.spongepowered.api.Platform;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.network.ChannelBinding;
import org.spongepowered.api.network.ChannelBuf;
import org.spongepowered.api.network.PlayerConnection;
import org.spongepowered.api.network.RawDataListener;
import org.spongepowered.api.network.RemoteConnection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

public class ClientizenSupport implements RawDataListener {

    private static final Map<String, String> clientScripts = new HashMap<>();
    private static final List<UUID> playersWithMod = new ArrayList<>();

    public static final File clientScriptsFolder;

    private static ChannelBinding.RawDataChannel channel;

    static {
        clientScriptsFolder = new File(Depenizen2Sponge.instance.configDir.toFile(), "client_scripts");
        clientScriptsFolder.mkdirs();
    }

    public ClientizenSupport() {
        Denizen2Core.register(new ClientRunCommand());
        Sponge.getEventManager().registerListeners(Denizen2Sponge.instance, this);
        channel = Sponge.getChannelRegistrar().createRawChannel(Depenizen2Sponge.plugin, "Clientizen");
        channel.addListener(this);
        reloadClientScripts();
    }

    private static void reloadClientScripts() {
        clientScripts.clear();
        try {
            Stream<Path> paths = Files.walk(Denizen2Core.getImplementation().getScriptsFolder().toPath(), FileVisitOption.FOLLOW_LINKS);
            Iterator<Path> pathi = paths.iterator();
            while (pathi.hasNext()) {
                Path p = pathi.next();
                if (!CoreUtilities.toLowerCase(p.toString()).endsWith(".yml")) {
                    continue;
                }
                File f = p.toFile();
                if (f.exists() && !f.isDirectory()) {
                    try (FileInputStream fis = new FileInputStream(f)) {
                        String contents = CoreUtilities.streamToString(fis);
                        String fileName = f.getName();
                        clientScripts.put(fileName, contents);
                        Depenizen2Sponge.instance.debugMessage("Loaded client script file: " + fileName);
                    }
                    catch (FileNotFoundException e) {
                        Depenizen2Sponge.instance.debugException(e);
                    }
                }
            }
        }
        catch (IOException e) {
            Depenizen2Sponge.instance.debugException(e);
        }
    }

    @Listener
    public void onScriptReload(Denizen2SpongeReloadEvent event) {
        reloadClientScripts();
        for (Player player : Sponge.getServer().getOnlinePlayers()) {
            if (playersWithMod.contains(player.getUniqueId())) {
                sendAllScripts(player);
            }
        }
    }

    public static void sendAllScripts(Player player) {
        DataSerializer serializer = new DataSerializer();
        serializer.writeString("LoadAllScripts");
        serializer.writeStringMap(clientScripts);
        send(player, serializer);
    }

    public static void runScript(Player player, String script, Map<String, String> definitions) {
        DataSerializer serializer = new DataSerializer();
        serializer.writeString("RunScript");
        serializer.writeString(script);
        serializer.writeStringMap(definitions);
        send(player, serializer);
    }

    private static void send(Player player, DataSerializer serializer) {
        channel.sendTo(player, (channelBuf) -> channelBuf.writeByteArray(serializer.toByteArray()));
    }

    @Override
    public void handlePayload(ChannelBuf channelBuf, RemoteConnection remoteConnection, Platform.Type type) {
        if (!(remoteConnection instanceof PlayerConnection)) {
            return;
        }
        if (channelBuf.available() <= 0) {
            return;
        }
        Player player = ((PlayerConnection) remoteConnection).getPlayer();
        DataDeserializer deserializer = new DataDeserializer(channelBuf.readByteArray());
        String subchannel = deserializer.readString();
        if (subchannel.equals("READY")) {
            UUID uuid = player.getUniqueId();
            if (!playersWithMod.contains(uuid)) {
                playersWithMod.add(uuid);
                sendAllScripts(player);
            }
        }
    }
}
