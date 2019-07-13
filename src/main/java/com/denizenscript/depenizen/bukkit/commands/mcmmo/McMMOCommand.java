package com.denizenscript.depenizen.bukkit.commands.mcmmo;

import com.denizenscript.denizencore.objects.Argument;
import com.gmail.nossr50.api.ExperienceAPI;
import com.gmail.nossr50.api.PartyAPI;
import com.gmail.nossr50.config.Config;
import com.gmail.nossr50.config.experience.ExperienceConfig;
import com.gmail.nossr50.database.DatabaseManagerFactory;
import com.gmail.nossr50.datatypes.skills.SkillType;
import com.gmail.nossr50.party.PartyManager;
import com.denizenscript.denizen.BukkitScriptEntryData;
import com.denizenscript.denizen.objects.dPlayer;
import com.denizenscript.denizen.utilities.debugging.Debug;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.Element;
import com.denizenscript.denizencore.objects.ArgumentHelper;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;

public class McMMOCommand extends AbstractCommand {

    // <--[command]
    // @Name mcMMO
    // @Syntax mcmmo [add/remove/set] [levels/xp/xprate/vampirism/hardcore/leader] (skill:<skill>) (state:{toggle}/true/false) (qty:<#>) (party:<party>)
    // @Group Depenizen
    // @Plugin Depenizen, mcMMO
    // @Required 1
    // @Short Edits mcMMO information.

    // @Description
    // This command allows you to add or remove skill levels and experience for players, add or remove
    // players to/from parties, set the level, xp, xprate, vampirism state, hardcore state of a player's
    // skill, or set the leader of a party.

    // @Tags
    // <player.mcmmo. * >

    // @Usage
    // Use to add 5 levels to a player's skill.
    // - mcmmo add levels skill:acrobatics qty:5

    // @Usage
    // Use to remove a player from a party.
    // - mcmmo remove player:SerpentX party:SerpentPeople

    // @Usage
    // Use to set vampirism mode for a player's skill.
    // - mcmmo set vampirism skill:woodcutting state:true

    // -->

    private enum Action {ADD, REMOVE, SET}

    private enum State {TRUE, FALSE, TOGGLE}

    private enum Type {XP, LEVELS, TOGGLE, XPRATE, LEADER, VAMPIRISM, HARDCORE}

    public McMMOCommand() {

    }

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {

        // Iterate through arguments
        for (Argument arg : ArgumentHelper.interpret(scriptEntry.getArguments())) {

            if (!scriptEntry.hasObject("action")
                    && arg.matchesEnum(Action.values())) {
                scriptEntry.addObject("action", arg.asElement());
            }

            else if (!scriptEntry.hasObject("state")
                    && arg.matchesPrefix("state")
                    && arg.matchesEnum(State.values())) {
                scriptEntry.addObject("state", arg.asElement());
            }

            else if (!scriptEntry.hasObject("party")
                    && arg.matchesPrefix("party")) {
                scriptEntry.addObject("party", arg.asElement());
            }

            else if (!scriptEntry.hasObject("skill")
                    && arg.matchesPrefix("skill")) {
                scriptEntry.addObject("skill", arg.asElement());
            }

            else if (!scriptEntry.hasObject("qty")
                    && arg.matchesPrefix("q", "qty", "quantity")
                    && arg.matchesPrimitive(ArgumentHelper.PrimitiveType.Double)) {
                scriptEntry.addObject("qty", arg.asElement());
            }

            else if (!scriptEntry.hasObject("type")
                    && arg.matchesEnum(Type.values())) {
                scriptEntry.addObject("type", arg.asElement());
            }
            else {
                arg.reportUnhandled();
            }

        }

        if (!scriptEntry.hasObject("action")) {
            throw new InvalidArgumentsException("Must specify a valid action!");
        }

        if (!scriptEntry.hasObject("type")) {
            throw new InvalidArgumentsException("Must specify a valid type!");
        }

        scriptEntry.defaultObject("state", new Element("TOGGLE"))
                .defaultObject("qty", new Element(-1));

    }

