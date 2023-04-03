package com.denizenscript.depenizen.bukkit.properties.mythicmobs;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.DurationTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.core.MapTag;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.depenizen.bukkit.bridges.MythicMobsBridge;
import com.denizenscript.depenizen.bukkit.objects.mythicmobs.MythicMobsMobTag;
import io.lumine.mythic.api.skills.placeholders.PlaceholderString;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.skills.auras.Aura;
import io.lumine.mythic.core.skills.auras.AuraRegistry;

import java.util.Map;
import java.util.Queue;

public class MythicMobsEntityProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "MythicMobsEntity";
    }

    @Override
    public void adjust(Mechanism mechanism) {
        // None
    }

    public static boolean describes(ObjectTag object) {
        return object instanceof EntityTag;
    }

    public static MythicMobsEntityProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        } else {
            return new MythicMobsEntityProperties((EntityTag) object);
        }
    }

    public static final String[] handledMechs = new String[]{
    }; // None

    public MythicMobsEntityProperties(EntityTag entity) {
        this.entity = entity;
    }

    EntityTag entity;

    public static void register() {

        // <--[tag]
        // @attribute <EntityTag.is_mythicmob>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns whether the entity is a MythicMob.
        // -->
        PropertyParser.registerTag(MythicMobsEntityProperties.class, ElementTag.class, "is_mythicmob", (attribute, object) -> {
            return new ElementTag(object.isMythicMob());
        }, "is_mythic_mob");

        // <--[tag]
        // @attribute <EntityTag.mythicmob>
        // @returns MythicMobsMobTag
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns the MythicMob for this entity.
        // -->
        PropertyParser.registerTag(MythicMobsEntityProperties.class, MythicMobsMobTag.class, "mythicmob", (attribute, object) -> {
            if (object.isMythicMob()) {
                return object.getMythicMob();
            }
            return null;
        }, "mythic_mob");

        // <--[tag]
        // @attribute <EntityTag.parse_mythic[<placeholder_text>]>
        // @returns ElementTag
        // @plugin Depenizen, MythicMobs
        // @description
        // Parses Mythic placeholders and returns the result. The target scope is set as the entity. (For example, <target.name> returns the entity's name.)
        // Keep in mind that <> will have to be escaped using tags. This is usually over-engineering, consider using other Denizen tags instead.
        // -->
        PropertyParser.registerTag(MythicMobsEntityProperties.class, ElementTag.class, ElementTag.class, "parse_mythic", (attribute, object, text) -> {
            return new ElementTag(PlaceholderString.of(text.asString()).get(BukkitAdapter.adapt(object.entity.getBukkitEntity())));
        });

        // <--[tag]
        // @attribute <EntityTag.auras>
        // @return MapTag
        // @plugin Depenizen, MythicMobs
        // @description
        // Returns a MapTag of the mob's aura information, where the key is the aura name and the value is a ListTag of MapTags containing info about that aura (stacks, interval, duration, etc).
        // -->
        PropertyParser.registerTag(MythicMobsEntityProperties.class, MapTag.class, "auras", (attribute, object) -> {
            MapTag result = new MapTag();
            AuraRegistry registry = MythicBukkit.inst().getSkillManager().getAuraManager().getAuraRegistry(BukkitAdapter.adapt(object.entity.getBukkitEntity()));
            for (Map.Entry<String, Queue<Aura.AuraTracker>> aura : registry.getAuras().entrySet()) {
                ListTag list = new ListTag();
                for (Aura.AuraTracker tracker : aura.getValue()) {
                    MapTag auraInfo = new MapTag();
                    auraInfo.putObject("stacks", new ElementTag(tracker.getStacks()));
                    auraInfo.putObject("max_stacks", new ElementTag(tracker.getMaxStacks()));
                    auraInfo.putObject("interval", new ElementTag(tracker.getInterval()));
                    auraInfo.putObject("start_charges", new ElementTag(tracker.getStartCharges()));
                    auraInfo.putObject("charges_remaining", new ElementTag(tracker.getChargesRemaining()));
                    auraInfo.putObject("start_duration", new DurationTag((long) tracker.getStartDuration()));
                    auraInfo.putObject("expiration", new DurationTag((long) tracker.getTicksRemaining()));
                    list.addObject(auraInfo);
                }
                result.putObject(aura.getKey(), list);
            }
            return result;
        });
    }

    public boolean isMythicMob() {
        return MythicMobsBridge.isMythicMob(entity.getBukkitEntity());
    }

    public MythicMobsMobTag getMythicMob() {
        return new MythicMobsMobTag(MythicMobsBridge.getActiveMob(entity.getBukkitEntity()));
    }
}
