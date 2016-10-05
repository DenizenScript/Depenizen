package com.denizenscript.depenizen.bungee;

import com.denizenscript.depenizen.common.Depenizen;
import com.denizenscript.depenizen.common.DepenizenImplementation;
import com.denizenscript.depenizen.common.socket.server.SocketServer;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class DepenizenPlugin extends Plugin implements DepenizenImplementation {

    private static DepenizenPlugin instance;

    public static DepenizenPlugin getCurrentInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        Depenizen.init(this);
        instance = this;
        try {
            this.loadConfig();
        } catch (Exception e) {
            dB.echoError(e);
        }
        this.startSocket();
        getProxy().getPluginManager().registerListener(this, new EventManager());
    }

    @Override
    public void onDisable() {
        this.closeSocket();
        instance = null;
    }

    private SocketServer socketServer;
    private Configuration config;
    private File configFile;

    public void startSocket() {
        if (Settings.socketEnabled()) {
            String password = Settings.socketPassword();
            if (password == null) {
                dB.echoError("SocketServer is enabled, but no password is specified.");
                return;
            }
            try {
                socketServer = new BungeeSocketServer(Settings.socketPort(), Settings.socketMaxClients(), password.toCharArray());
                socketServer.start();
            }
            catch (Exception e) {
                dB.echoError("SocketServer failed to start due to an exception.");
                dB.echoError(e);
            }
        }
    }

    public void closeSocket() {
        if (socketServer != null) {
            socketServer.stop();
        }
    }

    public SocketServer getSocketServer() {
        return this.socketServer;
    }

    public void loadConfig() throws Exception{

        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        configFile = new File(getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            configFile.createNewFile();
            try (InputStream in = getResourceAsStream("config.yml");
                 OutputStream out = new FileOutputStream(configFile)) {
                ByteStreams.copy(in, out);
            }
        }

        config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
    }

    public Configuration getConfig() {
        return config;
    }

    @Override
    public void debugMessage(String message) {
        dB.log(message);
    }

    @Override
    public void debugException(Exception exception) {
        dB.echoError(exception);
    }

    @Override
    public void debugError(String error) {
        dB.echoError(error);
    }
}
