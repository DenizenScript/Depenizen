package com.denizenscript.depenizen.bukkit.objects.pvparena;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.Fetchable;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.TagContext;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import net.slipcor.pvparena.arena.Arena;
import net.slipcor.pvparena.arena.ArenaPlayer;
import net.slipcor.pvparena.managers.ArenaManager;

public class PVPArenaArenaTag implements ObjectTag {

    // <--[ObjectType]
    // @name PVPArenaArenaTag
    // @prefix pvparena
    // @base ElementTag
    // @format
    // The identity format for arenas is <arena_name>
    // For example, 'pvparena@myarena'.
    //
    // @plugin Depenizen, PvPArena
    // @description
    // A PVPArenaArenaTag represents a PvP Arena in the world.
    //
    // -->

    String prefix = "PVPArena";
    Arena arena = null;

    public static boolean matches(String name) {
        return valueOf(name) != null;
    }

    public static PVPArenaArenaTag valueOf(String name) {
        return valueOf(name, null);
    }

    @Fetchable("pvparena")
    public static PVPArenaArenaTag valueOf(String name, TagContext context) {
        name = name.replace("pvparena@", "");
        Arena a = ArenaManager.getArenaByName(name);
        if (a == null) {
            return null;
        }
        return new PVPArenaArenaTag(a);
    }

    public PVPArenaArenaTag(Arena a) {
        if (a != null) {
            this.arena = a;
        }
        else {
            Debug.echoError("Arena referenced is null");
        }
    }

    public Arena getArena() {
        return arena;
    }

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
        return "pvparena@" + arena.getName();
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
        if (attribute == null) {
            return null;
        }

        // <--[tag]
        // @attribute <PVPArenaArenaTag.name>
        // @returns ElementTag
        // @plugin Depenizen, PVPArena
        // @description
        // Returns the name of the arena.
        // -->
        if (attribute.startsWith("name")) {
            return new ElementTag(arena.getName()).getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <PVPArenaArenaTag.fighters>
        // @returns ListTag(PlayerTag)
        // @plugin Depenizen, PvPArena
        // @description
        // Returns a list of all fighters in the arena.
        // -->
        else if (attribute.startsWith("fighters")) {
            ListTag fighters = new ListTag();
            for (ArenaPlayer p : arena.getFighters()) {
                fighters.addObject(new PlayerTag(p.get()));
            }
            return fighters.getObjectAttribute(attribute.fulfill(1));
        }

        return new ElementTag(identify()).getObjectAttribute(attribute);
    }
}
