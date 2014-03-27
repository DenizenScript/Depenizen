package net.gnomeffinway.depenizen.events;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;

import net.aufdemrand.denizen.events.EventManager;
import net.aufdemrand.denizen.objects.Element;
import net.aufdemrand.denizen.objects.dObject;
import net.aufdemrand.denizen.objects.dPlayer;
import net.gnomeffinway.depenizen.Depenizen;

public class VotifierEvents implements Listener {

    public VotifierEvents(Depenizen depenizen) {
        depenizen.getServer().getPluginManager().registerEvents(this, depenizen);
    }

    // <--[event]
    // @Events
    // votifier vote
    // @Triggers when a Votifier vote is made.
    // @Context
    // <context.time> returns the time the vote was sent.
    // <context.service> returns what service was used to send the vote.
    // @Plugin Votifier
    // -->

    // <--[event]
    // @Events
    // bungeefier vote
    // @Triggers when a Bungeefier vote is made.
    // @Context
    // <context.time> returns the time the vote was sent.
    // <context.service> returns what service was used to send the vote.
    // @Plugin Bungeefier
    // -->

    @EventHandler
    public void onVotifierEvent(VotifierEvent event) {

        Map<String, dObject> context = new HashMap<String, dObject>();
        Vote vote = event.getVote();

        context.put("time", new Element(vote.getTimeStamp()));
        context.put("service", new Element(vote.getServiceName()));

        EventManager.doEvents(Arrays.asList
                ("votifier vote", "bungeefier vote"),
                null, dPlayer.valueOf(vote.getUsername()), context);

    }

}
