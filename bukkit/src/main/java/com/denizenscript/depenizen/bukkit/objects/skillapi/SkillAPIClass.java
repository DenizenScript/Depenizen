package com.denizenscript.depenizen.bukkit.objects.skillapi;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.classes.RPGClass;
import net.aufdemrand.denizen.objects.dItem;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.Fetchable;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;
import net.aufdemrand.denizencore.tags.TagContext;

public class SkillAPIClass implements dObject {

    /////////////////////
    //   OBJECT FETCHER
    /////////////////

    public static SkillAPIClass valueOf(String string) {
        return valueOf(string, null);
    }

    @Fetchable("skillapiclass")
    public static SkillAPIClass valueOf(String string, TagContext context) {
        if (string == null) return null;

        string = string.replace("skillapiclass@", "");

        RPGClass rpgClass = SkillAPI.getClass(string);
        if (rpgClass != null) {
            return new SkillAPIClass(rpgClass);
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

    public SkillAPIClass(RPGClass rpgClass) {
        this.rpgClass = rpgClass;
    }

    public RPGClass getRPGClass() {
        return rpgClass;
    }


    /////////////////////
    //   dObject Methods
    /////////////////

    private String prefix = "SkillAPIClass";

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public dObject setPrefix(String prefix) {
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
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <skillapiclass@class.name>
        // @returns Element
        // @description
        // Returns the name of this SkillAPI class.
        // @plugin Depenizen, SkillAPI
        // -->
        if (attribute.startsWith("name")) {
            return new Element(rpgClass.getName()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <skillapiclass@class.prefix_color>
        // @returns Element
        // @description
        // Returns the color of the prefix of this SkillAPI class.
        // @plugin Depenizen, SkillAPI
        // -->
        if (attribute.startsWith("prefix_color")) {
            return new Element(rpgClass.getPrefixColor().toString()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <skillapiclass@class.prefix>
        // @returns Element
        // @description
        // Returns the prefix of this SkillAPI class.
        // @plugin Depenizen, SkillAPI
        // -->
        else if (attribute.startsWith("prefix")) {
            return new Element(rpgClass.getPrefix()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <skillapiclass@class.needs_permission>
        // @returns Element(Boolean)
        // @description
        // Returns whether this SkillAPI class requires permission to profess as it.
        // @plugin Depenizen, SkillAPI
        // -->
        else if (attribute.startsWith("needs_permission")) {
            return new Element(rpgClass.isNeedsPermission()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <skillapiclass@class.group.name>
        // @returns Element
        // @description
        // Returns the name of the group that this SkillAPI class falls into.
        // @plugin Depenizen, SkillAPI
        // -->
        if (attribute.startsWith("group.name")) { // TODO: SkillAPIGroup object?
            return new Element(rpgClass.getGroup()).getAttribute(attribute.fulfill(2));
        }

        // <--[tag]
        // @attribute <skillapiclass@class.has_parent>
        // @returns Element(Boolean)
        // @description
        // Returns the whether this SkillAPI class has a parent class.
        // @plugin Depenizen, SkillAPI
        // -->
        if (attribute.startsWith("has_parent")) {
            return new Element(rpgClass.hasParent()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <skillapiclass@class.parent>
        // @returns SkillAPIClass
        // @description
        // Returns the parent class of this SkillAPI class.
        // @plugin Depenizen, SkillAPI
        // -->
        if (attribute.startsWith("parent")) {
            return new SkillAPIClass(rpgClass.getParent()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <skillapiclass@class.icon>
        // @returns dItem
        // @description
        // Returns the item icon representing this SkillAPI class in menus.
        // @plugin Depenizen, SkillAPI
        // -->
        if (attribute.startsWith("icon")) {
            return new dItem(rpgClass.getIcon()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <skillapiclass@class.max_level>
        // @returns Element(Decimal)
        // @description
        // Returns the maximum level that this SkillAPI class can reach.
        // @plugin Depenizen, SkillAPI
        // -->
        if (attribute.startsWith("max_level")) {
            return new Element(rpgClass.getMaxLevel()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <skillapiclass@class.base_health>
        // @returns Element(Decimal)
        // @description
        // Returns the base amount of health for this SkillAPI class.
        // @plugin Depenizen, SkillAPI
        // -->
        if (attribute.startsWith("base_health")) {
            return new Element(rpgClass.getBaseHealth()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <skillapiclass@class.health_scale>
        // @returns Element(Decimal)
        // @description
        // Returns the amount of health gained per level for this SkillAPI class.
        // @plugin Depenizen, SkillAPI
        // -->
        if (attribute.startsWith("health_scale")) {
            return new Element(rpgClass.getHealthScale()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <skillapiclass@class.base_mana>
        // @returns Element(Decimal)
        // @description
        // Returns the base amount of mana for this SkillAPI class.
        // @plugin Depenizen, SkillAPI
        // -->
        if (attribute.startsWith("base_mana")) {
            return new Element(rpgClass.getBaseMana()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <skillapiclass@class.mana_scale>
        // @returns Element(Decimal)
        // @description
        // Returns the amount of mana gained per level for this SkillAPI class.
        // @plugin Depenizen, SkillAPI
        // -->
        if (attribute.startsWith("mana_scale")) {
            return new Element(rpgClass.getManaScale()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <skillapiclass@class.mana_name>
        // @returns Element
        // @description
        // Returns the alias for mana that this SkillAPI class uses.
        // @plugin Depenizen, SkillAPI
        // -->
        if (attribute.startsWith("mana_name")) {
            return new Element(rpgClass.getManaName()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <skillapiclass@class.has_mana_regen>
        // @returns Element(Boolean)
        // @description
        // Returns whether this SkillAPI class has mana regeneration.
        // @plugin Depenizen, SkillAPI
        // -->
        if (attribute.startsWith("has_mana_regen")) {
            return new Element(rpgClass.hasManaRegen()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <skillapiclass@class.mana_regen>
        // @returns Element(Decimal)
        // @description
        // Returns the amount of mana regeneration that this SkillAPI class has.
        // @plugin Depenizen, SkillAPI
        // -->
        if (attribute.startsWith("mana_regen")) {
            return new Element(rpgClass.getManaRegen()).getAttribute(attribute.fulfill(1));
        }


        return new Element(identify()).getAttribute(attribute);

    }
}
