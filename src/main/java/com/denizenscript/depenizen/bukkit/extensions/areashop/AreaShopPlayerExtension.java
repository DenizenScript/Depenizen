package com.denizenscript.depenizen.bukkit.extensions.areashop;

import com.denizenscript.depenizen.bukkit.objects.areashop.dAreaShop;
import me.wiefferink.areashop.AreaShop;
import me.wiefferink.areashop.regions.GeneralRegion;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;
import com.denizenscript.depenizen.bukkit.extensions.dObjectExtension;

import java.util.UUID;

public class AreaShopPlayerExtension extends dObjectExtension {

    public static boolean describes(dObject object) {
        return object instanceof dPlayer;
    }

    public static AreaShopPlayerExtension getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new AreaShopPlayerExtension((dPlayer) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "areashop"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    private AreaShopPlayerExtension(dPlayer player) {
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
            // @returns dList(AreaShop)
            // @description
            // Returns a list of AreaShops the player owns.
            // @Plugin Depenizen, AreaShop
            // -->
            if (attribute.startsWith("owned")) {
                dList list = new dList();
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
