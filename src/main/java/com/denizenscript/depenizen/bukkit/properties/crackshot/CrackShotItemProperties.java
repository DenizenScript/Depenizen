package com.denizenscript.depenizen.bukkit.properties.crackshot;

import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.depenizen.bukkit.bridges.CrackShotBridge;

public class CrackShotItemProperties implements Property {

    public static final String[] handledTags = new String[] {
            "crackshot_weapon"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    ItemTag item;

    public CrackShotItemProperties(ItemTag item) {
        this.item = item;
    }

    public static CrackShotItemProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new CrackShotItemProperties((ItemTag) object);
        }
    }

    public static boolean describes(ObjectTag object) {
        return object instanceof ItemTag;
    }

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "CrackShotItem";
    }

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        // <--[tag]
        // @attribute <ItemTag.crackshot_weapon>
        // @returns ElementTag
        // @plugin Depenizen, CrackShot
        // @description
        // Returns the CrackShot weapon name for the item.
        // -->
        if (attribute.startsWith("crackshot_weapon")) {
            String name = CrackShotBridge.utility.getWeaponTitle(item.getItemStack());
            if (name != null) {
                return new ElementTag(name).getObjectAttribute(attribute.fulfill(1));
            }
            else {
                return null;
            }
        }
        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {
    }
}
