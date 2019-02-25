package com.denizenscript.depenizen.bungee;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;

import java.util.Map;
import java.util.WeakHashMap;

public class dB {

    private static final Map<Class<?>, String> classNameCache = new WeakHashMap<>();
    private static final CommandSender console;

    static {
        console = ProxyServer.getInstance().getConsole();
    }

    private static class SecurityManagerTrick extends SecurityManager {
        @Override
        @SuppressWarnings("rawtypes")
        protected Class[] getClassContext() {
            return super.getClassContext();
        }
    }

    public static void log(String message) {
        if (!Settings.debugEnabled()) {
            return;
        }
        Class[] classes = new SecurityManagerTrick().getClassContext();
        Class caller = classes.length > 2 ? classes[2] : dB.class;
        String callerName = classNameCache.get(caller);
        if (callerName == null) {
            classNameCache.put(caller, (callerName = caller.getSimpleName()).length() > 16
                    ? callerName.substring(0, 12) + "..." : callerName);
        }
        console.sendMessage(new ComponentBuilder("+> [" + callerName + "] ").color(ChatColor.YELLOW)
                .append(message).color(ChatColor.WHITE).create());
    }

    public static void echoError(String message) {
        console.sendMessage(new ComponentBuilder(" ").color(ChatColor.LIGHT_PURPLE)
                .append("ERROR! ").color(ChatColor.RED).append(message).color(ChatColor.WHITE).create());
    }

    public static void echoError(Throwable throwable) {
        dB.echoError("Internal exception was thrown!");
        trace(throwable);
    }

    private static void trace(Throwable e) {
        if (e == null) {
            return;
        }
        log("   " + e.getClass().getCanonicalName() + ": " + e.getMessage());
        for (StackTraceElement ste : e.getStackTrace()) {
            log("     " + ste.toString());
        }
        if (e.getCause() != e) {
            trace(e.getCause());
        }
    }

}
