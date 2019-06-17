package com.denizenscript.depenizen.bukkit.events.askyblock;

import com.wasteofplastic.askyblock.events.ChallengeCompleteEvent;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizen.objects.dItem;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.ScriptEntryData;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerCompletesSkyBlockChallengeScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // player completes skyblock challenge
    //
    // @Regex ^on player completes skyblock challenge$
    //
    // @Cancellable false
    //
    // @Triggers when a player completes a skyblock challenge.
    //
    // @Context
    // <context.challenge> Returns the name of the challenge.
    // <context.xp_reward> Return the amount of experience to be rewarded.
    // <context.money_reward> Returns the amount of money to be rewarded.
    // <context.item_rewards> Returns a list of items to be awarded.
    // NOTE: item rewards is dependant on how the plugin handles item rewards. Untested and no guarantee of working.
    //
    // @Plugin Depenizen, A SkyBlock
    //
    // -->

    public static PlayerCompletesSkyBlockChallengeScriptEvent instance;
    public ChallengeCompleteEvent event;
    public Element challenge;
    public Element xp_reward;
    public Element money_reward;
    public dList item_rewards;

    public PlayerCompletesSkyBlockChallengeScriptEvent() {
        instance = this;
    }

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        return CoreUtilities.toLowerCase(s).startsWith("player completes skyblock challenge");
    }

    @Override
    public boolean matches(ScriptPath path) {
        return true;
    }

    @Override
    public String getName() {
        return "PlayerCompletesSkyBlockChallenge";
    }

    @Override
    public boolean applyDetermination(ScriptContainer container, String determination) {
        return super.applyDetermination(container, determination);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(new dPlayer(event.getPlayer()), null);
    }

    @Override
    public dObject getContext(String name) {
        if (name.equals("challenge")) {
            return challenge;
        }
        else if (name.equals("xp_reward")) {
            return xp_reward;
        }
        else if (name.equals("money_reward")) {
            return money_reward;
        }
        else if (name.equals("item_rewards")) {
            return item_rewards;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onPlayerCompletesSkyBlockChallenge(ChallengeCompleteEvent event) {
        challenge = new Element(event.getChallengeName());
        xp_reward = new Element(event.getExpReward());
        money_reward = new Element(event.getMoneyReward());
        item_rewards = new dList();
        for (String i : event.getItemRewards()) {
            dItem item = dItem.valueOf(i);
            if (item != null) {
                item_rewards.add(item.identify());
            }
        }
        this.event = event;
        fire(event);
    }

}
