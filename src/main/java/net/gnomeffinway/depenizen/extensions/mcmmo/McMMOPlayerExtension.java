package net.gnomeffinway.depenizen.extensions.mcmmo;

import com.gmail.nossr50.api.ExperienceAPI;
import com.gmail.nossr50.datatypes.skills.SkillType;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;
import net.gnomeffinway.depenizen.extensions.dObjectExtension;
import net.gnomeffinway.depenizen.objects.dParty;

public class McMMOPlayerExtension extends dObjectExtension {

    public static boolean describes(dObject object) {
        return object instanceof dPlayer;
    }

    public static McMMOPlayerExtension getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new McMMOPlayerExtension((dPlayer) object);
        }
    }

    private McMMOPlayerExtension(dPlayer player) {
        this.player = player;
    }

    dPlayer player = null;

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
            // @plugin Depenizen, mcMMO
            // -->
            if (attribute.startsWith("level")) {
                if (!attribute.hasContext(1)) {
                    if (player.isOnline()) {
                        return new Element(ExperienceAPI.getPowerLevel(player.getPlayerEntity()))
                                .getAttribute(attribute.fulfill(1));
                    }
                    else {
                        return new Element(ExperienceAPI.getPowerLevelOffline(player.getOfflinePlayer().getUniqueId()))
                                .getAttribute(attribute.fulfill(1));
                    }
                }
                else {
                    if (player.isOnline()) {
                        return new Element(ExperienceAPI.getLevel(player.getPlayerEntity(), attribute.getContext(1)))
                                .getAttribute(attribute.fulfill(1));
                    }
                    else {
                        return new Element(ExperienceAPI.getLevelOffline(player.getOfflinePlayer().getUniqueId(), attribute.getContext(1)))
                                .getAttribute(attribute.fulfill(1));
                    }
                }
            }

            // <--[tag]
            // @attribute <p@player.mcmmo.party>
            // @returns Element
            // @description
            // Returns the name of the player's party.
            // @plugin Depenizen, mcMMO
            // -->
            else if (attribute.startsWith("party")) {
                dParty party = dParty.forPlayer(player);
                if (party != null) {
                    return party.getAttribute(attribute.fulfill(1));
                }
            }

            else if (attribute.startsWith("xp")) {

                String skill = attribute.getContext(1);
                attribute = attribute.fulfill(1);

                // <--[tag]
                // @attribute <p@player.mcmmo.xp.to_next_level[<skill>]>
                // @returns Element(Integer)
                // @description
                // Returns the amount of experience a player has left to level up
                // in a skill.
                // @plugin Depenizen, mcMMO
                // -->
                if (attribute.startsWith("tonextlevel") || attribute.startsWith("to_next_level")) {
                    if (player.isOnline()) {
                        return new Element(ExperienceAPI.getXPToNextLevel(player.getPlayerEntity(), skill))
                                .getAttribute(attribute.fulfill(1));
                    }
                    else {
                        return new Element(ExperienceAPI.getOfflineXPToNextLevel(player.getOfflinePlayer().getUniqueId(), skill))
                                .getAttribute(attribute.fulfill(1));
                    }
                }

                // <--[tag]
                // @attribute <p@player.mcmmo.xp[<skill>].level>
                // @returns Element(Integer)
                // @description
                // Returns the player's experience level in a skill.
                // @plugin Depenizen, mcMMO
                // -->
                else if (attribute.startsWith("level")) {
                    if (player.isOnline()) {
                        return new Element(ExperienceAPI.getLevel(player.getPlayerEntity(), skill))
                                .getAttribute(attribute.fulfill(1));
                    }
                    else {
                        return new Element(ExperienceAPI.getLevelOffline(player.getOfflinePlayer().getUniqueId(), skill))
                                .getAttribute(attribute.fulfill(1));
                    }
                }

                // <--[tag]
                // @attribute <p@player.mcmmo.xp[<skill>]>
                // @returns Element(Integer)
                // @description
                // Returns the player's amount of experience in a skill.
                // @plugin Depenizen, mcMMO
                // -->
                else if (player.isOnline()) {
                    return new Element(ExperienceAPI.getXP(player.getPlayerEntity(), skill))
                            .getAttribute(attribute.fulfill(0));
                }
                else {
                    return new Element(ExperienceAPI.getOfflineXP(player.getOfflinePlayer().getUniqueId(), skill))
                            .getAttribute(attribute.fulfill(0));
                }


            }

            // <--[tag]
            // @attribute <p@player.mcmmo.rank[<skill>]>
            // @returns Element(Integer)
            // @description
            // Returns the player's current rank in a skill. If no skill is specified,
            // this returns the player's overall rank.
            // @plugin Depenizen, mcMMO
            // -->
            else if (attribute.startsWith("rank")) {
                if (!attribute.hasContext(1)) {
                    return new Element(ExperienceAPI.getPlayerRankOverall(player.getName()))
                            .getAttribute(attribute.fulfill(1));
                }
                else {
                    if (SkillType.getSkill(attribute.getContext(1)) != null) {
                        return new Element(ExperienceAPI.getPlayerRankSkill(player.getOfflinePlayer().getUniqueId(), attribute.getContext(1)))
                                .getAttribute(attribute.fulfill(1));
                    }
                    else if (!attribute.hasAlternative()) {
                        dB.echoError("Skill type '" + attribute.getContext(1) + "' does not exist!");
                    }
                }
            }

        }

        return null;

    }

}
