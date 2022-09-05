package com.denizenscript.depenizen.bukkit.events.essentials;

import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
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
    // @Location true
    //
    // @Context
    // <context.old_balance> Returns the balance before changes are made.
    // <context.new_balance> Returns the balance after changes are made.
    // <context.cause> returns the reason for the balance change, refer to <@link url https://github.com/EssentialsX/Essentials/blob/2.x/Essentials/src/main/java/net/ess3/api/events/UserBalanceUpdateEvent.java#L73-L78>.
    //
    // @Plugin Depenizen, Essentials
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->

    public UserBalanceUpdateEvent event;

    public PlayerBalanceChangeScriptEvent() {
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
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(event.getPlayer());
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("new_balance")) {
            return new ElementTag(event.getNewBalance());
        }
        if (name.equals("old_balance")) {
            return new ElementTag(event.getOldBalance());
        }
        if (name.equals("cause")) {
            return new ElementTag(event.getCause().name());
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onPlayerBalanceChange(UserBalanceUpdateEvent event) {
        this.event = event;
        fire(event);
    }
}
