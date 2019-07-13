package com.denizenscript.depenizen.bukkit.events.bungee;

import com.denizenscript.denizen.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.dPlayer;
import com.denizenscript.denizencore.objects.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.denizencore.scripts.containers.ScriptContainer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class BungeePlayerQuitsScriptEvent extends BukkitScriptEvent {

    // <--[event]
    // @Events
    // bungee player leaves network
    //
    // @Regex ^on bungee player leaves network$
    //
    // @Triggers when a player leaves the Bungee network.
    //
    // @Context
    // <context.name> returns the leaving player's name.
    // <context.uuid> returns the leaving player's UUID.
    //
    // -->

    public BungeePlayerQuitsScriptEvent() {
        instance = this;
    }

    public static BungeePlayerQuitsScriptEvent instance;

    public String name;

    public UUID uuid;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        return s.startsWith("bungee player leaves network");
    }

    @Override
    public boolean matches(ScriptPath path) {
        return true;
    }

    @Override
    public String getName() {
        return "BungeePlayerQuits";
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        OfflinePlayer player = null;
        try {
            player = Bukkit.getOfflinePlayer(uuid);
        }
        catch (IllegalArgumentException ex) {
            // Ignore.
        }
        return new BukkitScriptEntryData(player == null ? null : new dPlayer(player), null);
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("name")) {
            return new ElementTag(name);
        }
        else if (name.equals("uuid")) {
            return new ElementTag(uuid.toString());
        }
        return super.getContext(name);
    }
}
