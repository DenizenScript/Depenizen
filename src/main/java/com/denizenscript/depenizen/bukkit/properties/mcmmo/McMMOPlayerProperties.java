package com.denizenscript.depenizen.bukkit.properties.mcmmo;

import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.gmail.nossr50.api.ExperienceAPI;
import com.gmail.nossr50.datatypes.skills.SkillType;
import com.denizenscript.denizen.objects.dPlayer;
import com.denizenscript.denizen.utilities.debugging.Debug;
import com.denizenscript.denizencore.objects.Element;
import com.denizenscript.denizencore.objects.dObject;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.depenizen.bukkit.objects.mcmmo.dParty;

public class McMMOPlayerProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "McMMOPlayer";
    }

    @Override
    public void adjust(Mechanism mechanism) {
        // None
    }

    public static boolean describes(dObject object) {
        return object instanceof dPlayer;
    }

    public static McMMOPlayerProperties getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new McMMOPlayerProperties((dPlayer) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "mcmmo"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    private McMMOPlayerProperties(dPlayer player) {
        this.player = player;
    }

    dPlayer player = null;

    @Override
    public String getAttribute(Attribute attribute) {

        if (attribute.startsWith("mcmmo")) {

            attribute = attribute.fulfill(1);

            // <--[tag]
            // @attribute <p@player.mcmmo.level[<skill>]>
            // @returns Element(Number)
            // @description
            // Returns the player's level in a skill. If no skill is specified,
            // this returns the player's overall level.
            // @Plugin Depenizen, mcMMO
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
            // @Plugin Depenizen, mcMMO
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
                // @attribute <p@player.mcmmo.xp[<skill>].to_next_level>
                // @returns Element(Number)
                // @description
                // Returns the amount of experience a player needs to level up
                // in a skill.
                // @Plugin Depenizen, mcMMO
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
                // @returns Element(Number)
                // @description
                // Returns the player's experience level in a skill.
                // @Plugin Depenizen, mcMMO
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
                // @returns Element(Number)
                // @description
                // Returns the player's amount of experience in a skill.
                // @Plugin Depenizen, mcMMO
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
            // @returns Element(Number)
            // @description
            // Returns the player's current rank in a skill. If no skill is specified,
            // this returns the player's overall rank.
            // @Plugin Depenizen, mcMMO
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
                        Debug.echoError("Skill type '" + attribute.getContext(1) + "' does not exist!");
                    }
                }
            }

        }

        return null;

    }
}
