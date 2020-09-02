package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.depenizen.bukkit.Bridge;
import net.minecraft.server.v1_16_R2.Vec3D;
import org.bukkit.Location;
import org.vivecraft.VSE;
import org.vivecraft.VivePlayer;

public class VivecraftBridge extends Bridge {

    @Override
    public void init() {
        // This is used instead of a property class because
        // property classes can only return strings and not tags
        PlayerTag.registerTag("vivecraft", (attribute, player) -> {

            // <--[tag]
            // @attribute <PlayerTag.vivecraft>
            // @returns ObjectTag
            // @plugin Depenizen, ViveCraft
            // @description
            // Returns null if player is not using ViveCraft
            // -->

            if (attribute.startsWith("vivecraft")) {
                attribute = attribute.fulfill(1);
                if (VSE.isVive(player.getPlayerEntity())) {
                    VivePlayer vp = new VivePlayer(player.getPlayerEntity());

                    // <--[tag]
                    // @attribute <PlayerTag.vivecraft.crawling>
                    // @returns ElementTag
                    // @plugin Depenizen, ViveCraft
                    // @description
                    // Returns whether the player is crawling
                    // -->

                    if (attribute.startsWith("crawling")) {
                        return new ElementTag(vp.crawling);
                    }

                    // <--[tag]
                    // @attribute <PlayerTag.vivecraft.headset_location>
                    // @returns LocationTag
                    // @plugin Depenizen, ViveCraft
                    // @description
                    // TODO
                    // -->

                    if (attribute.startsWith("headset_location") && attribute.hasContext(1)) {
                        return new LocationTag((Location)player.getPlayerEntity().getMetadata("head.pos").get(0).value());
                    }

                    // <--[tag]
                    // @attribute <PlayerTag.vivecraft.righthand_location>
                    // @returns LocationTag
                    // @plugin Depenizen, ViveCraft
                    // @description
                    // TODO
                    // -->

                    if (attribute.startsWith("righthand_location") && attribute.hasContext(1)) {
                        return new LocationTag((Location)player.getPlayerEntity().getMetadata("righthand.pos").get(0).value());
                    }

                    // <--[tag]
                    // @attribute <PlayerTag.vivecraft.lefthand_location>
                    // @returns LocationTag
                    // @plugin Depenizen, ViveCraft
                    // @description
                    // TODO
                    // -->

                    if (attribute.startsWith("lefthand_location") && attribute.hasContext(1)) {
                        return new LocationTag((Location)player.getPlayerEntity().getMetadata("lefthand.pos").get(0).value());
                    }

                    // <--[tag]
                    // @attribute <PlayerTag.vivecraft.headset_rotation_x>
                    // @returns ElementTag
                    // @plugin Depenizen, ViveCraft
                    // @description
                    // TODO
                    // -->

                    if (attribute.startsWith("headset_rotation_x")) {
                        return new ElementTag(((Vec3D)player.getPlayerEntity().getMetadata("head.aim").get(0).value()).getX());
                    }

                    // <--[tag]
                    // @attribute <PlayerTag.vivecraft.righthand_rotation_x>
                    // @returns ElementTag
                    // @plugin Depenizen, ViveCraft
                    // @description
                    // TODO
                    // -->

                    if (attribute.startsWith("righthand_rotation_x")) {
                        return new ElementTag(((Vec3D)player.getPlayerEntity().getMetadata("righthand.aim").get(0).value()).getX());
                    }

                    // <--[tag]
                    // @attribute <PlayerTag.vivecraft.lefthand_rotation_x>
                    // @returns ElementTag
                    // @plugin Depenizen, ViveCraft
                    // @description
                    // TODO
                    // -->

                    if (attribute.startsWith("lefthand_rotation_x")) {
                        return new ElementTag(((Vec3D)player.getPlayerEntity().getMetadata("lefthand.aim").get(0).value()).getX());
                    }

                    // <--[tag]
                    // @attribute <PlayerTag.vivecraft.headset_rotation_y>
                    // @returns ElementTag
                    // @plugin Depenizen, ViveCraft
                    // @description
                    // TODO
                    // -->

                    if (attribute.startsWith("headset_rotation_y")) {
                        return new ElementTag(((Vec3D)player.getPlayerEntity().getMetadata("head.aim").get(0).value()).getY());
                    }

                    // <--[tag]
                    // @attribute <PlayerTag.vivecraft.righthand_rotation_y>
                    // @returns ElementTag
                    // @plugin Depenizen, ViveCraft
                    // @description
                    // TODO
                    // -->

                    if (attribute.startsWith("righthand_rotation_y")) {
                        return new ElementTag(((Vec3D)player.getPlayerEntity().getMetadata("righthand.aim").get(0).value()).getY());
                    }

                    // <--[tag]
                    // @attribute <PlayerTag.vivecraft.lefthand_rotation_y>
                    // @returns ElementTag
                    // @plugin Depenizen, ViveCraft
                    // @description
                    // TODO
                    // -->

                    if (attribute.startsWith("lefthand_rotation_y")) {
                        return new ElementTag(((Vec3D)player.getPlayerEntity().getMetadata("lefthand.aim").get(0).value()).getY());
                    }

                    // <--[tag]
                    // @attribute <PlayerTag.vivecraft.headset_rotation_z>
                    // @returns ElementTag
                    // @plugin Depenizen, ViveCraft
                    // @description
                    // TODO
                    // -->

                    if (attribute.startsWith("headset_rotation_z")) {
                        return new ElementTag(((Vec3D)player.getPlayerEntity().getMetadata("head.aim").get(0).value()).getZ());
                    }

                    // <--[tag]
                    // @attribute <PlayerTag.vivecraft.righthand_rotation_z>
                    // @returns ElementTag
                    // @plugin Depenizen, ViveCraft
                    // @description
                    // TODO
                    // -->

                    if (attribute.startsWith("righthand_rotation_z")) {
                        return new ElementTag(((Vec3D)player.getPlayerEntity().getMetadata("righthand.aim").get(0).value()).getZ());
                    }

                    // <--[tag]
                    // @attribute <PlayerTag.vivecraft.lefthand_rotation_z>
                    // @returns ElementTag
                    // @plugin Depenizen, ViveCraft
                    // @description
                    // TODO
                    // -->

                    if (attribute.startsWith("lefthand_rotation_z")) {
                        return new ElementTag(((Vec3D)player.getPlayerEntity().getMetadata("lefthand.aim").get(0).value()).getZ());
                    }

                } else {
                    return null;
                }
            }
            return null;
        });
    }
}
