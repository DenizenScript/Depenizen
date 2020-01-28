package com.denizenscript.depenizen.bukkit.commands.quests;

import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.objects.ArgumentHelper;

import me.blackvein.quests.Quest;
import me.blackvein.quests.Quester;
import me.blackvein.quests.Quests;
import me.blackvein.quests.exceptions.InvalidStageException;
import me.blackvein.quests.util.Lang;

import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;

import org.bukkit.entity.Player;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.debugging.Debug;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.denizenscript.depenizen.bukkit.bridges.QuestsBridge;

public class QuestsCommand extends AbstractCommand {

    // <--[command]
    // @Name Quests
    // @Syntax quests [add/remove/set] (quest_id:<quest_id>) (stage_no:<#>) (points:<#>) (state:true/false)
    // @Group Depenizen
    // @Plugin Depenizen, Quests
    // @Required 1
    // @Short Edits quest player information.
    //
    // @Description
    // This command allows you to give, quit, or set the stage of a quest for a player, or to add, subtract, or set
    // the amount of Quest Points that a player holds.
    //
    // @Tags
    // None
    //
    // @Usage
    // Use to force player to take MyFirstQuest quest, ignoring requirements.
    // - quests add quest_id:custom1 state:true
    //
    // @Usage
    // Use to force player to quit MyFirstQuest quest, notifying player.
    // - quests remove quest_id:custom2 state:true
    //
    // @Usage
    // Use to force player into specified stage 2 of MyFirstQuest quest.
    // - quests set quest_id:custom3 stage_no:2
    //
    // @Usage
    // Use to give player 100 Quest Points.
    // - quests add points:100
    //
    // -->

    private enum Action {ADD, REMOVE, SET}
    
    private enum State {TRUE, FALSE}

    public QuestsCommand() {

    }

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {

        // Iterate through arguments
        for (Argument arg : scriptEntry.getProcessedArgs()) {

            if (!scriptEntry.hasObject("action")
                    && arg.matchesEnum(Action.values())) {
                scriptEntry.addObject("action", arg.asElement());
            }

            else if (!scriptEntry.hasObject("quest_id")
                    && arg.matchesPrefix("quest", "quest_id")) {
                scriptEntry.addObject("quest_id", arg.asElement());
            }
            
            else if (!scriptEntry.hasObject("stage_no")
                    && arg.matchesPrefix("stage", "stage_no")
                    && arg.matchesPrimitive(ArgumentHelper.PrimitiveType.Integer)) {
                scriptEntry.addObject("stage_no", arg.asElement());
            }
            
            else if (!scriptEntry.hasObject("points")
                    && arg.matchesPrefix("points")
                    && arg.matchesPrimitive(ArgumentHelper.PrimitiveType.Integer)) {
                scriptEntry.addObject("points", arg.asElement());
            }
            
            else if (!scriptEntry.hasObject("state")
                    && arg.matchesPrefix("state")
                    && arg.matchesEnum(State.values())) {
                scriptEntry.addObject("state", arg.asElement());
            }
            else {
                arg.reportUnhandled();
            }

        }

        if (!scriptEntry.hasObject("action")) {
            throw new InvalidArgumentsException("Must specify a valid action!");
        }
        
        scriptEntry.defaultObject("state", new ElementTag("TRUE"));
    }

    @Override
    public void execute(ScriptEntry scriptEntry) {
        Quests quests = (Quests) QuestsBridge.instance.plugin;
        
        BukkitScriptEntryData scriptEntryData = (BukkitScriptEntryData) scriptEntry.entryData;

        // Get objects
        ElementTag action = scriptEntry.getElement("action");
        ElementTag questId = scriptEntry.getElement("quest_id");
        ElementTag stageNum = scriptEntry.getElement("stage_no");
        ElementTag points = scriptEntry.getElement("points");
        ElementTag state = scriptEntry.getElement("state");

        PlayerTag player = scriptEntryData.getPlayer();

        // Report to dB
        Debug.report(scriptEntry, getName(), action.debug() 
                + (questId != null ? questId.debug() : "") 
                + (stageNum != null ? stageNum.debug() : "") 
                + (points != null ? points.debug() : "") 
                + (state != null ? state.debug() : ""));

        switch (Action.valueOf(action.asString().toUpperCase())) {

            case ADD: {

                if (questId != null && state != null) {
                    for (Quest quest : quests.getQuests()) {
                        if (quest.getId().equals(questId.asString()) && player != null) {
                            quests.getQuester(player.getPlayerEntity().getUniqueId()).takeQuest(quest, state.asBoolean());
                            break;
                        }
                    }
                }
                else if (points != null && points.asInt() > 0) {
                    Quester quester = quests.getQuester(player.getPlayerEntity().getUniqueId());
                    quester.setQuestPoints(quester.getQuestPoints() + points.asInt());
                    reloadData(quester);
                }
                
                break;

            }
            case REMOVE: {
                
                if (questId != null && state != null) {
                    for (Quest quest : quests.getQuests()) {
                        if (quest.getId().equals(questId.asString()) && player != null) {
                            Player p = player.getPlayerEntity();
                            Quester quester = quests.getQuester(p.getUniqueId());
                            quester.hardQuit(quest);
                            if (state.asBoolean() == true && p.isOnline()) {
                                p.sendMessage(Lang.get(p, "questQuit").replace("<quest>", quest.getName()));
                            }
                            reloadData(quester);
                            quester.updateJournal();
                            break;
                        }
                    }
                }
                else if (points != null && points.asInt() > 0) {
                    Quester quester = quests.getQuester(player.getPlayerEntity().getUniqueId());
                    int n = quester.getQuestPoints() - points.asInt();
                    quester.setQuestPoints(n <= 0 ? 0 : n);
                    reloadData(quester);
                }
                
                break;
                
            }
            case SET: {

                if (questId != null && stageNum != null && stageNum.asInt() > 0) {
                    for (Quest quest : quests.getQuests()) {
                        if (quest.getId().equals(questId.asString()) && player != null) {
                            try {
                                quest.setStage(quests.getQuester(player.getPlayerEntity().getUniqueId()), stageNum.asInt());
                            } catch (InvalidStageException e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                    }
                }
                else if (points != null && points.asInt() >= 0) {
                    Quester quester = quests.getQuester(player.getPlayerEntity().getUniqueId());
                    quester.setQuestPoints(points.asInt());
                    reloadData(quester);
                }
                
                break;

            }
        }

    }
    
    private void reloadData(Quester quester) {
        if (quester == null) return;
        quester.saveData();
        quester.loadData();
    }
}

