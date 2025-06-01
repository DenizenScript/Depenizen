package com.denizenscript.depenizen.bukkit.properties.auraskills;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.MapTag;
import com.denizenscript.denizencore.utilities.text.StringHolder;
import dev.aurelium.auraskills.api.AuraSkillsApi;
import dev.aurelium.auraskills.api.skill.Skills;
import dev.aurelium.auraskills.api.user.SkillsUser;

import java.util.Map;

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
            SkillsUser user = AuraSkillsApi.get().getUser(player.getUUID());
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
            SkillsUser user = AuraSkillsApi.get().getUser(player.getUUID());
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
        // Changes a player's skill level. Inputted as "<skill>=<level>".
        // @tags
        // <PlayerTag.auraskills_levels>
        // @example
        // # Sets the player's agility level to 5 and their excavation level to 3.
        // - adjust <player> auraskills_levels:agility=5;excavation=3
        // -->
        PlayerTag.registerOnlineOnlyMechanism("auraskills_levels", MapTag.class, (player, mechanism, value) -> {
            SkillsUser user = AuraSkillsApi.get().getUser(player.getUUID());
            for (Map.Entry<StringHolder, ObjectTag> entry : value.entrySet()) {
                try {
                    Skills.valueOf(entry.getKey().toString().toUpperCase());
                }
                catch (Exception e) {
                    mechanism.echoError("'" + entry.getKey() + "' is not a valid skill.");
                    continue;
                }
                ElementTag element = entry.getValue().asElement();
                if (element.isInt() && element.asInt() >= 0) {
                    user.setSkillLevel(Skills.valueOf(entry.getKey().toString().toUpperCase()), element.asInt());
                }
                else {
                    mechanism.echoError("'" + entry.getValue() + "' is not a valid level.");
                }
            }
        });

        // <--[mechanism]
        // @object PlayerTag
        // @name auraskills_experience
        // @input MapTag
        // @plugin Depenizen, AuraSkills
        // @description
        // Changes a player's skill experience. Inputted as "<skill>=<experience>".
        // @tags
        // <PlayerTag.auraskills_experience>
        // @example
        // # Sets the player's agility experience to 500 and their excavation experience to 300.
        // - adjust <player> auraskills_experience:AGILITY=500;EXCAVATION=300
        // -->
        PlayerTag.registerOnlineOnlyMechanism("auraskills_experience", MapTag.class, (player, mechanism, value) -> {
            SkillsUser user = AuraSkillsApi.get().getUser(player.getUUID());
            for (Map.Entry<StringHolder, ObjectTag> entry : value.entrySet()) {
                try {
                    Skills.valueOf(entry.getKey().toString().toUpperCase());
                }
                catch (Exception e) {
                    mechanism.echoError("'" + entry.getKey() + "' is not a valid skill.");
                    continue;
                }
                ElementTag element = entry.getValue().asElement();
                if (element.isFloat() && element.asDouble() >= 0) {
                    user.setSkillXp(Skills.valueOf(entry.getKey().toString().toUpperCase()), element.asDouble());
                }
                else {
                    mechanism.echoError("'" + entry.getValue() + "' is not a valid experience amount.");
                }
            }
        });
    }
}
