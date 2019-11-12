package com.denizenscript.depenizen.bukkit.events.essentials;

import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.denizencore.objects.core.ElementTag;
import net.ess3.api.events.UserBalanceUpdateEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerBalanceChangeScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // essentials player balance changes
    //
    // @Regex ^on essentials player balance changes$
    //
    // @Triggers when a player's balance changes, when using Essentials economy.
    //
    // @Switch in:<area> to only process the event if it occurred within a specified area.
    //
    // @Context
    // <context.old_balance> Returns the balance before changes are made.
    // <context.new_balance> Returns the balance after changes are made.
    //
    // @Plugin Depenizen, Essentials
    //
    // -->

    public static PlayerBalanceChangeScriptEvent instance;
    public UserBalanceUpdateEvent event;


    public PlayerBalanceChangeScriptEvent() {
        instance = this;
    }

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("essentials player balance changes");
    }

    @Override
    public boolean matches(ScriptPath path) {
        if (!runInCheck(path, event.getPlayer().getLocation())) {
            return false;
        }
        return super.matches(path);
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
            return new ElementTag(event.getNewBalance());
        }
        if (name.equals("old_balance")) {
            return new ElementTag(event.getOldBalance());
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onPlayerBalanceChange(UserBalanceUpdateEvent event) {
        this.event = event;
        fire(event);
    }
}
