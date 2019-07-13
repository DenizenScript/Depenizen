package com.denizenscript.depenizen.bukkit.properties.areashop;

import com.denizenscript.depenizen.bukkit.objects.areashop.dAreaShop;
import me.wiefferink.areashop.AreaShop;
import me.wiefferink.areashop.regions.GeneralRegion;
import com.denizenscript.denizen.objects.dPlayer;
import com.denizenscript.denizencore.objects.ListTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.tags.Attribute;

import java.util.UUID;

public class AreaShopPlayerProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "AreaShopPlayer";
    }

    @Override
    public void adjust(Mechanism mechanism) {
        // None
    }

    public static boolean describes(ObjectTag object) {
        return object instanceof dPlayer;
    }

    public static AreaShopPlayerProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new AreaShopPlayerProperties((dPlayer) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "areashop"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    private AreaShopPlayerProperties(dPlayer player) {
        this.player = player;
    }

    dPlayer player = null;

    @Override
    public String getAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        if (attribute.startsWith("areashop")) {
            attribute = attribute.fulfill(1);

            // <--[tag]
            // @attribute <p@player.areashop.owned>
            // @returns ListTag(AreaShop)
            // @description
            // Returns a list of AreaShops the player owns.
            // @Plugin Depenizen, AreaShop
            // -->
            if (attribute.startsWith("owned")) {
                ListTag list = new ListTag();
                UUID uuid = player.getOfflinePlayer().getUniqueId();
                for (GeneralRegion region : AreaShop.getInstance().getFileManager().getRegions()) {
                    if (uuid.equals(region.getOwner())) {
                        list.add(new dAreaShop(region).identify());
                    }
                }
                return list.getAttribute(attribute.fulfill(1));
            }
        }

        return null;
    }
}
