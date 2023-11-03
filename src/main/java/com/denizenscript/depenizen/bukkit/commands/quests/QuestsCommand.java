package com.denizenscript.depenizen.bukkit.commands.quests;

import com.denizenscript.denizencore.objects.Argument;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.Utilities;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.denizenscript.depenizen.bukkit.bridges.QuestsBridge;
import me.pikamug.quests.BukkitQuestsPlugin;
import me.pikamug.quests.player.Quester;
import me.pikamug.quests.quests.Quest;
import me.pikamug.quests.util.BukkitLang;

public class QuestsCommand extends AbstractCommand {

    public QuestsCommand() {
        setName("quests");
        setSyntax("quests [add/remove/set] (quest:<quest_id>) (stage:<#>) (points:<#>) (override_checks:{true}/false)");
        setRequiredArguments(2, 5);
    }

    // <--[command]
    // @Name Quests
    // @Syntax quests [add/remove/set] (quest:<quest_id>) (stage:<#>) (points:<#>) (override_checks:{true}/false)
    // @Group Depenizen
    // @Plugin Depenizen, Quests
    // @Required 2
    // @Maximum 5
    // @Short Edits quest player information.
    //
    // @Description
    // This command allows you to give, quit, or set the stage of a quest for a player, or to add, subtract, or set the amount of Quest Points that a player holds.
    // When modifying quests, the ID (read: NOT the name) must be specified.
    // Numerical stage number must be present when setting progress.
    // 'override_checks' indicates whether to bypass associated checks (see Usage).
    // If not modifying a quest, points must be included as a numerical value.
    //
    // @Tags
    // None
    //
    // @Usage
    // Use to force the player to take quest with ID custom1, ignoring requirements.
    // - quests add quest:custom1 override_checks:true
    //
    // @Usage
    // Use to force the player to quit quest with ID custom2, notifying said player.
    // - quests remove quest:custom2 override_checks:true
    //
    // @Usage
    // Use to force the player into specified stage 2 of quest with ID custom3.
    // - quests set quest:custom3 stage:2
    //
    // @Usage
    // Use to give the player 100 Quest Points.
    // - quests add points:100
    //
    // -->

    private enum Action {ADD, REMOVE, SET}

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {

        for (Argument arg : scriptEntry) {
            if (!scriptEntry.hasObject("action")
                    && arg.matchesEnum(Action.class)) {
                scriptEntry.addObject("action", arg.asElement());
            }
            else if (!scriptEntry.hasObject("quest")
                    && arg.matchesPrefix("quest")) {
                scriptEntry.addObject("quest", arg.asElement());
            }
            else if (!scriptEntry.hasObject("stage")
                    && arg.matchesPrefix("stage")
                    && arg.matchesInteger()) {
                scriptEntry.addObject("stage", arg.asElement());
            }
            else if (!scriptEntry.hasObject("points")
                    && arg.matchesPrefix("points")
                    && arg.matchesInteger()) {
                scriptEntry.addObject("points", arg.asElement());
            }
            else if (!scriptEntry.hasObject("override_checks")
                    && arg.matchesPrefix("override_checks")
                    && arg.matchesBoolean()) {
                scriptEntry.addObject("override_checks", arg.asElement());
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
        scriptEntry.defaultObject("override_checks", new ElementTag("true"));
    }

    @Override
    public void execute(ScriptEntry scriptEntry) {
        BukkitQuestsPlugin quests = (BukkitQuestsPlugin) QuestsBridge.instance.plugin;
        ElementTag action = scriptEntry.getElement("action");
        ElementTag questId = scriptEntry.getElement("quest");
        ElementTag stageNum = scriptEntry.getElement("stage");
        ElementTag points = scriptEntry.getElement("points");
        ElementTag override_checks = scriptEntry.getElement("override_checks");
        PlayerTag player = Utilities.getEntryPlayer(scriptEntry);
        if (scriptEntry.dbCallShouldDebug()) {
            Debug.report(scriptEntry, getName(), action, questId, stageNum, points, override_checks);
        }
        switch (Action.valueOf(action.asString().toUpperCase())) {
            case ADD -> {
                if (questId != null) {
                    for (Quest quest : quests.getLoadedQuests()) {
                        if (quest.getId().equals(questId.asString())) {
                            quests.getQuester(player.getPlayerEntity().getUniqueId()).takeQuest(quest, override_checks.asBoolean());
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
                    Debug.echoError("Must specify either a quest or a points value.");
                }
            }
            case REMOVE -> {
                if (questId != null) {
                    for (Quest quest : quests.getLoadedQuests()) {
                        if (quest.getId().equals(questId.asString())) {
                            Quester quester = quests.getQuester(player.getPlayerEntity().getUniqueId());
                            quester.hardQuit(quest);
                            if (override_checks.asBoolean() && player.isOnline()) {
                                player.getPlayerEntity().sendMessage(BukkitLang.get(player.getPlayerEntity(), "questQuit").replace("<quest>", quest.getName()));
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
                    quester.setQuestPoints(Math.max(n, 0));
                    reloadData(quester);
                }
                else {
                    Debug.echoError("Must specify either a quest value or a points value.");
                }
            }
            case SET -> {
                if (questId != null && stageNum != null && stageNum.asInt() > 0) {
                    for (Quest quest : quests.getLoadedQuests()) {
                        if (quest.getId().equals(questId.asString())) {
                            try {
                                quest.setStage(quests.getQuester(player.getPlayerEntity().getUniqueId()), stageNum.asInt());
                            }
                            catch (IndexOutOfBoundsException ex) {
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
                    Debug.echoError("Must specify either a quest and stage value, or a points value.");
                }
            }
        }
    }

    private void reloadData(Quester quester) {
        if (quester == null) {
            return;
        }
        quester.saveData();
        quester.hasData();
    }
}
