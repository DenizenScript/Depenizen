package com.denizenscript.depenizen.bukkit.objects.libsdisguises;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.Fetchable;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;
import net.aufdemrand.denizencore.tags.TagContext;
import net.aufdemrand.denizencore.utilities.CoreUtilities;

import java.util.List;

public class LibsDisguise implements dObject {
    /////////////////////
    //   OBJECT FETCHER
    /////////////////

    public static LibsDisguise valueOf(String string) {
        return valueOf(string, null);
    }

    @Fetchable("libsdisguise")
    public static LibsDisguise valueOf(String string, TagContext context) {
        if (string == null) {
            return null;
        }

        ////////
        // Match disguise name

        string = string.replace("libsdisguise@", "");
        try {
            List<String> split = CoreUtilities.split(string, ',');
            return new LibsDisguise(DisguiseAPI.getCustomDisguise(split.get(0)));
        }
        catch (Throwable e) {
        }
        return null;
    }

    public static boolean matches(String arg) {
        return arg.startsWith("disguise@");
    }

    /////////////////////
    //   STATIC CONSTRUCTORS
    /////////////////

    Disguise disguise = null;

    public LibsDisguise(Disguise pl) {
        disguise = pl;
    }

    /////////////////////
    //   dObject Methods
    /////////////////

    private String prefix = "LibsDisguise";

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public LibsDisguise setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    @Override
    public int hashCode() {
        return disguise.hashCode();
    }

    @Override
    public boolean equals(Object a) {
        if (a instanceof LibsDisguise) {
            return ((LibsDisguise) a).disguise.equals(disguise);
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
        // @attribute <libsdisguise@libsdisguise.type>
        // @returns Element
        // @description
        // Returns the type of the disguise.
        // @Plugin Depenizen, LibsDisguises
        // -->
        if (attribute.startsWith("type")) {
            return new Element(disguise.getType().toString()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <libsdisguise@libsdisguise.type>
        // @returns dEntity
        // @description
        // Returns the entity of the disguise.
        // @Plugin Depenizen, LibsDisguises
        // -->
        if (attribute.startsWith("entity")) {
            return new dEntity(disguise.getEntity()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <libsdisguise@libsdisguise.display_name>
        // @returns Element
        // @description
        // Returns the display name of the disguise.
        // @Plugin Depenizen, LibsDisguises
        // -->
        if (attribute.startsWith("display_name")) {
            return new Element(disguise.getWatcher().getCustomName()).getAttribute(attribute.fulfill(1));
        }

        return new Element(identify()).getAttribute(attribute);

    }

}
