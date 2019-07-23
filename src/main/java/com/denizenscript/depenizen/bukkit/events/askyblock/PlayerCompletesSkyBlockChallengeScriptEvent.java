package com.denizenscript.depenizen.bukkit.events.askyblock;

import com.wasteofplastic.askyblock.events.ChallengeCompleteEvent;
import com.denizenscript.denizen.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.denizencore.scripts.containers.ScriptContainer;
import com.denizenscript.denizencore.utilities.CoreUtilities;
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
    public ElementTag challenge;
    public ElementTag xp_reward;
    public ElementTag money_reward;
    public ListTag item_rewards;

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
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(new PlayerTag(event.getPlayer()), null);
    }

    @Override
    public ObjectTag getContext(String name) {
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
        challenge = new ElementTag(event.getChallengeName());
        xp_reward = new ElementTag(event.getExpReward());
        money_reward = new ElementTag(event.getMoneyReward());
        item_rewards = new ListTag();
        for (String i : event.getItemRewards()) {
            ItemTag item = ItemTag.valueOf(i);
            if (item != null) {
                item_rewards.add(item.identify());
            }
        }
        this.event = event;
        fire(event);
    }
}
