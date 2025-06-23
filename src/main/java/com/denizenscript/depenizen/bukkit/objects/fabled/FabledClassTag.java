package com.denizenscript.depenizen.bukkit.objects.fabled;

import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.ObjectTagProcessor;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.Fetchable;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.TagContext;
import studio.magemonkey.fabled.Fabled;
import studio.magemonkey.fabled.api.classes.FabledClass;

public class FabledClassTag implements ObjectTag {

    // <--[ObjectType]
    // @name FabledClassTag
    // @prefix fabledclass
    // @base ElementTag
    // @format
    // The identity format for regions is <class_name>
    // For example, 'fabledclass@myclass'.
    //
    // @plugin Depenizen, Fabled
    // @description
    // A FabledClassTag represents a Fabled class.
    //
    // -->

    /////////////////////
    //   OBJECT FETCHER
    /////////////////

    @Fetchable("fabledclass")
    public static FabledClassTag valueOf(String string, TagContext context) {
        if (string.startsWith("fabledclass@")) {
            string = string.substring("fabledclass@".length());
        }
        FabledClass fabledClass = Fabled.getClass(string);
        if (fabledClass == null) {
            Debug.echoError("No class with the name '" + string + "' found.");
            return null;
        }
        return new FabledClassTag(fabledClass);
    }

    public static boolean matches(String arg) {
        if (arg.startsWith("fabledclass@")) {
            return true;
        }
        return valueOf(arg, CoreUtilities.noDebugContext) != null;
    }

    /////////////////////
    //   CONSTRUCTORS
    /////////////////

    static FabledClass fabledClass;

    public FabledClassTag(FabledClass fabledClass) {
        FabledClassTag.fabledClass = fabledClass;
    }

    /////////////////////
    //   ObjectTag Methods
    /////////////////

    private String prefix = "FabledClass";

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
    public boolean isUnique() {
        return true;
    }

