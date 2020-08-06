package com.denizenscript.depenizen.bukkit.utilities.mythicmobs.conditions;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.tags.BukkitTagContext;
import com.denizenscript.denizencore.events.OldEventManager;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.tags.TagManager;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillCaster;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.conditions.ICasterCondition;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;
import io.lumine.xikage.mythicmobs.skills.conditions.ILocationCondition;
import io.lumine.xikage.mythicmobs.skills.conditions.ISkillMetaCondition;
import org.bukkit.World;

import java.util.HashMap;

public class DenizenCondition extends SkillCondition implements IEntityCondition, ILocationCondition, ICasterCondition, ISkillMetaCondition {
    final String tag;
    OldEventManager.OldEventContextSource source;
    HashMap<String, ObjectTag> context;

    public DenizenCondition(String line, MythicLineConfig mlc) {
        super(line);
        tag = mlc.getString("tag");
        context = new HashMap<>();
        source = new OldEventManager.OldEventContextSource();
        source.contexts = new HashMap<>();
    }

    @Override
    public boolean check(AbstractEntity entity) {
        source.contexts.put("target", new EntityTag(entity.getBukkitEntity()));
        return runCheck();
    }

    @Override
    public boolean check(AbstractLocation location) {
        source.contexts.put("location", new LocationTag((World) location.getWorld(), location.getX(), location.getY(), location.getZ()));
        return runCheck();
    }

    @Override
    public boolean check(SkillCaster caster) {
        source.contexts.put("entity", new EntityTag(caster.getEntity().getBukkitEntity()));
        return runCheck();
    }

    @Override
    public boolean check(SkillMetadata skillMetadata) {
        source.contexts.put("entity", new EntityTag(skillMetadata.getCaster().getEntity().getBukkitEntity()));
        if (!(skillMetadata.getEntityTargets() == null)) {
            ListTag entity_targets = new ListTag();
            for (AbstractEntity entity : skillMetadata.getEntityTargets()) {
                entity_targets.addObject(new EntityTag(entity.getBukkitEntity()));
            }
            source.contexts.put("entity_targets", entity_targets);
        }
        if (!(skillMetadata.getLocationTargets() == null)) {
            ListTag location_targets = new ListTag();
            for (AbstractLocation location : skillMetadata.getLocationTargets()) {
                location_targets.addObject(new LocationTag((World) location.getWorld(), location.getX(),location.getY(), location.getZ()));
            }
            source.contexts.put("location_targets", location_targets);
        }
        return runCheck();
    }

    public boolean runCheck() {
        BukkitTagContext tagContext = new BukkitTagContext(null, null, null, false, null);
        tagContext.contextSource = source;
        ObjectTag object = TagManager.tagObject(tag , tagContext);
        return object.asType(ElementTag.class, tagContext).asBoolean();

    }
}
