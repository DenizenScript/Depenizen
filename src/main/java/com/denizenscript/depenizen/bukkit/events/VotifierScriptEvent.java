package com.denizenscript.depenizen.bukkit.events;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.ScriptEntryData;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class VotifierScriptEvent extends BukkitScriptEvent implements Listener {

    public VotifierScriptEvent() {
        instance = this;
    }

    public static VotifierScriptEvent instance;

    // <--[event]
    // @Events
    // votifier vote
    //
    // @Regex ^on votifier vote$
    //
    // @Triggers when a Votifier vote is made.
    //
    // @Context
    // <context.time> returns the time the vote was sent.
    // <context.service> returns what service was used to send the vote.
    // <context.username> returns the username input with the vote.
    //
    // @Plugin DepenizenBukkit, Votifier
    // -->

    public Vote vote;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        return s.startsWith("votifier vote");
    }

    @Override
    public boolean matches(ScriptPath path) {
        return true;
    }

    @Override
    public String getName() {
        return "VotifierVote";
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        Player player = Bukkit.getPlayerExact(vote.getUsername());
        return new BukkitScriptEntryData(player == null ? null : new dPlayer(player), null);
    }

    @Override
    public dObject getContext(String name) {
        if (name.equals("time")) {
            return new Element(vote.getTimeStamp());
        }
        else if (name.equals("service")) {
            return new Element(vote.getServiceName());
        }
        else if (name.equals("username")) {
            return new Element(vote.getUsername());
        }
        return super.getContext(name);
    }
    @EventHandler
    public void onVotifierEvent(VotifierEvent event) {
        Vote vote = event.getVote();
        fire(event);
    }
}
