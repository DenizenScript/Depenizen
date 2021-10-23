package com.denizenscript.depenizen.bukkit.commands.mcmmo;

import com.denizenscript.denizen.utilities.Utilities;
import com.denizenscript.denizencore.objects.Argument;
import com.gmail.nossr50.api.ExperienceAPI;
import com.gmail.nossr50.api.PartyAPI;
import com.gmail.nossr50.config.experience.ExperienceConfig;
import com.gmail.nossr50.datatypes.skills.PrimarySkillType;
import com.gmail.nossr50.mcMMO;
import com.gmail.nossr50.party.PartyManager;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.debugging.Debug;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;

public class McMMOCommand extends AbstractCommand {

    public McMMOCommand() {
        setName("mcmmo");
        setSyntax("mcmmo [add/remove/set] [levels/xp/xprate/vampirism/hardcore/leader] (skill:<skill>) (state:{toggle}/true/false) (quantity:<#>) (party:<party>)");
        setRequiredArguments(1, 6);
    }

    // <--[command]
    // @Name mcMMO
    // @Syntax mcmmo [add/remove/set] [levels/xp/xprate/vampirism/hardcore/leader] (skill:<skill>) (state:{toggle}/true/false) (quantity:<#>) (party:<party>)
    // @Group Depenizen
    // @Plugin Depenizen, mcMMO
    // @Required 1
    // @Maximum 6
    // @Short Edits mcMMO information.
    //
    // @Description
    // This command allows you to add or remove skill levels and experience for players, add or remove
    // players to/from parties, set the level, xp, xprate, vampirism state, hardcore state of a player's
    // skill, or set the leader of a party.
    //
    // @Tags
    // <PlayerTag.mcmmo.party>
    //
    // @Usage
    // Use to add 5 levels to a player's skill.
    // - mcmmo add levels skill:acrobatics quantity:5
    //
    // @Usage
    // Use to remove a different player from a party.
    // - mcmmo remove player:<[player]> party:SerpentPeople
    //
    // @Usage
    // Use to set vampirism mode for a player's skill.
    // - mcmmo set vampirism skill:woodcutting state:true
    //
    // -->

    private enum Action {ADD, REMOVE, SET}

    private enum State {TRUE, FALSE, TOGGLE}

