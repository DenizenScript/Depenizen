package com.denizenscript.depenizen.bukkit.events.bungee;

import com.denizenscript.denizen.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.denizenscript.depenizen.bukkit.bungee.BungeeBridge;

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
    // <context.command> returns the command executed.
    //
    // @Determine
    // "COMMAND:" + Element to change the command that will be ran.
    //
    // -->

    public BungeeProxyServerCommandScriptEvent() {
        instance = this;
    }

    public static BungeeProxyServerCommandScriptEvent instance;

    public String sender;

    public String command;

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
        return true;
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
        return new BukkitScriptEntryData(null, null);
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
        else if (name.equals("command")) {
            return new ElementTag(command);
        }
        return super.getContext(name);
    }
}
