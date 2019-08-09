package com.denizenscript.depenizen.bukkit.events.votifier;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import com.denizenscript.denizen.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.denizencore.scripts.containers.ScriptContainer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class VotifierVoteScriptEvent extends BukkitScriptEvent implements Listener {

    public VotifierVoteScriptEvent() {
        instance = this;
    }

    public static VotifierVoteScriptEvent instance;

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
    // @Plugin Depenizen, Votifier
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
        return new BukkitScriptEntryData(player == null ? null : new PlayerTag(player), null);
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("time")) {
            return new ElementTag(vote.getTimeStamp());
        }
        else if (name.equals("service")) {
            return new ElementTag(vote.getServiceName());
        }
        else if (name.equals("username")) {
            return new ElementTag(vote.getUsername());
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onVotifierEvent(VotifierEvent event) {
        vote = event.getVote();
        fire(event);
    }
}
