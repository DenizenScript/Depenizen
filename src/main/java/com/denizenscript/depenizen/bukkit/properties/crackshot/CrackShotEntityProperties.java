package com.denizenscript.depenizen.bukkit.properties.crackshot;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.depenizen.bukkit.bridges.CrackShotBridge;

public class CrackShotEntityProperties implements Property {

    public static final String[] handledTags = new String[]{
            "shot_from"
    };

    EntityTag entity;

    private CrackShotEntityProperties(EntityTag entity) {
        this.entity = entity;
    }

    public static CrackShotEntityProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new CrackShotEntityProperties((EntityTag) object);
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
        return "CrackShotEntity";
    }

    @Override
    public String getAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        // <--[tag]
        // @attribute <EntityTag.shot_from>
        // @returns ElementTag
        // @mechanism EntityTag.shot_from
        // @plugin Depenizen, CrackShot
        // @description
        // Returns the CrackShot weapon name that the projectile was shot from.
        // -->
        if (attribute.startsWith("shot_from")) {
            String name = CrackShotBridge.utility.getWeaponTitle(entity.getProjectile());
            if (name != null) {
                return new ElementTag(name).getAttribute(attribute.fulfill(1));
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
