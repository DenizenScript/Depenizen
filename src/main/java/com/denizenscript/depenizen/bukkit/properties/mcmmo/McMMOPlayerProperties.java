package com.denizenscript.depenizen.bukkit.properties.mcmmo;

import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.gmail.nossr50.api.ExperienceAPI;
import com.gmail.nossr50.datatypes.skills.PrimarySkillType;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.depenizen.bukkit.objects.mcmmo.PartyTag;

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

    public static boolean describes(ObjectTag object) {
        return object instanceof PlayerTag;
    }

    public static McMMOPlayerProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new McMMOPlayerProperties((PlayerTag) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "mcmmo"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    public McMMOPlayerProperties(PlayerTag player) {
        this.player = player;
    }

    PlayerTag player;

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {

        if (attribute.startsWith("mcmmo")) {

            attribute = attribute.fulfill(1);

            // <--[tag]
            // @attribute <PlayerTag.mcmmo.level[(<skill>)]>
            // @returns ElementTag(Number)
            // @plugin Depenizen, mcMMO
            // @description
            // Returns the player's level in a skill.
            // If no skill is specified, this returns the player's overall level.
            // -->
            if (attribute.startsWith("level")) {
                if (!attribute.hasParam()) {
                    if (player.isOnline()) {
                        return new ElementTag(ExperienceAPI.getPowerLevel(player.getPlayerEntity()))
                                .getObjectAttribute(attribute.fulfill(1));
                    }
                    else {
                        return new ElementTag(ExperienceAPI.getPowerLevelOffline(player.getUUID()))
                                .getObjectAttribute(attribute.fulfill(1));
                    }
                }
                else {
                    if (player.isOnline()) {
                        return new ElementTag(ExperienceAPI.getLevel(player.getPlayerEntity(), attribute.getParam()))
                                .getObjectAttribute(attribute.fulfill(1));
                    }
                    else {
                        return new ElementTag(ExperienceAPI.getLevelOffline(player.getUUID(), attribute.getParam()))
                                .getObjectAttribute(attribute.fulfill(1));
                    }
                }
            }

            // <--[tag]
            // @attribute <PlayerTag.mcmmo.party>
            // @returns ElementTag
            // @plugin Depenizen, mcMMO
            // @description
            // Returns the name of the player's party.
            // -->
            else if (attribute.startsWith("party")) {
                PartyTag party = PartyTag.forPlayer(player);
                if (party != null) {
                    return party.getObjectAttribute(attribute.fulfill(1));
                }
            }

            else if (attribute.startsWith("xp")) {

                String skill = attribute.getParam();
                attribute = attribute.fulfill(1);

                // <--[tag]
                // @attribute <PlayerTag.mcmmo.xp[<skill>].to_next_level>
                // @returns ElementTag(Number)
                // @plugin Depenizen, mcMMO
                // @description
                // Returns the amount of experience a player needs to level up in a skill.
                // -->
                if (attribute.startsWith("tonextlevel") || attribute.startsWith("to_next_level")) {
                    if (player.isOnline()) {
                        return new ElementTag(ExperienceAPI.getXPToNextLevel(player.getPlayerEntity(), skill))
                                .getObjectAttribute(attribute.fulfill(1));
                    }
                    else {
                        return new ElementTag(ExperienceAPI.getOfflineXPToNextLevel(player.getUUID(), skill))
                                .getObjectAttribute(attribute.fulfill(1));
                    }
                }

                // <--[tag]
                // @attribute <PlayerTag.mcmmo.xp[<skill>].level>
                // @returns ElementTag(Number)
                // @plugin Depenizen, mcMMO
                // @description
                // Returns the player's experience level in a skill.
                // -->
                else if (attribute.startsWith("level")) {
                    if (player.isOnline()) {
                        return new ElementTag(ExperienceAPI.getLevel(player.getPlayerEntity(), skill))
                                .getObjectAttribute(attribute.fulfill(1));
                    }
                    else {
                        return new ElementTag(ExperienceAPI.getLevelOffline(player.getUUID(), skill))
                                .getObjectAttribute(attribute.fulfill(1));
                    }
                }

                // <--[tag]
                // @attribute <PlayerTag.mcmmo.xp[<skill>]>
                // @returns ElementTag(Number)
                // @plugin Depenizen, mcMMO
                // @description
                // Returns the player's amount of experience in a skill.
                // -->
                else if (player.isOnline()) {
                    return new ElementTag(ExperienceAPI.getXP(player.getPlayerEntity(), skill))
                            .getObjectAttribute(attribute.fulfill(0));
                }
                else {
                    return new ElementTag(ExperienceAPI.getOfflineXP(player.getUUID(), skill))
                            .getObjectAttribute(attribute.fulfill(0));
                }

            }

            // <--[tag]
            // @attribute <PlayerTag.mcmmo.rank[(<skill>)]>
            // @returns ElementTag(Number)
            // @plugin Depenizen, mcMMO
            // @description
            // Returns the player's current rank in a skill.
            // If no skill is specified, this returns the player's overall rank.
            // -->
            else if (attribute.startsWith("rank")) {
                if (!attribute.hasParam()) {
                    return new ElementTag(ExperienceAPI.getPlayerRankOverall(player.getName()))
                            .getObjectAttribute(attribute.fulfill(1));
                }
                else {
                    if (PrimarySkillType.getSkill(attribute.getParam()) != null) {
                        return new ElementTag(ExperienceAPI.getPlayerRankSkill(player.getUUID(), attribute.getParam()))
                                .getObjectAttribute(attribute.fulfill(1));
                    }
                    else if (!attribute.hasAlternative()) {
                        Debug.echoError("Skill type '" + attribute.getParam() + "' does not exist!");
                    }
                }
            }

        }

        return null;

    }
}
