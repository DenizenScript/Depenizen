package com.denizenscript.depenizen.bukkit.commands.quests;

import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.objects.ArgumentHelper;

import me.blackvein.quests.Quest;
import me.blackvein.quests.Quester;
import me.blackvein.quests.Quests;
import me.blackvein.quests.exceptions.InvalidStageException;
import me.blackvein.quests.util.Lang;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.Utilities;
import com.denizenscript.denizen.utilities.debugging.Debug;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.denizenscript.depenizen.bukkit.bridges.QuestsBridge;

public class QuestsCommand extends AbstractCommand {

    // <--[command]
    // @Name Quests
    // @Syntax quests [add/remove/set] (quest_id:<quest_id>) (stage:<#>) (points:<#>) (state:true/false)
    // @Group Depenizen
    // @Plugin Depenizen, Quests
    // @Required 1
    // @Short Edits quest player information.
    //
    // @Description
    // This command allows you to give, quit, or set the stage of a quest for a player, or to add, subtract, or set
    // the amount of Quest Points that a player holds. When modifying quests, the ID (read: NOT the name) must be
    // specified. Numerical stage number must be present when setting progress. State indicates whether to perform
    // associated checks (see Usage). If not modifying a quest, points must be included as a numerical value.
    //
    // @Tags
    // None
    //
    // @Usage
    // Use to force the player to take quest with ID custom1, ignoring requirements.
    // - quests add quest_id:custom1 state:true
    //
    // @Usage
    // Use to force the player to quit quest with ID custom2, notifying said player.
    // - quests remove quest_id:custom2 state:true
    //
    // @Usage
    // Use to force the player into specified stage 2 of quest with ID custom3.
    // - quests set quest_id:custom3 stage:2
    //
    // @Usage
    // Use to give the player 100 Quest Points.
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
            
            else if (!scriptEntry.hasObject("stage")
                    && arg.matchesPrefix("stage", "stage")
                    && arg.matchesPrimitive(ArgumentHelper.PrimitiveType.Integer)) {
                scriptEntry.addObject("stage", arg.asElement());
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
        
        if (!Utilities.entryHasPlayer(scriptEntry)) {
            throw new InvalidArgumentsException("Must have a linked player!");
        }

        if (!scriptEntry.hasObject("action")) {
            throw new InvalidArgumentsException("Must specify a valid action!");
        }
        
        scriptEntry.defaultObject("state", new ElementTag("TRUE"));
    }

    @Override
    public void execute(ScriptEntry scriptEntry) {
        Quests quests = (Quests) QuestsBridge.instance.plugin;

        ElementTag action = scriptEntry.getElement("action");
        ElementTag questId = scriptEntry.getElement("quest_id");
        ElementTag stageNum = scriptEntry.getElement("stage");
        ElementTag points = scriptEntry.getElement("points");
        ElementTag state = scriptEntry.getElement("state");

        PlayerTag player = Utilities.getEntryPlayer(scriptEntry);

        if (scriptEntry.dbCallShouldDebug()) {
            Debug.report(scriptEntry, getName(), action.debug() 
                    + (questId != null ? questId.debug() : "") 
                    + (stageNum != null ? stageNum.debug() : "") 
                    + (points != null ? points.debug() : "")
                    + state.debug());
        }

        switch (Action.valueOf(action.asString().toUpperCase())) {

            case ADD: {

                if (questId != null && state != null) {
                    for (Quest quest : quests.getQuests()) {
                        if (quest.getId().equals(questId.asString())) {
                            
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
                else {
                    Debug.echoError("Must specify either a quest_id and state value, or a points value.");
                }
                break;
            }
            case REMOVE: {
                
                if (questId != null && state != null) {
                    for (Quest quest : quests.getQuests()) {
                        if (quest.getId().equals(questId.asString())) {
                            Quester quester = quests.getQuester(player.getPlayerEntity().getUniqueId());
                            quester.hardQuit(quest);
                            if (state.asBoolean() && player.isOnline()) {
                                player.getPlayerEntity().sendMessage(Lang.get(player.getPlayerEntity(), "questQuit").replace("<quest>", quest.getName()));
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
                else {
                    Debug.echoError("Must specify either a quest_id and state value, or a points value.");
                }
                break;
            }
            case SET: {

                if (questId != null && stageNum != null && stageNum.asInt() > 0) {
                    for (Quest quest : quests.getQuests()) {
                        if (quest.getId().equals(questId.asString())) {
                            try {
                                quest.setStage(quests.getQuester(player.getPlayerEntity().getUniqueId()), stageNum.asInt());
                            } 
                            catch (InvalidStageException ex) {
                                Debug.echoError(ex);
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
                else {
                    Debug.echoError("Must specify either a quest_id and stage value, or a points value.");
                }
                break;
            }
        }

    }
    
    private void reloadData(Quester quester) {
        if (quester == null) {
            return;
        }
        quester.saveData();
        quester.loadData();
    }
}

