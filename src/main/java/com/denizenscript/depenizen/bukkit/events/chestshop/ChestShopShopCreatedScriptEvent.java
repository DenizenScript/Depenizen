package com.denizenscript.depenizen.bukkit.events.chestshop;

import com.Acrobot.ChestShop.Events.ShopCreatedEvent;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Arrays;

public class ChestShopShopCreatedScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // chestshop shop created
    //
    // @Triggers when a chestshop is created.
    //
    // @Context
    // <context.container> returns a LocationTag of the container attached to the shop, if any.
    // <context.created_by_owner> returns true if the player that created this shop is also the owner of this shop.
    // <context.sign> returns a LocationTag of the sign.
    // <context.sign_text> returns a ListTag of the text on the sign.
    //
    // @Plugin Depenizen, ChestShop
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->

    public ChestShopShopCreatedScriptEvent() {
        registerCouldMatcher("chestshop shop created");
    }

    public ShopCreatedEvent event;

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(event.getPlayer());
    }

    @Override
    public ObjectTag getContext(String name) {
        return switch (name) {
            case "container" -> event.getContainer() == null ? null : new LocationTag(event.getContainer().getLocation());
            case "created_by_owner" -> new ElementTag(event.createdByOwner());
            case "sign" -> new LocationTag(event.getSign().getLocation());
            case "sign_text" -> new ListTag(Arrays.asList(event.getSignLines()));
            default -> super.getContext(name);
        };
    }

    @EventHandler
    public void onChestShopCreated(ShopCreatedEvent event) {
        this.event = event;
        fire(event);
    }
}
