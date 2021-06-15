package com.denizenscript.depenizen.bukkit.objects.libsdisguises;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.Fetchable;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.TagContext;
import com.denizenscript.denizencore.utilities.CoreUtilities;

import java.util.List;

public class LibsDisguiseTag implements ObjectTag {

    // <--[ObjectType]
    // @name LibsDisguiseTag
    // @prefix libsdisguise
    // @base ElementTag
    // @format
    // The identity format for disguises is <disguise_name>
    // For example, 'libsdisguise@zombie'.
    //
    // @plugin Depenizen, LibsDisguises
    // @description
    // A LibsDisguiseTag represents a LibsDisguises disguise type.
    //
    // -->

    /////////////////////
    //   OBJECT FETCHER
    /////////////////

    public static LibsDisguiseTag valueOf(String string) {
        return valueOf(string, null);
    }

    @Fetchable("libsdisguise")
    public static LibsDisguiseTag valueOf(String string, TagContext context) {
        if (string == null) {
            return null;
        }
        string = string.replace("libsdisguise@", "");
        try {
            List<String> split = CoreUtilities.split(string, ',');
            return new LibsDisguiseTag(DisguiseAPI.getCustomDisguise(split.get(0)));
        }
        catch (Throwable e) {
        }
        return null;
    }

    public static boolean matches(String arg) {
        return arg.startsWith("libsdisguise@");
    }

    /////////////////////
    //   STATIC CONSTRUCTORS
    /////////////////

    Disguise disguise;

    public LibsDisguiseTag(Disguise pl) {
        disguise = pl;
    }

    /////////////////////
    //   ObjectTag Methods
    /////////////////

    private String prefix = "LibsDisguise";

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public LibsDisguiseTag setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    @Override
    public int hashCode() {
        return disguise.hashCode();
    }

    @Override
    public boolean equals(Object a) {
        if (a instanceof LibsDisguiseTag) {
            return ((LibsDisguiseTag) a).disguise.equals(disguise);
        }
        return false;
    }

    @Override
    public String debug() {
        return (prefix + "='<A>" + identify() + "<G>' ");
    }

    @Override
    public boolean isUnique() {
        return true;
    }

    @Override
    public String getObjectType() {
        return "LibsDisguise";
    }

    @Override
    public String identify() {
        return "libsdisguise@" + disguise.toString();
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
        // @attribute <LibsDisguiseTag.disguise_type>
        // @returns ElementTag
        // @plugin Depenizen, LibsDisguises
        // @description
        // Returns the type of the disguise.
        // -->
        if (attribute.startsWith("disguise_type")) {
            return new ElementTag(disguise.getType().toString()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <LibsDisguiseTag.entity>
        // @returns EntityTag
        // @plugin Depenizen, LibsDisguises
        // @description
        // Returns the entity of the disguise.
        // -->
        if (attribute.startsWith("entity")) {
            return new EntityTag(disguise.getEntity()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <LibsDisguiseTag.display_name>
        // @returns ElementTag
        // @plugin Depenizen, LibsDisguises
        // @description
        // Returns the display name of the disguise.
        // -->
        if (attribute.startsWith("display_name")) {
            return new ElementTag(disguise.getWatcher().getCustomName()).getAttribute(attribute.fulfill(1));
        }

        return new ElementTag(identify()).getAttribute(attribute);

    }
}
