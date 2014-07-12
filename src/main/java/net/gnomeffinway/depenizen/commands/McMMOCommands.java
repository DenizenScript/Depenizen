package net.gnomeffinway.depenizen.commands;

import com.gmail.nossr50.api.ExperienceAPI;
import com.gmail.nossr50.api.PartyAPI;
import com.gmail.nossr50.config.Config;
import com.gmail.nossr50.config.experience.ExperienceConfig;
import com.gmail.nossr50.database.DatabaseManager;
import com.gmail.nossr50.database.DatabaseManagerFactory;
import com.gmail.nossr50.database.FlatfileDatabaseManager;
import com.gmail.nossr50.datatypes.database.DatabaseUpdateType;
import com.gmail.nossr50.datatypes.party.Party;
import com.gmail.nossr50.datatypes.skills.SkillType;
import com.gmail.nossr50.party.PartyManager;
import com.gmail.nossr50.util.Misc;
import net.aufdemrand.denizen.exceptions.CommandExecutionException;
import net.aufdemrand.denizen.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizen.objects.Element;
import net.aufdemrand.denizen.objects.aH;
import net.aufdemrand.denizen.scripts.ScriptEntry;
import net.aufdemrand.denizen.scripts.commands.AbstractCommand;
import net.aufdemrand.denizen.utilities.debugging.dB;
import org.bukkit.entity.Player;

public class McMMOCommands extends AbstractCommand {

    // <--[command]
    // @Name mcMMO
    // @Syntax mcmmo [add/remove/set] [levels/xp/xprate/vampirism/hardcore/leader] (skill:<skill>) (state:{toggle}/true/false) (qty:<#>) (party:<party>)
    // @Group Depenizen
    // @Plugin mcMMO
    // @Required 1
    // @Stable untested
    // @Short Edits mcMMO information.
    // @Author GnomeffinWay

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

    public McMMOCommands() {

    }

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {

        // Iterate through arguments
        for (aH.Argument arg : aH.interpret(scriptEntry.getArguments())) {

            if (!scriptEntry.hasObject("action")
                    && arg.matchesEnum(Action.values()))
                scriptEntry.addObject("action", Action.valueOf(arg.getValue().toUpperCase()));

            else if (!scriptEntry.hasObject("state")
                    && arg.matchesPrefix("state")
                    && arg.matchesEnum(State.values()))
                scriptEntry.addObject("state", State.valueOf(arg.getValue().toUpperCase()));

            else if (!scriptEntry.hasObject("party")
                    && arg.matchesPrefix("party"))
                scriptEntry.addObject("party", arg.asElement());

            else if (!scriptEntry.hasObject("skill")
                    && arg.matchesPrefix("skill"))
                scriptEntry.addObject("skill", arg.asElement());

            else if (!scriptEntry.hasObject("qty")
                    && arg.matchesPrefix("q, qty, quantity")
                    && arg.matchesPrimitive(aH.PrimitiveType.Double))
                scriptEntry.addObject("qty", arg.asElement());

            else if (!scriptEntry.hasObject("type")
                    && arg.matchesEnum(Type.values()))
                scriptEntry.addObject("type", Type.valueOf(arg.getValue().toUpperCase()));

        }

        scriptEntry.defaultObject("state", State.TOGGLE).defaultObject("qty", new Element(-1)).defaultObject("skill", new Element("")).defaultObject("party", new Element(""));

    }

