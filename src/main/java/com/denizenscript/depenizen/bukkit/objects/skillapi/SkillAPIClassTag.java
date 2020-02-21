package com.denizenscript.depenizen.bukkit.objects.skillapi;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.classes.RPGClass;
import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.Fetchable;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.TagContext;

public class SkillAPIClassTag implements ObjectTag {

    // <--[language]
    // @name SkillAPIClassTag Objects
    // @group Depenizen Object Types
    // @plugin Depenizen, SkillAPI
    // @description
    // A SkillAPIClassTag represents a SkillAPI class.
    //
    // These use the object notation "skillapiclass@".
    // The identity format for regions is <class_name>
    // For example, 'skillapiclass@myclass'.
    //
    // -->

    /////////////////////
    //   OBJECT FETCHER
    /////////////////

    public static SkillAPIClassTag valueOf(String string) {
        return valueOf(string, null);
    }

    @Fetchable("skillapiclass")
    public static SkillAPIClassTag valueOf(String string, TagContext context) {
        if (string == null) {
            return null;
        }

        string = string.replace("skillapiclass@", "");

        RPGClass rpgClass = SkillAPI.getClass(string);
        if (rpgClass != null) {
            return new SkillAPIClassTag(rpgClass);
        }

        return null;
    }

    public static boolean matches(String arg) {
        if (valueOf(arg) != null) {
            return true;
        }

        return false;
    }

    /////////////////////
    //   CONSTRUCTORS
    /////////////////

    RPGClass rpgClass;

    public SkillAPIClassTag(RPGClass rpgClass) {
        this.rpgClass = rpgClass;
    }

    public RPGClass getRPGClass() {
        return rpgClass;
    }

    /////////////////////
    //   ObjectTag Methods
    /////////////////

    private String prefix = "SkillAPIClass";

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public ObjectTag setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    @Override
    public String debug() {
        return prefix + "='<A>" + identify() + "<G>' ";
    }

    @Override
    public boolean isUnique() {
        return true;
    }

    @Override
    public String getObjectType() {
        return "SkillAPI Class";
    }

    @Override
    public String identify() {
        return "skillapiclass@" + rpgClass.getName();
    }

    @Override
    public String identifySimple() {
        return identify();
    }

    @Override
    public String toString() {
        return identify();
    }

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <SkillAPIClassTag.name>
        // @returns ElementTag
        // @plugin Depenizen, SkillAPI
        // @description
        // Returns the name of this SkillAPI class.
        // -->
        if (attribute.startsWith("name")) {
            return new ElementTag(rpgClass.getName()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <SkillAPIClassTag.prefix_color>
        // @returns ElementTag
        // @plugin Depenizen, SkillAPI
        // @description
        // Returns the color of the prefix of this SkillAPI class.
        // -->
        if (attribute.startsWith("prefix_color")) {
            return new ElementTag(rpgClass.getPrefixColor().toString()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <SkillAPIClassTag.prefix>
        // @returns ElementTag
        // @plugin Depenizen, SkillAPI
        // @description
        // Returns the prefix of this SkillAPI class.
        // -->
        else if (attribute.startsWith("prefix")) {
            return new ElementTag(rpgClass.getPrefix()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <SkillAPIClassTag.needs_permission>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, SkillAPI
        // @description
        // Returns whether this SkillAPI class requires permission to profess as it.
        // -->
        else if (attribute.startsWith("needs_permission")) {
            return new ElementTag(rpgClass.isNeedsPermission()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <SkillAPIClassTag.group.name>
        // @returns ElementTag
        // @plugin Depenizen, SkillAPI
        // @description
        // Returns the name of the group that this SkillAPI class falls into.
        // -->
        if (attribute.startsWith("group.name")) { // TODO: SkillAPIGroup object?
            return new ElementTag(rpgClass.getGroup()).getAttribute(attribute.fulfill(2));
        }

        // <--[tag]
        // @attribute <SkillAPIClassTag.has_parent>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, SkillAPI
        // @description
        // Returns the whether this SkillAPI class has a parent class.
        // -->
        if (attribute.startsWith("has_parent")) {
            return new ElementTag(rpgClass.hasParent()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <SkillAPIClassTag.parent>
        // @returns SkillAPIClass
        // @plugin Depenizen, SkillAPI
        // @description
        // Returns the parent class of this SkillAPI class.
        // -->
        if (attribute.startsWith("parent")) {
            return new SkillAPIClassTag(rpgClass.getParent()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <SkillAPIClassTag.icon>
        // @returns ItemTag
        // @plugin Depenizen, SkillAPI
        // @description
        // Returns the item icon representing this SkillAPI class in menus.
        // -->
        if (attribute.startsWith("icon")) {
            return new ItemTag(rpgClass.getIcon()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <SkillAPIClassTag.max_level>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, SkillAPI
        // @description
        // Returns the maximum level that this SkillAPI class can reach.
        // -->
        if (attribute.startsWith("max_level")) {
            return new ElementTag(rpgClass.getMaxLevel()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <SkillAPIClassTag.base_health>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, SkillAPI
        // @description
        // Returns the base amount of health for this SkillAPI class.
        // -->
        if (attribute.startsWith("base_health")) {
            return new ElementTag(rpgClass.getBaseHealth()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <SkillAPIClassTag.health_scale>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, SkillAPI
        // @description
        // Returns the amount of health gained per level for this SkillAPI class.
        // -->
        if (attribute.startsWith("health_scale")) {
            return new ElementTag(rpgClass.getHealthScale()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <SkillAPIClassTag.base_mana>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, SkillAPI
        // @description
        // Returns the base amount of mana for this SkillAPI class.
        // -->
        if (attribute.startsWith("base_mana")) {
            return new ElementTag(rpgClass.getBaseMana()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <SkillAPIClassTag.mana_scale>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, SkillAPI
        // @description
        // Returns the amount of mana gained per level for this SkillAPI class.
        // -->
        if (attribute.startsWith("mana_scale")) {
            return new ElementTag(rpgClass.getManaScale()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <SkillAPIClassTag.mana_name>
        // @returns ElementTag
        // @plugin Depenizen, SkillAPI
        // @description
        // Returns the alias for mana that this SkillAPI class uses.
        // -->
        if (attribute.startsWith("mana_name")) {
            return new ElementTag(rpgClass.getManaName()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <SkillAPIClassTag.has_mana_regen>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, SkillAPI
        // @description
        // Returns whether this SkillAPI class has mana regeneration.
        // -->
        if (attribute.startsWith("has_mana_regen")) {
            return new ElementTag(rpgClass.hasManaRegen()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <SkillAPIClassTag.mana_regen>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, SkillAPI
        // @description
        // Returns the amount of mana regeneration that this SkillAPI class has.
        // -->
        if (attribute.startsWith("mana_regen")) {
            return new ElementTag(rpgClass.getManaRegen()).getAttribute(attribute.fulfill(1));
        }

        return new ElementTag(identify()).getAttribute(attribute);

    }
}