    private enum Type {XP, LEVELS, TOGGLE, XPRATE, LEADER, VAMPIRISM, HARDCORE}

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {
        for (Argument arg : scriptEntry) {
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
            else if (!scriptEntry.hasObject("quantity")
                    && arg.matchesPrefix("q", "qty", "quantity")
                    && arg.matchesFloat()) {
                scriptEntry.addObject("quantity", arg.asElement());
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
        scriptEntry.defaultObject("state", new ElementTag("TOGGLE"))
                .defaultObject("quantity", new ElementTag(-1));
    }

    @Override
    public void execute(ScriptEntry scriptEntry) {
        ElementTag action = scriptEntry.getElement("action");
        ElementTag state = scriptEntry.getElement("state");
        ElementTag type = scriptEntry.getElement("type");
        ElementTag quantity = scriptEntry.getElement("quantity");
        ElementTag party = scriptEntry.getElement("party");
        ElementTag skill = scriptEntry.getElement("skill");
        PlayerTag player = Utilities.getEntryPlayer(scriptEntry);
        if (scriptEntry.dbCallShouldDebug()) {
            Debug.report(scriptEntry, getName(), action.debug() + type.debug() + (state != null ? state.debug() : "") + quantity.debug()
                    + (party != null ? party.debug() : "") + (skill != null ? skill.debug() : ""));
        }
        switch (Action.valueOf(action.asString().toUpperCase())) {
            case ADD: {
                if (quantity.asDouble() >= 0 && skill != null && player != null) {
                    switch (Type.valueOf(type.asString().toUpperCase())) {
                        case LEVELS: {
                            if (player.isOnline()) {
                                ExperienceAPI.addLevel(player.getPlayerEntity(), skill.asString(), quantity.asInt());
                            }
                            else {
                                ExperienceAPI.addLevelOffline(player.getName(), skill.asString(), quantity.asInt());
                            }
                            break;
                        }
                        case XP: {
                            if (player.isOnline()) {
                                ExperienceAPI.addRawXP(player.getPlayerEntity(), skill.asString(), quantity.asFloat());
                            }
                            else {
                                ExperienceAPI.addRawXPOffline(player.getName(), skill.asString(), quantity.asFloat());
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
                if (quantity.asDouble() >= 0 && skill != null && player != null) {
                    switch (Type.valueOf(type.asString().toUpperCase())) {
                        case LEVELS: {
                            if (player.isOnline()) {
                                ExperienceAPI.setLevel(player.getPlayerEntity(), skill.asString(), ExperienceAPI.getLevel(player.getPlayerEntity(), skill.asString()) - quantity.asInt());
                            }
                            else {
                                ExperienceAPI.setLevelOffline(player.getName(), skill.asString(), ExperienceAPI.getLevelOffline(player.getName(), skill.asString()) - quantity.asInt());
                            }
                            break;
                        }
                        case XP: {
                            if (player.isOnline()) {
                                ExperienceAPI.removeXP(player.getPlayerEntity(), skill.asString(), quantity.asInt());
                            }
                            else {
                                ExperienceAPI.removeXPOffline(player.getName(), skill.asString(), quantity.asInt());
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
                    mcMMO.getDatabaseManager().removeUser(player.getName(), player.getUUID());
                }
                break;
            }
            case SET: {
                if (quantity.asDouble() >= 0 && skill != null && player != null) {
                    switch (Type.valueOf(type.asString().toUpperCase())) {
                        case LEVELS: {
                            if (player.isOnline()) {
                                ExperienceAPI.setLevel(player.getPlayerEntity(), skill.asString(), quantity.asInt());
                            }
                            else {
                                ExperienceAPI.setLevelOffline(player.getName(), skill.asString(), quantity.asInt());
                            }
                            break;
                        }
                        case XP: {
                            if (player.isOnline()) {
                                ExperienceAPI.setXP(player.getPlayerEntity(), skill.asString(), quantity.asInt());
                            }
                            else {
                                ExperienceAPI.setXPOffline(player.getName(), skill.asString(), quantity.asInt());
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
                            if (quantity.asInt() > 0) {
                                ExperienceConfig.getInstance().setExperienceGainsGlobalMultiplier(quantity.asInt());
                            }
                            break;
                        }
                        case HARDCORE: {
                            if (skill == null) {
                                return;
                            }
                            PrimarySkillType skillType = PrimarySkillType.getSkill(skill.asString());
                            boolean isEnabled = mcMMO.p.getGeneralConfig().getHardcoreStatLossEnabled(skillType);
                            switch (State.valueOf(state.asString().toUpperCase())) {
                                case TOGGLE: {
                                    mcMMO.p.getGeneralConfig().setHardcoreStatLossEnabled(skillType, !isEnabled);
                                    break;
                                }
                                case TRUE: {
                                    mcMMO.p.getGeneralConfig().setHardcoreStatLossEnabled(skillType, true);
                                    break;
                                }
                                case FALSE: {
                                    mcMMO.p.getGeneralConfig().setHardcoreStatLossEnabled(skillType, false);
                                    break;
                                }
                            }
                            break;
                        }
                        case VAMPIRISM: {
                            if (skill == null) {
                                return;
                            }
                            PrimarySkillType skillType = PrimarySkillType.getSkill(skill.asString());
                            boolean isEnabled = mcMMO.p.getGeneralConfig().getHardcoreVampirismEnabled(skillType);
                            switch (State.valueOf(state.asString().toUpperCase())) {
                                case TOGGLE: {
                                    mcMMO.p.getGeneralConfig().setHardcoreVampirismEnabled(skillType, !isEnabled);
                                    break;
                                }
                                case TRUE: {
                                    mcMMO.p.getGeneralConfig().setHardcoreVampirismEnabled(skillType, true);
                                    break;
                                }
                                case FALSE: {
                                    mcMMO.p.getGeneralConfig().setHardcoreVampirismEnabled(skillType, false);
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
