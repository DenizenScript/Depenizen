package com.denizenscript.depenizen.bukkit.properties.shopkeepers;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.depenizen.bukkit.objects.shopkeepers.ShopKeeperTag;

public class ShopKeepersEntityProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "ShopKeepersEntity";
    }

    @Override
    public void adjust(Mechanism mechanism) {
        // None
    }

    public static boolean describes(ObjectTag object) {
        return object instanceof EntityTag;
    }

    public static ShopKeepersEntityProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new ShopKeepersEntityProperties((EntityTag) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "is_shopkeeper", "shopkeeper"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    public ShopKeepersEntityProperties(EntityTag entity) {
        this.entity = entity;
        this.isShopKeeper = ShopKeeperTag.isShopKeeper(entity);
    }

    EntityTag entity;
    boolean isShopKeeper = false;

    @Override
    public String getAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        // <--[tag]
        // @attribute <EntityTag.is_shopkeeper>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, ShopKeepers
        // @description
        // Returns whether the entity is a Shopkeeper.
        // -->
        if (attribute.startsWith("is_shopkeeper")) {
            return new ElementTag(isShopKeeper)
                    .getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <EntityTag.shopkeeper>
        // @returns ShopKeeper
        // @plugin Depenizen, ShopKeepers
        // @description
        // Returns the ShopKeeper for this entity.
        // -->
        else if (attribute.startsWith("shopkeeper") && isShopKeeper) {
            return ShopKeeperTag.fromEntity(entity).getAttribute(attribute.fulfill(1));
        }

        return null;
    }
}