    @Override
    public String identify() {
        return "fabledclass@" + fabledClass.getName();
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
    public ObjectTag getObjectAttribute(Attribute attribute) {
        return tagProcessor.getObjectAttribute(this, attribute);
    }

    public static void register() {

        // <--[tag]
        // @attribute <FabledClassTag.name>
        // @returns ElementTag
        // @plugin Depenizen, Fabled
        // @description
        // Returns the name of this Fabled class.
        // -->
        tagProcessor.registerTag(ElementTag.class, "name", (attribute, object) -> {
            return new ElementTag(fabledClass.getName(), true);
        });

        // <--[tag]
        // @attribute <FabledClassTag.prefix_color>
        // @returns ElementTag
        // @plugin Depenizen, Fabled
        // @description
        // Returns the color of the prefix of this Fabled class.
        // -->
        tagProcessor.registerTag(ElementTag.class, "prefix_color", (attribute, object) -> {
            return new ElementTag(fabledClass.getPrefixColor());
        });

        // <--[tag]
        // @attribute <FabledClassTag.class_prefix>
        // @returns ElementTag
        // @plugin Depenizen, Fabled
        // @description
        // Returns the prefix of this Fabled class.
        // -->
        tagProcessor.registerTag(ElementTag.class, "class_prefix", (attribute, object) -> {
            return new ElementTag(fabledClass.getPrefix(), true);
        });

        // <--[tag]
        // @attribute <FabledClassTag.needs_permission>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Fabled
        // @description
        // Returns whether this Fabled class requires permission to profess as it.
        // -->
        tagProcessor.registerTag(ElementTag.class, "needs_permission", (attribute, object) -> {
            return new ElementTag(fabledClass.isNeedsPermission());
        });

        // <--[tag]
        // @attribute <FabledClassTag.group_name>
        // @returns ElementTag
        // @plugin Depenizen, Fabled
        // @description
        // Returns the name of the group that this Fabled class falls into.
        // -->
        tagProcessor.registerTag(ElementTag.class, "group_name", (attribute, object) -> {
            return new ElementTag(fabledClass.getGroup(), true);
        });

        // <--[tag]
        // @attribute <FabledClassTag.has_parent>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Fabled
        // @description
        // Returns whether this Fabled class has a parent class.
        // -->
        tagProcessor.registerTag(ElementTag.class, "has_parent", (attribute, object) -> {
            return new ElementTag(fabledClass.hasParent());
        });

        // <--[tag]
        // @attribute <FabledClassTag.parent>
        // @returns FabledClassTag
        // @plugin Depenizen, Fabled
        // @description
        // Returns the parent class of this Fabled class. Returns null if this class does not have a parent.
        // -->
        tagProcessor.registerTag(FabledClassTag.class, "parent", (attribute, object) -> {
            return fabledClass.getParent() != null ? new FabledClassTag(fabledClass.getParent()) : null;
        });

        // <--[tag]
        // @attribute <FabledClassTag.icon>
        // @returns ItemTag
        // @plugin Depenizen, Fabled
        // @description
        // Returns the item icon representing this Fabled class in menus.
        // -->
        tagProcessor.registerTag(ItemTag.class, "icon", (attribute, object) -> {
            return new ItemTag(fabledClass.getIcon());
        });

        // <--[tag]
        // @attribute <FabledClassTag.max_level>
        // @returns ElementTag(Number)
        // @plugin Depenizen, Fabled
        // @description
        // Returns the maximum level that this Fabled class can reach.
        // -->
        tagProcessor.registerTag(ElementTag.class, "max_level", (attribute, object) -> {
            return new ElementTag(fabledClass.getMaxLevel());
        });

        // <--[tag]
        // @attribute <FabledClassTag.base_health>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, Fabled
        // @description
        // Returns the base amount of health for this Fabled class.
        // -->
        tagProcessor.registerTag(ElementTag.class, "base_health", (attribute, object) -> {
            return new ElementTag(fabledClass.getBaseHealth());
        });

        // <--[tag]
        // @attribute <FabledClassTag.health_scale>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, Fabled
        // @description
        // Returns the amount of health gained per level for this Fabled class.
        // -->
        tagProcessor.registerTag(ElementTag.class, "health_scale", (attribute, object) -> {
            return new ElementTag(fabledClass.getHealthScale());
        });

        // <--[tag]
        // @attribute <FabledClassTag.base_mana>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, Fabled
        // @description
        // Returns the base amount of mana for this Fabled class.
        // -->
        tagProcessor.registerTag(ElementTag.class, "base_mana", (attribute, object) -> {
            return new ElementTag(fabledClass.getBaseMana());
        });

        // <--[tag]
        // @attribute <FabledClassTag.mana_scale>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, Fabled
        // @description
        // Returns the amount of mana gained per level for this Fabled class.
        // -->
        tagProcessor.registerTag(ElementTag.class, "mana_scale", (attribute, object) -> {
            return new ElementTag(fabledClass.getManaScale());
        });

        // <--[tag]
        // @attribute <FabledClassTag.mana_name>
        // @returns ElementTag
        // @plugin Depenizen, Fabled
        // @description
        // Returns the alias for mana that this Fabled class uses.
        // -->
        tagProcessor.registerTag(ElementTag.class, "mana_name", (attribute, object) -> {
            return new ElementTag(fabledClass.getManaName(), true);
        });

        // <--[tag]
        // @attribute <FabledClassTag.has_mana_regen>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Fabled
        // @description
        // Returns whether this Fabled class has mana regeneration.
        // -->
        tagProcessor.registerTag(ElementTag.class, "has_mana_regen", (attribute, object) -> {
            return new ElementTag(fabledClass.hasManaRegen());
        });

        // <--[tag]
        // @attribute <FabledClassTag.mana_regen>
        // @returns ElementTag(Decimal)
        // @plugin Depenizen, Fabled
        // @description
        // Returns the amount of mana regeneration that this Fabled class has.
        // -->
        tagProcessor.registerTag(ElementTag.class, "mana_regen", (attribute, object) -> {
            return new ElementTag(fabledClass.getManaRegen());
        });
    }

    public static final ObjectTagProcessor<FabledClassTag> tagProcessor = new ObjectTagProcessor<>();

}
