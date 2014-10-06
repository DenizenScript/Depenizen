package net.gnomeffinway.depenizen.events;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import net.aufdemrand.denizen.events.EventManager;
import net.aufdemrand.denizen.objects.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.*;

public class VotifierEvents implements Listener {

    // <--[event]
    // @Events
    // votifier vote
    // @Triggers when a Votifier vote is made.
    // @Context
    // <context.time> returns the time the vote was sent.
    // <context.service> returns what service was used to send the vote.
    // <context.username> returns the username input with the vote.
    // @Plugin Votifier
    // -->
    @EventHandler
    public void onVotifierEvent(VotifierEvent event) {

        Map<String, dObject> context = new HashMap<String, dObject>();
        Vote vote = event.getVote();

        context.put("time", new Element(vote.getTimeStamp()));
        context.put("service", new Element(vote.getServiceName()));
        context.put("username", new Element(vote.getUsername()));

        EventManager.doEvents(Arrays.asList("votifier vote"), null, dPlayer.valueOf(vote.getUsername()), context);

    }
}
