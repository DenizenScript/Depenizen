package com.denizenscript.depenizen.bukkit.objects.luckperms;

import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.Track;
import com.denizenscript.denizen.utilities.debugging.Debug;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.Fetchable;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.TagContext;

public class LuckPermsTrack implements ObjectTag {
    /////////////////////
    //   OBJECT FETCHER
    /////////////////

    public static LuckPermsTrack valueOf(String string) {
        return valueOf(string, null);
    }

    @Fetchable("luckpermstrack")
    public static LuckPermsTrack valueOf(String string, TagContext context) {
        if (string == null) {
            return null;
        }

        ////////
        // Match luckpermstrack name

        string = string.replace("luckpermstrack@", "");
        try {
            LuckPermsApi api = LuckPerms.getApi();
            Track track = api.getTrack(string);
            if (track == null) {
                return null;
            }
            return new LuckPermsTrack(track);
        }
        catch (Throwable e) {
            Debug.echoError(e);
        }
        return null;
    }

    public static boolean matches(String arg) {
        return arg.startsWith("luckpermstrack@");
    }

    /////////////////////
    //   STATIC CONSTRUCTORS
    /////////////////

    Track track = null;

    public LuckPermsTrack(Track t) {
        track = t;
    }

    /////////////////////
    //   ObjectTag Methods
    /////////////////

    private String prefix = "LuckPermsTrack";

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public LuckPermsTrack setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    @Override
    public int hashCode() {
        return track.hashCode();
    }

    @Override
    public boolean equals(Object a) {
        if (a instanceof LuckPermsTrack) {
            return ((LuckPermsTrack) a).track.getName().equals(track.getName());
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
        return "LuckPermsTrack";
    }

    @Override
    public String identify() {
        return "luckpermstrack@" + track.getName();
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
        // @attribute <luckpermstrack@luckpermstrack.name>
        // @returns ElementTag
        // @description
        // Returns the name of the track.
        // @Plugin Depenizen, LuckPerms
        // -->
        if (attribute.startsWith("name")) {
            return new ElementTag(track.getName()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <luckpermstrack@luckpermstrack.groups>
        // @returns ListTag(Element)
        // @description
        // Returns the list of groups in the track.
        // @Plugin Depenizen, LuckPerms
        // -->
        if (attribute.startsWith("groups")) {
            ListTag groups = new ListTag();
            LuckPermsApi api = LuckPerms.getApi();
            groups.addAll(track.getGroups());
            return groups.getAttribute(attribute.fulfill(1));
        }

        return new ElementTag(identify()).getAttribute(attribute);

    }
}