    @Override
    public void execute(ScriptEntry scriptEntry) {

        BukkitScriptEntryData scriptEntryData = (BukkitScriptEntryData) scriptEntry.entryData;

        // Get objects
        Element action = scriptEntry.getElement("action");
        Element state = scriptEntry.getElement("state");
        Element type = scriptEntry.getElement("type");
        Element qty = scriptEntry.getElement("qty");
        Element party = scriptEntry.getElement("party");
        Element skill = scriptEntry.getElement("skill");

        dPlayer player = scriptEntryData.getPlayer();

        // Report to dB
        Debug.report(scriptEntry, getName(), action.debug() + type.debug() + (state != null ? state.debug() : "") + qty.debug()
                + (party != null ? party.debug() : "") + (skill != null ? skill.debug() : ""));

        switch (Action.valueOf(action.asString().toUpperCase())) {

            case ADD: {

                if (qty.asDouble() >= 0 && skill != null && player != null) {
                    switch (Type.valueOf(type.asString().toUpperCase())) {
                        case LEVELS: {
                            if (player.isOnline()) {
                                ExperienceAPI.addLevel(player.getPlayerEntity(), skill.asString(), qty.asInt());
                            }
                            else {
                                ExperienceAPI.addLevelOffline(player.getName(), skill.asString(), qty.asInt());
                            }
                            break;
                        }
                        case XP: {
                            if (player.isOnline()) {
                                ExperienceAPI.addRawXP(player.getPlayerEntity(), skill.asString(), qty.asFloat());
                            }
                            else {
                                ExperienceAPI.addRawXPOffline(player.getName(), skill.asString(), qty.asFloat());
                            }
                        }
                    }
                }
                else if (party != null && PartyManager.getParty(party.asString()) != null) {
                    PartyAPI.addToParty(player.getPlayerEntity(), party.asString());
                }
                break;

            }
            case REMOVE: {

                if (qty.asDouble() >= 0 && skill != null && player != null) {
                    switch (Type.valueOf(type.asString().toUpperCase())) {
                        case LEVELS: {
                            if (player.isOnline()) {
                                ExperienceAPI.setLevel(player.getPlayerEntity(), skill.asString(), ExperienceAPI.getLevel(player.getPlayerEntity(), skill.asString()) - qty.asInt());
                            }
                            else {
                                ExperienceAPI.setLevelOffline(player.getName(), skill.asString(), ExperienceAPI.getLevelOffline(player.getName(), skill.asString()) - qty.asInt());
                            }
                            break;
                        }
                        case XP: {
                            if (player.isOnline()) {
                                ExperienceAPI.removeXP(player.getPlayerEntity(), skill.asString(), qty.asInt());
                            }
                            else {
                                ExperienceAPI.removeXPOffline(player.getName(), skill.asString(), qty.asInt());
                            }
                            break;
                        }
                    }
                }
                else if (player != null && player.isOnline() && party != null && PartyManager.getParty(party.asString()) != null) {
                    if (PartyAPI.getPartyLeader(party.asString()).equals(player.getName())) {
                        PartyManager.disbandParty(PartyManager.getParty(party.asString()));
                    }

                    else {
                        PartyAPI.removeFromParty(player.getPlayerEntity());
                    }
                }
                else if (player != null) {
                    DatabaseManagerFactory.getDatabaseManager().removeUser(player.getName());
                }
                break;

            }
            case SET: {

                if (qty.asDouble() >= 0 && skill != null && player != null) {
                    switch (Type.valueOf(type.asString().toUpperCase())) {
                        case LEVELS: {
                            if (player.isOnline()) {
                                ExperienceAPI.setLevel(player.getPlayerEntity(), skill.asString(), qty.asInt());
                            }
                            else {
                                ExperienceAPI.setLevelOffline(player.getName(), skill.asString(), qty.asInt());
                            }
                            break;
                        }
                        case XP: {
                            if (player.isOnline()) {
                                ExperienceAPI.setXP(player.getPlayerEntity(), skill.asString(), qty.asInt());
                            }
                            else {
                                ExperienceAPI.setXPOffline(player.getName(), skill.asString(), qty.asInt());
                            }
                            break;
                        }
                    }
                }
                else {
                    switch (Type.valueOf(type.asString().toUpperCase())) {
                        case LEADER: {
                            if (party != null && PartyManager.getParty(party.asString()) != null) {
                                PartyAPI.setPartyLeader(party.asString(), player.getName());
                            }
                            break;
                        }
                        case XPRATE: {
                            if (qty.asInt() > 0) {
                                ExperienceConfig.getInstance().setExperienceGainsGlobalMultiplier(qty.asInt());
                            }
                            break;
                        }
                        case HARDCORE: {
                            if (skill == null) {
                                return;
                            }
                            SkillType skillType = SkillType.getSkill(skill.asString());
                            boolean isEnabled = Config.getInstance().getHardcoreStatLossEnabled(skillType);

                            switch (State.valueOf(state.asString().toUpperCase())) {
                                case TOGGLE: {
                                    Config.getInstance().setHardcoreStatLossEnabled(skillType, !isEnabled);
                                    break;
                                }
                                case TRUE: {
                                    Config.getInstance().setHardcoreStatLossEnabled(skillType, true);
                                    break;
                                }
                                case FALSE: {
                                    Config.getInstance().setHardcoreStatLossEnabled(skillType, false);
                                    break;
                                }
                            }
                            break;
                        }
                        case VAMPIRISM: {
                            if (skill == null) {
                                return;
                            }
                            SkillType skillType = SkillType.getSkill(skill.asString());
                            boolean isEnabled = Config.getInstance().getHardcoreVampirismEnabled(skillType);

                            switch (State.valueOf(state.asString().toUpperCase())) {
                                case TOGGLE: {
                                    Config.getInstance().setHardcoreVampirismEnabled(skillType, !isEnabled);
                                    break;
                                }
                                case TRUE: {
                                    Config.getInstance().setHardcoreVampirismEnabled(skillType, true);
                                    break;
                                }
                                case FALSE: {
                                    Config.getInstance().setHardcoreVampirismEnabled(skillType, false);
                                    break;
                                }
                            }
                            break;
                        }
                    }
                }
                break;

            }
        }

    }
}
