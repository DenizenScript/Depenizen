package com.denizenscript.depenizen.bukkit.events.bungee;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.denizenscript.depenizen.bukkit.bungee.BungeeBridge;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class BungeeProxyServerCommandScriptEvent extends BukkitScriptEvent {

    // <--[event]
    // @Events
    // proxy server command
    // proxy server (<command>) command
    //
    // @Regex ^on proxy server( [^\s]+)? command$
    //
    // @Triggers when a player runs a command on the bungee proxy server.
    //
    // @Cancellable true
    //
    // @Context
    // <context.sender> returns the name of the command sender.
    // <context.sender_id> returns the UUID of the command sender, if available.
    // <context.command> returns the command executed.
    //
    // @Determine
    // "COMMAND:" + Element to change the command that will be ran.
    //
    // @Plugin Depenizen, BungeeCord
    //
    // @Group Depenizen
    //
    // @Player when the player has been on this specific server before.
    //
    // -->

    public BungeeProxyServerCommandScriptEvent() {
        instance = this;
    }

    public static BungeeProxyServerCommandScriptEvent instance;

    public String sender;

    public String command;

    public UUID senderId;

    @Override
    public boolean couldMatch(ScriptPath path) {
        if (!path.eventLower.startsWith("proxy server ")) {
            return false;
        }
        if (!path.eventArgLowerAt(2).equals("command") && !path.eventArgLowerAt(3).equals("command")) {
            return false;
        }
        return true;
    }

    public String commandName() {
        return CoreUtilities.toLowerCase(command.substring(1, command.contains(" ") ? command.indexOf(' ') : command.length()));
    }

    @Override
    public boolean matches(ScriptPath path) {
        if (!path.eventArgLowerAt(2).equals("command") && !path.eventArgLowerAt(2).equals(commandName())) {
            return false;
        }
        return super.matches(path);
    }

    @Override
    public void init() {
        BungeeBridge.instance.controlsProxyCommand = true;
        BungeeBridge.instance.checkBroadcastProxyCommand();
    }
    @Override
    public void destroy() {
        BungeeBridge.instance.controlsProxyCommand = false;
        BungeeBridge.instance.checkBroadcastProxyCommand();
    }

    @Override
    public String getName() {
        return "BungeeProxyServerCommand";
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        OfflinePlayer player = null;
        if (senderId != null) {
            try {
                player = Bukkit.getOfflinePlayer(senderId);
                if (!player.hasPlayedBefore()) {
                    player = null;
                }
            }
            catch (IllegalArgumentException ex) {
                // Ignore.
            }
        }
        return new BukkitScriptEntryData(player == null ? null : new PlayerTag(player), null);
    }

    @Override
    public boolean applyDetermination(ScriptPath path, ObjectTag determinationObj) {
        if (determinationObj instanceof ElementTag) {
            String determination = determinationObj.toString();
            String determinationLow = CoreUtilities.toLowerCase(determination);
            if (determinationLow.startsWith("command:")) {
                command = determination.substring("command:".length());
                return true;
            }
        }
        return super.applyDetermination(path, determinationObj);
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("sender")) {
            return new ElementTag(sender);
        }
        else if (name.equals("sender_id") && senderId != null) {
            return new ElementTag(senderId.toString());
        }
        else if (name.equals("command")) {
            return new ElementTag(command);
        }
        return super.getContext(name);
    }
}
