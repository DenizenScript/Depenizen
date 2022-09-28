package com.denizenscript.depenizen.bukkit.clientizen.events;

public class ClientizenEventRegistry {

    public static void registerEvents() {
        ClientizenEventManager.registerEvent(PlayerOpenCloseScreenClientizenEvent.class);
        ClientizenEventManager.registerEvent(PlayerPressReleaseKeyClientizenEvent.class);
    }
}
