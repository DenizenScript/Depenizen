package com.denizenscript.depenizen.bukkit.properties.auraskills;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import dev.aurelium.auraskills.api.AuraSkillsApi;
import dev.aurelium.auraskills.api.skill.Skills;

public class AuraSkillsPlayerProperties {

    public static void register() {

        // <--[tag]
        // @attribute <PlayerTag.auraskills_level[<skill>]>
        // @returns ElementTag(Number)
        // @plugin Depenizen, AuraSkills
        // @description
        // Returns the skill level of a player.
        // -->
        PlayerTag.tagProcessor.registerTag(ElementTag.class, ElementTag.class, "auraskills_level", (attribute, player, value) -> {
            Skills skill;
            try {
                skill = Skills.valueOf(value.toString().toUpperCase());
            }
            catch (IllegalArgumentException e) {
                return null;
            }
            return new ElementTag(getAuraApi().getUser(player.getUUID()).getSkillLevel(skill));
        });

        // <--[mechanism]
        // @object PlayerTag
        // @name auraskills_agility_level
        // @input ElementTag(Number)
        // @plugin Depenizen, AuraSkills
        // @description
        // Changes a player's agility level.
        // @tags
        // <PlayerTag.auraskills_level[<skill>]>
        // -->
        PlayerTag.registerOnlineOnlyMechanism("auraskills_agility_level", ElementTag.class, (player, mechanism, value) -> {
            if (mechanism.requireInteger()) {
                if (value.asInt() < 0) {
                    mechanism.echoError("Invalid auraskills agility level: " + value.asInt());
                }
                else {
                    getAuraApi().getUser(player.getUUID()).setSkillLevel(Skills.AGILITY, value.asInt());
                }
            }
        });

        // <--[mechanism]
        // @object PlayerTag
        // @name auraskills_alchemy_level
        // @input ElementTag(Number)
        // @plugin Depenizen, AuraSkills
        // @description
        // Changes a player's alchemy level.
        // @tags
        // <PlayerTag.auraskills_level[<skill>]>
        // -->
        PlayerTag.registerOnlineOnlyMechanism("auraskills_alchemy_level", ElementTag.class, (player, mechanism, value) -> {
            if (mechanism.requireInteger()) {
                if (value.asInt() < 0) {
                    mechanism.echoError("Invalid auraskills alchemy level: " + value.asInt());
                }
                else {
                    getAuraApi().getUser(player.getUUID()).setSkillLevel(Skills.ALCHEMY, value.asInt());
                }
            }
        });

        // <--[mechanism]
        // @object PlayerTag
        // @name auraskills_archery_level
        // @input ElementTag(Number)
        // @plugin Depenizen, AuraSkills
        // @description
        // Changes a player's archery level.
        // @tags
        // <PlayerTag.auraskills_level[<skill>]>
        // -->
        PlayerTag.registerOnlineOnlyMechanism("auraskills_archery_level", ElementTag.class, (player, mechanism, value) -> {
            if (mechanism.requireInteger()) {
                if (value.asInt() < 0) {
                    mechanism.echoError("Invalid auraskills archery level: " + value.asInt());
                }
                else {
                    getAuraApi().getUser(player.getUUID()).setSkillLevel(Skills.ARCHERY, value.asInt());
                }
            }
        });

        // <--[mechanism]
        // @object PlayerTag
        // @name auraskills_defense_level
        // @input ElementTag(Number)
        // @plugin Depenizen, AuraSkills
        // @description
        // Changes a player's defense level.
        // @tags
        // <PlayerTag.auraskills_level[<skill>]>
        // -->
        PlayerTag.registerOnlineOnlyMechanism("auraskills_defense_level", ElementTag.class, (player, mechanism, value) -> {
            if (mechanism.requireInteger()) {
                if (value.asInt() < 0) {
                    mechanism.echoError("Invalid auraskills defense level: " + value.asInt());
                }
                else {
                    getAuraApi().getUser(player.getUUID()).setSkillLevel(Skills.DEFENSE, value.asInt());
                }
            }
        });

        // <--[mechanism]
        // @object PlayerTag
        // @name auraskills_enchanting_level
        // @input ElementTag(Number)
        // @plugin Depenizen, AuraSkills
        // @description
        // Changes a player's enchanting level.
        // @tags
        // <PlayerTag.auraskills_level[<skill>]>
        // -->
        PlayerTag.registerOnlineOnlyMechanism("auraskills_enchanting_level", ElementTag.class, (player, mechanism, value) -> {
            if (mechanism.requireInteger()) {
                if (value.asInt() < 0) {
                    mechanism.echoError("Invalid auraskills enchanting level: " + value.asInt());
                }
                else {
                    getAuraApi().getUser(player.getUUID()).setSkillLevel(Skills.ENCHANTING, value.asInt());
                }
            }
        });

        // <--[mechanism]
        // @object PlayerTag
        // @name auraskills_excavation_level
        // @input ElementTag(Number)
        // @plugin Depenizen, AuraSkills
        // @description
        // Changes a player's excavation level.
        // @tags
        // <PlayerTag.auraskills_level[<skill>]>
        // -->
        PlayerTag.registerOnlineOnlyMechanism("auraskills_excavation_level", ElementTag.class, (player, mechanism, value) -> {
            if (mechanism.requireInteger()) {
                if (value.asInt() < 0) {
                    mechanism.echoError("Invalid auraskills excavation level: " + value.asInt());
                }
                else {
                    getAuraApi().getUser(player.getUUID()).setSkillLevel(Skills.EXCAVATION, value.asInt());
                }
            }
        });

        // <--[mechanism]
        // @object PlayerTag
        // @name auraskills_farming_level
        // @input ElementTag(Number)
        // @plugin Depenizen, AuraSkills
        // @description
        // Changes a player's farming level.
        // @tags
        // <PlayerTag.auraskills_level[<skill>]>
        // -->
        PlayerTag.registerOnlineOnlyMechanism("auraskills_farming_level", ElementTag.class, (player, mechanism, value) -> {
            if (mechanism.requireInteger()) {
                if (value.asInt() < 0) {
                    mechanism.echoError("Invalid auraskills farming level: " + value.asInt());
                }
                else {
                    getAuraApi().getUser(player.getUUID()).setSkillLevel(Skills.FARMING, value.asInt());
                }
            }
        });

        // <--[mechanism]
        // @object PlayerTag
        // @name auraskills_fighting_level
        // @input ElementTag(Number)
        // @plugin Depenizen, AuraSkills
        // @description
        // Changes a player's fighting level.
        // @tags
        // <PlayerTag.auraskills_level[<skill>]>
        // -->
        PlayerTag.registerOnlineOnlyMechanism("auraskills_fighting_level", ElementTag.class, (player, mechanism, value) -> {
            if (mechanism.requireInteger()) {
                if (value.asInt() < 0) {
                    mechanism.echoError("Invalid auraskills fighting level: " + value.asInt());
                }
                else {
                    getAuraApi().getUser(player.getUUID()).setSkillLevel(Skills.FIGHTING, value.asInt());
                }
            }
        });

        // <--[mechanism]
        // @object PlayerTag
        // @name auraskills_fishing_level
        // @input ElementTag(Number)
        // @plugin Depenizen, AuraSkills
        // @description
        // Changes a player's fishing level.
        // @tags
        // <PlayerTag.auraskills_level[<skill>]>
        // -->
        PlayerTag.registerOnlineOnlyMechanism("auraskills_fishing_level", ElementTag.class, (player, mechanism, value) -> {
            if (mechanism.requireInteger()) {
                if (value.asInt() < 0) {
                    mechanism.echoError("Invalid auraskills fishing level: " + value.asInt());
                }
                else {
                    getAuraApi().getUser(player.getUUID()).setSkillLevel(Skills.FISHING, value.asInt());
                }
            }
        });

        // <--[mechanism]
        // @object PlayerTag
        // @name auraskills_foraging_level
        // @input ElementTag(Number)
        // @plugin Depenizen, AuraSkills
        // @description
        // Changes a player's foraging level.
        // @tags
        // <PlayerTag.auraskills_level[<skill>]>
        // -->
        PlayerTag.registerOnlineOnlyMechanism("auraskills_foraging_level", ElementTag.class, (player, mechanism, value) -> {
            if (mechanism.requireInteger()) {
                if (value.asInt() < 0) {
                    mechanism.echoError("Invalid auraskills foraging level: " + value.asInt());
                }
                else {
                    getAuraApi().getUser(player.getUUID()).setSkillLevel(Skills.FORAGING, value.asInt());
                }
            }
        });

        // <--[mechanism]
        // @object PlayerTag
        // @name auraskills_mining_level
        // @input ElementTag(Number)
        // @plugin Depenizen, AuraSkills
        // @description
        // Changes a player's mining level.
        // @tags
        // <PlayerTag.auraskills_level[<skill>]>
        // -->
        PlayerTag.registerOnlineOnlyMechanism("auraskills_mining_level", ElementTag.class, (player, mechanism, value) -> {
            if (mechanism.requireInteger()) {
                if (value.asInt() < 0) {
                    mechanism.echoError("Invalid auraskills mining level: " + value.asInt());
                }
                else {
                    getAuraApi().getUser(player.getUUID()).setSkillLevel(Skills.MINING, value.asInt());
                }
            }
        });
    }

    public static AuraSkillsApi getAuraApi() {
        return AuraSkillsApi.get();
    }
}
