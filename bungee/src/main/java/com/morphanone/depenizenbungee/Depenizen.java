package com.morphanone.depenizenbungee;

import com.google.common.io.ByteStreams;
import com.morphanone.depenizenbungee.sockets.SocketServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class Depenizen extends Plugin {

    private static Depenizen instance;

    public static Depenizen getCurrentInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        try {
            this.loadConfig();
        } catch (Exception e) {
            dB.echoError(e);
        }
        this.startSocket();
        EventManager eventManager = new EventManager();
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
            this.socketServer = new SocketServer(Settings.socketMaxClients(), Settings.socketPort(), password);
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
}
