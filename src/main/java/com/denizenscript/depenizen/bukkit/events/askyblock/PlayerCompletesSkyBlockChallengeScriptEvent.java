package com.denizenscript.depenizen.bukkit.events.askyblock;

import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.wasteofplastic.askyblock.events.ChallengeCompleteEvent;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerCompletesSkyBlockChallengeScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // player completes skyblock challenge
    //
    // @Regex ^on player completes skyblock challenge$
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
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->

    public ChallengeCompleteEvent event;
    public ElementTag challenge;
    public ElementTag xp_reward;
    public ElementTag money_reward;

    public PlayerCompletesSkyBlockChallengeScriptEvent() {
    }

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("player completes skyblock challenge");
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(event.getPlayer());
    }

    @Override
    public ObjectTag getContext(String name) {
        switch (name) {
            case "challenge":
                return challenge;
            case "xp_reward":
                return xp_reward;
            case "money_reward":
                return money_reward;
            case "item_rewards":
                ListTag item_rewards = new ListTag();
                for (String i : event.getItemRewards()) {
                    ItemTag item = ItemTag.valueOf(i, CoreUtilities.basicContext);
                    if (item != null) {
                        item_rewards.addObject(item);
                    }
                }
                return item_rewards;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onPlayerCompletesSkyBlockChallenge(ChallengeCompleteEvent event) {
        challenge = new ElementTag(event.getChallengeName());
        xp_reward = new ElementTag(event.getExpReward());
        money_reward = new ElementTag(event.getMoneyReward());
        this.event = event;
        fire(event);
    }
}
