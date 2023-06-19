package com.denizenscript.depenizen.bukkit.properties.shopkeepers;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.depenizen.bukkit.objects.shopkeepers.ShopKeeperTag;

public class ShopKeepersEntityExtensions {

    public static void register() {

        // <--[tag]
        // @attribute <EntityTag.is_shopkeeper>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, ShopKeepers
        // @description
        // Returns whether the entity is a Shopkeeper.
        // -->
        EntityTag.tagProcessor.registerTag(ElementTag.class, "is_shopkeeper", (attribute, object) -> {
            return new ElementTag(ShopKeeperTag.isShopKeeper(object));
        });

        // <--[tag]
        // @attribute <EntityTag.shopkeeper>
        // @returns ShopKeeperTag
        // @plugin Depenizen, ShopKeepers
        // @description
        // Returns the ShopKeeper for this entity.
        // -->
        EntityTag.tagProcessor.registerTag(ShopKeeperTag.class, "shopkeeper", (attribute, object) -> {
            return ShopKeeperTag.fromEntity(object);
        });
    }
}
