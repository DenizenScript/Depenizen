package net.gnomeffinway.depenizen.events;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;

import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.scripts.containers.core.WorldScriptHelper;
import net.gnomeffinway.depenizen.Depenizen;

public class VotifierEvents implements Listener {

    public VotifierEvents(Depenizen depenizen) {
        depenizen.getServer().getPluginManager().registerEvents(this, depenizen);
    }
    
    @EventHandler
    public void onVotifierEvent(VotifierEvent event) {
        
        Map<String, Object> context = new HashMap<String, Object>();
        Vote vote = event.getVote();
        Player player = Bukkit.getPlayer(vote.getUsername());

        context.put("player", new dPlayer(player));
        context.put("time", vote.getTimeStamp());
        context.put("service", vote.getServiceName());
        
        WorldScriptHelper.doEvents(Arrays.asList
                ("votifier vote"),
                null, player, context);
    
    }

}
