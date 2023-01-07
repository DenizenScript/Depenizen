package com.denizenscript.depenizen.bukkit.clientizen.network;

public class Channels {
    public static final String CHANNEL_NAMESPACE = "clientizen";
    public static final String SET_SCRIPTS = id("set_scripts");
    public static final String RECEIVE_CONFIRM = id("receive_confirmation");
    public static final String RECEIVE_EVENT = id("fire_event");
    public static final String RUN_CLIENT_SCRIPT = id("run_script");

    public static String id(String key) {
        return CHANNEL_NAMESPACE + ':' + key;
    }
}
