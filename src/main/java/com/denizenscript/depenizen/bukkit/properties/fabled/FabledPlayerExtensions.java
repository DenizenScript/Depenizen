package com.denizenscript.depenizen.bukkit.properties.fabled;

import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizen.objects.MaterialTag;
import com.denizenscript.depenizen.bukkit.objects.fabled.FabledClassTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.DurationTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import studio.magemonkey.fabled.Fabled;
import studio.magemonkey.fabled.api.classes.FabledClass;
import studio.magemonkey.fabled.api.player.*;
import studio.magemonkey.fabled.api.skills.Skill;
import studio.magemonkey.fabled.manager.FabledAttribute;

public class FabledPlayerExtensions {

    static PlayerClass playerClass;
    static PlayerSkill playerSkill;

    public static PlayerData getPlayer(PlayerTag player) {
        return Fabled.getData(player.getOfflinePlayer());
    }

    public static void register() {

        // <--[tag]
        // @attribute <PlayerTag.fabled_attribute_points[<attribute>]>
        // @returns ElementTag(Number)
        // @plugin Depenizen, Fabled
        // @description
        // Returns how many attribute points a player has put into a specific attribute.
        // -->
        PlayerTag.tagProcessor.registerTag(ElementTag.class, ElementTag.class, "fabled_attribute_points", (attribute, player, input) -> {
            FabledAttribute fabledAttribute = Fabled.getAttributesManager().getAttribute(input.toString());
            return fabledAttribute != null ? new ElementTag(getPlayer(player).getAttribute(input.toString())) : null;
        });

        // <--[tag]
        // @attribute <PlayerTag.fabled_class_exp[<class>]>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, Fabled
        // @description
        // Returns the amount of experience the player has toward the next level in the specified class.
        // -->
        PlayerTag.tagProcessor.registerTag(ElementTag.class, ElementTag.class, "fabled_class_exp", (attribute, player, value) -> {
            playerClass = getPlayer(player).getClass(value.asString());
            return playerClass != null ? new ElementTag(playerClass.getExp()) : null;
        });

        // <--[tag]
        // @attribute <PlayerTag.fabled_class_health[<class>]>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, Fabled
        // @description
        // Returns the amount of health the player gets from the specified class.
        // -->
        PlayerTag.tagProcessor.registerTag(ElementTag.class, ElementTag.class, "fabled_class_health", (attribute, player, value) -> {
            playerClass = getPlayer(player).getClass(value.asString());
            return playerClass != null ? new ElementTag(playerClass.getHealth()) : null;
        });

        // <--[tag]
        // @attribute <PlayerTag.fabled_class_level[<class>]>
        // @returns ElementTag(Number)
        // @plugin Depenizen, Fabled
        // @description
        // Returns the level the player is in the specified class.
        // -->
        PlayerTag.tagProcessor.registerTag(ElementTag.class, ElementTag.class, "fabled_class_level", (attribute, player, value) -> {
            playerClass = getPlayer(player).getClass(value.asString());
            return playerClass != null ? new ElementTag(playerClass.getLevel()) : null;
        });

        // <--[tag]
        // @attribute <PlayerTag.fabled_class_mana[<class>]>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, Fabled
        // @description
        // Returns the amount of mana the player gets from the specified class.
        // -->
        PlayerTag.tagProcessor.registerTag(ElementTag.class, ElementTag.class, "fabled_class_mana", (attribute, player, value) -> {
            playerClass = getPlayer(player).getClass(value.asString());
            return playerClass != null ? new ElementTag(playerClass.getMana()) : null;
        });

        // <--[tag]
        // @attribute <PlayerTag.fabled_class_maxed[<class>]>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Fabled
        // @description
        // Returns whether the player has hit maximum level in the specified class.
        // -->
        PlayerTag.tagProcessor.registerTag(ElementTag.class, ElementTag.class, "fabled_class_maxed", (attribute, player, value) -> {
            playerClass = getPlayer(player).getClass(value.asString());
            return playerClass != null ? new ElementTag(playerClass.isLevelMaxed()) : null;
        });

        // <--[tag]
        // @attribute <PlayerTag.fabled_class_points[<class>]>
        // @returns ElementTag(Number)
        // @plugin Depenizen, Fabled
        // @description
        // Returns the number of available skill points the player has in the specified class.
        // -->
        PlayerTag.tagProcessor.registerTag(ElementTag.class, ElementTag.class, "fabled_class_points", (attribute, player, value) -> {
            playerClass = getPlayer(player).getClass(value.asString());
            return playerClass != null ? new ElementTag(playerClass.getPoints()) : null;
        });

        // <--[tag]
        // @attribute <PlayerTag.fabled_class_required_exp[<class>]>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, Fabled
        // @description
        // Returns the amount of experience the player must receive to get to the next level in the specified class.
        // -->
        PlayerTag.tagProcessor.registerTag(ElementTag.class, ElementTag.class, "fabled_class_required_exp", (attribute, player, value) -> {
            playerClass = getPlayer(player).getClass(value.asString());
            return playerClass != null ? new ElementTag(playerClass.getRequiredExp()) : null;
        });

        // <--[tag]
        // @attribute <PlayerTag.fabled_class_total_exp[<class>]>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, Fabled
        // @description
        // Returns the total amount of experience the player has in the specified class.
        // -->
        PlayerTag.tagProcessor.registerTag(ElementTag.class, ElementTag.class, "fabled_class_total_exp", (attribute, player, value) -> {
            playerClass = getPlayer(player).getClass(value.asString());
            return playerClass != null ? new ElementTag(playerClass.getTotalExp()) : null;
        });

        // <--[tag]
        // @attribute <PlayerTag.fabled_has_skill[<skill>]>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Fabled
        // @description
        // Returns whether the player has the specified skill.
        // -->
        PlayerTag.tagProcessor.registerTag(ElementTag.class, ElementTag.class, "fabled_has_skill", (attribute, player, skill) -> {
            Skill fabledSkill = Fabled.getSkill(skill.toString());
            return fabledSkill != null ? new ElementTag(getPlayer(player).hasSkill(fabledSkill.getName())) : null;
        });

        // <--[tag]
        // @attribute <PlayerTag.fabled_in_class[<class>]>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Fabled
        // @description
        // Returns whether the player professes in the specified class.
        // If none is specified, returns whether the player professes in any class.
        // -->
        PlayerTag.tagProcessor.registerTag(ElementTag.class, ElementTag.class, "fabled_in_class", (attribute, player, value) -> {
            FabledClass fabledClass = Fabled.getClass(value.asString());
            return fabledClass != null ? new ElementTag(getPlayer(player).isExactClass(fabledClass)) : null;
        });

        // <--[tag]
        // @attribute <PlayerTag.fabled_main_class>
        // @returns FabledClassTag
        // @plugin Depenizen, Fabled
        // @description
        // Returns the player's main Fabled class. Returns null if the player doesn't have one.
        // -->
        PlayerTag.tagProcessor.registerTag(FabledClassTag.class, "fabled_main_class", (attribute, player) -> {
            playerClass = getPlayer(player).getMainClass();
            return playerClass != null ? new FabledClassTag(playerClass.getData()) : null;
        });

        // <--[tag]
        // @attribute <PlayerTag.fabled_mana>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, Fabled
        // @description
        // Returns the player's current amount of mana.
        // -->
        PlayerTag.tagProcessor.registerTag(ElementTag.class, "fabled_mana", (attribute, player) -> {
            return new ElementTag(getPlayer(player).getMana());
        });

        // <--[tag]
        // @attribute <PlayerTag.fabled_max_mana>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, Fabled
        // @description
        // Returns the player's maximum amount of mana.
        // -->
        PlayerTag.tagProcessor.registerTag(ElementTag.class, "fabled_max_mana", (attribute, player) -> {
            return new ElementTag(getPlayer(player).getMaxMana());
        });

        // <--[tag]
        // @attribute <PlayerTag.fabled_skill_bind[<skill>]>
        // @returns MaterialTag
        // @plugin Depenizen, Fabled
        // @description
        // Returns the material this skill is currently bound to.
        // -->
        PlayerTag.tagProcessor.registerTag(MaterialTag.class, ElementTag.class, "fabled_skill_bind", (attribute, player, skill) -> {
            playerSkill = getPlayer(player).getSkill(skill.asString());
            return playerSkill != null ? new MaterialTag(playerSkill.getBind()) : null;
        });

        // <--[tag]
        // @attribute <PlayerTag.fabled_skill_cooldown[<skill>]>
        // @returns DurationTag
        // @plugin Depenizen, Fabled
        // @description
        // Returns the remaining cooldown the player has in the specified skill.
        // -->
        PlayerTag.tagProcessor.registerTag(DurationTag.class, ElementTag.class, "fabled_skill_cooldown", (attribute, player, skill) -> {
            playerSkill = getPlayer(player).getSkill(skill.asString());
            return playerSkill != null ? new DurationTag(playerSkill.getCooldown()) : null;
        });

        // <--[tag]
        // @attribute <PlayerTag.fabled_skill_cost[<skill>]>
        // @returns ElementTag(Number)
        // @plugin Depenizen, Fabled
        // @description
        // Returns the cost the for the player to level up the specified skill.
        // -->
        PlayerTag.tagProcessor.registerTag(ElementTag.class, ElementTag.class, "fabled_skill_cost", (attribute, player, skill) -> {
            playerSkill = getPlayer(player).getSkill(skill.asString());
            return playerSkill != null ? new ElementTag(playerSkill.getCost()) : null;
        });

        // <--[tag]
        // @attribute <PlayerTag.fabled_skill_indicator[<skill>]>
        // @returns ItemTag
        // @plugin Depenizen, Fabled
        // @description
        // Returns the indicator item for the skill.
        // -->
        PlayerTag.tagProcessor.registerTag(ItemTag.class, ElementTag.class, "fabled_skill_indicator", (attribute, player, skill) -> {
            playerSkill = getPlayer(player).getSkill(skill.asString());
            return playerSkill != null ? new ItemTag(playerSkill.getData().getIcon(getPlayer(player))) : null;
        });

        // <--[tag]
        // @attribute <PlayerTag.fabled_skill_level[<skill>]>
        // @returns ElementTag(Number)
        // @plugin Depenizen, Fabled
        // @description
        // Returns the level the player is in the specified skill.
        // -->
        PlayerTag.tagProcessor.registerTag(ElementTag.class, ElementTag.class, "fabled_skill_level", (attribute, player, skill) -> {
            playerSkill = getPlayer(player).getSkill(skill.asString());
            return playerSkill != null ? new ElementTag(playerSkill.getLevel()) : null;
        });

        // <--[tag]
        // @attribute <PlayerTag.fabled_skill_maxed[<skill>]>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Fabled
        // @description
        // Returns whether the player has reached max level in the specified skill.
        // -->
        PlayerTag.tagProcessor.registerTag(ElementTag.class, ElementTag.class, "fabled_skill_maxed", (attribute, player, skill) -> {
            playerSkill = getPlayer(player).getSkill(skill.asString());
            return playerSkill != null ? new ElementTag(playerSkill.isMaxed()) : null;
        });

        // <--[tag]
        // @attribute <PlayerTag.fabled_skill_level_requirement[<skill>]>
        // @returns ElementTag(Number)
        // @plugin Depenizen, Fabled
        // @description
        // Returns the level the player must be to level up the specified skill.
        // -->
        PlayerTag.tagProcessor.registerTag(ElementTag.class, ElementTag.class, "fabled_skill_level_requirement", (attribute, player, skill) -> {
            playerSkill = getPlayer(player).getSkill(skill.asString());
            return playerSkill != null ? new ElementTag(playerSkill.getLevelReq()) : null;
        });

        // <--[tag]
        // @attribute <PlayerTag.fabled_skill_on_cooldown[<skill>]>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Fabled
        // @description
        // Returns whether the specified skill is currently on cooldown for the player.
        // -->
        PlayerTag.tagProcessor.registerTag(ElementTag.class, ElementTag.class, "fabled_skill_on_cooldown", (attribute, player, skill) -> {
            playerSkill = getPlayer(player).getSkill(skill.asString());
            return playerSkill != null ? new ElementTag(playerSkill.isOnCooldown()) : null;
        });

        // <--[tag]
        // @attribute <PlayerTag.fabled_skill_status[<skill>]>
        // @returns ElementTag
        // @plugin Depenizen, Fabled
        // @description
        // Returns the player's current status for the specified skill.
        // Can be: MISSING_MANA, ON_COOLDOWN, or READY.
        // -->
        PlayerTag.tagProcessor.registerTag(ElementTag.class, ElementTag.class, "fabled_skill_status", (attribute, player, skill) -> {
            playerSkill = getPlayer(player).getSkill(skill.asString());
            return playerSkill != null ? new ElementTag(playerSkill.getStatus()) : null;
        });
    }
}
