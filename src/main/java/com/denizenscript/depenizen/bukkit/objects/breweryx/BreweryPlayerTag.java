package com.denizenscript.depenizen.bukkit.objects.breweryx;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.Fetchable;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.ObjectTagProcessor;
import com.denizenscript.denizencore.tags.TagContext;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.dre.brewery.BPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BreweryPlayerTag extends PlayerTag {

    // <--[ObjectType]
    // @name BreweryPlayerTag
    // @prefix bplayer
    // @base PlayerTag
    // @format
    // The identity format for brewery players is <player_name>
    // For example, 'bplayer@my_player'.
    //
    // @plugin Depenizen, BreweryX
    // @description
    // A BPlayerTag represents a Brewery player.
    //
    // -->

    @Fetchable("bplayer")
    public static BreweryPlayerTag valueOf(String string, TagContext context) {
        if (string.startsWith("bplayer@")) {
            string = string.substring("bplayer@".length());
        }
        Player player = Bukkit.getPlayerExact(string);
        if (player == null) {
            return null;
        }
        return new BreweryPlayerTag(player);
    }

    public static boolean matches(String arg) {
        arg = arg.replace("bplayer@", "");
        Player player = Bukkit.getPlayerExact(arg);
        if (player == null) {
            return false;
        }
        return BPlayer.hasPlayer(player);
    }

    public static BreweryPlayerTag forPlayer(PlayerTag player) {
        return new BreweryPlayerTag(player.getPlayerEntity());
    }

    BPlayer bPlayer = null;

    public BreweryPlayerTag(Player player) {
        super(player);
        if (BPlayer.hasPlayer(player)) {
            bPlayer = BPlayer.get(player);
        }
        else {
            Debug.echoError("BPlayer referenced is null!");
        }
    }


    String prefix = "bplayer";

    @Override
    public String getPrefix() {
        return this.prefix;
    }

    @Override
    public boolean isUnique() {
        return true;
    }

    @Override
    public String identify() {
        return "bplayer@" + bPlayer.getUuid();
    }

    @Override
    public String identifySimple() {
        return "bplayer@" + bPlayer.getName();
    }

    @Override
    public PlayerTag setPrefix(String aString) {
        this.prefix = aString;
        return this;
    }

    public static void register() {

        // <--[tag]
        // @attribute <BPlayerTag.drunkenness>
        // @returns ElementTag
        // @plugin Depenizen, BreweryX
        // @description
        // Returns the drunkness of the brewery player.
        // -->
        tagProcessor.registerTag(ElementTag.class, "drunkenness", (attribute, object) -> {
            return new ElementTag(object.bPlayer.getDrunkeness());
        });

        // <--[tag]
        // @attribute <BPlayerTag.quality>
        // @returns ElementTag
        // @plugin Depenizen, BreweryX
        // @description
        // Returns the quality of the brewery player's drunkenness (drunkeness * drunkeness).
        // -->
        tagProcessor.registerTag(ElementTag.class, "quality", (attribute, object) -> {
            return new ElementTag(object.bPlayer.getQuality());
        });

        // <--[tag]
        // @attribute <BPlayerTag.alcrecovery>
        // @returns ElementTag
        // @plugin Depenizen, BreweryX
        // @description
        // Returns the drunkenness reduction per minute.
        // -->
        tagProcessor.registerTag(ElementTag.class, "alcrecovery", (attribute, object) -> {
            return new ElementTag(object.bPlayer.getAlcRecovery());
        });

    }

    public static ObjectTagProcessor<BreweryPlayerTag> tagProcessor = new ObjectTagProcessor<>();

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {
        return tagProcessor.getObjectAttribute(this, attribute);
    }
}
