package com.denizenscript.depenizen.bukkit.events.breweryx;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.depenizen.bukkit.objects.breweryx.BPlayerTag;
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
    // <context.bplayer> Returns an BPlayerTag of the player that drank the brew.
    // <context.player> Returns a PlayerTag of the player that had their chat distorted.
    //
    // @Determine
    // ElementTag(String) to set the message to be sent after being distorted.
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
    public PlayerTag playerTag;

    @Override
    public boolean matches(ScriptPath path) {
        return super.matches(path);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(event.getPlayer());
    }

    @Override
    public ObjectTag getContext(String name) {
        return switch (name) {
            case "message" -> new ElementTag(event.getDistortedMessage());
            case "original_message" -> new ElementTag(event.getWrittenMessage());
            case "bplayer" -> BPlayerTag.forPlayer(playerTag);
            case "player" -> playerTag;
            default -> super.getContext(name);
        };
    }

    @Override
    public boolean handleDetermination(ScriptPath path, String prefix, ObjectTag value) {
        if (prefix.equals("message") && value instanceof ElementTag elementTag) { // FIXME - Cast redundant but .toString() returns class and hash?
            event.setDistortedMessage(elementTag.asString());
            return true;
        }
        return super.handleDetermination(path, prefix, value);
    }

    @EventHandler
    public void onChatDistort(PlayerChatDistortEvent event) {
        this.event = event;
        this.playerTag = new PlayerTag(event.getPlayer());
        fire(event);
    }
}
