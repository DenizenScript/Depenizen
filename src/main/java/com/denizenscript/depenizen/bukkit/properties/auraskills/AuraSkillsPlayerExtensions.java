package com.denizenscript.depenizen.bukkit.properties.auraskills;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.MapTag;
import dev.aurelium.auraskills.api.AuraSkillsApi;
import dev.aurelium.auraskills.api.skill.Skill;
import dev.aurelium.auraskills.api.skill.Skills;

public class AuraSkillsPlayerExtensions {

    public static void register() {

        // <--[tag]
        // @attribute <PlayerTag.auraskills_levels>
        // @returns MapTag
        // @plugin Depenizen, AuraSkills
        // @description
        // Returns the skill levels of a player.
        // @mechanism PlayerTag.auraskills_levels
        // -->
        PlayerTag.tagProcessor.registerTag(MapTag.class,  "auraskills_levels", (attribute, player) -> {
            MapTag levels = new MapTag();
            for (Skills skill : Skills.values()) {
                levels.putObject(skill.name(), new ElementTag(getAuraApi().getUser(player.getUUID()).getSkillLevel(skill)));
            }
            return levels;
        });

        // <--[tag]
        // @attribute <PlayerTag.auraskills_experience>
        // @returns MapTag
        // @plugin Depenizen, AuraSkills
        // @description
        // Returns the experience for each skill level of a player.
        // @mechanism PlayerTag.auraskills_experience
        // -->
        PlayerTag.tagProcessor.registerTag(MapTag.class,  "auraskills_experience", (attribute, player) -> {
            MapTag levels = new MapTag();
            for (Skills skill : Skills.values()) {
                levels.putObject(skill.name(), new ElementTag(getAuraApi().getUser(player.getUUID()).getSkillXp(skill)));
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
        // -->
        PlayerTag.registerOnlineOnlyMechanism("auraskills_levels", MapTag.class, (player, mechanism, levels) -> {
            boolean valid = false;
            for (Skills skill : Skills.values()) {
                if (levels.containsKey(skill.name().toLowerCase())) {
                    valid = true;
                    break;
                }
            }
            if (!valid) {
                mechanism.echoError("There are no valid skills as part of the input.");
                return;
            }
            for (Skill skill : Skills.values()) {
                if (levels.containsKey(skill.name())) {
                    if (!levels.getElement(skill.name()).isInt() || levels.getElement(skill.name()).asInt() < 0) {
                        mechanism.echoError("'" + levels.getElement(skill.name()).asInt() + "' is not a valid level.");
                    }
                    else {
                        getAuraApi().getUser(player.getUUID()).setSkillLevel(skill, levels.getElement(skill.name()).asInt());
                    }
                }
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
        // -->
        PlayerTag.registerOnlineOnlyMechanism("auraskills_experience", MapTag.class, (player, mechanism, experience) -> {
            boolean valid = false;
            for (Skills skill : Skills.values()) {
                if (experience.containsKey(skill.name().toLowerCase())) {
                    valid = true;
                    break;
                }
            }
            if (!valid) {
                mechanism.echoError("There are no valid skills as part of the input.");
                return;
            }
            for (Skill skill : Skills.values()) {
                if (experience.containsKey(skill.name())) {
                    if (!experience.getElement(skill.name()).isDouble() || experience.getElement(skill.name()).asDouble() < 0) {
                        mechanism.echoError("'" + experience.getElement(skill.name()).asDouble() + "' is not a valid experience amount.");
                    }
                    else {
                        getAuraApi().getUser(player.getUUID()).setSkillXp(skill, experience.getElement(skill.name()).asDouble());
                    }
                }
            }
        });
    }

    public static AuraSkillsApi getAuraApi() {
        return AuraSkillsApi.get();
    }
}
