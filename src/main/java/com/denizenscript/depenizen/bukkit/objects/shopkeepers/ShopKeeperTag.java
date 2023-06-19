package com.denizenscript.depenizen.bukkit.objects.shopkeepers;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.Fetchable;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.ObjectTagProcessor;
import com.denizenscript.denizencore.tags.TagContext;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.depenizen.bukkit.bridges.ShopkeepersBridge;
import com.nisovin.shopkeepers.api.ShopkeepersAPI;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.api.shopkeeper.player.PlayerShopkeeper;
import com.nisovin.shopkeepers.api.shopobjects.entity.EntityShopObject;
import com.nisovin.shopkeepers.api.ui.UISession;
import org.bukkit.entity.Entity;

import java.util.UUID;

public class ShopKeeperTag implements ObjectTag {

    // <--[ObjectType]
    // @name ShopKeeperTag
    // @prefix shopkeeper
    // @base ElementTag
    // @format
    // The identity format for shopkeepers is <uuid>
    // For example, 'shopkeeper@1234-1234-1234'.
    //
    // @plugin Depenizen, Shopkeepers
    // @description
    // A ShopKeeperTag represents a ShopKeeper entity in the world.
    //
    // -->

    @Fetchable("shopkeeper")
    public static ShopKeeperTag valueOf(String string, TagContext context) {
        if (string.startsWith("shopkeeper@")) {
            string = string.substring("shopkeeper@".length());
        }
        try {
            UUID uuid = UUID.fromString(string);
            Shopkeeper keeper = ShopkeepersAPI.getShopkeeperRegistry().getShopkeeperByUniqueId(uuid);
            if (keeper == null) {
                return null;
            }
            return new ShopKeeperTag(keeper);
        }
        catch (IllegalArgumentException e) {
            if (context == null || context.showErrors()) {
                Debug.echoError("valueOf ShopKeeperTag returning null: Invalid ShopKeeper UUID '" + string + "' specified.");
            }
            return null;
        }
    }

    public static boolean matches(String string) {
        if (string.startsWith("shopkeeper@")) {
            return true;
        }
        return valueOf(string, CoreUtilities.noDebugContext) != null;
    }

    public static boolean isShopKeeper(EntityTag entity) {
        return entity != null && ShopkeepersAPI.getShopkeeperRegistry().isShopkeeper(entity.getBukkitEntity());
    }

    public static ShopKeeperTag fromEntity(EntityTag entity) {
        if (entity == null) {
            return null;
        }
        Shopkeeper shopkeeper = ShopkeepersAPI.getShopkeeperRegistry().getShopkeeperByEntity(entity.getBukkitEntity());
        return shopkeeper != null ? new ShopKeeperTag(shopkeeper) : null;
    }

    public ShopKeeperTag(Shopkeeper shopkeeper) {
        if (shopkeeper != null) {
            this.shopkeeper = shopkeeper;
        }
        else {
            Debug.echoError("Shopkeeper referenced is null!");
        }
    }
    Shopkeeper shopkeeper;

    public Shopkeeper getShopkeeper() {
        return shopkeeper;
    }

    public EntityTag getDenizenEntity() {
        Entity entity = getBukkitEntity();
        return entity != null ? new EntityTag(entity) : null;
    }

    public Entity getBukkitEntity() {
        return shopkeeper.getShopObject() instanceof EntityShopObject entityShopObject ? entityShopObject.getEntity() : null;
    }

    public static void register() {

        // <--[tag]
        // @attribute <ShopKeeperTag.is_active>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, ShopKeepers
        // @description
        // Returns whether the Shopkeeper is active (has been spawned and is still valid and present in the world).
        // -->
        tagProcessor.registerTag(ElementTag.class, "is_active", (attribute, object) -> {
            return new ElementTag(object.shopkeeper.getShopObject().isActive());
        });

        // <--[tag]
        // @attribute <ShopKeeperTag.is_ui_active>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, ShopKeepers
        // @description
        // Returns whether the Shopkeeper's UI is currently active (may be false when the UI is about to be closed).
        // -->
        // TODO: It seems a single shopkeeper can have multiple UI sessions now, should have better handling here
        tagProcessor.registerTag(ElementTag.class, "is_ui_active", (attribute, object) -> {
           return new ElementTag(object.shopkeeper.getUISessions().stream().anyMatch(UISession::isUIActive));
        });

        // <--[tag]
        // @attribute <ShopKeeperTag.trades>
        // @returns ListTag(ListTag(ItemTag))
        // @plugin Depenizen, ShopKeepers
        // @description
        // Returns a ListTag of the Shopkeeper's trades (as sub-lists).
        // -->
        tagProcessor.registerTag(ListTag.class, "trades", (attribute, object) -> {
            return new ListTag(object.shopkeeper.getTradingRecipes(null), ShopkeepersBridge::wrapTradingRecipe);
        }, "recipes");

        // <--[tag]
        // @attribute <ShopKeeperTag.entity>
        // @returns EntityTag
        // @plugin Depenizen, ShopKeepers
        // @description
        // Returns the EntityTag for this ShopKeeper, if any.
        // -->
        tagProcessor.registerTag(EntityTag.class, "entity", (attribute, object) -> {
            return object.getDenizenEntity();
        });

        // <--[tag]
        // @attribute <ShopKeeperTag.owner>
        // @returns PlayerTag
        // @plugin Depenizen, ShopKeepers
        // @description
        // Returns the player that owns this ShopKeeper, if any.
        // -->
        tagProcessor.registerTag(PlayerTag.class, "owner", (attribute, object) -> {
            return object.shopkeeper instanceof PlayerShopkeeper playerShopkeeper ? new PlayerTag(playerShopkeeper.getOwnerUUID()) : null;
        });

    }

    public static final ObjectTagProcessor<ShopKeeperTag> tagProcessor = new ObjectTagProcessor<>();

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {
        return tagProcessor.getObjectAttribute(this, attribute);
    }

    @Override
    public boolean isUnique() {
        return true;
    }

    @Override
    public String identify() {
        return "shopkeeper@" + shopkeeper.getUniqueId();
    }

    @Override
    public String debuggable() {
        return "<LG>shopkeeper@<Y>" + shopkeeper.getUniqueId();
    }

    @Override
    public String identifySimple() {
        return identify();
    }

    @Override
    public String toString() {
        return identify();
    }

    private String prefix;

    @Override
    public ObjectTag setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    @Override
    public String getPrefix() {
        return prefix;
    }
}