    @Override
    public void execute(ScriptEntry scriptEntry) throws CommandExecutionException {
        // Get objects
        Action action = (Action) scriptEntry.getObject("action");
        State state = (State) scriptEntry.getObject("state");
        Type type = (Type) scriptEntry.getObject("type");
        Player player = scriptEntry.getPlayer().getPlayerEntity();
        double qty = scriptEntry.getElement("qty").asDouble();
        String party = scriptEntry.getElement("party").asString();
        String skill = scriptEntry.getElement("skill").asString();

        /*
        // Report to dB TODO: fix... this, this is not right
        dB.report(scriptEntry, getName(),
                aH.debugObj("NPC", scriptEntry.getNPC().toString())
                        + aH.debugObj("Action", action.toString())
                        + aH.debugObj("Player", player.getName())
                        + aH.debugObj("State", String.valueOf(state))
                        + aH.debugObj("Type", String.valueOf(type))
                        + aH.debugObj("Party", String.valueOf(party))
                        + aH.debugObj("Skill", String.valueOf(skill))
                        + aH.debugObj("Qty", String.valueOf(qty)));
        */

        switch (action) {

            case ADD: {

                if (qty >= 0 && !skill.equals("")) {
                    if (type == Type.LEVELS) {
                        if (player.isOnline()) {
                            ExperienceAPI.addLevel(player, skill, (int) qty);
                        } else {
                            ExperienceAPI.addLevelOffline(player.getName(), skill, (int) qty);
                        }
                    } else if (type == Type.XP) {
                        if (player.isOnline()) {
                            ExperienceAPI.addRawXP(player, skill, (float) qty);
                        } else {
                            ExperienceAPI.addRawXPOffline(player.getName(), skill, (float) qty);
                        }
                    }
                } else if (PartyManager.getParty(party) != null) {
                    PartyAPI.addToParty(player, party);
                }
                break;

            }
            case REMOVE: {

                if (qty >= 0 && !skill.equals("")) {
                    if (type == Type.LEVELS) {
                        if (player.isOnline())
                            ExperienceAPI.setLevel(player, skill, ExperienceAPI.getLevel(player, skill) - (int) qty);
                        else
                            ExperienceAPI.setLevelOffline(player.getName(), skill, ExperienceAPI.getLevel(player, skill) - (int) qty);
                    } else if (type == Type.XP) {
                        if (player.isOnline())
                            ExperienceAPI.removeXP(player, skill, (int) qty);
                        else
                            ExperienceAPI.removeXPOffline(player.getName(), skill, (int) qty);
                    }
                }
                else if (PartyManager.getParty(party) != null) {
                    if (PartyAPI.getPartyLeader(party).equals(player.getName()))
                        PartyManager.disbandParty(PartyManager.getParty(party));

                    else
                        PartyAPI.removeFromParty(player);
                }
                else if (player != null) {
                    DatabaseManagerFactory.getDatabaseManager().removeUser(player.getName());
                }
                break;

            }
            case SET: {

                if (qty >= 0 && !skill.equals("")) {
                    if (type == Type.LEVELS) {
                        if (player.isOnline())
                            ExperienceAPI.setLevel(player, skill, (int) qty);
                        else
                            ExperienceAPI.setLevelOffline(player.getName(), skill, (int) qty);
                    } else if (type == Type.XP) {
                        if (player.isOnline())
                            ExperienceAPI.setXP(player, skill, (int) qty);
                        else
                            ExperienceAPI.setXPOffline(player.getName(), skill, (int) qty);
                    }
                } else if (type == Type.LEADER && !party.equals(""))
                    PartyAPI.setPartyLeader(party, player.getName());
                else if(type == Type.XPRATE)
                    ExperienceConfig.getInstance().setExperienceGainsGlobalMultiplier(qty);
                else if(type == Type.HARDCORE) {
                    SkillType skillType = SkillType.getSkill(skill);
                    boolean isEnabled = Config.getInstance().getHardcoreStatLossEnabled(skillType);

                    if(state == State.TOGGLE)
                        Config.getInstance().setHardcoreStatLossEnabled(skillType, !isEnabled);
                    else if(state == State.TRUE)
                        Config.getInstance().setHardcoreStatLossEnabled(skillType, true);
                    else if(state == State.FALSE)
                        Config.getInstance().setHardcoreStatLossEnabled(skillType, false);

                }
                else if(type == Type.VAMPIRISM) {
                    SkillType skillType = SkillType.getSkill(skill);
                    boolean isEnabled = Config.getInstance().getHardcoreVampirismEnabled(skillType);

                    if(state == State.TOGGLE)
                        Config.getInstance().setHardcoreVampirismEnabled(skillType, !isEnabled);
                    else if(state == State.TRUE)
                        Config.getInstance().setHardcoreVampirismEnabled(skillType, true);
                    else if(state == State.FALSE)
                        Config.getInstance().setHardcoreVampirismEnabled(skillType, false);

                }
                break;

            }
        }
    }
}
