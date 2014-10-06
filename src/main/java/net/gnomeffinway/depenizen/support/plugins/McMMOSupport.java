package net.gnomeffinway.depenizen.support.plugins;

import com.gmail.nossr50.api.ExperienceAPI;
import com.gmail.nossr50.api.PartyAPI;
import com.gmail.nossr50.datatypes.party.Party;
import com.gmail.nossr50.datatypes.skills.SkillType;
import com.gmail.nossr50.party.PartyManager;
import net.aufdemrand.denizen.objects.Element;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.tags.Attribute;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.gnomeffinway.depenizen.commands.McMMOCommands;
import net.gnomeffinway.depenizen.support.Support;

public class McMMOSupport extends Support {

    public McMMOSupport() {
        registerAdditionalTags("party");
        new McMMOCommands().activate().as("MCMMO").withOptions("see documentation", 1);
    }

    @Override
    public String playerTags(dPlayer p, Attribute attribute) {

        if (attribute.startsWith("mcmmo")) {

            attribute = attribute.fulfill(1);

            // <--[tag]
            // @attribute <p@player.mcmmo.level[<skill>]>
            // @returns Element(Integer)
            // @description
            // Returns the player's level in a skill. If no skill is specified,
            // this returns the player's overall level.
            // @plugin mcMMO
            // -->
            if (attribute.startsWith("level")) {
                if (!attribute.hasContext(1)) {
                    if (p.isOnline()) {
                        return new Element(ExperienceAPI.getPowerLevel(p.getPlayerEntity()))
                                .getAttribute(attribute.fulfill(1));
                    } else {
                        return new Element(ExperienceAPI.getPowerLevelOffline(p.getPlayerEntity().getName()))
                                .getAttribute(attribute.fulfill(1));
                    }
                } else {
                    if (p.isOnline()) {
                        return new Element(ExperienceAPI.getLevel(p.getPlayerEntity(), attribute.getContext(1)))
                                .getAttribute(attribute.fulfill(1));
                    } else {
                        return new Element(ExperienceAPI.getLevelOffline(p.getName(), attribute.getContext(1)))
                                .getAttribute(attribute.fulfill(1));
                    }
                }
            }

            // <--[tag]
            // @attribute <p@player.mcmmo.party>
            // @returns Element
            // @description
            // Returns the name of the player's party.
            // @plugin mcMMO
            // -->
            else if (attribute.startsWith("party"))
                return new Element(PartyAPI.getPartyName(p.getPlayerEntity()))
                        .getAttribute(attribute.fulfill(1));

            else if (attribute.startsWith("xp")) {

                String skill = attribute.getContext(1);
                attribute = attribute.fulfill(1);

                // <--[tag]
                // @attribute <p@player.mcmmo.xp.to_next_level[<skill>]>
                // @returns Element(Integer)
                // @description
                // Returns the amount of experience a player has left to level up
                // in a skill.
                // @plugin mcMMO
                // -->
                if (attribute.startsWith("tonextlevel") || attribute.startsWith("to_next_level")) {
                    if (p.isOnline()) {
                        return new Element(ExperienceAPI.getXPToNextLevel(p.getPlayerEntity(), skill))
                                .getAttribute(attribute.fulfill(1));
                    } else {
                        return new Element(ExperienceAPI.getOfflineXPToNextLevel(p.getName(), skill))
                                .getAttribute(attribute.fulfill(1));
                    }
                }

                // <--[tag]
                // @attribute <p@player.mcmmo.xp[<skill>].level>
                // @returns Element(Integer)
                // @description
                // Returns the player's experience level in a skill.
                // @plugin mcMMO
                // -->
                else if (attribute.startsWith("level")) {
                    if (p.isOnline()) {
                        return new Element(ExperienceAPI.getLevel(p.getPlayerEntity(), skill))
                                .getAttribute(attribute.fulfill(1));
                    } else {
                        return new Element(ExperienceAPI.getLevelOffline(p.getName(), skill))
                                .getAttribute(attribute.fulfill(1));
                    }
                }

                // <--[tag]
                // @attribute <p@player.mcmmo.xp[<skill>]>
                // @returns Element(Integer)
                // @description
                // Returns the player's amount of experience in a skill.
                // @plugin mcMMO
                // -->
                else if (p.isOnline()) {
                    return new Element(ExperienceAPI.getXP(p.getPlayerEntity(), skill))
                            .getAttribute(attribute.fulfill(0));
                } else {
                    return new Element(ExperienceAPI.getOfflineXP(p.getName(), skill))
                            .getAttribute(attribute.fulfill(0));
                }


            }

            // <--[tag]
            // @attribute <p@player.mcmmo.rank[<skill>]>
            // @returns Element(Integer)
            // @description
            // Returns the player's current rank in a skill. If no skill is specified,
            // this returns the player's overall rank.
            // @plugin mcMMO
            // -->
            else if (attribute.startsWith("rank")) {
                if (!attribute.hasContext(1)) {
                    return new Element(ExperienceAPI.getPlayerRankOverall(p.getName()))
                            .getAttribute(attribute.fulfill(1));
                } else {
                    if (SkillType.getSkill(attribute.getContext(1)) != null)
                        return new Element(ExperienceAPI.getPlayerRankSkill(p.getName(), attribute.getContext(1)))
                                .getAttribute(attribute.fulfill(1));
                    else
                        dB.echoError("Skill type '" + attribute.getContext(1) + "' does not exist!");
                }
            }

        }

        return null;

    }

    @Override
    public String additionalTags(Attribute attribute) {

        if (attribute.startsWith("party")) {

            if (!attribute.hasContext(1) || PartyManager.getParty(attribute.getContext(1)) == null)
                return null;

            Party party = PartyManager.getParty(attribute.getContext(1));
            attribute = attribute.fulfill(1);

            // <--[tag]
            // @attribute <party[<party>].leader>
            // @returns dPlayer
            // @description
            // Returns the leader of the party.
            // @plugin mcMMO
            // -->
            if (attribute.startsWith("leader"))
                return dPlayer.valueOf(party.getLeader()).getAttribute(attribute.fulfill(1));

            // <--[tag]
            // @attribute <party[<party>].player_count>
            // @returns Element(Integer)
            // @description
            // Returns the number of players in the party.
            // @plugin mcMMO
            // -->
            else if (attribute.startsWith("playercount") || attribute.startsWith("player_count"))
                return new Element(party.getMembers().size()).getAttribute(attribute.fulfill(1));

        }

        return null;

    }
}
