package com.denizenscript.depenizen.bukkit.events.hyperconomy;

import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.objects.dItem;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.events.OldEventManager;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.utilities.debugging.dB;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import regalowl.hyperconomy.account.HyperPlayer;
import regalowl.hyperconomy.event.HyperEvent;
import regalowl.hyperconomy.event.HyperEventListener;
import regalowl.hyperconomy.event.TransactionEvent;
import regalowl.hyperconomy.inventory.HItemStack;
import regalowl.hyperconomy.transaction.PlayerTransaction;
import regalowl.hyperconomy.transaction.TransactionResponse;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HyperConomyEvents implements HyperEventListener {

    private static final Field TRANSACTION_RESPONSE_PLAYER;

    static {
        Field field = null;
        try {
            field = TransactionResponse.class.getDeclaredField("hp");
            field.setAccessible(true);
        }
        catch (NoClassDefFoundError e) {
            // HyperConomy isn't enabled
        }
        catch (Exception e) {
            dB.echoError(e);
        }
        TRANSACTION_RESPONSE_PLAYER = field;
    }

    public static HyperPlayer getHyperPlayer(TransactionResponse transactionResponse) {
        try {
            return (HyperPlayer) TRANSACTION_RESPONSE_PLAYER.get(transactionResponse);
        }
        catch (Exception e) {
            dB.echoError(e);
        }
        return null;
    }

    public void onTransaction(PlayerTransaction playerTransaction, TransactionResponse transactionResponse) {
        HyperPlayer hyperPlayer = getHyperPlayer(transactionResponse);

        List<String> events = new ArrayList<>();
        Map<String, dObject> context = new HashMap<>();

        HItemStack his = playerTransaction.getHyperObject().getItem();
        dItem item = new dItem(new ItemStack(Material.valueOf(his.getMaterial().toUpperCase()),
                his.getAmount(), his.getDurability(), his.getData()));

        switch (playerTransaction.getTransactionType()) {
            // <--[event]
            // @Events
            // player buys item
            // player buys <item>
            // player buys <material>
            //
            // @Regex ^on player buys [^\s]+$
            //
            // @Triggers when a player buys an item from a HyperConomy shop.
            //
            // @Context
            // <context.item> returns the dItem the player bought.
            //
            // @Determine
            // NONE
            //
            // @Plugin Depenizen, HyperConomy
            // -->
            case BUY:
                context.put("item", item);
                events.add("player buys item");
                events.add("player buys " + item.identifySimple());
                events.add("player buys " + item.identifyMaterial());
                break;

            // <--[event]
            // @Events
            // player sells item
            // player sells <item>
            // player sells <material>
            //
            // @Regex ^on player sells [^\s]+$
            //
            // @Triggers when a player sells an item to a HyperConomy shop.
            //
            // @Context
            // <context.item> returns the dItem the player sold.
            //
            // @Determine
            // NONE
            //
            // @Plugin Depenizen, HyperConomy
            // -->
            case SELL:
                context.put("item", item);
                events.add("player sells item");
                events.add("player sells " + item.identifySimple());
                events.add("player sells " + item.identifyMaterial());
                break;

            // TODO: implement?

            case BUY_CUSTOM:
                break;
            case SELL_CUSTOM:
                break;
        }

        OldEventManager.doEvents(events, new BukkitScriptEntryData(dPlayer.mirrorBukkitPlayer(Bukkit.getServer()
                .getPlayer(hyperPlayer.getUUID())), null), context);
    }

    @Override
    public void handleHyperEvent(HyperEvent hyperEvent) {
        if (hyperEvent instanceof TransactionEvent) {
            TransactionEvent event = (TransactionEvent) hyperEvent;
            onTransaction(event.getTransaction(), event.getTransactionResponse());
        }
    }
}
