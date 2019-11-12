package com.denizenscript.depenizen.bukkit.events.quests;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import me.blackvein.quests.events.quester.QuesterPostChangeStageEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerQuestStageChangeScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // quests player changes quest stage
    // quests player changes <quest> stage
    //
    // @Regex ^on quests player changes [^\s]+ stage$
    //
    // @Cancellable true
    //
    // @Triggers when a player changes stage in a quest from the Quests plugin. Note that stages don't have unique IDs.
    //
    // @Context
    // <context.quest> returns the ID of the quest.
    //
    // @Plugin Depenizen, Quests
    //
    // -->

    public PlayerQuestStageChangeScriptEvent() {
        instance = this;
    }

    public static PlayerQuestStageChangeScriptEvent instance;
    public QuesterPostChangeStageEvent event;

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("quests player changes");
    }

    @Override
    public boolean matches(ScriptPath path) {
        String questName = path.eventArgLowerAt(3);
        if (!questName.equals("quest") && !questName.equals(CoreUtilities.toLowerCase(event.getQuest().getId()))) {
            return false;
        }

        return super.matches(path);
    }

    @Override
    public String getName() {
        return "QuestsPlayerQuestStageChange";
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(new PlayerTag(event.getQuester().getOfflinePlayer()), null);
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("quest")) {
            return new ElementTag(event.getQuest().getId());
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onQuestEvent(QuesterPostChangeStageEvent event) {
        this.event = event;
        fire(event);
    }
}
