package com.denizenscript.depenizen.sponge;

import com.denizenscript.denizen2core.Denizen2Core;
import com.denizenscript.denizen2core.utilities.CoreUtilities;
import com.denizenscript.denizen2core.utilities.yaml.YAMLConfiguration;
import com.denizenscript.denizen2sponge.Denizen2Sponge;
import com.denizenscript.denizen2sponge.spongeevents.Denizen2SpongeLoadingEvent;
import com.denizenscript.depenizen.common.Depenizen;
import com.denizenscript.depenizen.common.DepenizenImplementation;
import com.denizenscript.depenizen.sponge.commands.bungee.BungeeRunCommand;
import com.denizenscript.depenizen.sponge.events.bungee.*;
import com.denizenscript.depenizen.sponge.support.bungee.BungeeSupport;
import com.denizenscript.depenizen.sponge.support.clientizen.ClientizenSupport;
import com.denizenscript.depenizen.sponge.tags.bungee.handlers.BungeeServerTagBase;
import com.denizenscript.depenizen.sponge.tags.bungee.objects.BungeeServerTag;
import com.google.inject.Inject;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Path;

@Plugin(
        id = Depenizen2Sponge.PLUGIN_ID,
        name = Depenizen2Sponge.PLUGIN_NAME,
        version = Depenizen2Sponge.PLUGIN_VERSION,
        authors = "Morphan1 and the DenizenScript team",
        dependencies = @Dependency(id = Denizen2Sponge.PLUGIN_ID)
)
public class Depenizen2Sponge implements DepenizenImplementation {

    public final static String PLUGIN_ID = "depenizen2sponge";

    public final static String PLUGIN_NAME = "Depenizen2Sponge";

    public final static String PLUGIN_VERSION = PomData.VERSION + " (build " + PomData.BUILD_NUMBER + ")";

    public static PluginContainer plugin;

    public static Depenizen2Sponge instance;

    @Inject
    @ConfigDir(sharedRoot = false)
    public Path configDir;

    private File configFile;

    public YAMLConfiguration config;

    @Listener
    public void onDenizenLoading(Denizen2SpongeLoadingEvent event) {
        // Setup
        instance = this;
        plugin = Sponge.getPluginManager().getPlugin(PLUGIN_ID).orElse(null);
        configFile = configDir.resolve("config.yml").toFile();
        saveDefaultConfig();
        loadConfig();
        // Depenizen
        Depenizen.init(this);
        if (Settings.socketEnabled()) {
            // Bungee Commands
            Denizen2Core.register(new BungeeRunCommand());
            // Bungee Tag Handlers
            Denizen2Core.register(new BungeeServerTagBase());
            // Bungee Events
            Denizen2Core.register(new BungeeRegisteredScriptEvent());
            Denizen2Core.register(new BungeeServerConnectScriptEvent());
            Denizen2Core.register(new BungeeServerDisconnectScriptEvent());
            Denizen2Core.register(new ProxyPingScriptEvent());
            Denizen2Core.register(new PostLoginScriptEvent());
            Denizen2Core.register(new PlayerDisconnectScriptEvent());
            Denizen2Core.register(new ReconnectFailScriptEvent());
            Denizen2Core.register(new ServerSwitchScriptEvent());
            // Bungee Tags
            Denizen2Core.customSaveLoaders.put("BungeeServerTag", BungeeServerTag::getFor);
            // Start BungeeCord socket
            BungeeSupport.startSocket();
        }
        ClientizenSupport clientizenSupport = new ClientizenSupport(); // TODO: Support classes (register)
    }

    @Listener
    public void onServerStop(GameStoppingEvent event) {
        BungeeSupport.closeSocket();
        instance = null;
        plugin = null;
    }

    private void saveDefaultConfig() {
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            try (InputStream is = getClass().getResourceAsStream("defaultConfig.yml");
                 PrintWriter writer = new PrintWriter(configFile)) {
                writer.write(CoreUtilities.streamToString(is));
            }
            catch (IOException e) {
                debugException(e);
            }
        }
    }

    private void loadConfig() {
        try {
            config = YAMLConfiguration.load(CoreUtilities.streamToString(new FileInputStream(configFile)));
        }
        catch (IOException e) {
            debugException(e);
        }
    }

    @Override
    public void debugMessage(String message) {
        Denizen2Core.getImplementation().outputInfo(message);
    }

    @Override
    public void debugException(Exception exception) {
        Denizen2Core.getImplementation().outputException(exception);
    }

    @Override
    public void debugError(String error) {
        Denizen2Core.getImplementation().outputError(error);
    }
}
