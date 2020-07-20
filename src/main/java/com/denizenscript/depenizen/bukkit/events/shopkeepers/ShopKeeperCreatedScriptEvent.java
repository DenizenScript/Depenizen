package com.denizenscript.depenizen.bukkit.events.shopkeepers;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.nisovin.shopkeepers.api.events.PlayerCreatePlayerShopkeeperEvent;
import com.nisovin.shopkeepers.api.events.PlayerCreateShopkeeperEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ShopKeeperCreatedScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // player creates shopkeeper
    //
    // @Regex ^on player creates shopkeeper$
    //
    // @Cancellable true
    //
    // @Triggers when a player creates a shopkeeper.
    //
    // @Context
    // <context.location> Returns the location of the shopkeeper being created.
    //
    // @Plugin Depenizen, ShopKeepers
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->

    public ShopKeeperCreatedScriptEvent() {
        instance = this;
    }

    public static ShopKeeperCreatedScriptEvent instance;
    public PlayerCreateShopkeeperEvent event;
    public PlayerTag player;

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("player creates shopkeeper");
    }

    @Override
    public String getName() {
        return "ShopKeeperCreated";
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(player, null);
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.startsWith("location")) {
            return new LocationTag(event.getShopCreationData().getSpawnLocation());
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onShopKeeperCreated(PlayerCreateShopkeeperEvent event) {
        player = new PlayerTag(event.getShopCreationData().getCreator());
        this.event = event;
        fire(event);
    }

    @EventHandler
    public void onPlayerShopKeeperCreated(PlayerCreatePlayerShopkeeperEvent event) {
        onShopKeeperCreated(event);
    }
}
