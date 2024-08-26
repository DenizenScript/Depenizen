package com.denizenscript.depenizen.bukkit.objects.vivecraft;

import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.Fetchable;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.QuaternionTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.ObjectTagProcessor;
import com.denizenscript.denizencore.tags.TagContext;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.depenizen.bukkit.bridges.ViveCraftBridge;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.vivecraft.VSE;
import org.vivecraft.VivePlayer;

import java.util.UUID;

public class ViveCraftPlayerTag implements ObjectTag {

    // <--[ObjectType]
    // @name ViveCraftPlayerTag
    // @prefix vivecraft
    // @base ElementTag
    // @format
    // The identity format for ViveCraftPlayerTag is the UUID of the relevant player.
    //
    // @plugin Depenizen, ViveCraft
    // @description
    // A ViveCraftPlayerTag represents a player which is in VR.
    //
    // -->

    public static ViveCraftPlayerTag valueOf(String uuid) {
        return valueOf(uuid, null);
    }

    @Fetchable("vivecraft")
    public static ViveCraftPlayerTag valueOf(String string, TagContext context) {
        if (string == null) {
            return null;
        }
        try {
            string = string.replace("vivecraft@", "");
            UUID uuid = UUID.fromString(string);
            PlayerTag player = new PlayerTag(uuid);
            if (!ViveCraftBridge.isViveCraftPlayer(player.getPlayerEntity())) {
                return null;
            }
            return new ViveCraftPlayerTag(player.getPlayerEntity());
        }
        catch (Exception e) {
            return null;
        }
    }

    public static boolean matches(String string) {
        return string.startsWith("vivecraft@");
    }

    public ViveCraftPlayerTag(Player player) {
        if (VSE.isVive(player)) {
            this.vivePlayer = ViveCraftBridge.getViveCraftPlayer(player);
            this.player = player;
        }
        else {
            Debug.echoError("ViceCraftPlayer referenced is null!");
        }
    }

    public VivePlayer getVivePlayer() {
        return vivePlayer;
    }

    public Player getPlayer() {
        return player;
    }

    public Player player;

    public VivePlayer vivePlayer;

    private String prefix;

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {
        return tagProcessor.getObjectAttribute(this, attribute);
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public boolean isUnique() {
        return true;
    }

    @Override
    public String identify() {
        return "vivecraft@" + player.getUniqueId();
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
    public ObjectTag setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public static ObjectTagProcessor<ViveCraftPlayerTag> tagProcessor = new ObjectTagProcessor<>();

    public static void register() {

        // <--[tag]
        // @attribute <ViveCraftPlayerTag.active_hand>
        // @returns ElementTag
        // @description
        // Returns the active hand of the ViveCraftPlayer. Returns either right or left.
        // -->
        tagProcessor.registerTag(ElementTag.class, "active_hand", (attribute, object) -> {
            return new ElementTag(object.getPlayer().getMetadata("activehand").get(0).asString());
        });

        // <--[tag]
        // @attribute <ViveCraftPlayerTag.is_seated>
        // @returns ElementTag(Boolean)
        // @description
        // Returns whether the ViveCraftPlayer sits or not.
        // -->
        tagProcessor.registerTag(ElementTag.class, "is_seated", (attribute, object) -> {
            return new ElementTag(object.getPlayer().getMetadata("seated").get(0).asBoolean());
        });

        // <--[tag]
        // @attribute <ViveCraftPlayerTag.position[head/left/right]>
        // @returns LocationTag
        // @description
        // Returns a LocationTag of the given position.
        // -->
        tagProcessor.registerTag(LocationTag.class, "position", (attribute, object) -> {
            if (!attribute.hasParam()) {
                attribute.echoError("ViveCraftPlayer.position[...] tag must have an input.");
                return null;
            }
            Location location;
            ElementTag type = attribute.paramAsType(ElementTag.class);
            switch (type.toString()) {
                case "head":
                    location = (Location) object.getPlayer().getMetadata("head.pos").get(0).value();
                    break;
                case "left":
                    location = (Location) object.getPlayer().getMetadata("lefthand.pos").get(0).value();
                    break;
                case "right":
                    location = (Location) object.getPlayer().getMetadata("righthand.pos").get(0).value();
                    break;
                default:
                    attribute.echoError("Attribute must be 'head', 'left' or 'right'");
                    return null;
            }
            if (location == null) {
                attribute.echoError("Location is not valid. Did a plugin overwrite the data?");
                return null;
            }
            return new LocationTag(location);
        });

        tagProcessor.registerTag(QuaternionTag.class, "rotation", (attribute, object) -> {
            if (!attribute.hasParam()) {
                attribute.echoError("ViveCraftPlayer.rotation[...] tag must have an input.");
                return null;
            }
            float[] rotation;
            ElementTag type = attribute.paramAsType(ElementTag.class);
            switch (type.toString()) {
                case "head":
                    rotation = (float[]) object.getPlayer().getMetadata("head.rot").get(0).value();
                    break;
                case "left":
                    rotation = (float[]) object.getPlayer().getMetadata("lefthand.rot").get(0).value();
                    break;
                case "right":
                    rotation = (float[]) object.getPlayer().getMetadata("righthand.rot").get(0).value();
                    break;
                default:
                    attribute.echoError("Attribute must be 'head', 'left' or 'right'");
                    return null;
            }
            if (rotation == null) {
                attribute.echoError("Location is not valid. Did a plugin overwrite the data?");
                return null;
            }
            return new QuaternionTag(rotation[1], rotation[2], rotation[3], rotation[0]);
        });
    }
}
