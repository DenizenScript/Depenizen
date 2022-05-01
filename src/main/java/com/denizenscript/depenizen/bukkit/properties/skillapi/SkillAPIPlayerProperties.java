package com.denizenscript.depenizen.bukkit.properties.skillapi;

import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizen.objects.MaterialTag;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.depenizen.bukkit.objects.skillapi.SkillAPIClassTag;
import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.player.PlayerClass;
import com.sucy.skill.api.player.PlayerData;
import com.sucy.skill.api.player.PlayerSkill;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.DurationTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;

public class SkillAPIPlayerProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "SkillAPIPlayer";
    }

    @Override
    public void adjust(Mechanism mechanism) {
        // None
    }

    public static boolean describes(ObjectTag pl) {
        return pl instanceof PlayerTag;
    }

    public static SkillAPIPlayerProperties getFrom(ObjectTag pl) {
        if (!describes(pl)) {
            return null;
        }
        else {
            return new SkillAPIPlayerProperties((PlayerTag) pl);
        }
    }

    public static final String[] handledTags = new String[] {
            "skillapi"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    public SkillAPIPlayerProperties(PlayerTag player) {
        this.player = player;
    }

    PlayerTag player;

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {

        if (attribute.startsWith("skillapi")) {

            PlayerData data = SkillAPI.getPlayerData(player.getOfflinePlayer());
            attribute = attribute.fulfill(1);

            // <--[tag]
            // @attribute <PlayerTag.skillapi.main_class>
            // @returns SkillAPIClassTag
            // @plugin Depenizen, SkillAPI
            // @description
            // Returns the player's main SkillAPI class.
            // -->
            if (attribute.startsWith("main_class")) {
                if (data == null || data.getMainClass() == null) {
                    return null;
                }
                return new SkillAPIClassTag(data.getMainClass().getData()).getObjectAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <PlayerTag.skillapi.in_class[<class>]>
            // @returns ElementTag(Boolean)
            // @plugin Depenizen, SkillAPI
            // @description
            // Returns whether the player professes in the specified class. If none is specified, returns
            // whether the player professes in any class.
            // -->
            if (attribute.startsWith("in_class")) {
                if (attribute.hasParam()) {
                    SkillAPIClassTag testClass = attribute.paramAsType(SkillAPIClassTag.class);
                    if (testClass == null) {
                        return null;
                    }
                    return new ElementTag(data.isExactClass(testClass.getRPGClass())).getObjectAttribute(attribute.fulfill(1));
                }
                return new ElementTag(data.hasClass()).getObjectAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <PlayerTag.skillapi.has_skill[<skill>]>
            // @returns ElementTag(Boolean)
            // @plugin Depenizen, SkillAPI
            // @description
            // Returns whether the player has the specified skill.
            // -->
            if (attribute.startsWith("has_skill") && attribute.hasParam()) {
                return new ElementTag(data.hasSkill(attribute.getParam())).getObjectAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <PlayerTag.skillapi.mana>
            // @returns ElementTag(Decimal)
            // @plugin Depenizen, SkillAPI
            // @description
            // Returns the player's current amount of mana.
            // -->
            if (attribute.startsWith("mana")) {
                return new ElementTag(data.getMana()).getObjectAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <PlayerTag.skillapi.max_mana>
            // @returns ElementTag(Decimal)
            // @plugin Depenizen, SkillAPI
            // @description
            // Returns the player's maximum amount of mana.
            // -->
            if (attribute.startsWith("max_mana")) {
                return new ElementTag(data.getMaxMana()).getObjectAttribute(attribute.fulfill(1));
            }

            if (attribute.getAttribute(1).startsWith("class_") && attribute.hasParam()) {

                PlayerClass playerClass = null;
                SkillAPIClassTag skillAPIClass = attribute.paramAsType(SkillAPIClassTag.class);
                if (skillAPIClass != null) {
                    String name = skillAPIClass.getRPGClass().getName();
                    for (PlayerClass plClass : data.getClasses()) {
                        if (plClass.getData().getName().equals(name)) {
                            playerClass = plClass;
                            break;
                        }
                    }
                }
                if (playerClass == null) {
                    return null;
                }

                // <--[tag]
                // @attribute <PlayerTag.skillapi.class_exp[<class>]>
                // @returns ElementTag(Decimal)
                // @plugin Depenizen, SkillAPI
                // @description
                // Returns the amount of experience the player has toward the next level in the specified class.
                // -->
                if (attribute.startsWith("class_exp")) {
                    return new ElementTag(playerClass.getExp()).getObjectAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <PlayerTag.skillapi.class_required_exp[<class>]>
                // @returns ElementTag(Decimal)
                // @plugin Depenizen, SkillAPI
                // @description
                // Returns the amount of experience the player must receive to get to the next level
                // in the specified class.
                // -->
                if (attribute.startsWith("class_required_exp")) {
                    return new ElementTag(playerClass.getRequiredExp()).getObjectAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <PlayerTag.skillapi.class_total_exp[<class>]>
                // @returns ElementTag(Decimal)
                // @plugin Depenizen, SkillAPI
                // @description
                // Returns the total amount of experience the player has in the specified class.
                // -->
                if (attribute.startsWith("class_total_exp")) {
                    return new ElementTag(playerClass.getTotalExp()).getObjectAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <PlayerTag.skillapi.class_level[<class>]>
                // @returns ElementTag(Number)
                // @plugin Depenizen, SkillAPI
                // @description
                // Returns the level the player is in the specified class.
                // -->
                if (attribute.startsWith("class_level")) {
                    return new ElementTag(playerClass.getLevel()).getObjectAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <PlayerTag.skillapi.class_points[<class>]>
                // @returns ElementTag(Number)
                // @plugin Depenizen, SkillAPI
                // @description
                // Returns the number of skill points the player has in the specified class.
                // -->
                if (attribute.startsWith("class_points")) {
                    return new ElementTag(playerClass.getPoints()).getObjectAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <PlayerTag.skillapi.class_maxed[<class>]>
                // @returns ElementTag(Boolean)
                // @plugin Depenizen, SkillAPI
                // @description
                // Returns whether the player has hit maximum level in the specified class.
                // -->
                if (attribute.startsWith("class_maxed")) {
                    return new ElementTag(playerClass.isLevelMaxed()).getObjectAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <PlayerTag.skillapi.class_health[<class>]>
                // @returns ElementTag(Decimal)
                // @plugin Depenizen, SkillAPI
                // @description
                // Returns the amount of health the player gets from the specified class.
                // -->
                if (attribute.startsWith("class_health")) {
                    return new ElementTag(playerClass.getHealth()).getObjectAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <PlayerTag.skillapi.class_mana[<class>]>
                // @returns ElementTag(Decimal)
                // @plugin Depenizen, SkillAPI
                // @description
                // Returns the amount of mana the player gets from the specified class.
                // -->
                if (attribute.startsWith("class_mana")) {
                    return new ElementTag(playerClass.getMana()).getObjectAttribute(attribute.fulfill(1));
                }
            }

            if (attribute.getAttribute(1).startsWith("skill_") && attribute.hasParam()) {

                PlayerSkill playerSkill = data.getSkill(attribute.getParam()); // TODO: SkillAPISkill object?
                if (playerSkill == null) {
                    return null;
                }

                // <--[tag]
                // @attribute <PlayerTag.skillapi.skill_indicator[<skill>]>
                // @returns ItemTag
                // @plugin Depenizen, SkillAPI
                // @description
                // Returns the indicator item for the skill.
                // -->
                if (attribute.startsWith("skill_indicator")) {
                    return new ItemTag(playerSkill.getData().getIndicator(playerSkill)).getObjectAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <PlayerTag.skillapi.skill_bind[<skill>]>
                // @returns MaterialTag
                // @plugin Depenizen, SkillAPI
                // @description
                // Returns the material this skill is currently bound to.
                // -->
                if (attribute.startsWith("skill_bind")) {
                    return new MaterialTag(playerSkill.getBind()).getObjectAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <PlayerTag.skillapi.skill_level_requirement[<skill>]>
                // @returns ElementTag(Number)
                // @plugin Depenizen, SkillAPI
                // @description
                // Returns the level the player must be to level up the specified skill.
                // -->
                if (attribute.startsWith("skill_level_req")) {
                    return new ElementTag(playerSkill.getLevelReq()).getObjectAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <PlayerTag.skillapi.skill_level[<skill>]>
                // @returns ElementTag(Number)
                // @plugin Depenizen, SkillAPI
                // @description
                // Returns the level the player is in the specified skill.
                // -->
                if (attribute.startsWith("skill_level")) {
                    return new ElementTag(playerSkill.getLevel()).getObjectAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <PlayerTag.skillapi.skill_points[<skill>]>
                // @returns ElementTag(Number)
                // @plugin Depenizen, SkillAPI
                // @description
                // Returns how many skill points the player has invested in the specified skill.
                // -->
                if (attribute.startsWith("skill_points")) {
                    return new ElementTag(playerSkill.getPoints()).getObjectAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <PlayerTag.skillapi.skill_cost[<skill>]>
                // @returns ElementTag(Number)
                // @plugin Depenizen, SkillAPI
                // @description
                // Returns the cost the for the player to level up the specified skill.
                // -->
                if (attribute.startsWith("skill_cost")) {
                    return new ElementTag(playerSkill.getCost()).getObjectAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <PlayerTag.skillapi.skill_on_cooldown[<skill>]>
                // @returns ElementTag(Boolean)
                // @plugin Depenizen, SkillAPI
                // @description
                // Returns whether the specified skill is currently on cooldown for the player.
                // -->
                if (attribute.startsWith("skill_on_cooldown")) {
                    return new ElementTag(playerSkill.getLevel()).getObjectAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <PlayerTag.skillapi.skill_cooldown[<skill>]>
                // @returns DurationTag
                // @plugin Depenizen, SkillAPI
                // @description
                // Returns the remaining cooldown the player has in the specified skill.
                // -->
                if (attribute.startsWith("skill_cooldown")) {
                    return new DurationTag(playerSkill.getCooldown()).getObjectAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <PlayerTag.skillapi.skill_maxed[<skill>]>
                // @returns ElementTag(Boolean)
                // @plugin Depenizen, SkillAPI
                // @description
                // Returns whether the player has reached max level in the specified skill.
                // -->
                if (attribute.startsWith("skill_maxed")) {
                    return new ElementTag(playerSkill.isMaxed()).getObjectAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <PlayerTag.skillapi.skill_status[<skill>]>
                // @returns ElementTag
                // @plugin Depenizen, SkillAPI
                // @description
                // Returns the player's current status for the specified skill.
                // Can be: ON_COOLDOWN, MISSING_MANA, or READY
                // -->
                if (attribute.startsWith("skill_status")) {
                    return new ElementTag(playerSkill.getStatus().name()).getObjectAttribute(attribute.fulfill(1));
                }
            }

        }

        return null;

    }
}
