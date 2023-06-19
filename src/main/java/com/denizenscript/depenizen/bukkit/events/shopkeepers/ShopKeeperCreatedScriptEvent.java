package com.denizenscript.depenizen.bukkit.events.shopkeepers;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.nisovin.shopkeepers.api.events.PlayerCreateShopkeeperEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ShopKeeperCreatedScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // player creates shopkeeper
    //
    // @Cancellable true
    //
    // @Triggers when a player creates a shopkeeper.
    //
    // @Context
    // <context.location> Returns the LocationTag of the shopkeeper being created.
    //
    // @Plugin Depenizen, ShopKeepers
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->

    public ShopKeeperCreatedScriptEvent() {
        registerCouldMatcher("player creates shopkeeper");
    }

    public PlayerCreateShopkeeperEvent event;

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(event.getShopCreationData().getCreator());
    }

    @Override
    public ObjectTag getContext(String name) {
        return switch (name) {
            case "location" -> new LocationTag(event.getShopCreationData().getSpawnLocation());
            default -> super.getContext(name);
        };
    }

    @EventHandler
    public void onPlayerCreateShopkeeper(PlayerCreateShopkeeperEvent event) {
        this.event = event;
        fire(event);
    }
}
