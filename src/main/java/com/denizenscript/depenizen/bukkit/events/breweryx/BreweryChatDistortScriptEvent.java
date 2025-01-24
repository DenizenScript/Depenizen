package com.denizenscript.depenizen.bukkit.events.breweryx;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.dre.brewery.api.events.PlayerChatDistortEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class BreweryChatDistortScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // brewery chat distort
    //
    // @Cancellable true
    //
    // @Triggers when a drunk brewery player's chat is distorted.
    //
    // @Context
    // <context.message> Returns the message to be sent after being distorted.
    // <context.original_message> Returns the original message before being distorted.
    // <context.player> Returns a PlayerTag of the player that had their chat distorted.
    //
    // @Determine
    // ElementTag to set the message to be sent after being distorted.
    //
    // @Plugin Depenizen, BreweryX
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->
    public BreweryChatDistortScriptEvent() {
        registerCouldMatcher("brewery chat distort");
    }

    public PlayerChatDistortEvent event;

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(event.getPlayer());
    }

    @Override
    public ObjectTag getContext(String name) {
        return switch (name) {
            case "message" -> new ElementTag(event.getDistortedMessage());
            case "original_message" -> new ElementTag(event.getWrittenMessage());
            default -> super.getContext(name);
        };
    }

    @Override
    public boolean handleDetermination(ScriptPath path, String prefix, ObjectTag value) {
        if (prefix.equals("message")) {
            event.setDistortedMessage(value.asElement().asString());
            return true;
        }
        return super.handleDetermination(path, prefix, value);
    }

    @EventHandler
    public void onChatDistort(PlayerChatDistortEvent event) {
        this.event = event;
        fire(event);
    }
}
