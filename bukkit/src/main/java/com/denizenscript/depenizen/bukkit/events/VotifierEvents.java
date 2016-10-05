package com.denizenscript.depenizen.bukkit.events;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.events.OldEventManager;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dObject;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class VotifierEvents implements Listener {

    // <--[event]
    // @Events
    // votifier vote
    // @Triggers when a Votifier vote is made.
    // @Context
    // <context.time> returns the time the vote was sent.
    // <context.service> returns what service was used to send the vote.
    // <context.username> returns the username input with the vote.
    // @Plugin DepenizenBukkit, Votifier
    // -->
    @EventHandler
    public void onVotifierEvent(VotifierEvent event) {

        Map<String, dObject> context = new HashMap<String, dObject>();
        Vote vote = event.getVote();

        context.put("time", new Element(vote.getTimeStamp()));
        context.put("service", new Element(vote.getServiceName()));
        context.put("username", new Element(vote.getUsername()));

        OldEventManager.doEvents(Arrays.asList("votifier vote"),
                new BukkitScriptEntryData(dPlayer.valueOf(vote.getUsername()), null), context);

    }
}
