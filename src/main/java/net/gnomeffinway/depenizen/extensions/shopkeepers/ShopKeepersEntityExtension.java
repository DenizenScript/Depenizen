package net.gnomeffinway.depenizen.extensions.shopkeepers;

import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;
import net.gnomeffinway.depenizen.extensions.dObjectExtension;
import net.gnomeffinway.depenizen.objects.shopkeepers.ShopKeeper;

public class ShopKeepersEntityExtension extends dObjectExtension {

    public static boolean describes(dObject object) {
        return object instanceof dEntity;
    }

    public static ShopKeepersEntityExtension getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new ShopKeepersEntityExtension((dEntity) object);
        }
    }

    public ShopKeepersEntityExtension(dEntity entity) {
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
        // @plugin Depenizen, ShopKeepers
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
        // @plugin Depenizen, ShopKeepers
        // -->
        else if (attribute.startsWith("shopkeeper") && isShopKeeper) {
            return ShopKeeper.fromEntity(entity).getAttribute(attribute.fulfill(1));
        }

        return null;
    }

}
