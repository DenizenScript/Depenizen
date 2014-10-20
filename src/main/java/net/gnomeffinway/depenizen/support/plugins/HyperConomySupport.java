package net.gnomeffinway.depenizen.support.plugins;

import net.aufdemrand.denizen.events.EventManager;
import net.aufdemrand.denizen.objects.dItem;
import net.aufdemrand.denizen.objects.dObject;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.utilities.debugging.dB;
import net.gnomeffinway.depenizen.Depenizen;
import net.gnomeffinway.depenizen.support.Support;
import net.gnomeffinway.depenizen.support.Supported;
import org.bukkit.scheduler.BukkitRunnable;
import regalowl.hyperconomy.HyperConomy;
import regalowl.hyperconomy.account.HyperPlayer;
import regalowl.hyperconomy.event.HyperEventHandler;
import regalowl.hyperconomy.event.TransactionListener;
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
        } catch (Exception e) {
            dB.echoError(e);
        }
        TRANSACTION_RESPONSE_PLAYER = field;
    }

    public HyperConomySupport() {
        new BukkitRunnable() {
            @Override
            public void run() {
                HyperConomy hyperConomy = Supported.get("HYPERCONOMY").getPlugin();
                HyperEventHandler eventHandler = hyperConomy.getHyperEventHandler();
                eventHandler.registerListener(new TransactionListener() {
                    @Override
                    public void onTransaction(PlayerTransaction playerTransaction, TransactionResponse transactionResponse) {
                        HyperPlayer hyperPlayer = getHyperPlayer(transactionResponse);

                        List<String> events = new ArrayList<String>();
                        Map<String, dObject> context = new HashMap<String, dObject>();

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
                            // @Plugin HyperConomy
                            // -->
                            case BUY:
                                dItem buying = new dItem(playerTransaction.getHyperObject().getItemStack());
                                context.put("item", buying);
                                events.add("player buys item");
                                events.add("player buys " + buying.identifySimple());
                                events.add("player buys " + buying.identifyMaterial());
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
                            // @Plugin HyperConomy
                            // -->
                            case SELL:
                                dItem selling = new dItem(playerTransaction.getHyperObject().getItemStack());
                                context.put("item", selling);
                                events.add("player sells item");
                                events.add("player sells " + selling.identifySimple());
                                events.add("player sells " + selling.identifyMaterial());
                                break;

                            // TODO: are these actually used?
                            case BUY_FROM_INVENTORY:
                                break;

                            case SELL_TO_INVENTORY:
                                break;

                            case BUY_FROM_ITEM:
                                break;
                        }

                        EventManager.doEvents(events, null, dPlayer.mirrorBukkitPlayer(hyperPlayer.getPlayer()), context);
                    }
                });
            }
        }.runTaskLaterAsynchronously(Depenizen.getCurrentInstance(), 1);
    }

    public static HyperPlayer getHyperPlayer(TransactionResponse transactionResponse) {
        try {
            return (HyperPlayer) TRANSACTION_RESPONSE_PLAYER.get(transactionResponse);
        } catch (Exception e) {
            dB.echoError(e);
        }
        return null;
    }

}
