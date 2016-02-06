package net.gnomeffinway.depenizen.events.ShopKeepers;

import com.nisovin.shopkeepers.Shopkeeper;
import com.nisovin.shopkeepers.events.ShopkeeperTradeEvent;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizen.objects.dItem;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.utilities.DenizenAPI;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.ScriptEntryData;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class ShopKeeperTradeScriptEvent extends BukkitScriptEvent implements Listener {

    public ShopKeeperTradeScriptEvent() {
        instance = this;
    }

    ShopKeeperTradeScriptEvent instance;
    ShopkeeperTradeEvent event;
    Shopkeeper keeper;
    dPlayer player;
    dList recipe;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        return CoreUtilities.toLowerCase(s).startsWith("shopkeeper trade");
    }

    @Override
    public boolean matches(ScriptContainer scriptContainer, String s) {
        return true;
    }

    @Override
    public String getName() {
        return "ShopKeeperTrade";
    }

    @Override
    public void init() {
        Bukkit.getServer().getPluginManager().registerEvents(this, DenizenAPI.getCurrentInstance());
    }

    @Override
    public void destroy() {
        ShopkeeperTradeEvent.getHandlerList().unregister(this);
    }

    @Override
    public boolean applyDetermination(ScriptContainer container, String determination) {
        return super.applyDetermination(container, determination);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(player, null);
    }

    @Override
    public dObject getContext(String name) {
        if (name.startsWith("recipe")) {
            return recipe;
        }
        else if (name.equals("keeper")) {
            return new Element("");
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onShopKeeperTrade(ShopkeeperTradeEvent event) {
        player = dPlayer.mirrorBukkitPlayer(event.getPlayer());
        keeper = event.getShopkeeper();
        recipe = new dList();
        for (ItemStack item : event.getTradeRecipe()) {
            if (item != null) {
                recipe.add(new dItem(item).identify());
            }
            else {
                recipe.add(new dItem(Material.AIR).identify());
            }
        }
        cancelled = event.isCancelled();
        this.event = event;
        fire();
        event.setCancelled(cancelled);
    }
}
