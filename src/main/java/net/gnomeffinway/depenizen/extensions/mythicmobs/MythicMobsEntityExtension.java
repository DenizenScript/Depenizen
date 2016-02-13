package net.gnomeffinway.depenizen.extensions.mythicmobs;

import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;
import net.elseland.xikage.MythicMobs.API.IMobsAPI;
import net.elseland.xikage.MythicMobs.MythicMobs;
import net.gnomeffinway.depenizen.extensions.dObjectExtension;
import net.gnomeffinway.depenizen.objects.mythicmobs.MythicMobsMob;

public class MythicMobsEntityExtension extends dObjectExtension {

    IMobsAPI api = MythicMobs.inst().getAPI().getMobAPI();

    public static boolean describes(dObject object) {
        return object instanceof dEntity;
    }

    public static MythicMobsEntityExtension getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new MythicMobsEntityExtension((dEntity) object);
        }
    }

    public MythicMobsEntityExtension(dEntity entity) {
        this.entity = entity;
    }

    dEntity entity;

    @Override
    public String getAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        // <--[tag]
        // @attribute <e@entity.is_mythicmob>
        // @returns Element(Boolean)
        // @description
        // Returns whether the entity is a MythicMob.
        // @plugin Depenizen, MythicMobs
        // -->
        if (attribute.startsWith("is_mythic_mob") || attribute.startsWith("is_mythicmob")) {
            return new Element(api.isMythicMob(entity.getBukkitEntity())).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <e@entity.mythicmob>
        // @returns MythicMobsMob
        // @description
        // Returns the MythicMob for this entity.
        // @plugin Depenizen, MythicMobs
        // -->
        else if ((attribute.startsWith("mythicmob") || attribute.startsWith("mythic_mob"))
                && api.isMythicMob(entity.getBukkitEntity())) {
            return new MythicMobsMob(api.getMythicMobInstance(entity.getBukkitEntity()))
                    .getAttribute(attribute.fulfill(1));
        }

        return null;
    }

}
