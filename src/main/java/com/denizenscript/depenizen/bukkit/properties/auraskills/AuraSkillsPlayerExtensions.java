package com.denizenscript.depenizen.bukkit.properties.auraskills;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.MapTag;
import dev.aurelium.auraskills.api.AuraSkillsApi;
import dev.aurelium.auraskills.api.skill.Skills;
import dev.aurelium.auraskills.api.user.SkillsUser;

public class AuraSkillsPlayerExtensions {

    public static void register() {

        // <--[tag]
        // @attribute <PlayerTag.auraskills_levels>
        // @returns MapTag
        // @plugin Depenizen, AuraSkills
        // @mechanism PlayerTag.auraskills_levels
        // @description
        // Returns a map of a player's skill levels in the format "<skill>=<level>".
        // @example
        // # Returns 'map@[FARMING=0;FORAGING=0;MINING=0;FISHING=0;EXCAVATION=0;ARCHERY=0;FIGHTING=0;DEFENSE=0;AGILITY=0;ENDURANCE=0;ALCHEMY=0;ENCHANTING=0;SORCERY=0;HEALING=0;FORGING=0]' on a brand new player.
        // - narrate <player.auraskills_levels>
        // -->
        PlayerTag.tagProcessor.registerTag(MapTag.class,  "auraskills_levels", (attribute, player) -> {
            MapTag levels = new MapTag();
            SkillsUser user = getAuraApi().getUser(player.getUUID());
            for (Skills skill : Skills.values()) {
                levels.putObject(skill.name(), new ElementTag(user.getSkillLevel(skill)));
            }
            return levels;
        });

        // <--[tag]
        // @attribute <PlayerTag.auraskills_experience>
        // @returns MapTag
        // @plugin Depenizen, AuraSkills
        // @mechanism PlayerTag.auraskills_experience
        // @description
        // Returns a map of a player's skill experience in the format "<skill>=<experience>".
        // @example
        // # Returns 'map@[FARMING=0;FORAGING=0;MINING=0;FISHING=0;EXCAVATION=0;ARCHERY=0;FIGHTING=0;DEFENSE=0;AGILITY=0;ENDURANCE=0;ALCHEMY=0;ENCHANTING=0;SORCERY=0;HEALING=0;FORGING=0]' on a brand new player.
        // - narrate <player.auraskills_experience>
        // -->
        PlayerTag.tagProcessor.registerTag(MapTag.class,  "auraskills_experience", (attribute, player) -> {
            MapTag levels = new MapTag();
            SkillsUser user = getAuraApi().getUser(player.getUUID());
            for (Skills skill : Skills.values()) {
                levels.putObject(skill.name(), new ElementTag(user.getSkillXp(skill)));
            }
            return levels;
        });

        // <--[mechanism]
        // @object PlayerTag
        // @name auraskills_levels
        // @input MapTag
        // @plugin Depenizen, AuraSkills
        // @description
        // Changes a player's skill levels. Inputted as "skill=level".
        // @tags
        // <PlayerTag.auraskills_levels>
        // @example
        // # Sets the player's agility level to 5 and their excavation level to 3.
        // - adjust <player> auraskills_levels:agility=5;excavation=3
        // -->
        PlayerTag.registerOnlineOnlyMechanism("auraskills_levels", MapTag.class, (player, mechanism, levels) -> {
            boolean valid = false;
            SkillsUser user = getAuraApi().getUser(player.getUUID());
            for (Skills skill : Skills.values()) {
                if (levels.containsKey(skill.name().toLowerCase())) {
                    if (!levels.getElement(skill.name()).isInt() || levels.getElement(skill.name()).asInt() < 0) {
                        mechanism.echoError("'" + levels.getElement(skill.name()).asInt() + "' is not a valid level.");
                    }
                    else {
                        user.setSkillLevel(skill, levels.getElement(skill.name()).asInt());
                    }
                    valid = true;
                }
            }
            if (!valid) {
                mechanism.echoError("There are no valid skills as part of the input.");
            }
        });

        // <--[mechanism]
        // @object PlayerTag
        // @name auraskills_experience
        // @input MapTag
        // @plugin Depenizen, AuraSkills
        // @description
        // Changes a player's skill experience. Inputted as "skill=experience".
        // @tags
        // <PlayerTag.auraskills_experience>
        // @example
        // # Sets the player's agility experience to 500 and their excavation experience to 300.
        // - adjust <player> auraskills_experienceAGILITY=500;EXCAVATION=300
        // -->
        PlayerTag.registerOnlineOnlyMechanism("auraskills_experience", MapTag.class, (player, mechanism, experience) -> {
            boolean valid = false;
            SkillsUser user = getAuraApi().getUser(player.getUUID());
            for (Skills skill : Skills.values()) {
                if (experience.containsKey(skill.name().toLowerCase())) {
                    if (!experience.getElement(skill.name()).isDouble() || experience.getElement(skill.name()).asDouble() < 0) {
                        mechanism.echoError("'" + experience.getElement(skill.name()).asDouble() + "' is not a valid experience amount.");
                    }
                    else {
                        user.setSkillXp(skill, experience.getElement(skill.name()).asDouble());
                    }
                    valid = true;
                }
            }
            if (!valid) {
                mechanism.echoError("There are no valid skills as part of the input.");
            }
        });
    }

    public static AuraSkillsApi getAuraApi() {
        return AuraSkillsApi.get();
    }
}
