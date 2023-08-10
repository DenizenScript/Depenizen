package com.denizenscript.depenizen.bukkit.objects.luckperms;

import com.denizenscript.denizencore.objects.Fetchable;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.ObjectTagProcessor;
import com.denizenscript.denizencore.tags.TagContext;
import com.denizenscript.depenizen.bukkit.bridges.LuckPermsBridge;
import net.luckperms.api.model.group.Group;

import java.util.OptionalInt;

public class LuckPermsGroupTag implements ObjectTag {

    public static ObjectTagProcessor<LuckPermsGroupTag> tagProcessor = new ObjectTagProcessor<>();

    // <--[ObjectType]
    // @name LuckPermsGroupTag
    // @prefix luckpermsgroup
    // @base ElementTag
    // @format
    // The identity format for group is <group_name>
    // For example, 'luckpermsgroup@my_group'.
    //
    // @plugin Depenizen, LuckPerms
    // @description
    // A LuckPermsGroupTag represents a LuckPerms group.
    //
    // -->

    @Fetchable("luckpermsgroup")
    public static LuckPermsGroupTag valueOf(String string, TagContext context) {
        if (string == null) {
            return null;
        }
        string = string.replace("luckpermsgroup@", "");
        Group group = LuckPermsBridge.luckPermsInstance.getGroupManager().getGroup(string);
        if (group == null) {
            return null;
        }
        return new LuckPermsGroupTag(group);
    }

    public static boolean matches(String arg) {
        arg = arg.replace("luckpermsgroup@", "");
        return LuckPermsBridge.luckPermsInstance.getGroupManager().getGroup(arg) != null;
    }

    Group group = null;

    public LuckPermsGroupTag(Group group) { this.group = group; }

    String prefix = "LuckPermsGroup";

    @Override
    public String getPrefix() { return prefix; }

    @Override
    public ObjectTag setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    @Override
    public boolean isUnique() { return true; }

    @Override
    public String identify() { return "luckpermsgroup@" + group.getName(); }

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

    public Group getGroup() {
        return group;
    }

    public static void register() {

        // <--[tag]
        // @attribute <LuckPermsGroupTag.name>
        // @returns ElementTag
        // @plugin Depenizen, LuckPerms
        // @description
        // Returns the group's name.
        // -->
        tagProcessor.registerTag(ElementTag.class, "name", (attribute, object) -> {
            return new ElementTag(object.getGroup().getName(), true);
        });

        // <--[tag]
        // @attribute <LuckPermsGroupTag.prefix>
        // @returns ElementTag
        // @plugin Depenizen, LuckPerms
        // @description
        // Returns the group's prefix, if any.
        // -->
        tagProcessor.registerTag(ElementTag.class, "prefix", (attribute, object) -> {
            String prefix = object.getGroup().getCachedData().getMetaData().getPrefix();
            return prefix != null ? new ElementTag(prefix, true) : null;
        });

        // <--[tag]
        // @attribute <LuckPermsGroupTag.suffix>
        // @returns ElementTag
        // @plugin Depenizen, LuckPerms
        // @description
        // Returns the group's suffix, if any.
        // -->
        tagProcessor.registerTag(ElementTag.class, "suffix", (attribute, object) -> {
            String suffix = object.getGroup().getCachedData().getMetaData().getSuffix();
            return suffix != null ? new ElementTag(suffix, true) : null;
        });

        // <--[tag]
        // @attribute <LuckPermsGroupTag.weight>
        // @returns ElementTag
        // @plugin Depenizen, LuckPerms
        // @description
        // Returns the group's weight, if any.
        // -->
        tagProcessor.registerTag(ElementTag.class, "weight", (attribute, object) -> {
            OptionalInt weight = object.getGroup().getWeight();
            return weight.isPresent() ? new ElementTag(weight.getAsInt()) : null;
        });

        // <--[tag]
        // @attribute <LuckPermsGroupTag.has_permission[permission.node]>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, LuckPerms
        // @description
        // Returns whether the group has the specified permission node.
        // -->
        tagProcessor.registerTag(ElementTag.class, ElementTag.class, "has_permission", (attribute, object, p) -> {
            return new ElementTag(object.getGroup().getCachedData().getPermissionData().checkPermission(p.toString()).asBoolean());
        });
    }
}
