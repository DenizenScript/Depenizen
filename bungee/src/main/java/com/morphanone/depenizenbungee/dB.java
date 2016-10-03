package com.morphanone.depenizenbungee;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;

import java.util.Map;
import java.util.WeakHashMap;

public class dB {

    private static final Map<Class<?>, String> classNameCache = new WeakHashMap<Class<?>, String>();
    private static final CommandSender console;

    static {
        console = ProxyServer.getInstance().getConsole();
    }

    public static void log(String message) {
        if (!Settings.debugEnabled())
            return;
        Class<?> caller = sun.reflect.Reflection.getCallerClass(2);
        String callerName = classNameCache.get(caller);
        if (callerName == null)
            classNameCache.put(caller, (callerName = caller.getSimpleName()).length() > 16
                    ? callerName.substring(0, 12) + "..." : callerName);
        console.sendMessage(new ComponentBuilder("+> [" + callerName + "] ").color(ChatColor.YELLOW)
                .append(message).color(ChatColor.WHITE).create());
    }

    public static void echoError(String message) {
        console.sendMessage(new ComponentBuilder(" ").color(ChatColor.LIGHT_PURPLE)
                .append("ERROR! ").color(ChatColor.RED).append(message).color(ChatColor.WHITE).create());
    }

    public static void echoError(Throwable throwable) {
        dB.echoError("Internal exception was thrown!");
        throwable.printStackTrace();
    }

}
