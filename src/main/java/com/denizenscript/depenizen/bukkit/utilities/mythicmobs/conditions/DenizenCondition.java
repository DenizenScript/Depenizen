package com.denizenscript.depenizen.bukkit.utilities.mythicmobs.conditions;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.queues.ContextSource;
import com.denizenscript.denizencore.tags.ParseableTag;
import com.denizenscript.denizencore.tags.TagContext;
import com.denizenscript.denizencore.tags.TagManager;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillCaster;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.conditions.*;
import org.bukkit.World;

import java.util.HashMap;

public class DenizenCondition extends SkillCondition implements IEntityCondition, ILocationCondition, ICasterCondition, ISkillMetaCondition, IEntityComparisonCondition {

    public ParseableTag tag;
    public ContextSource.SimpleMap source;

    public DenizenCondition(String line, MythicLineConfig mlc) {
        super(line);
        tag = TagManager.parseTextToTag(mlc.getString("tag"), CoreUtilities.noDebugContext);
        source = new ContextSource.SimpleMap();
        source.contexts = new HashMap<>();
    }

    public void reset() {
        source.contexts.clear();
    }

    @Override
    public boolean check(AbstractEntity entity) {
        reset();
        source.contexts.put("target", new EntityTag(entity.getBukkitEntity()));
        return runCheck();
    }

    @Override
    public boolean check(AbstractLocation location) {
        reset();
        source.contexts.put("location", new LocationTag((World) location.getWorld(), location.getX(), location.getY(), location.getZ()));
        return runCheck();
    }

    public void addContext(SkillCaster caster) {
        source.contexts.put("entity", new EntityTag(caster.getEntity().getBukkitEntity()));
        AbstractEntity target = caster.getEntity().getTarget();
        if (target != null) {
            source.contexts.put("target", new EntityTag(target.getBukkitEntity()));
        }
    }

    @Override
    public boolean check(SkillCaster caster) {
        reset();
        addContext(caster);
        return runCheck();
    }

    @Override
    public boolean check(SkillMetadata skillMetadata) {
        reset();
        if (skillMetadata.getTrigger() == null) {
            source.contexts.put("trigger", new EntityTag(skillMetadata.getTrigger().getBukkitEntity()));
        }
        addContext(skillMetadata.getCaster());
        return runCheck();
    }

    @Override
    public boolean check(AbstractEntity entity1, AbstractEntity entity2) {
        source.contexts.put("entity", new EntityTag(entity1.getBukkitEntity()));
        source.contexts.put("target", new EntityTag(entity2.getBukkitEntity()));
        return runCheck();
    }

    public boolean runCheck() {
        TagContext tagContext = CoreUtilities.noDebugContext.clone();
        tagContext.contextSource = source;
        ObjectTag object = tag.parse(tagContext);
        return object.asElement().asBoolean();
    }
}
