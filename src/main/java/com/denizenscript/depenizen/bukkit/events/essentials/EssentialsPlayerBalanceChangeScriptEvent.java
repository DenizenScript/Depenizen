package com.denizenscript.depenizen.bukkit.events.essentials;

import com.denizenscript.denizen.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.denizencore.scripts.containers.ScriptContainer;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.denizenscript.denizencore.objects.core.ElementTag;
import net.ess3.api.events.UserBalanceUpdateEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EssentialsPlayerBalanceChangeScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // player balance changes
    //
    // @Regex ^on essentials player balance changes$
    //
    // @Triggers when a player's balance changes, when using Essentials economy.
    //
    // @Switch in <area>
    //
    // @Context
    // <context.old_balance> Returns the balance before changes are made
    // <context.new_balance> Returns the balance after changes are made
    //
    // @Plugin Depenizen, Essentials
    //
    // -->

    public static EssentialsPlayerBalanceChangeScriptEvent instance;
    public UserBalanceUpdateEvent event;
    public ElementTag newBalance;
    public ElementTag oldBalance;
    public LocationTag location;


    public EssentialsPlayerBalanceChangeScriptEvent() {
        instance = this;
    }

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        return lower.startsWith("essentials player balance changes");
    }

    @Override
    public boolean matches(ScriptPath path) {
        if (!runInCheck(path, location)) {
            return false;
        }
        return true;
    }

    @Override
    public String getName() {
        return "EssentialsPlayerBalanceChange";
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(PlayerTag.mirrorBukkitPlayer(event.getPlayer()), null);
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("new_balance")) {
            return newBalance;
        }
        if (name.equals("old_balance")) {
            return oldBalance;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onPlayerBalanceChange(UserBalanceUpdateEvent event) {
        this.event = event;
        location = new LocationTag(event.getPlayer().getLocation());
        newBalance = new ElementTag(event.getNewBalance());
        oldBalance = new ElementTag(event.getOldBalance());
        fire(event);
    }
}
