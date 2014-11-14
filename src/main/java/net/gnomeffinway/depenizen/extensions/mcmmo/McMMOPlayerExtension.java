package net.gnomeffinway.depenizen.extensions.mcmmo;

import com.gmail.nossr50.api.ExperienceAPI;
import com.gmail.nossr50.api.PartyAPI;
import com.gmail.nossr50.datatypes.skills.SkillType;
import net.aufdemrand.denizen.objects.Element;
import net.aufdemrand.denizen.objects.dObject;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.tags.Attribute;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.gnomeffinway.depenizen.extensions.dObjectExtension;

public class McMMOPlayerExtension extends dObjectExtension {

    public static boolean describes(dObject pl) {
        return pl instanceof dPlayer;
    }

    public static McMMOPlayerExtension getFrom(dObject pl) {
        if (!describes(pl)) return null;
        else return new McMMOPlayerExtension((dPlayer) pl);
    }

    private McMMOPlayerExtension(dPlayer pl) {
        p = pl;
    }

    dPlayer p = null;

    @Override
    public String getAttribute(Attribute attribute) {

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
                    else if (!attribute.hasAlternative())
                        dB.echoError("Skill type '" + attribute.getContext(1) + "' does not exist!");
                }
            }

        }

        return null;

    }

}
