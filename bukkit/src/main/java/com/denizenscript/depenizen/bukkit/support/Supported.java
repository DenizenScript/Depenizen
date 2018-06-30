package com.denizenscript.depenizen.bukkit.support;

import com.denizenscript.depenizen.bukkit.DepenizenPlugin;
import com.denizenscript.depenizen.bukkit.support.bungee.BungeeSupport;
import com.denizenscript.depenizen.bukkit.support.clientizen.ClientizenSupport;
import net.aufdemrand.denizen.utilities.debugging.dB;
import com.denizenscript.depenizen.bukkit.Settings;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.HashMap;

public class Supported {

    public static void setup(DepenizenPlugin depenizen, PluginManager pluginManager) {
        SupportManager supportManager = new SupportManager(depenizen);
        for (String name : depenizen.getDescription().getSoftDepend()) {
            try {
                if (set(name, pluginManager.getPlugin(name), supportManager)) {
                    DepenizenPlugin.depenizenLog(name + " hooked, enabling add-ons.");
                }
                else {
                    DepenizenPlugin.depenizenLog(name + " not found, add-ons will not enable.");
                }
            }
            catch (Exception e) {
                dB.echoError("Error while hooking plugin '" + name + "'");
                dB.echoError(e);
            }
        }
        if (Settings.socketEnabled()) {
            DepenizenPlugin.depenizenLog("BungeeCord socket enabled in config, enabling add-ons.");
            supportManager.register(new BungeeSupport());
        }
        else {
            DepenizenPlugin.depenizenLog("BungeeCord socket disabled in config, add-ons will not enable.");
        }
        supportManager.register(new ClientizenSupport());
        supportManager.registerNewObjects();
    }

    private static boolean set(String name, Plugin plugin, SupportManager supportManager) {
        try {
            if (plugin == null) {
                return false;
            }
            Class<? extends Support> clazz = getSupportClass(name);
            if (clazz == null) {
                return false;
            }
            supportManager.register(Support.setPlugin(clazz, plugin));
            return true;
        }
        catch (IllegalAccessException e) {
            dB.echoError("Report this error to the Depenizen dev team: SupportedIllegalAccess-" + name);
            dB.echoError(e);
        }
        catch (InstantiationException e) {
            dB.echoError("Report this error to the Depenizen dev team: SupportedInstantiationSupport-" + name);
            dB.echoError(e);
        }
        return false;
    }

    private static final String pkg = "com.denizenscript.depenizen.bukkit.support.plugins.";

    private static HashMap<String, String> casingFixes = new HashMap<String, String>();

    static {
        casingFixes.put("mcMMO", "McMMO");
        casingFixes.put("pvparena", "PVPArena");
        casingFixes.put("pvpstats", "PVPStats");
    }

    private static Class<? extends Support> getSupportClass(String name) {
        try {
            String patched = casingFixes.get(name);
            return (Class<? extends Support>) Class.forName(pkg + (patched == null ? name : patched) + "Support");
        }
        catch (Throwable ex) {
            dB.echoError("Critical error loading Depenizen support for " + name);
            dB.echoError(ex);
        }
        return null;
    }
}
