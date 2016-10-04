package com.denizenscript.depenizen.bukkit;

import com.denizenscript.depenizen.bukkit.support.Supported;
import com.denizenscript.depenizen.bukkit.support.bungee.BungeeSupport;
import com.denizenscript.depenizen.common.Depenizen;
import com.denizenscript.depenizen.common.DepenizenImplementation;
import net.aufdemrand.denizen.utilities.debugging.dB;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class DepenizenPlugin extends JavaPlugin implements DepenizenImplementation {

    private static DepenizenPlugin instance;

    public static DepenizenPlugin getCurrentInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        Depenizen.init(this);
        instance = this;
        this.loadConfig();
        this.checkPlugins();
    }

    @Override
    public void onDisable() {
        BungeeSupport.closeSocket();
        instance = null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("depenizen")) {
            sender.sendMessage(ChatColor.UNDERLINE + "Depenizen");
            sender.sendMessage(ChatColor.GRAY + "Developers: " + ChatColor.AQUA + "Morphan1" + ChatColor.GRAY + ", " + ChatColor.GREEN + "Jeebiss" + ChatColor.GRAY + ", and GnomeffinWay");
            sender.sendMessage(ChatColor.GRAY + "Current version: " + ChatColor.GOLD + this.getDescription().getVersion());
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
        }
        else {
            getServer().getLogger().severe("[Depenizen] Denizen not found, disabling");
            getPluginLoader().disablePlugin(this);
            return;
        }

        Supported.setup(this, pm, getClassLoader());
    }

    public static void depenizenLog(String message) {
        dB.log(message);
    }

    @Override
    public void debugMessage(String message) {
        dB.log(message);
    }

    @Override
    public void debugException(Throwable exception) {
        dB.echoError(exception);
    }

    @Override
    public void debugError(String error) {
        dB.echoError(error);
    }
}
