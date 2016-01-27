package net.gnomeffinway.depenizen.support.plugins;

import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.objects.dItem;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.events.OldEventManager;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.utilities.debugging.dB;
import net.gnomeffinway.depenizen.Depenizen;
import net.gnomeffinway.depenizen.support.Support;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import regalowl.hyperconomy.account.HyperPlayer;
import regalowl.hyperconomy.bukkit.BukkitConnector;
import regalowl.hyperconomy.event.HyperEventHandler;
import regalowl.hyperconomy.inventory.HItemStack;
import regalowl.hyperconomy.transaction.PlayerTransaction;
import regalowl.hyperconomy.transaction.TransactionResponse;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HyperConomySupport extends Support {

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

    public HyperConomySupport() {
        new BukkitRunnable() {
            @Override
            public void run() {
                BukkitConnector hyperConomy = Support.getPlugin(HyperConomySupport.class);
                HyperEventHandler eventHandler = hyperConomy.getHC().getHyperEventHandler();
                eventHandler.registerListener(this);
            }
        }.runTaskLaterAsynchronously(Depenizen.getCurrentInstance(), 1);
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

        List<String> events = new ArrayList<String>();
        Map<String, dObject> context = new HashMap<String, dObject>();

        HItemStack his = playerTransaction.getHyperObject().getItem();
        dItem item = new dItem(new ItemStack(Material.valueOf(his.getMaterial().toUpperCase()),
                his.getAmount(), his.getDurability(), his.getData()));

        switch (playerTransaction.getTransactionType()) {
            // <--[event]
            // @Events
            // player buys item
            // player buys <item>
            // player buys <material>
            // @Triggers when a player buys an item from a HyperConomy shop.
            // @Context
            // <context.item> returns the dItem the player bought.
            // @Determine
            // NONE
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
            // @Triggers when a player sells an item to a HyperConomy shop.
            // @Context
            // <context.item> returns the dItem the player sold.
            // @Determine
            // NONE
            // @Plugin Depenizen, HyperConomy
            // -->
            case SELL:
                context.put("item", item);
                events.add("player sells item");
                events.add("player sells " + item.identifySimple());
                events.add("player sells " + item.identifyMaterial());
                break;

            // TODO: are these actually used?
            case BUY_FROM_INVENTORY:
                break;

            case SELL_TO_INVENTORY:
                break;
        }

        OldEventManager.doEvents(events, new BukkitScriptEntryData(dPlayer.mirrorBukkitPlayer(Bukkit.getServer()
                .getPlayer(hyperPlayer.getUUID())), null), context);
    }

}
