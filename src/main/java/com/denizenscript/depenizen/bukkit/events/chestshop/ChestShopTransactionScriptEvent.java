package com.denizenscript.depenizen.bukkit.events.chestshop;

import com.Acrobot.ChestShop.Events.TransactionEvent;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChestShopTransactionScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // chestshop transaction
    //
    // @Triggers when a transaction occurs at a chestshop.
    //
    // @Switch type:<type> to only process if the transaction was either a "buy" or "sell" event.
    //
    // @Cancellable true
    //
    // @Context
    // <context.container> returns a LocationTag of the container attached to the shop, if any.
    // <context.item> returns an ItemTag of the item involved in the transaction.
    // <context.money> returns an ElementTag(Decimal) of the amount of money involved in the transaction.
    // <context.sign> returns a LocationTag of the sign running the shop.
    // <context.type> returns whether the transaction was a "BUY" or "SELL" event.
    //
    // @Plugin Depenizen, ChestShop
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->

    public ChestShopTransactionScriptEvent() {
        registerCouldMatcher("chestshop transaction");
        registerSwitches("type");
    }

    public TransactionEvent event;

    @Override
    public boolean matches(ScriptPath path) {
        if (!runGenericSwitchCheck(path, "type", event.getTransactionType().toString())) {
            return false;
        }
        return super.matches(path);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(event.getClient());
    }

    @Override
    public ObjectTag getContext(String name) {
        return switch (name) {
            case "container" -> event.getOwnerInventory().getLocation() == null ? null : new LocationTag(event.getOwnerInventory().getLocation());
            case "item" -> new ItemTag(event.getStock()[0]);
            case "money" -> new ElementTag(event.getExactPrice());
            case "sign" -> new LocationTag(event.getSign().getLocation());
            case "type" -> new ElementTag(event.getTransactionType());
            default -> super.getContext(name);
        };
    }

    @EventHandler
    public void onChestShopTransaction(TransactionEvent event) {
        this.event = event;
        fire(event);
    }
}
