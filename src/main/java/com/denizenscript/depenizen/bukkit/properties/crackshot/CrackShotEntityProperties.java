package com.denizenscript.depenizen.bukkit.properties.crackshot;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.depenizen.bukkit.bridges.CrackShotBridge;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.TNTPrimed;

public class CrackShotEntityProperties implements Property {

    public static final String[] handledTags = new String[] {
            "crackshot_weapon"
    };

    public static final String[] handledMechs = new String[] {
    }; // None


    EntityTag entity;

    public CrackShotEntityProperties(EntityTag entity) {
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
        return object instanceof EntityTag;
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
    public ObjectTag getObjectAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        // <--[tag]
        // @attribute <EntityTag.crackshot_weapon>
        // @returns ElementTag
        // @plugin Depenizen, CrackShot
        // @description
        // Returns the CrackShot weapon name that the projectile was shot from.
        // -->
        if (attribute.startsWith("crackshot_weapon")) {
            String name = null;
            if (entity instanceof TNTPrimed) {
                name = CrackShotBridge.utility.getWeaponTitle((TNTPrimed) entity);
            }
            else if (entity instanceof Projectile) {
                name = CrackShotBridge.utility.getWeaponTitle(entity.getProjectile());
            }
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
