package com.denizenscript.depenizen.bukkit.properties.shopkeepers;

import net.aufdemrand.denizen.objects.dEntity;
import com.denizenscript.denizencore.objects.Element;
import com.denizenscript.denizencore.objects.dObject;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.depenizen.bukkit.objects.shopkeepers.ShopKeeper;

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

    public static boolean describes(dObject object) {
        return object instanceof dEntity;
    }

    public static ShopKeepersEntityProperties getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new ShopKeepersEntityProperties((dEntity) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "is_shopkeeper", "shopkeeper"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    public ShopKeepersEntityProperties(dEntity entity) {
        this.entity = entity;
        this.isShopKeeper = ShopKeeper.isShopKeeper(entity);
    }

    dEntity entity;
    boolean isShopKeeper = false;

    @Override
    public String getAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        // <--[tag]
        // @attribute <e@entity.is_shopkeeper>
        // @returns Element(Boolean)
        // @description
        // Returns whether the entity is a Shopkeeper.
        // @Plugin Depenizen, ShopKeepers
        // -->
        if (attribute.startsWith("is_shopkeeper")) {
            return new Element(isShopKeeper)
                    .getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <e@entity.shopkeeper>
        // @returns ShopKeeper
        // @description
        // Returns the ShopKeeper for this entity.
        // @Plugin Depenizen, ShopKeepers
        // -->
        else if (attribute.startsWith("shopkeeper") && isShopKeeper) {
            return ShopKeeper.fromEntity(entity).getAttribute(attribute.fulfill(1));
        }

        return null;
    }
}
