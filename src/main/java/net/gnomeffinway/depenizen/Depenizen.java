package net.gnomeffinway.depenizen;

import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.utilities.DenizenAPI;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizencore.events.ScriptEvent;
import net.aufdemrand.denizencore.objects.ObjectFetcher;
import net.gnomeffinway.depenizen.commands.BungeeCommand;
import net.gnomeffinway.depenizen.events.bungee.ProxyPingScriptEvent;
import net.gnomeffinway.depenizen.extensions.bungee.BungeePlayerExtension;
import net.gnomeffinway.depenizen.objects.bungee.dServer;
import net.gnomeffinway.depenizen.support.Supported;
import net.gnomeffinway.depenizen.support.bungee.PluginMessageHandler;
import net.gnomeffinway.depenizen.support.bungee.SocketClient;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class Depenizen extends JavaPlugin implements PluginMessageListener {

    private static Depenizen instance;

    public static Depenizen getCurrentInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        this.loadConfig();
        this.checkPlugins();
        this.enableBungeeCord();
        this.startSocket();
    }

    @Override
    public void onDisable() {
        this.closeSocket();
        instance = null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("depenizen")) {
            sender.sendMessage(ChatColor.UNDERLINE + "Depenizen");
            sender.sendMessage(ChatColor.GRAY + "Developers: " + ChatColor.AQUA + "Morphan1" + ChatColor.GRAY + ", " + ChatColor.GREEN + "Jeebiss" + ChatColor.GRAY + ", and GnomeffinWay");
            sender.sendMessage(ChatColor.GRAY + "Current version: "+ ChatColor.GOLD + this.getDescription().getVersion());
            return true;
        }
        return false;
    }

    public void loadConfig() {
        saveDefaultConfig();
        reloadConfig();
    }

    public void checkPlugins() {

        PluginManager pm = getServer().getPluginManager();

        if (pm.getPlugin("Denizen") != null) {
            depenizenLog("Denizen hooked");
        } else {
            getServer().getLogger().severe("[Depenizen] Denizen not found, disabling");
            getPluginLoader().disablePlugin(this);
            return;
        }

        Supported.setup(this, pm, getClassLoader());
    }

    private SocketClient socketClient;

    public void enableBungeeCord() {
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
        new BungeeCommand().activate().as("BUNGEE").withOptions("bungee", 1);
        ObjectFetcher.registerWithObjectFetcher(dServer.class);
        DenizenAPI.getCurrentInstance().getPropertyParser()
                .registerProperty(BungeePlayerExtension.class, dPlayer.class);
        ScriptEvent.registerScriptEvent(new ProxyPingScriptEvent());
    }

    public void startSocket() {
        if (Settings.socketEnabled()) {
            String ipAddress = Settings.socketIpAddress();
            if (ipAddress == null) {
                dB.echoError("BungeeCord Socket is enabled, but no IP address is specified.");
                return;
            }
            String password = Settings.socketPassword();
            if (password == null) {
                dB.echoError("BungeeCord Socket is enabled, but no password is specified.");
                return;
            }
            String name = Settings.socketName();
            if (name == null) {
                dB.echoError("BungeeCord Socket is enabled, but no registration name is specified.");
                return;
            }
            this.socketClient = new SocketClient(ipAddress, Settings.socketPort(),
                    password, name, Settings.socketTimeout());
            this.socketClient.connect();
        }
    }

    public SocketClient getSocketClient() {
        return socketClient;
    }

    public boolean isSocketConnected() {
        return socketClient != null && socketClient.isConnected();
    }

    public void closeSocket() {
        if (this.socketClient != null) {
            this.socketClient.close();
        }
    }

    public static void depenizenLog(String message) {
        dB.log(message);
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (channel.equals("Depenizen-Bungee")) {
            PluginMessageHandler.handle(message);
        }
    }
}
