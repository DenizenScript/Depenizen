package com.denizenscript.depenizen.bukkit.events.votifier;

import com.denizenscript.denizen.utilities.debugging.Debug;
import com.denizenscript.denizencore.objects.core.TimeTag;
import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class VotifierVoteScriptEvent extends BukkitScriptEvent implements Listener {

    public VotifierVoteScriptEvent() {
    }


    // <--[event]
    // @Events
    // votifier vote
    //
    // @Regex ^on votifier vote$
    //
    // @Triggers when a Votifier vote is made.
    //
    // @Context
    // <context.time_sent> returns the time the vote was sent.
    // <context.service> returns what service was used to send the vote.
    // <context.username> returns the username input with the vote.
    //
    // @Plugin Depenizen, Votifier
    //
    // @Player When the vote is made using a recognized username.
    //
    // @Group Depenizen
    //
    // -->

    public Vote vote;

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("votifier vote");
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        Player player = Bukkit.getPlayerExact(vote.getUsername());
        return new BukkitScriptEntryData(player == null ? null : new PlayerTag(player), null);
    }

    @Override
    public ObjectTag getContext(String name) {
        switch (name) {
            case "time":
                Debug.echoError("Deprecation notice: Use 'context.time_sent' (TimeTag) instead of 'context.time' (ElementTag) for votifier vote event.");
                return new ElementTag(vote.getTimeStamp());
            case "time_sent":
                return new TimeTag(Long.parseLong(vote.getTimeStamp()) * 1000);
            case "service":
                return new ElementTag(vote.getServiceName());
            case "username":
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
